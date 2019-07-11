package com.hb.notepad.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hb.notepad.R
import com.hb.notepad.activities.MainActivity
import com.hb.notepad.activities.NoteEdit
import com.hb.notepad.activities.NoteEdit.Companion.EXTRA_BODY
import com.hb.notepad.activities.NoteEdit.Companion.EXTRA_ID
import com.hb.notepad.activities.NoteEdit.Companion.EXTRA_TIME
import com.hb.notepad.activities.NoteEdit.Companion.EXTRA_TITLE
import com.hb.notepad.entities.Note

import java.text.SimpleDateFormat
import java.util.*

class NotesListAdapter internal constructor(
    val context: Context
):RecyclerView.Adapter<NotesListAdapter.WordViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item,parent,false)
        return WordViewHolder(itemView,context)

    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(p0: WordViewHolder, p1: Int) {
        val current = notes[p1]
        p0.noteTitleView.text = if(current.title!="") current.title else current.body
        var formatter = SimpleDateFormat("MMM, dd")
        var dt = Date(notes[p1].time)
        p0.noteDateView.text = formatter.format(dt)
        p0.root.setOnClickListener {



            noteClicked(notes[p1])

        }


    }
    private fun noteClicked(note:Note){
        var intent = Intent(context,NoteEdit::class.java);
        intent.putExtra(EXTRA_ID,note.id)
        intent.putExtra(EXTRA_TITLE,note.title)
        intent.putExtra(EXTRA_BODY,note.body)
        intent.putExtra(EXTRA_TIME,note.time)
        var ct = context as MainActivity
        ct.startActivity(intent)
    }
    internal fun setNotes(notes:List<Note>){
        this.notes= notes
        notifyDataSetChanged()
    }

    private val inflater:LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>()
    inner class WordViewHolder(itemView: View,context: Context):RecyclerView.ViewHolder(itemView){
        val root:View = itemView.findViewById(R.id.recycler_item_root)
        val noteTitleView:TextView = itemView.findViewById(R.id.note_title_rv)
        val noteDateView:TextView =itemView.findViewById(R.id.note_date_rv)

    }

}


