package br.com.fiap.testecalendario.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import br.com.fiap.testecalendario.model.Event
import java.time.LocalDate

@Dao
interface EventDao {

    //@Query("SELECT * FROM events ORDER BY date ASC")
    //fun getAllEvents(): Flow<List<Event>>

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //fun salvar(event: Event)

    //@Query("DELETE FROM events WHERE date = :date")
    //fun excluir(date: LocalDate)

    @Insert
    fun salvar(event: Event): Long
//
//    @Update
//    fun atualizar(event: Event): Int

    //@Delete
    //fun excluir(dateString: String): Int
}
