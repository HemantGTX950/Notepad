package com.hb.notepad.activities

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.hb.notepad.R
import com.hb.notepad.adapters.NotesListAdapter
import com.hb.notepad.entities.Note

import com.hb.notepad.view_models.NotesViewModel

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var notesViewModel: NotesViewModel
    private var deleteAllItem: MenuItem? = null
    private var count =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)


        fab.setOnClickListener { view ->
            val intent = Intent(this@MainActivity,NoteEdit::class.java)
            startActivity(intent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotesListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesViewModel.allWords.observe(this, Observer { words->
            words?.let {
                Log.d("update",it.toString())
                adapter.setNotes(it)
                count = it.size

            }
            deleteAllItem?.isVisible = words?.size != 0

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        deleteAllItem = menu.findItem(R.id.action_delete_all)
        deleteAllItem?.isVisible=count>0
        return true
    }
    companion object {
        const val newWordActivityRequestCode = 1
        const val noteEditActivityRequestCode = 2
        const val noteEditRVRequestCode = 3
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_all ->{
                notesViewModel.deleteAll()
                return true
            }
            R.id.action_exit_app->{
                handleExit()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun handleExit(){

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Exit")
        builder.setMessage("Are you sure you want to exit?")
        builder.setNeutralButton("Cancel" ){_,_->

        }
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog,which->
            finish()
        }
        builder.setNegativeButton("No"){dialog, which ->  }
        val alertDialog = builder.create()
        alertDialog.show()
    }
    private fun handleDeleteAll(){

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Delete All")
        builder.setMessage("Are you sure you want to delete all notes?")
        builder.setNeutralButton("Cancel" ){_,_->

        }
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog,which->
            notesViewModel.deleteAll()
            Toast.makeText(this@MainActivity,"Deleted All Notes",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){dialog, which ->  }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
