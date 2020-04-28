package com.xfhy.weather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.xfhy.weather.R
import com.xfhy.weather.logic.model.Place

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
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.tvPlaceName.text = place.name
        holder.tvPlaceAddress.text = place.address
    }
}