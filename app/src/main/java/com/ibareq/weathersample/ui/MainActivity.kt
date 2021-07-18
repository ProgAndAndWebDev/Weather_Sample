package com.ibareq.weathersample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ibareq.weathersample.data.Status
import com.ibareq.weathersample.data.response.WeatherResponse
import com.ibareq.weathersample.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import com.ibareq.weathersample.data.repository.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSearch.setOnClickListener {
            getWeatherForCity("Baghdad")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    fun getWeatherForCity(cityName: String){
        WeatherRepository.getWeatherForCity(cityName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onWeatherResult)
    }

    private fun onWeatherResult(response: Status<WeatherResponse>){
        hideAllViews()
        when(response){
            is Status.Error -> {
                binding.imageError.show()
            }
            is Status.Loading -> {
                binding.progressLoading.show()
            }
            is Status.Success -> {
                binding.textMaxTemp.run {
                    show()
                    text = response.data.consolidatedWeather[0].maxTemp.toString()
                }
            }
        }
    }

    private fun hideAllViews(){
        binding.run {
            progressLoading.hide()
            textMaxTemp.hide()
            imageError.hide()

        }
    }

    companion object{
        const val TAG = "BK_PROGRAMMER"
    }


}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}