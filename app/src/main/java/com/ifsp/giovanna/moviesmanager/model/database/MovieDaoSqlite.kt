package com.ifsp.giovanna.moviesmanager.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ifsp.giovanna.moviesmanager.R
import com.ifsp.giovanna.moviesmanager.model.entity.Movie
import com.ifsp.giovanna.moviesmanager.model.dao.MovieDao
import java.sql.SQLException

class MovieDaoSqlite(context: Context) : MovieDao {
    companion object Constant {
        private const val MOVIE_DATABASE_FILE = "movies"
        private const val MOVIE_TABLE = "movie"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val YEAR_COLUMN = "year"
        private const val STUDIO_COLUMN = "studio"
        private const val PRODUCER_COLUMN = "producer"
        private const val TIME_COLUMN = "time"
        private const val WATCHED_COLUMN = "watched"
        private const val RATE_COLUMN = "rate"
        private const val GENDER_COLUMN = "gender"


        private const val CREATE_MOVIE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $MOVIE_TABLE ( " +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$YEAR_COLUMN TEXT NOT NULL, " +
                    "$STUDIO_COLUMN TEXT NOT NULL, " +
                    "$PRODUCER_COLUMN TEXT NOT NULL, " +
                    "$TIME_COLUMN TEXT NOT NULL, " +
                    "$WATCHED_COLUMN TEXT NOT NULL, " +
                    "$RATE_COLUMN TEXT NOT NULL, " +
                    "$GENDER_COLUMN TEXT NOT NULL );"
    }

    // Referência para o banco de dados
    private val movieSqliteDatabase: SQLiteDatabase

    init {
        // Criando ou abrindo o banco
        movieSqliteDatabase = context.openOrCreateDatabase(
            MOVIE_DATABASE_FILE,
            Context.MODE_PRIVATE,
            null
        )
        try {
            movieSqliteDatabase.execSQL(CREATE_MOVIE_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    private fun Movie.toContentValues() = with(ContentValues()) {
        put(NAME_COLUMN, name)
        put(YEAR_COLUMN, year)
        put(STUDIO_COLUMN, studio)
        put(PRODUCER_COLUMN, producer)
        put(TIME_COLUMN, timeOfDuration)
        put(WATCHED_COLUMN, hasWatched)
        put(RATE_COLUMN, rate)
        put(GENDER_COLUMN, genres)
        this
    }

    private fun movieToContentValues(movie: Movie) = with(ContentValues()) {
        put(NAME_COLUMN, movie.name)
        put(YEAR_COLUMN, movie.year)
        put(STUDIO_COLUMN, movie.studio)
        put(PRODUCER_COLUMN, movie.producer)
        put(TIME_COLUMN, movie.timeOfDuration)
        put(WATCHED_COLUMN, movie.hasWatched)
        put(RATE_COLUMN, movie.rate)
        put(GENDER_COLUMN, movie.genres)
        this
    }

    private fun Cursor.rowToMovie() = Movie(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getString(getColumnIndexOrThrow(YEAR_COLUMN)),
        getString(getColumnIndexOrThrow(STUDIO_COLUMN)),
        getString(getColumnIndexOrThrow(PRODUCER_COLUMN)),
        getString(getColumnIndexOrThrow(TIME_COLUMN)),
        getString(getColumnIndexOrThrow(WATCHED_COLUMN)).toBoolean(),
        getString(getColumnIndexOrThrow(RATE_COLUMN)).toInt(),
        getString(getColumnIndexOrThrow(GENDER_COLUMN)),
    )

    override fun createMovie(movie: Movie) = movieSqliteDatabase.insert(
        MOVIE_TABLE,
        null,
        movieToContentValues(movie)
    ).toInt()


    override fun retrieveMovie(id: Int): Movie? {
        val cursor = movieSqliteDatabase.rawQuery(
            "SELECT * FROM $MOVIE_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )
        val movie = if (cursor.moveToFirst()) cursor.rowToMovie() else null

        cursor.close()
        return movie
    }

    override fun retrieveMovies(): MutableList<Movie> {
        val movieList = mutableListOf<Movie>()
        val cursor = movieSqliteDatabase.rawQuery(
            "SELECT * FROM $MOVIE_TABLE ORDER BY $NAME_COLUMN",
            null
        )
        while (cursor.moveToNext()) {
            movieList.add(cursor.rowToMovie())
        }
        cursor.close()
        return movieList
    }

    override fun updateMovie(movie: Movie) = movieSqliteDatabase.update(
        MOVIE_TABLE,
        movie.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(movie.id.toString())
    )

    override fun deleteMovie(id: Int) =
        movieSqliteDatabase.delete(
            MOVIE_TABLE,
            "$ID_COLUMN = ?",
            arrayOf(id.toString())
        )
}