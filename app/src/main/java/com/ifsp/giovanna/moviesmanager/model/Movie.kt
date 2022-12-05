package com.ifsp.giovanna.moviesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    var name: String,
    var year: Int,
    var studio: String,
    var producer: String,
    var timeOfDuration: Int,
    var hasWatched: Boolean,
    var rate: Int,
    var genres: String,
): Parcelable
