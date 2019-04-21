package com.hb.notepad.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.hb.notepad.entities.Note

@Dao
interface NotesDao{

    @Query("Select * from notes_table ORDER BY time DESC")
    fun getAllNotes():LiveData< List<Note> >

    @Query("Select * from notes_table where :id=note_id")
    fun getNote(id: Long):Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note):Long
    @Query("Delete FROM notes_table")
    fun deleteAll()
    @Query("Delete FROM notes_table where :id = note_id")
    fun deleteNote(id:Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(vararg note: Note)
}