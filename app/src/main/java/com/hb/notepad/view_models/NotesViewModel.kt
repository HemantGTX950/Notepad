package com.hb.notepad.view_models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import com.hb.notepad.databases.WordRoomDatabase
import com.hb.notepad.entities.Note
import com.hb.notepad.repositories.NotesRepository
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext


class NotesViewModel(application: Application):AndroidViewModel(application){
    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
        get() = parentJob+ Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository:NotesRepository
    val allWords:LiveData<List<Note>>
    init {
        val wordsDao = WordRoomDatabase.getDatabase(application,scope).notesDao()
        repository = NotesRepository(wordsDao)
        allWords = repository.allWords
    }
    fun insert(note: Note,f:(v:Long)->Unit)= scope.launch(Dispatchers.IO){

        repository.insert(note,f)

    }
    fun deleteNote(id:Long)= scope.launch(Dispatchers.IO){
        repository.deleteNote(id)
    }
    fun updateNote(note: Note)= scope.launch (Dispatchers.IO){
        repository.updateNote(note)
    }
    fun deleteAll()=scope.launch(Dispatchers.IO){
        repository.deleteAll()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
