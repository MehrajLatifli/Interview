package com.example.interview.views.adapters.accounttype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemAccounttypeBinding
import com.example.interview.models.localadapdermodels.accounttype.AccountType
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.views.adapters.base.BaseAdapter

class AccountTypeAdapder : BaseAdapter<AccountType, AccountTypeAdapder.WalkthroughViewHolder>() {

    inner class WalkthroughViewHolder(val itemBinding: ItemAccounttypeBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkthroughViewHolder {
        val binding =
            ItemAccounttypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalkthroughViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalkthroughViewHolder, position: Int) {
        val item = list[position]

        holder.itemBinding.itemimageView.loadImageWithGlideAndResize(item.image.toString(),holder.itemView.context)
        holder.itemBinding.itemtextView.text = item.text

        if(item.text=="HR")
        {
            holder.itemBinding.newsMaterialCardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.Indigo))
        }
        if(item.text=="Custom")
        {
            holder.itemBinding.newsMaterialCardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.Amber))
        }
    }
}