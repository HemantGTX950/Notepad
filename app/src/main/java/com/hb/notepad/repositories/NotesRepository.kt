package com.hb.notepad.repositories

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.support.annotation.WorkerThread
import android.util.Log
import com.hb.notepad.entities.Note
import com.hb.notepad.interfaces.NotesDao

class NotesRepository(private val notesDao: NotesDao){
    val allWords:LiveData<List<Note>> = notesDao.getAllNotes()

    @WorkerThread
    fun insert(note: Note,f:(id:Long)->Unit){
        InsertNoteAsync(notesDao,f).execute(note)
    }

    @WorkerThread
    fun updateNote(note:Note){
        UpdateNoteAsync(notesDao).execute(note)
    }
    @WorkerThread
     fun deleteNote(id: Long){
        deleteNoteAsync(notesDao).execute(id)
    }
    @WorkerThread
    fun deleteAll(){
        deleteAllAsync(notesDao).execute()
    }
    private class InsertNoteAsync(private var notesDao: NotesDao,private var f:(id:Long)->Unit) : AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg params: Note?): Void? {
            var note=params[0]
            if (note != null) {
                Log.d("saved","yes"+note.id)
                val l = notesDao.insert(note)
                Log.d("savedid",""+l)
                f(l)
            }else{
                Log.d("saved","no")
            }
            return null
        }
    }
    private class UpdateNoteAsync(private var notesDao: NotesDao) : AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg params: Note?): Void? {
            var note=params[0]
            if (note != null) {
                Log.d("update","yes"+note.id)
                notesDao.updateNote(note)
            }
            return null
        }
    }
    private class deleteNoteAsync(private var notesDao: NotesDao) : AsyncTask<Long, Void, Void>() {

        override fun doInBackground(vararg params: Long?): Void? {
            var note=params[0]
            if (note != null) {
                notesDao.deleteNote(note)
            }
            return null
        }
    }
    private class deleteAllAsync(private var notesDao: NotesDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            notesDao.deleteAll()
            return null
        }
    }
}