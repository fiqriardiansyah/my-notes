package com.example.mynote.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.R
import com.example.mynote.model.Note
import kotlin.coroutines.coroutineContext

class NotesAdapter(context: Context): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    val context = context

    private lateinit var onClickListener: OnClickListener

    interface OnClickListener {
        fun onClick(data: Note)
    }

    fun setOnClickItem(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    var listNotes: ArrayList<Note> = ArrayList()

    fun setData(data: ArrayList<Note>){
        this.listNotes = data
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val desc = itemView.findViewById<TextView>(R.id.tv_desc)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
        val layout = itemView.findViewById<RelativeLayout>(R.id.item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listNotes[position]
        holder.title.text = note.title
        holder.desc.text = note.desc
        holder.date.text = note.time


        val animation = AnimationUtils.loadAnimation(context,R.anim.translate_anim)
        holder.layout.animation = animation

        holder.itemView.setOnClickListener{
            this.onClickListener.onClick(note)
        }
    }

    override fun getItemCount(): Int = listNotes.size
}