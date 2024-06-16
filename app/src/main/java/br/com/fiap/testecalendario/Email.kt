package br.com.fiap.testecalendario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import br.com.fiap.testecalendario.ui.theme.TesteCalendarioTheme

data class Email(val sender: String, val subject: String, val preview: String, val time: String)

@Composable
fun EmailItem(email: Email, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle email click */ }
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
            Text(text = email.sender, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = email.subject, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = email.preview, color = Color.Gray, maxLines = 1)
        }
        Text(text = email.time, color = Color.Gray)
    }
}

@Composable
fun EmailList(emails: List<Email>) {
    LazyColumn {
        items(emails) { email ->
            EmailItem(email = email)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun EmailScreen(navController: NavController) {

    val emails = listOf(
        Email("John Doe", "Meeting Reminder", "Don't forget our meeting at 10am", "10:00 AM"),
        Email("Jane Smith", "Sale Alert!", "Check out our latest sale on electronics", "9:30 AM"),
        Email("Service", "Your Order Confirmation", "Thank you for your purchase", "8:00 AM")
        // Add more emails here
    )
    EmailList(emails = emails)

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    TesteCalendarioTheme {
        val emails = listOf(
            Email("John Doe", "Meeting Reminder", "Don't forget our meeting at 10am", "10:00 AM"),
            Email(
                "Jane Smith",
                "Sale Alert!",
                "Check out our latest sale on electronics",
                "9:30 AM"
            ),
            Email("Service", "Your Order Confirmation", "Thank you for your purchase", "8:00 AM")
            // Add more emails here
        )
        EmailList(emails = emails)
    }
}
