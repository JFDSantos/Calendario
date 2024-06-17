package br.com.fiap.testecalendario.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.testecalendario.model.Email

@Dao
interface EmailDao {
    @Query("SELECT * FROM tbl_email WHERE sender = :sender ORDER BY date ASC")
    fun BuscarEmailsRemetente(sender: String?): List<Email>

    @Query("SELECT * FROM tbl_email WHERE isFavorite is 1 ORDER BY date ASC")
    fun BuscarEmailsFavoritos(): List<Email>

    @Query("SELECT * FROM tbl_email WHERE isImportant is 1 ORDER BY date ASC")
    fun BuscarEmailsImportantes(): List<Email>

    @Query("SELECT * FROM tbl_email ORDER BY date ASC")
    fun Buscar(): List<Email>

    @Insert
    fun salvar(email: Email): Long
//
//    @Update
//    fun atualizar(event: Event): Int

    @Delete
    fun excluir(email: Email): Int
}