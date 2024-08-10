package com.example.interview.views.adapters.question

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.R
import com.example.interview.databinding.ItemQuestionBinding
import com.example.interview.models.localadapdermodels.question.Question
import com.example.interview.views.adapters.base.BaseAdapter
import com.example.interview.views.adapters.questionvalue.QuestionValueAdapter

class QuestionAdapter : BaseAdapter<Question, QuestionAdapter.QuestionViewHolder>() {

    var itemClickHandler: ((selectedItemText: String, sessionQuestionId: Int) -> Unit)? = null

    // Track selected item state for each question
    private val selectedItems = mutableMapOf<Int, String>()


    private var primaryFontSize: Float = 16.0F
    private var secondaryFontSize: Float = 12.0F

    inner class QuestionViewHolder(val itemBinding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.itemtextView.text = item.text
        holder.itemBinding.itemimageView.setImageResource(item.image)

        val questionValueAdapter = QuestionValueAdapter { selectedItemText ->
            Log.d("QuestionAdapter", "Value selected: $selectedItemText for question ID: ${item.id}")
            itemClickHandler?.invoke(selectedItemText, item.id)
            selectedItems[item.id] = selectedItemText
        }
        holder.itemBinding.rvQuestionValue.adapter = questionValueAdapter
        holder.itemBinding.rvQuestionValue.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        questionValueAdapter.updateList(item.questionvalues)

        questionValueAdapter.setFontSizes(secondaryFontSize, secondaryFontSize)

        holder.itemBinding.itemtextView.textSize = primaryFontSize


        questionValueAdapter.setSelectedItem(selectedItems[item.id])

        val context = holder.itemView.context
        val (bgColor, cardColor, textColor) = Triple(R.color.LavenderTonic, R.color.DeepPurple, R.color.DeepPurple)
        holder.itemBinding.leftContainer.setBackgroundColor(ContextCompat.getColor(context, bgColor))
        holder.itemBinding.imagecardview.setCardBackgroundColor(ContextCompat.getColor(context, cardColor))
        holder.itemBinding.mainMaterialCardView.setStrokeColor(ContextCompat.getColor(context, bgColor))
        holder.itemBinding.itemtextView.setTextColor(ContextCompat.getColor(context, textColor))
    }

    fun setSelectedItem(questionId: Int, selectedValue: String) {
        selectedItems[questionId] = selectedValue
        notifyDataSetChanged()
    }


    fun setFontSizes(primary: Float, secondary: Float) {
        primaryFontSize = primary
        secondaryFontSize = secondary
        notifyDataSetChanged()
    }
}


