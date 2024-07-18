package com.example.interview.views.adapters.operationcrud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemOperationcrudBinding
import com.example.interview.models.localadapdermodels.operationcrud.OperationCRUD
import com.example.interview.views.adapters.base.BaseAdapter

class OperationCRUDAdapter : BaseAdapter<OperationCRUD, OperationCRUDAdapter.OperationCRUDViewHolder>() {

    inner class OperationCRUDViewHolder(val itemBinding: ItemOperationcrudBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationCRUDViewHolder {
        val binding =
            ItemOperationcrudBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationCRUDViewHolder(binding)
    }

    lateinit var onClickItem: (String) -> Unit

    override fun onBindViewHolder(holder: OperationCRUDViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.itemtextView.text = item.text

        holder.itemBinding.itemimageView.setImageResource(item.image)



        holder.itemBinding.mainMaterialCardView.setOnClickListener {
            val currentSelectedItemPosition = holder.bindingAdapterPosition

            if (currentSelectedItemPosition != lastSelectedItemPosition) {
                val previousSelectedItemPosition = lastSelectedItemPosition
                lastSelectedItemPosition = currentSelectedItemPosition
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(currentSelectedItemPosition)
                onClickItem.invoke(list[currentSelectedItemPosition].text.toString())
            } else {
                lastSelectedItemPosition = RecyclerView.NO_POSITION
                notifyItemChanged(currentSelectedItemPosition)
                onClickItem.invoke("")
            }
        }

        when (item.text) {
            "Create" -> {
                holder.itemBinding.constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.MintJelly
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.MintJelly
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.White
                    )
                )
            }
            "Read" -> {
                holder.itemBinding.constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.JoustBlue
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.JoustBlue
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.White
                    )
                )
            }
            "Update" -> {
                holder.itemBinding.constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Carona
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Carona
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.White
                    )
                )
            }
            "Delete" -> {
                holder.itemBinding.constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Flamboyant
                    )
                )

                holder.itemBinding.mainMaterialCardView.setStrokeColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.Flamboyant
                    )
                )

                holder.itemBinding.itemtextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.White
                    )
                )
            }
        }
    }
}
