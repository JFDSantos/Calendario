package br.com.fiap.testecalendario.database.repository

import android.content.Context
import br.com.fiap.testecalendario.database.dao.EmailDb
import br.com.fiap.testecalendario.model.Email

class EmailRepository (contex : Context) {

    var db = EmailDb.getDatabase(contex).emailDao()

    fun salvar(email: Email): Long {
        return db.salvar(email)
    }
    fun buscarEmailsRementente(sender: String?) : List<Email>{
        return db.BuscarEmailsRemetente(sender)
    }
    fun buscarEmailsImportantes() : List<Email>{
        return db.BuscarEmailsImportantes()
    }
    fun buscarEmailsFavoritos() : List<Email>{
        return db.BuscarEmailsFavoritos()
    }

    fun buscar() : List<Email>{
        return db.Buscar()
    }

    fun excluir(email: Email): Int {
        return db.excluir(email)
    }

    fun atualizarImportante(isImportant: Boolean, id: Int): Int {
        return db.atualizarImportante(isImportant, id)
    }
    fun atualizarFavorito(isFavorite: Boolean, id: Int): Int {
        return db.atualizarFavorito(isFavorite, id)
    }
}