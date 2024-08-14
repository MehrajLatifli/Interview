package com.example.interview.views.adapters.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemSessionBinding
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.views.adapters.base.BaseAdapter

class SessionAdapder : BaseAdapter<SessionResponse, SessionAdapder.SessionViewHolder>() {

    lateinit var onClickDeleteItem: (Int?) -> Unit
    lateinit var onClickDetailItem: (SessionResponse) -> Unit
    lateinit var onClickStartExemItem: (SessionResponse) -> Unit

    private var primaryFontSize: Float = 20.0F
    private var secondaryFontSize: Float = 16.0F

    private var deleteButtonVisible: Boolean = true
    private var detailButtonVisible: Boolean = true
    private var startExamButtonVisible: Boolean = true

    inner class SessionViewHolder(val itemBinding: ItemSessionBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val binding = ItemSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item

        holder.itemBinding.buttonDelete.visibility = if (deleteButtonVisible) View.VISIBLE else View.GONE
        holder.itemBinding.buttonDetail.visibility = if (detailButtonVisible) View.VISIBLE else View.GONE

        holder.itemBinding.buttonStartExam.visibility = when {
            !startExamButtonVisible -> View.GONE
            updateVisibility(item) -> View.INVISIBLE
            else -> View.VISIBLE
        }

        holder.itemBinding.buttonDelete?.textSize = 14.0F
        holder.itemBinding.buttonDetail?.textSize = 14.0F
        holder.itemBinding.buttonStartExam?.textSize = 14.0F

        holder.itemBinding.textView1.textSize = primaryFontSize
        holder.itemBinding.textView2.textSize = secondaryFontSize

        holder.itemBinding.buttonDelete.setOnClickListener {
            onClickDeleteItem.invoke(item.id)
        }

        holder.itemBinding.buttonDetail.setOnClickListener {
            onClickDetailItem.invoke(item)
        }

        holder.itemBinding.buttonStartExam.setOnClickListener {
            onClickStartExemItem.invoke(item)
        }
    }

    private fun updateVisibility(item: SessionResponse): Boolean {
        return item.endValue != null && item.endValue > 0
    }

    fun setFontSizes(primary: Float, secondary: Float) {
        primaryFontSize = primary
        secondaryFontSize = secondary
        notifyDataSetChanged()
    }

    fun setDeleteButtonVisibility(visible: Boolean) {
        deleteButtonVisible = visible
        notifyDataSetChanged()
    }

    fun setDetailButtonVisibility(visible: Boolean) {
        detailButtonVisible = visible
        notifyDataSetChanged()
    }

    fun setStartExamButtonVisibility(visible: Boolean) {
        startExamButtonVisible = visible
        notifyDataSetChanged()
    }
}

