package br.com.fiap.testecalendario

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import br.com.fiap.testecalendario.database.dao.Converters.fromDate
import br.com.fiap.testecalendario.database.dao.Converters.toDate
import br.com.fiap.testecalendario.database.repository.EventRepository
import br.com.fiap.testecalendario.ui.theme.TesteCalendarioTheme
import java.time.LocalDate
import br.com.fiap.testecalendario.model.Event

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TesteCalendarioTheme {
                // Container com a cor de fundo do tema
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Agenda de Eventos") }) }
                ) {
                    EventCalendarApp()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventCalendarApp() {
    val navController = rememberNavController()

    CalendarScreen()
}

// Em CalendarScreen
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var newEventDescription by remember { mutableStateOf(TextFieldValue()) }
    val events = remember { mutableStateListOf<Event>() }
    var isSelect by remember {
        mutableStateOf(false)
    }

    // State para controlar a exibição do popup
    var isDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val eventRepository = EventRepository(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Calendário", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        CalendarView(
            selectedDate = selectedDate, // Passar selectedDate para CalendarView
            onDateSelected = { date ->
                selectedDate = date
                isSelect = true
            },
            events = events.map { it.date }
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
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newEventDescription.text.isNotEmpty()) {
                                val event = Event(0, selectedDate!!.toString(), newEventDescription.text)
                                events.add(event)

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
        }

        // Lista de eventos
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Eventos:", fontSize = 20.sp, modifier = Modifier.padding(8.dp))
        events.filter { toDate(it.date) == toDate(fromDate(selectedDate)) }.forEach { event ->
            Text(text = "${event.date}: ${event.description}", fontSize = 16.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    selectedDate: LocalDate?, // Adicionar selectedDate como parâmetro
    onDateSelected: (LocalDate) -> Unit,
    events: List<String>
) {
    val today = LocalDate.now()
    val currentMonth = today.monthValue
    val currentYear = today.year

    // Calculate the number of days in the current month
    val daysInMonth = today.lengthOfMonth()

    // Calculate the day of the week for the first day of the month
    val firstDayOfWeek = today.withDayOfMonth(1).dayOfWeek.value // 1 = Monday, 7 = Sunday

    Column {
        // Header com os nomes dos dias
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab").forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp), // Ajuste o padding vertical
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        // Dias no mês
        var dayCounter = 1
        repeat(6) { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { day ->
                    val currentDay = dayCounter - firstDayOfWeek + 2
                    if (dayCounter <= daysInMonth && (week > 0 || day >= (firstDayOfWeek - 1))) {
                        val date = LocalDate.of(currentYear, currentMonth, currentDay)
                        val isSelected = (date == selectedDate)
                        val hasEvent = events.contains(fromDate(date))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f) // Manter aspect ratio quadrado
                                .clip(shape = MaterialTheme.shapes.medium)
                                .background(
                                    color = when {
                                        isSelected -> Color.Blue
                                        hasEvent -> Color.Green
                                        else -> Color.Transparent
                                    }
                                )
                                .clickable {
                                    onDateSelected(date)
                                }
                                .fillMaxSize()
                        ) {
                            Text(
                                text = currentDay.toString(),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(8.dp),
                                color = when {
                                    isSelected -> Color.White
                                    else -> Color.Black
                                }
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f) // Manter aspect ratio quadrado
                        )
                    }
                    dayCounter++
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    TesteCalendarioTheme {
        EventCalendarApp()
    }
}
