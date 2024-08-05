package com.example.interview.views.adapters.session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemSessionBinding
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.views.adapters.base.BaseAdapter

class SessionAdapder : BaseAdapter<SessionResponse, SessionAdapder.SessionViewHolder>() {

    lateinit var onClickDeleteItem: (Int?) -> Unit
    lateinit var onClickDetailItem: (SessionResponse) -> Unit
    lateinit var onClickStartExemItem: (SessionResponse) -> Unit

    inner class SessionViewHolder(val itemBinding: ItemSessionBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val binding = ItemSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item

        holder.itemBinding.buttonDelete.setOnClickListener {
            onClickDeleteItem.invoke(item.id)
        }

        holder.itemBinding.buttonDetail.setOnClickListener {
            onClickDetailItem.invoke(item)
        }

        holder.itemBinding.buttonUpdate.setOnClickListener {
            onClickStartExemItem.invoke(item)
        }
    }
}
