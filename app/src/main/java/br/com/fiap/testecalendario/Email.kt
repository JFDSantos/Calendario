
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.testecalendario.R
import br.com.fiap.testecalendario.database.repository.EmailRepository
import br.com.fiap.testecalendario.model.Email

@Composable
fun EmailItem(
    email: Email,
    onItemClick: (Email) -> Unit,
    atualizar: () -> Unit
) {

    val context = LocalContext.current
    val emailRepository = EmailRepository(context)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(email) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, CircleShape)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = email.sender,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (email.isImportant) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_label_important_24),
                        contentDescription = "Importante",
                        tint = Color.Blue,
                        modifier = Modifier.clickable {
                            emailRepository.atualizarImportante(false,email.id)
                            atualizar()
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_label_important_outline_24),
                        contentDescription = "Não Importante",
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            emailRepository.atualizarImportante(true,email.id)
                            atualizar()
                        }
                    )
                }
                if (email.isFavorite) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_star_24),
                        contentDescription = "Favorito",
                        tint = Color.Blue,
                        modifier = Modifier.clickable {
                            emailRepository.atualizarFavorito(false,email.id)
                            atualizar()
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_star_border_24),
                        contentDescription = "Não Favorito",
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            emailRepository.atualizarFavorito(true,email.id)
                            atualizar()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = email.subject, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = email.preview, color = Color.Gray, maxLines = 1)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(0.35f)
                .fillMaxSize()
        ) {
            Text(text = email.date, color = Color.Gray, fontSize = 13.sp)
            IconButton(onClick = {

                emailRepository.excluir(email)
                atualizar()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletar email"
                )
            }
        }

    }
}

@Composable
fun EmailList(
    emails: List<Email>,
    onItemClick: (Email) -> Unit,
    atualizar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (email in emails) {
            EmailItem(email = email, onItemClick, atualizar)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun EmailScreen(navController: NavController) {
    val context = LocalContext.current
    val emailRepository = EmailRepository(context)

    var emails by remember {
        mutableStateOf(emailRepository.buscar())
    }

    var showMenu by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todas") }

    LaunchedEffect(selectedFilter) {
        when (selectedFilter) {
            "Favoritas" -> emails = emailRepository.buscarEmailsFavoritos()
            "Importantes" -> emails = emailRepository.buscarEmailsImportantes()
            else -> emails = emailRepository.buscar()
        }
    }

    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    selectedEmail?.let {
        navController.navigate("emailRead?sender=${selectedEmail!!.sender}&subject=${selectedEmail!!.subject}&content=${selectedEmail!!.content}&isImportant=${selectedEmail!!.isImportant}&isFavorite=${selectedEmail!!.isFavorite}&isReading=TRUE")
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Email",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                contentDescription = "Filtrar",
                tint = Color.Gray,
                modifier = Modifier
                    .clickable { showMenu = !showMenu }
                    .padding(horizontal = 12.dp)
            )

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(onClick = {
                    selectedFilter = "Todas"
                    showMenu = false
                }) {
                    Text("Todas")
                }
                DropdownMenuItem(onClick = {
                    selectedFilter = "Favoritas"
                    showMenu = false
                }) {
                    Text("Favoritas")
                }
                DropdownMenuItem(onClick = {
                    selectedFilter = "Importantes"
                    showMenu = false
                }) {
                    Text("Importantes")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        EmailList(
            emails = emails,
            onItemClick = { email ->
                selectedEmail = email
            },
            atualizar = {
                emails = emailRepository.buscar()
            }
        )
    }

    OverlayButton {
        navController.navigate("emailRead")
        emails = emailRepository.buscar()
    }
}

@Composable
fun OverlayButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "+", textAlign = TextAlign.Center, fontSize = 35.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    EmailScreen(navController)
}
