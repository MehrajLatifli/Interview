package com.example.interview.views.adapters.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemProfilerolesBinding
import com.example.interview.models.responses.get.profile.Role
import com.example.interview.views.adapters.base.BaseAdapter

class RoleAdapter : BaseAdapter<Role, RoleAdapter.RoleViewHolder>() {

    private var primaryFontSize: Float = 20.0F
    private var secondaryFontSize: Float = 16.0F

    inner class RoleViewHolder(val itemBinding: ItemProfilerolesBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = ItemProfilerolesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item

        holder.itemBinding?.roletextView?.textSize = secondaryFontSize
    }

    fun setFontSizes(primary: Float, secondary: Float) {
        primaryFontSize = primary
        secondaryFontSize = secondary
        notifyDataSetChanged()
    }
}
