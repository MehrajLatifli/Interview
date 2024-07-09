package com.example.interview.views.adapters.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected val list = mutableListOf<T>()
    protected var lastSelectedItemPosition = RecyclerView.NO_POSITION

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<T>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun resetSelectedItemPosition() {
        lastSelectedItemPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }
}
