package br.com.fiap.testecalendario

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun EmailReadScreen(
    navController: NavController,
    sender: String,
    subject: String,
    content: String,
    isReading: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val ReadingOrWriting = isReading == "TRUE"
    var isImportant by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

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
                value = sender,
                readOnly = ReadingOrWriting,
                onValueChange = {},
                label = { Text("Remetente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subject,
                readOnly = ReadingOrWriting,
                onValueChange = {},
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
                    checked = isImportant,
                    onCheckedChange = { isImportant = it }
                )
                Text(text = "Importante")

                Checkbox(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
                Text(text = "Favorito")
            }
            OutlinedTextField(
                value = content,
                readOnly = ReadingOrWriting,
                onValueChange = { },
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

            if (ReadingOrWriting){
                Button(
                    onClick = { navController.navigate("email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Voltar")
                }
            }else{
                Button(
                    onClick = { /* Implementar ação de enviar e-mail */ },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEmailScreen() {
    val nav = rememberNavController()
    val sender = "JEFF"
    val sub = "JEFF"
    val con = "JEFF"
    val isre = "TRUE"
    EmailReadScreen(nav, sender, sub, con, isre)
}
