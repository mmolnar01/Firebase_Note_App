package hu.klm60o.android.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.klm60o.android.noteapp.databinding.ActivityLoginBinding
import hu.klm60o.android.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNoteButton.setOnClickListener {
            startActivity(Intent(this, NoteDetailsActivity::class.java))
        }
    }
}