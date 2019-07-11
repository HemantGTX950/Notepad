package com.hb.notepad.activities

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.hb.notepad.R
import com.hb.notepad.entities.Note
import com.hb.notepad.view_models.NotesViewModel
import kotlinx.android.synthetic.main.activity_note_edit.*

import java.text.SimpleDateFormat
import java.util.Date

class NoteEdit : AppCompatActivity()  {
    private lateinit var mTitleText: EditText
    private lateinit var mBodyText: EditText
    private lateinit var mDateText: TextView
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var deleteNoteItem:MenuItem
    private var id:Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        setSupportActionBar(toolbar_note)
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        setTitle(R.string.app_name)

        mTitleText = findViewById(R.id.title_input)
        mBodyText = findViewById(R.id.body_input)
        mDateText = findViewById(R.id.notelist_date)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init(intent)


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_note, menu)
        deleteNoteItem= menu.findItem(R.id.action_delete_note)
        if(id.equals(-1L)){
            deleteNoteItem.isVisible = false
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_delete_note ->{
                notesViewModel.deleteNote(id)
                Toast.makeText(this@NoteEdit,"Deleted!",Toast.LENGTH_SHORT).show()
                finish()
                return true
            }
            R.id.action_save_note->{
                saveNote()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun init(intent: Intent){


        if(!intent.hasExtra(EXTRA_ID)) {
            id=-1
            val msTime = System.currentTimeMillis()
            val curDateTime = Date(msTime)

            val formatter = SimpleDateFormat("d'/'M'/'y")
            curDate = formatter.format(curDateTime)
            val format = SimpleDateFormat("MMM")
            Log.d("data", format.format(curDateTime))

            mDateText!!.text = "" + curDate

        }else{
             id = intent.getLongExtra(EXTRA_ID,-1)
                if(!id.equals(-1)) {
                    val title = intent.getStringExtra(EXTRA_TITLE)
                    val body = intent.getStringExtra(EXTRA_BODY)
                    val time = intent.getLongExtra(EXTRA_TIME,Date().time)
                    mTitleText.setText(title)
                    mBodyText.setText(body)
                    val formatter = SimpleDateFormat("d'/'M'/'y")

                    mDateText.text = formatter.format(time)

                }


        }

    }
    private fun saveNote(){
        if(!(TextUtils.isEmpty(mBodyText.text) )){

            val title = mTitleText.text.toString()
            val body = mBodyText.text.toString()

            if(id.equals(-1L)){

                val note = Note(null,title,body,Date().time)
                notesViewModel.insert(note) {v->
                    id=v}
            }else{

                val note = Note(id,title,body,Date().time)
                notesViewModel.updateNote(note)
            }
            Toast.makeText(this@NoteEdit,"Saved!",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {

        saveNote()
        super.onBackPressed()
    }

    class LineEditText// we need this constructor for LayoutInflater
        (context: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatEditText(context, attrs) {

        private val mRect: Rect
        private val mPaint: Paint

        init {
            mRect = Rect()
            mPaint = Paint()
            mPaint.style = Paint.Style.FILL_AND_STROKE
            mPaint.color = Color.BLUE
        }

        override fun onDraw(canvas: Canvas) {

            val height = height
            val line_height = lineHeight

            var count = height / line_height

            if (lineCount > count)
                count = lineCount

            val r = mRect
            val paint = mPaint
            var baseline = getLineBounds(0, r)

            for (i in 0 until count) {

                canvas.drawLine(
                    r.left.toFloat(),
                    (baseline + 1).toFloat(),
                    r.right.toFloat(),
                    (baseline + 1).toFloat(),
                    paint
                )
                baseline += lineHeight

                super.onDraw(canvas)
            }

        }


    }

    companion object {
        var numTitle = 1
        var curDate = ""
        var curText = ""
        val EXTRA_TITLE="com.hb.notepad.title_reply"
        val EXTRA_BODY = "com.hb.notepad.body_reply"
        val EXTRA_TIME = "com.hb.notepad.extra_time"
        val EXTRA_ID = "com.hb.notepad.extra_id"
    }
}