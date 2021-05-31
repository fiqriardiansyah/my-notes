package com.example.mynote

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.example.mynote.databinding.ActivityAddBinding
import com.example.mynote.db.DatabaseNoteHelper
import com.example.mynote.model.Note
import java.text.SimpleDateFormat
import java.util.*

open class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var db: DatabaseNoteHelper
    private lateinit var note: Note
    private lateinit var currentNoteId: String

    companion object {
        const val EXTRA_NOTE = "note"
        const val IS_EDIT = "is_add"
        const val CURRENT_NOTE_ID = "current_note_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = Html.fromHtml("<p color=\"white\" >New Note</p>")
        supportActionBar?.elevation = 0f

        db = DatabaseNoteHelper(this)

        val isEdit = intent.getBooleanExtra(IS_EDIT,false)
        currentNoteId = intent.getStringExtra(CURRENT_NOTE_ID).toString()

        if(isEdit){
            supportActionBar?.title = Html.fromHtml("<p color=\"white\" >Edit Note</p>")
            note = intent.getParcelableExtra<Note>(EXTRA_NOTE) as Note
            binding.edtTitle.setText(note.title)
            binding.edtDesc.setText(note.desc)
        }

        binding.btnSave.setOnClickListener {
            val title = binding.edtTitle.text.trim().toString()
            val desc = binding.edtDesc.text.trim().toString()

            when{
                title.isEmpty() -> binding.edtTitle.error = "title required"
                desc.isEmpty() -> binding.edtDesc.error = "description required"
                else -> {
                    if(isEdit){
                        db.editNote(title,desc,getCurrentTime(),currentNoteId)
                    }else{
                        db.addNote(title,desc,getCurrentTime())
                    }
                    finish()
                }
            }
        }

        db.setOnCallback(object: DatabaseNoteHelper.OnCallbackFunction{
            override fun onCallback() {
                MainActivity().showToast()
            }
        })
    }

    fun getCurrentTime() : String{
        val daysString = arrayOf("","Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday")
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK);

        val sdf = SimpleDateFormat("hh:mm")

        return "${daysString[day]}, ${sdf.format(Date())}"
    }


}