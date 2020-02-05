package com.example.dather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.dather.R
import com.example.dather.adapter.CitiesAdapter
import com.example.dather.datasource.Repository


class CitiesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    //TODO: this is hot fix for animation
    private val iM = mutableMapOf<Int, Boolean>()

    private val defaultItemClickListener = object : ItemClickListener {

        //TODO: this is hot fix for animation
        override fun onClick(view: View, position: Int) {
            if (!iM.containsKey(position)) {
                iM[position] = true
            }
            if (iM[position]!!) {
                resize(view, true)
                iM[position] = false
            } else {
                resize(view, false)
                iM[position] = true
            }
        }

        //TODO: this is hot fix for animation
        fun resize(view: View, flag: Boolean) {
            val cp = view.layoutParams
            cp.height = if (flag) (view.height * 1.2).toInt() else ViewGroup.LayoutParams.WRAP_CONTENT
            (recyclerView.getChildViewHolder(view) as CitiesAdapter.CityHolder).showDescription(flag)
            view.layoutParams = cp
        }

        //TODO: this is hot fix for animation
        fun clear() {
            iM.forEach {
                resize(recyclerView[it.key], false)
            }
            iM.clear()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cities, container, false)
        recyclerView = view.findViewById(R.id.list_cities)
        return view
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        context?.let {
            recyclerView.addOnItemTouchListener(ItemTouchListener(it, recyclerView, listener))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val citiesAdapter = CitiesAdapter()
        recyclerView.adapter = citiesAdapter
        recyclerView.setHasFixedSize(false)
        setOnItemClickListener(defaultItemClickListener)

        Repository.getCitiesRepo().getLastLoadedCities().observe(this, Observer {
            it?.let {
                //TODO: this is hot fix for animation
                defaultItemClickListener.clear()

                citiesAdapter.cities = it
            }
        })
    }
}