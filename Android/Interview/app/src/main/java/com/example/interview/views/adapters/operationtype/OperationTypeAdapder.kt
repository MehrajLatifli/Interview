package com.example.interview.views.adapters.operationtype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemOperationtypeBinding
import com.example.interview.models.localadapdermodels.operationtype.OperationType
import com.example.interview.views.adapters.base.BaseAdapter
import com.example.interview.views.adapters.operationcrud.OperationCRUDAdapter
import kotlinx.coroutines.launch

class OperationTypeAdapder : BaseAdapter<OperationType, OperationTypeAdapder.OperationTypeViewHolder>() {

    private var primaryFontSize: Float = 20.0F
    private var secondaryFontSize: Float = 16.0F

    var itemClickHandler: ((selectedItemText: String, operationTypeText: String) -> Unit)? = null

    inner class OperationTypeViewHolder(val itemBinding: ItemOperationtypeBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationTypeViewHolder {
        val binding = ItemOperationtypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationTypeViewHolder, position: Int) {
        val item = list[position]

        holder.itemBinding.itemtextView.text = item.text
        holder.itemBinding.itemimageView.setImageResource(item.image)

        val adapter = OperationCRUDAdapter { selectedItemText ->
            itemClickHandler?.invoke(selectedItemText, item.text)
        }

        holder.itemBinding.rvOperation.adapter = adapter
        holder.itemBinding.rvOperation.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter.updateList(item.operations)

        holder.itemBinding.itemtextView.textSize = primaryFontSize


        val context = holder.itemView.context
        val (bgColor, cardColor, textColor) = when (item.text) {
            "Candidate" -> Triple(R.color.ThickBlue, R.color.Indigo, R.color.ThickBlue)
            "Vacancy" -> Triple(R.color.ComingUpRoses, R.color.PinkDazzle, R.color.ComingUpRoses)
            else -> Triple(R.color.Transparent, R.color.Transparent, R.color.Transparent)
        }

        holder.itemBinding.leftContainer.setBackgroundColor(ContextCompat.getColor(context, bgColor))
        holder.itemBinding.imagecardview.setCardBackgroundColor(ContextCompat.getColor(context, cardColor))
        holder.itemBinding.mainMaterialCardView.setStrokeColor(ContextCompat.getColor(context, bgColor))
        holder.itemBinding.itemtextView.setTextColor(ContextCompat.getColor(context, textColor))
    }

    fun setFontSizes(primary: Float, secondary: Float) {
        primaryFontSize = primary
        secondaryFontSize = secondary
        notifyDataSetChanged()
    }
}

