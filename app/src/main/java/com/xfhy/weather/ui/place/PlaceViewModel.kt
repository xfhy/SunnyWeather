package com.xfhy.weather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.xfhy.weather.logic.Repository
import com.xfhy.weather.logic.model.Place

/**
 * @author : xfhy
 * Create time : 2020-04-26 23:43
 * Description : 搜索
 */
class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    /**
     * 对于界面上显示的城市数据进行缓存
     */
    val placeList = ArrayList<Place>()
    //Transformations.switchMap 观察searchLiveData这个LiveData对象
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    /**
     * 搜索
     */
    fun searchPlaces(query: String) {
        //将搜索关键词传入,更新LiveData->更新上面的Transformations.switchMap(searchLiveData) 然后进行网络请求,更新了placeLiveData这个LiveData
        searchLiveData.value = query
    }

    fun savePlace(place: Place) {
        Repository.savePlace(place)
    }

    fun getSavedPlace() = Repository.getSavePlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}