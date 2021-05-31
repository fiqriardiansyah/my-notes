package com.example.mynote

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AlertDialog
import com.example.mynote.databinding.ActivityDetailBinding
import com.example.mynote.db.DatabaseNoteHelper
import com.example.mynote.model.Note

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: DatabaseNoteHelper

    companion object {
        const val EXTRA_NOTE = "note"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowHomeEnabled(true)

        db = DatabaseNoteHelper(this)

        val note = intent.getParcelableExtra<Note>(EXTRA_NOTE) as Note

        binding.tvTitleDetail.text = note.title
        binding.tvDateDetail.text = note.time
        binding.tvDescDetail.text = note.desc

        supportActionBar?.title = Html.fromHtml("<p color=\"white\" >${note.title}</p>")

        binding.btnEdit.setOnClickListener {
            val mIntent = Intent(this@DetailActivity,AddActivity::class.java)
            mIntent.putExtra(AddActivity.IS_EDIT,true)

            val currentNote = Note()
            currentNote.title = note.title
            currentNote.time = note.time
            currentNote.desc = note.desc

            mIntent.putExtra(AddActivity.EXTRA_NOTE,currentNote)
            mIntent.putExtra(AddActivity.CURRENT_NOTE_ID,note.id.toString())
            startActivity(mIntent)
            finish()
        }

        binding.btnDelete.setOnClickListener {

            val mAlertDialog = AlertDialog.Builder(this)
            mAlertDialog.setTitle("DELETE ?")
            mAlertDialog.setMessage("delete note \"${note.title}\" ?")
            mAlertDialog.setIcon(R.drawable.ic_baseline_error_outline_24)
            mAlertDialog.setPositiveButton("YES",DialogInterface.OnClickListener{ dialog,id ->
                db.deleteNote(note.id.toString())
                finish()
            })
            mAlertDialog.setNegativeButton("NO",DialogInterface.OnClickListener{ dialog, id ->
                dialog.dismiss()
            })
            mAlertDialog.show()

        }

    }


}