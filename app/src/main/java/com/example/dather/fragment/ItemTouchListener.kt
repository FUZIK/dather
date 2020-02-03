package com.example.dather.fragment

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ItemClickListener {
    fun onClick(view: View, position: Int)
}

class ItemTouchListener(
    private val recyclerView: RecyclerView,
    var itemClickListene: ItemClickListener
) : RecyclerView.OnItemTouchListener {
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            itemClickListene.onClick(view, recyclerView.getChildLayoutPosition(view))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
