
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.testecalendario.database.dao.EmailDao
import br.com.fiap.testecalendario.model.Email

@Database(
    entities = [Email::class],
    version = 1
)
abstract class EmailDb : RoomDatabase(){

    abstract fun emailDao() : EmailDao

    companion object {

        private lateinit var instance: EmailDb

        fun getDatabase(context: Context): EmailDb {
            if (!::instance.isInitialized) {
                instance =
                    Room.databaseBuilder(
                        context,
                        EmailDb::class.java,
                        "email_db"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration() //somente teste rs rs
                        .build()
            }
            return instance
        }
    }
}