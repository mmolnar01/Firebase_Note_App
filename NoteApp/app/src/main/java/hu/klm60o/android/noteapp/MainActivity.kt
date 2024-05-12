package hu.klm60o.android.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import hu.klm60o.android.noteapp.databinding.ActivityLoginBinding
import hu.klm60o.android.noteapp.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics


        binding.addNoteButton.setOnClickListener {
            noteDetails()
            //startActivity(Intent(this, NoteDetailsActivity::class.java))
        }

        binding.menuButton.setOnClickListener {
            logOut()
        }

        fillRecyclerView()

    }

    private fun noteDetails() {
        logButtonClick("AddNoteButtonClick")
        startActivity(Intent(this, NoteDetailsActivity::class.java))
    }


    //Egy gombra való nyomást logol a Firebase Analytics segítségével
    private fun logButtonClick(buttonName: String) {
        var params = Bundle()
        params.putInt("ButtonID", binding.root.id)
        var bName = buttonName
        firebaseAnalytics.logEvent(bName, params)
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.notifyDataSetChanged()
    }

    //Logs out from the app
    private fun logOut() {
        logButtonClick("LogOutButtonClick")
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    //Fills the recycler view with the data from Firestore
    private fun fillRecyclerView() {
        var user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            var notes = FirebaseFirestore.getInstance().collection("notes")
                .document(user.uid).collection("my_notes")

            var query = notes.orderBy("timestamp", Query.Direction.DESCENDING)
            var options = FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note::class.java).build()

            binding.noteRecyclerView.layoutManager = LinearLayoutManager(this)
            noteAdapter = NoteAdapter(options, this)
            binding.noteRecyclerView.adapter = noteAdapter
        }
    }

}