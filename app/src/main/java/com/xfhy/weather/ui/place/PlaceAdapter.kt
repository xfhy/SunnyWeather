package com.xfhy.weather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.weather.R
import com.xfhy.weather.logic.model.Place
import com.xfhy.weather.ui.weather.WeatherActivity

/**
 * @author : xfhy
 * Create time : 2020-04-27 21:39
 * Description : 搜索结果adapter
 */
class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
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
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            fragment.startActivity(intent)
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