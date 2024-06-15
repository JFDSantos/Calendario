package br.com.fiap.testecalendario.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val description: String
)
