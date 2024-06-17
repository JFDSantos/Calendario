package br.com.fiap.testecalendario.database.repository

import br.com.fiap.testecalendario.database.dao.Emaildb
import android.content.Context
import br.com.fiap.testecalendario.model.Email

class EmailRepository (contex : Context) {

    var db = Emaildb.getDatabase(contex).eventDao()

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
}