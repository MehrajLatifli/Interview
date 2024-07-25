package com.example.interview.views.adapters.operation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemOperationBinding
import com.example.interview.models.localadapdermodels.operationcrud.Operation
import com.example.interview.views.adapters.base.BaseAdapter

class OperationAdapter(
    private val onClickItem: (String) -> Unit
) : BaseAdapter<Operation, OperationAdapter.OperationCRUDViewHolder>() {

    inner class OperationCRUDViewHolder(val itemBinding: ItemOperationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationCRUDViewHolder {
        val binding =
            ItemOperationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationCRUDViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationCRUDViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.itemtextView.text = item.text
        holder.itemBinding.itemimageView.setImageResource(item.image)

        holder.itemBinding.mainMaterialCardView.setOnClickListener {
            onClickItem(item.text)
        }

        // Color setup based on item text (if needed)
        val context = holder.itemView.context
        val (bgColor, cardColor, textColor) = when (item.text) {
            "Create" -> Triple(R.color.MintJelly, R.color.MintJelly, R.color.White)
            "Read" -> Triple(R.color.JoustBlue, R.color.JoustBlue, R.color.White)
            "Update" -> Triple(R.color.Carona, R.color.Carona, R.color.White)
            "Delete" -> Triple(R.color.Flamboyant, R.color.Flamboyant, R.color.White)
            else -> Triple(R.color.Transparent, R.color.Transparent, R.color.Transparent)
        }

        holder.itemBinding.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, bgColor))
        holder.itemBinding.mainMaterialCardView.setStrokeColor(ContextCompat.getColor(context, bgColor))
        holder.itemBinding.itemtextView.setTextColor(ContextCompat.getColor(context, textColor))
    }
}
