package com.hb.notepad.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName="notes_table")
data class Note (@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "note_id") var id:Long?,
                 @ColumnInfo(name = "title") var title:String,
                 @ColumnInfo(name = "body") var body:String,
                 @ColumnInfo(name="time") var time:Long
     ){
    constructor():this(null,"","", Date().time)
}
