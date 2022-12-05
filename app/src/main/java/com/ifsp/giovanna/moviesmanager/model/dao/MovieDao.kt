package com.ifsp.giovanna.moviesmanager.model.dao

import com.ifsp.giovanna.moviesmanager.model.Movie

interface MovieDao {
    fun createMovie(contact: Movie): Int
    fun retrieveMovie(id: Int): Movie?
    fun retrieveMovies(): MutableList<Movie>
    fun updateMovie(contact: Movie): Int
    fun deleteMovie(id: Int): Int
}