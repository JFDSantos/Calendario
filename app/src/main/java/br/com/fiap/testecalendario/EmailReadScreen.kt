package br.com.fiap.testecalendario

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.testecalendario.database.repository.EmailRepository
import br.com.fiap.testecalendario.model.Email
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmailReadScreen(
    navController: NavController,
    sender: String,
    subject: String,
    content: String,
    isImportant: String,
    isFavorite: String,
    isReading: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val ReadingOrWriting = isReading == "TRUE"
    var isImportantText by remember {  mutableStateOf(isImportant == "true") }
    var isFavoriteText by remember {  mutableStateOf(isFavorite == "true") }
    val today = LocalDate.now().toString()
    val context = LocalContext.current
    val emailRepository = EmailRepository(context)

    var senderText by remember { mutableStateOf(sender) }
    var subjectText by remember { mutableStateOf(subject) }
    var contentText by remember { mutableStateOf(content) }
    Surface(
        color = Color.White, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 55.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = senderText,
                readOnly = ReadingOrWriting,
                onValueChange = {senderText = it},
                label = { Text("Remetente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subjectText,
                readOnly = ReadingOrWriting,
                onValueChange = {subjectText = it},
                label = { Text("Assunto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = isImportantText,
                    onCheckedChange = { isImportantText = it },
                    enabled = !ReadingOrWriting
                )
                Text(text = "Importante")

                Checkbox(
                    checked = isFavoriteText,
                    onCheckedChange = { isFavoriteText = it },
                    enabled = !ReadingOrWriting
                )
                Text(text = "Favorito")
            }
            OutlinedTextField(
                value = contentText,
                readOnly = ReadingOrWriting,
                onValueChange = { contentText = it},
                label = { Text("Conteúdo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                textStyle = TextStyle(fontSize = 16.sp)
            )

            if (ReadingOrWriting) {
                Button(
                    onClick = { navController.navigate("email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Voltar")
                }
            } else {
                Button(
                    onClick = {
                        val email = Email(
                            0,
                            senderText,
                            subjectText,
                            if(subjectText.length >= 20) subject.substring(0, 20) else subjectText,
                            today,
                            isImportantText,
                            isFavoriteText,
                            contentText
                        )
                        emailRepository.salvar(email)
                        navController.navigate("email")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Enviar Email")
                }
            }

        }
    }
}

