package com.example.mynote

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynote.adapter.NotesAdapter
import com.example.mynote.databinding.ActivityMainBinding
import com.example.mynote.db.DatabaseNoteHelper
import com.example.mynote.model.Note

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var db: DatabaseNoteHelper
    private lateinit var layout: View

    companion object {
        lateinit var toast: Toast
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = Html.fromHtml("<p color=\"white\" >Notes</p>")
        supportActionBar?.elevation = 0f
        
        db = DatabaseNoteHelper(this)

        layout = layoutInflater.inflate(R.layout.layout_toast,findViewById(R.id.layout_toast))
        toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        adapter = NotesAdapter(this)
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.adapter = adapter
        adapter.setOnClickItem(object :NotesAdapter.OnClickListener{
            override fun onClick(data: Note) {
                val mIntent = Intent(this@MainActivity,DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_NOTE,data)
                startActivity(mIntent)
            }
        })

        binding.addFab.setOnClickListener{
            val mIntent = Intent(this@MainActivity,AddActivity::class.java)
            startActivity(mIntent)
        }

        binding.edtNoteSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val listNote = db.searchNote(text.toString())

                if(listNote.size == 0){
                    binding.emptyAnim.visibility = View.VISIBLE
                }else{
                    binding.emptyAnim.visibility = View.INVISIBLE
                }

                adapter.setData(listNote)
                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    fun showToast() = toast.show()

    override fun onResume() {
        super.onResume()

        val listNote = db.allNote()

        if(listNote.size == 0){
            binding.emptyAnim.visibility = View.VISIBLE
        }else{
            binding.emptyAnim.visibility = View.INVISIBLE
        }

        adapter.setData(listNote)
        adapter.notifyDataSetChanged()
    }

}