package edu.uw.ischool.scottng.memorymentor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlashcardAdapter(private val flashcards: List<Flashcard>) : RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.txt_flashcard_question)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_flashcard, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.textView.text = flashcard.question

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, flashcard)
            }
        }
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, flashcard: Flashcard)
    }
}
