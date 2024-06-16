
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.testecalendario.R

data class Email(
    val sender: String,
    val subject: String,
    val preview: String,
    val time: String,
    var isImportant: Boolean = false,
    val content: String
)

@Composable
fun EmailItem(email: Email, onItemClick: (Email) -> Unit) {
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
                    Text(
                        text = "Marcado",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
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
                .weight(0.27f)
                .fillMaxSize()
        ) {
            Text(text = email.time, color = Color.Gray, fontSize = 13.sp)
            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletar evento"
                )
            }
        }

    }
}

@Composable
fun EmailList(emails: List<Email>, onItemClick: (Email) -> Unit) {
    LazyColumn() {
        items(emails) { email ->
            EmailItem(email = email) {
                onItemClick(email)
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}



@Composable
fun EmailScreen(navController: NavController) {
    val emails = listOf(
        Email("John Doe", "Meeting Reminder", "Don't forget our meeting at 10am", "10:00 AM", false, "Detailed content of the email..."),
        Email("Jane Smith", "Sale Alert!", "Check out our latest sale on electronics", "9:30 AM", true, "Detailed content of the email..."),
        Email("Service", "Your Order Confirmation", "Thank you for your purchase", "8:00 AM", false, "Detailed content of the email...")
        // Add more emails here
    )

    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    if (selectedEmail != null) {
        navController.navigate("emailRead/${selectedEmail!!.sender}/${selectedEmail!!.subject}/${selectedEmail!!.content}/TRUE")
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
        EmailList(emails = emails) { email ->
            selectedEmail = email
        }

        OverlayButton {
            navController.navigate("emailRead/${""}/${""}/${""}/FALSE")
        }
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
            //val icon = painterResource(id = R.drawable.ic_baseline_add_24)
            //con(icon, contentDescription = "Add",modifier = Modifier.fillMaxWidth())
            Text(text = "+",textAlign = TextAlign.Center, fontSize = 35.sp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController =  rememberNavController()
    EmailScreen(navController)
}
