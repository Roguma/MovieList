package edu.msudenver.cs3013.movielist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast

class AddMovieActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var editYear: EditText
    private lateinit var editGenre: EditText
    private lateinit var editRating: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        editTitle = findViewById(R.id.editTitle)
        editYear = findViewById(R.id.editYear)
        editGenre = findViewById(R.id.editGenre)
        editRating = findViewById(R.id.editRating)
    }

    fun backToFirst(v: View) {
        val title = editTitle.text.toString().trim()
        val year = editYear.text.toString().trim()
        val genre = editGenre.text.toString().trim()
        val rating = editRating.text.toString().trim()

        if (title.isEmpty() || year.isEmpty() || genre.isEmpty() || rating.isEmpty()) {
            Log.w("ADDMOVIE", "One or more fields are empty.")
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        setMovieInfo(title, year, genre, rating)
    }

    private fun setMovieInfo(title: String, year: String, genre: String, rating: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("title", title)
        resultIntent.putExtra("year", year)
        resultIntent.putExtra("genre", genre)
        resultIntent.putExtra("rating", rating)

        setResult(Activity.RESULT_OK, resultIntent)
        Log.d("ADDMOVIE", "Sending data back: $title, $year, $genre, $rating")
        finish()
    }
}