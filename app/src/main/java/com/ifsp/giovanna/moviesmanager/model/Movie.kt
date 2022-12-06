package com.ifsp.giovanna.moviesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    var id: Int,
    var name: String,
    var year: String,
    var studio: String,
    var producer: String,
    var timeOfDuration: String,
    var hasWatched: Boolean,
    var rate: Int,
    var genres: String,
): Parcelable
