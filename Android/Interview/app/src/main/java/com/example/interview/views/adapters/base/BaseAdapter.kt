package com.example.interview.views.adapters.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    public val list = mutableListOf<T>()
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

    fun deleteItem(position: Int) {

        if (position >= 0 && position < list.size) {
            list.removeAt(position)
            notifyItemRemoved(position)

            if (list.isEmpty()) {
                notifyItemRangeRemoved(0, list.size)
            }
        }
    }
}
