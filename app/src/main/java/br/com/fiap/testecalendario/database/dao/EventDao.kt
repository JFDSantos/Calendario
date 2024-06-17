package br.com.fiap.testecalendario.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.testecalendario.model.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM tbl_event WHERE date = :date ORDER BY date ASC")
    fun BuscarEventos(date: String?): List<Event>

    @Query("SELECT * FROM tbl_event ORDER BY date ASC")
    fun Buscar(): List<Event>
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //fun salvar(event: Event)

    //@Query("DELETE FROM events WHERE date = :date")
    //fun excluir(date: LocalDate)

    @Insert
    fun salvar(event: Event): Long
//
//    @Update
//    fun atualizar(event: Event): Int

    @Delete
    fun excluir(event: Event): Int
}
