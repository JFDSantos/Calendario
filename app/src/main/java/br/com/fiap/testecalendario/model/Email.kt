package br.com.fiap.testecalendario.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_email")
data class Email(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sender: String,
    val subject: String,
    val preview: String,
    val date: String,
    var isImportant: Boolean = false,
    var isFavorite: Boolean = false,
    val content: String
)
