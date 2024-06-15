package br.com.fiap.testecalendario.database.repository

import android.content.Context
import br.com.fiap.testecalendario.database.dao.EventDb
import br.com.fiap.testecalendario.model.Event

class EventRepository (contex : Context){

    var db = EventDb.getDatabase(contex).eventDao()

    fun salvar(event: Event): Long {
        return db.salvar(event)
    }

//    fun atualizar(event: Event) : Int{
//        return db.atualizar(event)
//    }

//    fun excluir(dateString: String): Int {
//        return db.excluir(dateString)
//    }
}