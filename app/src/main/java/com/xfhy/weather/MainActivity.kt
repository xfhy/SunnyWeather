package com.xfhy.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xfhy.weather.ui.place.PlaceViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Main_Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel::class.java)
        placeViewModel.placeLiveData.observe(this, Observer {
            Log.d(TAG, it.toString())
        })

        btnRequest.setOnClickListener {
            placeViewModel.searchPlaces("莫斯科")
        }
    }
}
