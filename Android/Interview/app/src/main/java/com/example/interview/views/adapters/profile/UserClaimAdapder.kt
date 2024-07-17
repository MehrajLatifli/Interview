package com.example.interview.views.adapters.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemProfileuserclaimsBinding
import com.example.interview.models.responses.get.profile.UserClaim
import com.example.interview.views.adapters.base.BaseAdapter

class UserClaimAdapder : BaseAdapter<UserClaim, UserClaimAdapder.UserClaimViewHolder>() {

    inner class UserClaimViewHolder(val itemBinding: ItemProfileuserclaimsBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserClaimViewHolder {
        val binding = ItemProfileuserclaimsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserClaimViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserClaimViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item

    }
}