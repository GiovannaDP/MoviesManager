package com.ifsp.giovanna.moviesmanager

import com.ifsp.giovanna.moviesmanager.model.Movie
import com.ifsp.giovanna.moviesmanager.model.dao.MovieDao
import com.ifsp.giovanna.moviesmanager.model.database.MovieDaoSqlite
import com.ifsp.giovanna.moviesmanager.view.MainActivity

class MovieController (mainActivity: MainActivity) {
    private val movieDaoImpl: MovieDao = MovieDaoSqlite(mainActivity)

    fun insertMovie(movie: Movie) = movieDaoImpl.createMovie(movie)
    fun getMovie(id: Int) = movieDaoImpl.retrieveMovie(id)
    fun getMovies() = movieDaoImpl.retrieveMovies()
    fun editMovie(movie: Movie) = movieDaoImpl.updateMovie(movie)
    fun removeMovie(id: Int) = movieDaoImpl.deleteMovie(id)
}