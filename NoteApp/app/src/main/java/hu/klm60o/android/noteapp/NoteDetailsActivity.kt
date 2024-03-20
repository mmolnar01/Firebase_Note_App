package hu.klm60o.android.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.klm60o.android.noteapp.databinding.ActivityMainBinding
import hu.klm60o.android.noteapp.databinding.ActivityNoteDetailsBinding

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNoteSaveButton.setOnClickListener {
            saveNote()
        }

    }

    private fun saveNote() {
        var title = binding.addNoteTitle.text.toString()
        var text = binding.addNoteText.text.toString()

        if (title.isEmpty()) {
            binding.addNoteTitle.setError("Title required!")
            return
        }
        return
    }
}