package com.ifsp.giovanna.moviesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ifsp.giovanna.moviesmanager.controller.MovieController
import com.ifsp.giovanna.moviesmanager.R
import com.ifsp.giovanna.moviesmanager.adapter.MovieAdapter
import com.ifsp.giovanna.moviesmanager.databinding.ActivityMainBinding
import com.ifsp.giovanna.moviesmanager.model.Constant.EXTRA_MOVIE
import com.ifsp.giovanna.moviesmanager.model.Constant.VIEW_MOVIE
import com.ifsp.giovanna.moviesmanager.model.entity.Movie

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val moviesList: MutableList<Movie> by lazy {
        movieController.getMovies()
    }

    private lateinit var movieAdapter: MovieAdapter

    private lateinit var marl: ActivityResultLauncher<Intent>

    private val movieController: MovieController by lazy {
        MovieController(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        movieAdapter = MovieAdapter(this, moviesList)
        amb.moviesLv.adapter = movieAdapter

        marl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val movie = result.data?.getParcelableExtra<Movie>(EXTRA_MOVIE)

                movie?.let { _movie->
                    val position = moviesList.indexOfFirst { it.id == _movie.id }
                    if (position != -1) {
                        moviesList[position] = _movie
                        movieController.editMovie(_movie)
                    }
                    else {
                        _movie.id = movieController.insertMovie(_movie)
                        moviesList.add(_movie)
                    }
                    moviesList.sortBy { it.name }
                    movieAdapter.notifyDataSetChanged()
                }
            }
        }



        registerForContextMenu(amb.moviesLv)

        amb.moviesLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val movie = moviesList[position]
                val movieIntent = Intent(this@MainActivity, MovieActivity::class.java)
                movieIntent.putExtra(EXTRA_MOVIE, movie)
                movieIntent.putExtra(VIEW_MOVIE, true)
                startActivity(movieIntent)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addMovieMi -> {
                marl.launch(Intent(this, MovieActivity::class.java))
                true
            }

            R.id.changeOrderRateMi -> {
                moviesList.sortBy { it.rate }
                movieAdapter.notifyDataSetChanged()
                true
            }

            R.id.changeOrderNameMi -> {
                moviesList.sortBy { it.name }
                movieAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }
    }




    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when(item.itemId) {
            R.id.removeMovieMi -> {
                movieController.removeMovie(moviesList[position].id)
                moviesList.removeAt(position)
                movieAdapter.notifyDataSetChanged()
                true
            }
            R.id.editMovieMi -> {
                val movie = moviesList[position]
                val movieIntent = Intent(this, MovieActivity::class.java)
                movieIntent.putExtra(EXTRA_MOVIE, movie)
                movieIntent.putExtra(VIEW_MOVIE, false)
                marl.launch(movieIntent)
                true
            }
            else -> { false }
        }
    }

}