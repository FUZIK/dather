package com.example.dather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.dather.R
import com.example.dather.activity.HostActivity
import com.example.dather.adapter.CitiesAdapter
import com.example.dather.adapter.CityHolder


class CitiesFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    private val defaultItemClickListener = object : ItemClickListener {
        override fun onClick(view: View, position: Int) {
//            println("HEIGHT: " + view.height)
//            println("HEIGHT: " + view.height + view.height/4)
//            view.layoutParams = RecyclerView.LayoutParams(view.width, view.height + view.height/4)
            (recyclerView.getChildViewHolder(view) as CityHolder).showDescription(true)
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

        HostActivity.getCitiesAround(HostActivity.latitude, HostActivity.longitude).observe(this, Observer {
            citiesAdapter.cities = it
            progressBar.visibility = View.INVISIBLE
        })

    }
}