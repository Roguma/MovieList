package edu.msudenver.cs3013.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private val movieList: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView
        val yearTextView: TextView
        val genreTextView: TextView
        val ratingTextView: TextView

        init {
            titleTextView = itemView.findViewById(R.id.titleTextView)
            yearTextView = itemView.findViewById(R.id.yearTextView)
            genreTextView = itemView.findViewById(R.id.genreTextView)
            ratingTextView = itemView.findViewById(R.id.ratingTextView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.titleTextView.text = currentMovie.title
        holder.yearTextView.text = "Year: ${currentMovie.year}"
        holder.genreTextView.text = "Genre: ${currentMovie.genre}"
        holder.ratingTextView.text = "Rating: ${currentMovie.rating}"
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < movieList.size) {
            movieList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class SwipeToDeleteCallback :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return if (viewHolder is MovieViewHolder) {
                makeMovementFlags(
                    ItemTouchHelper.ACTION_STATE_IDLE,
                    ItemTouchHelper.RIGHT
                ) or makeMovementFlags(
                    ItemTouchHelper.ACTION_STATE_SWIPE,
                    ItemTouchHelper.RIGHT
                )
            } else {
                0
            }
        }


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                removeItem(position)
            }
        }
    }

    val swipeToDeleteCallback = SwipeToDeleteCallback()
}