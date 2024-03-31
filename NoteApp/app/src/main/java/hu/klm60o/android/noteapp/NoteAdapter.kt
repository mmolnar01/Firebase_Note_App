package hu.klm60o.android.noteapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import hu.klm60o.android.noteapp.databinding.RecyclerNoteItemBinding
import java.text.SimpleDateFormat

class NoteAdapter :
    FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    private var context: Context

    constructor(options: FirestoreRecyclerOptions<Note>, context: Context) : super(options) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        var view =
        RecyclerNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.binding.noteTitleTextView.setText(model.title)
        holder.binding.noteContentTextView.setText(model.text)
        holder.binding.noteTimestampTextView.setText(SimpleDateFormat("yyyy/MM/dd hh:mm").format(model.timestamp?.toDate()
            ?: null))
    }
    inner class NoteViewHolder(val binding: RecyclerNoteItemBinding) : RecyclerView.ViewHolder(binding.root)
}