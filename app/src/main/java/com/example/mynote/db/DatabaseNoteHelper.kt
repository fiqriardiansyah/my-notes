package com.example.mynote.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.mynote.MainActivity
import com.example.mynote.model.Note

class DatabaseNoteHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val context = context

    private var onCallbackFunction: OnCallbackFunction? = null

    interface OnCallbackFunction {
        fun onCallback()
    }

    fun setOnCallback(onCallbackFunction: OnCallbackFunction){
        this.onCallbackFunction = onCallbackFunction
    }

    companion object {
        const val DATABASE_NAME = "note.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_DATE = "date"
    }

    val query = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY," +
            "$COLUMN_TITLE TEXT," +
            "$COLUMN_DESCRIPTION TEXT," +
            "$COLUMN_DATE TEXT" +
            ");"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }


    fun addNote(title: String,desc: String,time: String){
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_TITLE,title)
        value.put(COLUMN_DESCRIPTION,desc)
        value.put(COLUMN_DATE,time)

        val result = db.insert(TABLE_NAME,null,value)

        if(result == -1L){
            Toast.makeText(context,"gagal disimpan",Toast.LENGTH_SHORT).show()
        }else{
            this.onCallbackFunction?.onCallback()
        }
    }

    fun editNote(title: String,desc: String,time: String,id: String){
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_TITLE,title)
        value.put(COLUMN_DESCRIPTION,desc)
        value.put(COLUMN_DATE,time)

        val result = db.update(TABLE_NAME,value,"$COLUMN_ID LIKE ?", arrayOf(id))
        if(result == -1){
            Toast.makeText(context,"gagal disimpan",Toast.LENGTH_SHORT).show()
        }else{
            this.onCallbackFunction?.onCallback()
        }

    }

    fun deleteNote(id: String){
        val db = writableDatabase

        val result = db.delete(TABLE_NAME,"$COLUMN_ID LIKE ?", arrayOf(id))
        if(result == -1){
            Toast.makeText(context,"gagal disimpan",Toast.LENGTH_SHORT).show()
        }else{
            this.onCallbackFunction?.onCallback()
        }
    }

    fun searchNote(title: String): ArrayList<Note>{
        val db = readableDatabase

        val listNotes: ArrayList<Note> = arrayListOf()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE LIKE ? ", arrayOf("%$title%"))
        with(cursor){
            while (moveToNext()){
                val id = getInt(getColumnIndex(COLUMN_ID))
                val title = getString(getColumnIndex(COLUMN_TITLE))
                val desc = getString(getColumnIndex(COLUMN_DESCRIPTION))
                val date = getString(getColumnIndex(COLUMN_DATE))
                listNotes.add(Note(id,title,desc,date))
            }
        }

        return listNotes
    }

    fun allNote(): ArrayList<Note>{

        val listNotes: ArrayList<Note> = ArrayList()

        val db = readableDatabase

        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
            COLUMN_DATE),null,null,null,null,null)

        with(cursor){
            while (moveToNext()){
                val id = getInt(getColumnIndex(COLUMN_ID))
                val title = getString(getColumnIndex(COLUMN_TITLE))
                val desc = getString(getColumnIndex(COLUMN_DESCRIPTION))
                val date = getString(getColumnIndex(COLUMN_DATE))
                listNotes.add(Note(id,title,desc,date))
            }
        }

        return listNotes
    }
}