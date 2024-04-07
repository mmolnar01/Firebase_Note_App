package hu.klm60o.android.noteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import hu.klm60o.android.noteapp.databinding.ActivityMainBinding
import hu.klm60o.android.noteapp.databinding.ActivityNoteDetailsBinding

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailsBinding
    private lateinit var docId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        docId = intent.getStringExtra("docid").toString()


        if (docId != null) {
            binding.addNoteTopBarText.setText("Edit Note")
            binding.addNoteTitle.setText(intent.getStringExtra("title"))
            binding.addNoteText.setText(intent.getStringExtra("text"))
        }

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
        var note = Note(title, text, Timestamp.now())
        saveNoteToFireStore(note)
        return
    }

    private fun saveNoteToFireStore(note: Note) {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var documentReference: DocumentReference
        if (currentUser != null) {
            if (docId != null) {
                documentReference = FirebaseFirestore.getInstance().collection("notes")
                    .document(currentUser.uid).collection("my_notes").document(docId)
            } else {
                documentReference = FirebaseFirestore.getInstance().collection("notes")
                    .document(currentUser.uid).collection("my_notes").document()
            }

            documentReference.set(note).addOnCompleteListener(this) {
                task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "addNoteToFirestore:success")
                    Toast.makeText(
                        this,
                        "Note Added To Database Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Log.w(ContentValues.TAG, "addNoteToFirestore:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Failed Adding Note To Database",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

            /*var documentReferencee = FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.uid).collection("my_notes").add(note).addOnCompleteListener(this) {
                    task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "addNoteToFirestore:success")
                        Toast.makeText(
                            this,
                            "Note Added To Database Successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Log.w(ContentValues.TAG, "addNoteToFirestore:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Failed Adding Note To Database",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }*/
        }

    }
}