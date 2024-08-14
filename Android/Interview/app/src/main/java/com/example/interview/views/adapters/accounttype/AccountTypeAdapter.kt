package com.example.interview.views.adapters.accounttype


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemAccounttypeBinding
import com.example.interview.models.localadapdermodels.accounttype.AccountType
import com.example.interview.views.adapters.base.BaseAdapter

class AccountTypeAdapter : BaseAdapter<AccountType, AccountTypeAdapter.AccountTypeViewHolder>() {

    inner class AccountTypeViewHolder(val itemBinding: ItemAccounttypeBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountTypeViewHolder {
        val binding =
            ItemAccounttypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountTypeViewHolder(binding)
    }

    lateinit var onClickItem: (String) -> Unit

    override fun onBindViewHolder(holder: AccountTypeViewHolder, position: Int) {
        val item = list[position]

        holder.itemBinding.itemimageView.setImageResource(item.image)
        holder.itemBinding.itemtextView.text = item.text

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
            "Admin" -> {
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
            "HR" -> {
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
            "Custom" -> {
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
            else -> {
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
        }

    }
}