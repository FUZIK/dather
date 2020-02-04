package com.example.dather.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.dather.R
import com.example.dather.adapter.CitiesAdapter
import com.example.dather.datasource.Repository


class CitiesFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    private val defaultItemClickListener = object : ItemClickListener {
        //TODO finish animation correctly
        override fun onClick(view: View, position: Int) {
            view.scaleX = 1.2f
            (recyclerView.getChildViewHolder(view) as CitiesAdapter.CityHolder).showDescription(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cities, container, false)
        recyclerView = view.findViewById(R.id.list_cities)
        progressBar = view.findViewById(R.id.list_cities_progress)
        return view
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        recyclerView.addOnItemTouchListener(ItemTouchListener(recyclerView, listener))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val citiesAdapter = CitiesAdapter()
        recyclerView.adapter = citiesAdapter
        setOnItemClickListener(defaultItemClickListener)

        Repository.getPositionRepo().getLastPosition().observe(this, Observer {
            Repository.getWeatherRepo().getCitiesAround(it.latitude, it.longitude).observe(this, Observer {
                citiesAdapter.cities = it
                Log.e("CITIES", "updated! ${it.size}")
                progressBar.visibility = View.INVISIBLE
            })
        })
    }
}