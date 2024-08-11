package com.example.interview.views.adapters.vacancy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemVacancyBinding
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.views.adapters.base.BaseAdapter

class VacancyAdapter: BaseAdapter<VacancyResponse, VacancyAdapter.VacancyViewHolder>() {


    lateinit var onClickDeleteItem: (Int) -> Unit
    lateinit var onClickDetailItem: (VacancyResponse) -> Unit
    lateinit var onClickUpdateItem: (VacancyResponse) -> Unit

    private var deleteButtonVisible: Boolean = true
    private var detailButtonVisible: Boolean = true
    private var updateButtonVisible: Boolean = true

    private var primaryFontSize: Float = 20.0F
    private var secondaryFontSize: Float = 16.0F

    inner class VacancyViewHolder(val itemBinding: ItemVacancyBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item

        holder.itemBinding.buttonDelete?.textSize = 14.0F
        holder.itemBinding.buttonDetail?.textSize = 14.0F
        holder.itemBinding.buttonUpdate?.textSize = 14.0F

        holder.itemBinding.textView1.textSize = primaryFontSize
        holder.itemBinding.textView2.textSize = secondaryFontSize


        holder.itemBinding.buttonDelete.visibility = if (deleteButtonVisible) View.VISIBLE else View.GONE
        holder.itemBinding.buttonDetail.visibility = if (detailButtonVisible) View.VISIBLE else View.GONE
        holder.itemBinding.buttonUpdate.visibility = if (updateButtonVisible) View.VISIBLE else View.GONE


        holder.itemBinding.buttonDelete.setOnClickListener {
            onClickDeleteItem.invoke(item.id)


        }

        holder.itemBinding.buttonDetail.setOnClickListener {
            onClickDetailItem.invoke(item)


        }

        holder.itemBinding.buttonUpdate.setOnClickListener {
            onClickUpdateItem.invoke(item)


        }
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

    fun setUpdateButtonVisibility(visible: Boolean) {
        updateButtonVisible = visible
        notifyDataSetChanged()
    }

}