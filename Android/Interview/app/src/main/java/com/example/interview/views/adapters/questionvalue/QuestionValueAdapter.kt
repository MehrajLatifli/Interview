package com.example.interview.views.adapters.questionvalue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemQuestionvalueBinding
import com.example.interview.models.localadapdermodels.questionvalue.QuestionValue
import com.example.interview.views.adapters.base.BaseAdapter

class QuestionValueAdapter(
    private val onClickItem: (String) -> Unit
) : BaseAdapter<QuestionValue, QuestionValueAdapter.QuestionValueViewHolder>() {

    private var selectedItemValue: String? = null


    private var primaryFontSize: Float = 20.0F
    private var secondaryFontSize: Float = 16.0F

    inner class QuestionValueViewHolder(val itemBinding: ItemQuestionvalueBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionValueViewHolder {
        val binding = ItemQuestionvalueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionValueViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.itemtextView.text = item.value.toString()

        holder.itemBinding.itemtextView.textSize=secondaryFontSize

        // Highlight the selected item
        val isSelected = item.value.toString() == selectedItemValue
        val context = holder.itemView.context

        if (isSelected) {
            val (bgColor, cardColor, textColor) = when (item.value) {
                1 -> Triple(R.color.Flamboyant, R.color.Flamboyant, R.color.White)
                2 -> Triple(R.color.Carona, R.color.Carona, R.color.White)
                3 -> Triple(R.color.MintJelly, R.color.MintJelly, R.color.White)
                4 -> Triple(R.color.JoustBlue, R.color.JoustBlue, R.color.White)
                5 -> Triple(R.color.DeepPurple, R.color.DeepPurple, R.color.White)
                else -> Triple(R.color.Transparent, R.color.Transparent, R.color.Transparent)
            }

            holder.itemBinding.constraintLayout.setBackgroundColor(ContextCompat.getColor(context,  bgColor))
            holder.itemBinding.mainMaterialCardView.setStrokeColor(ContextCompat.getColor(context, cardColor))
            holder.itemBinding.itemtextView.setTextColor(ContextCompat.getColor(context, textColor))
        }
        else{
            val (bgColor, cardColor, textColor) = when (item.value) {
                1 -> Triple(R.color.White, R.color.Flamboyant, R.color.Flamboyant)
                2 -> Triple(R.color.White, R.color.Carona, R.color.Carona)
                3 -> Triple(R.color.White, R.color.MintJelly, R.color.MintJelly)
                4 -> Triple(R.color.White, R.color.JoustBlue, R.color.JoustBlue)
                5 -> Triple(R.color.White, R.color.DeepPurple, R.color.DeepPurple)
                else -> Triple(R.color.Transparent, R.color.Transparent, R.color.Transparent)
            }

            holder.itemBinding.constraintLayout.setBackgroundColor(ContextCompat.getColor(context,  bgColor))
            holder.itemBinding.mainMaterialCardView.setStrokeColor(ContextCompat.getColor(context, cardColor))
            holder.itemBinding.itemtextView.setTextColor(ContextCompat.getColor(context, textColor))
        }


        holder.itemView.setOnClickListener {
            selectedItemValue = item.value.toString()
            notifyDataSetChanged()
            onClickItem(item.value.toString())
        }
    }

    fun setSelectedItem(selectedValue: String?) {
        selectedItemValue = selectedValue
        notifyDataSetChanged()
    }

    fun setFontSizes(primary: Float, secondary: Float) {
        primaryFontSize = primary
        secondaryFontSize = secondary
        notifyDataSetChanged()
    }
}
