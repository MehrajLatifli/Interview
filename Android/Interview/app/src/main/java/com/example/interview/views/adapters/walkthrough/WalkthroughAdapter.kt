package com.example.interview.views.adapters.walkthrough

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemWalkthroughBinding
import com.example.interview.models.localadapdermodels.walkthrough.Walkthrough
import com.example.interview.views.adapters.base.BaseAdapter

class WalkthroughAdapter : BaseAdapter<Walkthrough, WalkthroughAdapter.WalkthroughViewHolder>() {

    inner class WalkthroughViewHolder(val itemBinding: ItemWalkthroughBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkthroughViewHolder {
        val binding = ItemWalkthroughBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalkthroughViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalkthroughViewHolder, position: Int) {
        val item = list[position]

        holder.itemBinding.imageView.setImageResource(item.image)
        holder.itemBinding.textView.text = item.text
    }
}
