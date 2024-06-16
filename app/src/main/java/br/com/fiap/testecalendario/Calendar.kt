package br.com.fiap.testecalendario

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.testecalendario.database.dao.Converters
import br.com.fiap.testecalendario.database.repository.EventRepository
import br.com.fiap.testecalendario.model.Event
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventCalendarApp(navController: NavController) {
    CalendarScreen()
}

// Em CalendarScreen
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var newEventDescription by remember { mutableStateOf(TextFieldValue()) }
    var isSelect by remember {
        mutableStateOf(false)
    }

    var selectedEventType by remember { mutableStateOf("task") }
    val eventTypes = listOf("task", "meeting", "other")
    // State para controlar a exibição do popup
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var isEventDetailDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val eventRepository = EventRepository(context)
    val listaEventosState = remember {
        mutableStateOf(eventRepository.buscarContatos(Converters.fromDate(selectedDate)))
    }
    val listaEventos = remember {
        mutableStateOf(eventRepository.buscar())
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            //.fillMaxSize()
            .padding(16.dp, bottom = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Calendário", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        MonthCarousel(selectedMonth = selectedMonth, onMonthSelected = { selectedMonth = it })
        CalendarView(
            selectedDate = selectedDate, // Passar selectedDate para CalendarView
            onDateSelected = { date ->
                selectedDate = date
                isSelect = true
            },
            events = listaEventos.value.map{ it.date },
            selectedMonth = selectedMonth,
            atualizar = { listaEventos.value = eventRepository.buscar()}
        )
        // Popup para adicionar evento
        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    // Fechar o popup ao clicar fora
                    isDialogOpen = false
                    newEventDescription = TextFieldValue() // Limpar campo de texto ao fechar
                },
                title = { Text("Adicionar Evento") },
                text = {
                    Column {
                        TextField(
                            value = newEventDescription,
                            onValueChange = { newEventDescription = it },
                            label = { Text("Descrição do Evento") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Dropdown for selecting event type
                        var expanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true }
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(8.dp)
                        ) {
                            Text(text = selectedEventType, color = Color.White)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            eventTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedEventType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newEventDescription.text.isNotEmpty()) {
                                val event = Event(0, selectedDate!!.toString(), newEventDescription.text, selectedEventType )

                                eventRepository.salvar(event)
                                isDialogOpen = false // Fechar o popup após adicionar o evento
                                newEventDescription = TextFieldValue() // Limpar campo de texto
                            }
                        }
                    ) {
                        Text("Adicionar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            isDialogOpen = false // Fechar o popup sem adicionar evento
                            newEventDescription = TextFieldValue() // Limpar campo de texto
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
        if (isSelect) {
            Button(
                onClick = {
                    isDialogOpen = true // Abrir o popup ao selecionar uma data
                }
            ) {
                Text("Adicionar Evento")
            }
            listaEventosState.value = eventRepository.buscarContatos(Converters.fromDate(selectedDate))
            EventList(listaEventosState,  atualizar = { listaEventosState.value = eventRepository.buscarContatos(
                Converters.fromDate(selectedDate)
            ) }, onEventClick = { event ->
                selectedEvent = event
                isEventDetailDialogOpen = true
            })
        }
        // Lista de eventos
        //Spacer(modifier = Modifier.height(16.dp))
    }

    // Popup para exibir detalhes do evento
    if (isEventDetailDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                isEventDetailDialogOpen = false
                selectedEvent = null // Limpar o evento selecionado ao fechar o popup
            },
            title = { Text("Detalhes do Evento") },
            text = {
                Column {
                    selectedEvent?.let { event ->
                        Text(text = "Descrição: ${event.description}")
                        Text(text = "Data: ${event.date}")
                        Text(text = "Tipo: ${event.type_event}")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isEventDetailDialogOpen = false
                        selectedEvent = null // Limpar o evento selecionado ao fechar o popup
                    }
                ) {
                    Text("Fechar")
                }
            }
        )
    }

}

@Composable
fun EventList(
    listEvent : MutableState<List<Event>>,
    atualizar: () -> Unit,
    onEventClick: (Event) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (events in listEvent.value) {
            EventCard(events, atualizar, onEventClick)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
@Composable
fun EventCard(
    event: Event,
    atualizar: () -> Unit,
    onEventClick: (Event) -> Unit
){
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEventClick(event) },
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            ) {
                Text(
                    text = event.description,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.date,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = {
                val eventRepository = EventRepository(context)
                eventRepository.excluir(event)
                atualizar()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletar evento"
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    events: List<String>,
    selectedMonth: YearMonth,
    atualizar: () -> Unit
) {
    val daysInMonth = selectedMonth.lengthOfMonth()
    val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value
    val totalCells = daysInMonth + firstDayOfWeek
    atualizar()
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Dom", fontWeight = FontWeight.Bold)
            Text(text = "Seg", fontWeight = FontWeight.Bold)
            Text(text = "Ter", fontWeight = FontWeight.Bold)
            Text(text = "Qua", fontWeight = FontWeight.Bold)
            Text(text = "Qui", fontWeight = FontWeight.Bold)
            Text(text = "Sex", fontWeight = FontWeight.Bold)
            Text(text = "Sab", fontWeight = FontWeight.Bold)
        }

        for (week in 0..5) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (day in 0..6) {
                    val cellIndex = week * 7 + day
                    if (cellIndex < firstDayOfWeek || cellIndex >= totalCells) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                        )
                    } else {
                        val dayOfMonth = cellIndex - firstDayOfWeek + 1
                        val date = selectedMonth.atDay(dayOfMonth)
                        val isSelected = date == selectedDate
                        val today = LocalDate.now()

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else if (date == today) Color.Cyan else Color.Transparent)
                                .clickable { onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = dayOfMonth.toString(), color = when {
                                    isSelected -> Color.White
                                    else -> Color.Black
                                })

                                // Mostrar eventos em datas específicas
                                if (events.contains(date.toString())) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(MaterialTheme.shapes.small)
                                            .background(Color.Red)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthCarousel(
    selectedMonth: YearMonth,
    onMonthSelected: (YearMonth) -> Unit
) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy") }

    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(12) { index ->
            val month = YearMonth.now().plusMonths(index.toLong())
            val isSelected = month == selectedMonth
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(backgroundColor)
                    .clickable { onMonthSelected(month) }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = month.format(monthFormatter),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

