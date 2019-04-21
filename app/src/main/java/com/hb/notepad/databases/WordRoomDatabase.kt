package com.hb.notepad.databases

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.hb.notepad.entities.Note
import com.hb.notepad.interfaces.NotesDao
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch
import java.util.*

@Database(entities = [Note::class], version = 1)
public abstract class WordRoomDatabase:RoomDatabase(){
    abstract fun notesDao():NotesDao
    companion object {
        @Volatile
        private var INSTANCE:WordRoomDatabase?=null

        fun getDatabase(context:Context,scope:CoroutineScope):WordRoomDatabase{
            return INSTANCE?: synchronized(this){
                //create db
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "Word_database"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

           /* INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.notesDao())
                }
            }*/
        }
        fun populateDatabase(notesDao: NotesDao) {
            notesDao.deleteAll()

            var word = Note(null,title ="Hello",body="world",time= Date().time)
            notesDao.insert(word)
            word = Note(null,"World!","is good",Date().time)
            notesDao.insert(word)
        }
    }

}