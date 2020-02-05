package com.example.dather.fragment

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


interface ItemClickListener {
    fun onClick(view: View, position: Int)
}

class ItemTouchListener(
    context: Context,
    private val recyclerView: RecyclerView,
    var itemClickListener: ItemClickListener
) : RecyclerView.OnItemTouchListener {
    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (gestureDetector.onTouchEvent(e) && view != null) {
            itemClickListener.onClick(view, recyclerView.getChildLayoutPosition(view))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
