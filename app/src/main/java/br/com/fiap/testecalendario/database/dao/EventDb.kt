package br.com.fiap.testecalendario.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.fiap.testecalendario.model.Event

@Database(
    entities = [Event::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class EventDb : RoomDatabase(){

    abstract fun eventDao() : EventDao

    companion object {

        private lateinit var instance: EventDb

        fun getDatabase(context: Context): EventDb {
            if (!::instance.isInitialized) {
                instance =
                    Room.databaseBuilder(
                        context,
                        EventDb::class.java,
                        "contato_db"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration() //somente teste rs rs
                        .build()
            }
            return instance
        }
    }
}