package com.ifsp.giovanna.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.ifsp.giovanna.moviesmanager.databinding.ActivityMovieBinding
import com.ifsp.giovanna.moviesmanager.model.Constant.EXTRA_MOVIE
import com.ifsp.giovanna.moviesmanager.model.Constant.VIEW_MOVIE
import com.ifsp.giovanna.moviesmanager.model.entity.Movie
import java.util.*

class MovieActivity : AppCompatActivity() {

    private val amb: ActivityMovieBinding by lazy{
        ActivityMovieBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)



        var generoSelecionado = 0

        val receivedMovie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        receivedMovie?.let{ _receivedMovie ->
            with(amb) {
                with(_receivedMovie) {
                    nomeEt.setText(name)
                    nomeEt.isEnabled = false
                    anoLancamentoEt.setText(year)
                    estudioEt.setText(studio)
                    produtoraEt.setText(producer)
                    duracaoEt.setText(timeOfDuration)
                    if(hasWatched) {
                        assistidoCb.isChecked = true
                        notaSp.setSelection(rate)
                    }
                    generoSp.setSelection(generoSelecionado)
                }
            }
        }

        amb.generoSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                generoSelecionado = position
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val viewMovie = intent.getBooleanExtra(VIEW_MOVIE, false)
        if (viewMovie) {
            amb.anoLancamentoEt.isEnabled = false
            amb.estudioEt.isEnabled = false
            amb.produtoraEt.isEnabled = false
            amb.duracaoEt.isEnabled = false
            amb.notaSp.isEnabled = false
            amb.assistidoCb.isEnabled = false
            amb.generoSp.isEnabled = false
            amb.salvarBt.visibility = View.GONE
        }

        amb.salvarBt.setOnClickListener {


            val movie = Movie(
                id = receivedMovie?.id?: Random(System.currentTimeMillis()).nextInt(),
                name = amb.nomeEt.text.toString(),
                year = amb.anoLancamentoEt.text.toString(),
                studio = amb.estudioEt.text.toString(),
                producer = amb.produtoraEt.text.toString(),
                timeOfDuration = amb.duracaoEt.text.toString(),
                hasWatched = amb.assistidoCb.isChecked,
                rate = amb.notaSp.selectedItem.toString().toInt(),
                genres = amb.generoSp.selectedItem.toString()
            )

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_MOVIE, movie)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}