// Rory Oguma CS3013 Major Project 3 Movie List

package edu.msudenver.cs3013.movielist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private val movieList = ArrayList<Movie>()
    private lateinit var myPlace: File

    companion object {
        private const val ADD_MOVIE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFileLocation()

        recyclerView = findViewById(R.id.recyclerView)
        val currentLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = currentLayoutManager

        movieAdapter = MovieAdapter(movieList)
        recyclerView.adapter = movieAdapter

        readFile()
        movieAdapter.notifyDataSetChanged()

        val itemTouchHelper = ItemTouchHelper(movieAdapter.swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_MOVIE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val title = data.getStringExtra("title") ?: ""
                val year = data.getStringExtra("year") ?: ""
                val genre = data.getStringExtra("genre") ?: ""
                val rating = data.getStringExtra("rating") ?: ""

                if (title.isNotEmpty() && year.isNotEmpty() && genre.isNotEmpty() && rating.isNotEmpty()) {
                    val newMovie = Movie(title, year, genre, rating)
                    movieList.add(newMovie)
                    movieAdapter.notifyItemInserted(movieList.size - 1)
                    Log.d("MOVIELIST", "Added movie via onActivityResult: $newMovie")
                } else {
                    Log.w("MOVIELIST", "Received incomplete movie data back via onActivityResult.")
                }
            }
        } else {
            Log.d("MOVIELIST", "Add movie cancelled or failed via onActivityResult.")
        }
    }


    private fun initializeFileLocation() {
        myPlace = filesDir
        Log.d("MOVIELIST", "File storage path: ${myPlace.absolutePath}")
    }

    private fun readFile() {
        Log.d("MOVIELIST", "readFile() called")
        val file = File(myPlace, "MOVIELIST.csv")
        movieList.clear()

        if (!file.exists()) {
            Log.d("MOVIELIST", "MOVIELIST.csv does not exist. Populating with dummy data.")
            movieList.add(Movie("The Godfather", "1972", "Crime", "9.2"))
            movieList.add(Movie("The Dark Knight", "2008", "Action", "9.0"))
            movieList.add(Movie("The Matrix", "1999", "Science Fiction", "8.7"))
            return
        }

        try {
            file.forEachLine { line ->
                val tokens = line.split(",")
                if (tokens.size == 4) {
                    movieList.add(Movie(tokens[0].trim(), tokens[1].trim(), tokens[2].trim(), tokens[3].trim()))
                } else {
                    Log.w("MOVIELIST", "Skipping malformed line in CSV: $line")
                }
            }
            Log.d("MOVIELIST", "${movieList.size} movies loaded from file.")
        } catch (e: IOException) {
            Log.e("MOVIELIST", "Error reading MOVIELIST.csv", e)
        }

    }

    private fun writeFile() {
        Log.d("MOVIELIST", "writeFile() called")
        val file = File(myPlace, "MOVIELIST.csv")
        try {
            file.printWriter().use { out ->
                movieList.forEach { movie ->
                    out.println("${movie.title},${movie.year},${movie.genre},${movie.rating}")
                }
            }
            Log.d("MOVIELIST", "${movieList.size} movies saved to file.")
        } catch (e: IOException) {
            Log.e("MOVIELIST", "Error writing MOVIELIST.csv", e)
        }
    }

    fun saveList(v: View) {
        writeFile()
    }

    fun startSecond(v: View) {
        val intent = Intent(this, AddMovieActivity::class.java)
        startActivityForResult(intent, ADD_MOVIE_REQUEST)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ratingSort -> {
                Log.d("MOVIELIST", "Sorting by Rating")
                movieList.sortByDescending { it.rating.toDoubleOrNull() ?: 0.0 }
                movieAdapter.notifyDataSetChanged()
                return true
            }
            R.id.yearSort -> {
                Log.d("MOVIELIST", "Sorting by Year")
                movieList.sortByDescending { it.year.toIntOrNull() ?: 0 }
                movieAdapter.notifyDataSetChanged()
                return true
            }
            R.id.genreSort -> {
                Log.d("MOVIELIST", "Sorting by Genre")
                movieList.sortBy { it.genre.lowercase() }
                movieAdapter.notifyDataSetChanged()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}