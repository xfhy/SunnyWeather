package com.xfhy.weather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xfhy.weather.MainActivity
import com.xfhy.weather.R
import com.xfhy.weather.ui.common.toast
import com.xfhy.weather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

/**
 * @author : xfhy
 * Create time : 2020-04-27 22:15
 * Description : 搜索界面
 *
 * 该fragment在MainActivity用于搜索
 * 也在天气详情页WeatherActivity中使用到
 *
 */
class PlaceFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (goWeatherDetailIfSaved()) {
            return
        }

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter

        //监听文本变化 -> 搜索
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable?.toString()
            if (content.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            } else {
                viewModel.searchPlaces(content)
            }
        }

        //拿到结果
        viewModel.placeLiveData.observe(this, Observer { result ->
            val places = result.getOrNull()
            if (places.isNullOrEmpty()) {
                toast("未能查询到任何地点")
                result.exceptionOrNull()?.printStackTrace()
            } else {
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                recyclerView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
                bgImageView.visibility = View.GONE
            }
        })
    }

    private fun goWeatherDetailIfSaved(): Boolean {
        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            if (activity is MainActivity) {
                WeatherActivity.startWeatherActivity(context, place.location.lng, place.location.lat, place.name)
                activity?.finish()
                return true
            }
        }
        return false
    }

}