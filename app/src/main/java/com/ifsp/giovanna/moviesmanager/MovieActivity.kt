package com.ifsp.giovanna.moviesmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifsp.giovanna.moviesmanager.databinding.ActivityMainBinding
import com.ifsp.giovanna.moviesmanager.databinding.ActivityMovieBinding

class MovieActivity : AppCompatActivity() {

    private val amb: ActivityMovieBinding by lazy{
        ActivityMovieBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
    }
}