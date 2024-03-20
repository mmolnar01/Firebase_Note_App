package hu.klm60o.android.noteapp

import com.google.firebase.Timestamp

class Note {
    var title: String? = null
    var text: String? = null
    var timestamp: Timestamp? = null

    constructor(title: String, text: String, timestamp: Timestamp) {
        this.title = title
        this.text = text
        this.timestamp = timestamp
    }
}