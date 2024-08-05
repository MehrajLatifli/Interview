package com.example.interview.views.adapters.vacancy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemVacancyBinding
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.views.adapters.base.BaseAdapter

class VacancyAdapter: BaseAdapter<VacancyResponse, VacancyAdapter.VacancyViewHolder>() {


    lateinit var onClickDeleteItem: (Int) -> Unit
    lateinit var onClickDetailItem: (VacancyResponse) -> Unit
    lateinit var onClickUpdateItem: (VacancyResponse) -> Unit

    inner class VacancyViewHolder(val itemBinding: ItemVacancyBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item


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

}