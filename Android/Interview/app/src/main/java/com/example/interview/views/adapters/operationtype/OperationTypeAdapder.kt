package com.example.interview.views.adapters.operationtype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemOperationtypeBinding
import com.example.interview.models.localadapdermodels.operationtype.OperationType
import com.example.interview.views.adapters.base.BaseAdapter
import com.example.interview.views.adapters.operationcrud.OperationCRUDAdapter
import com.google.android.material.animation.AnimationUtils


class OperationTypeAdapder : BaseAdapter<OperationType, OperationTypeAdapder.OperationTypeViewHolder>() {

    inner class OperationTypeViewHolder(val itemBinding: ItemOperationtypeBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationTypeViewHolder {
        val binding =
            ItemOperationtypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationTypeViewHolder, position: Int) {
        val item = list[position]

        holder.itemBinding.itemtextView.text = item.text
        holder.itemBinding.itemimageView.setImageResource(item.image)




        val adapter = OperationCRUDAdapter().apply {
            updateList(item.operationCRUDs)
            onClickItem = { selectedItemText ->
                // Handle item click if necessary
            }
        }
        holder.itemBinding.rvOperationCRUD.adapter = adapter
        holder.itemBinding.rvOperationCRUD.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter.updateList(item.operationCRUDs)

        when (item.text) {
            "Candidate" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ThickBlue
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Indigo
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ThickBlue
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ThickBlue
                    )
                )
            }
            "Category" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ComingUpRoses
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.PinkDazzle
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ComingUpRoses
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ComingUpRoses
                    )
                )
            }
            "Structure" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Patrice
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.FoulGreen
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Patrice
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Patrice
                    )
                )
            }
            "Level" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.TulipTree
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.AlamedaOchre
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.TulipTree
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.AlamedaOchre
                    )
                )
            }
            "Position" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ThickBlue
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Indigo
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ThickBlue
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ThickBlue
                    )
                )
            }
            "Vacancy" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ComingUpRoses
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.PinkDazzle
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ComingUpRoses
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.ComingUpRoses
                    )
                )
            }
            "Question" -> {
                holder.itemBinding.leftContainer.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Patrice
                    )
                )

                holder.itemBinding.imagecardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.FoulGreen
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Patrice
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Patrice
                    )
                )
            }
        }

    }
}