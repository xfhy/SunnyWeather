package com.xfhy.weather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.weather.R
import com.xfhy.weather.logic.model.Place
import com.xfhy.weather.ui.weather.WeatherActivity

/**
 * @author : xfhy
 * Create time : 2020-04-27 21:39
 * Description : 搜索结果adapter
 */
class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPlaceName: TextView = view.findViewById(R.id.placeName)
        val tvPlaceAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.place_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val place = placeList[position]
            //保存天气信息
            fragment.viewModel.savePlace(place)
            WeatherActivity.startWeatherActivity(fragment.context, place.location.lng, place.location.lat, place.name)
            //fragment.activity?.finish()
        }
        return viewHolder
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.tvPlaceName.text = place.name
        holder.tvPlaceAddress.text = place.address
    }
}