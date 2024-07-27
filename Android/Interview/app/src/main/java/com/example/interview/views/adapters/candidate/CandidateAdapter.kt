package com.example.interview.views.adapters.candidate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemCandidateBinding
import com.example.interview.models.responses.get.candidate.CandidateDocumentResponse
import com.example.interview.models.responses.get.profile.Role
import com.example.interview.views.adapters.base.BaseAdapter

class CandidateAdapter: BaseAdapter<CandidateDocumentResponse, CandidateAdapter.CandidateViewHolder>() {


    lateinit var onClickDeleteItem: (Int) -> Unit
    lateinit var onClickDetailItem: (Int) -> Unit
    lateinit var onClickUpdateItem: (Int) -> Unit

    inner class CandidateViewHolder(val itemBinding: ItemCandidateBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding =
            ItemCandidateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val item = list[position]
        holder.itemBinding.item = item


        holder.itemBinding.buttonDelete.setOnClickListener {
            onClickDeleteItem.invoke(item.id)


        }

        holder.itemBinding.buttonDetail.setOnClickListener {
            onClickDetailItem.invoke(item.id)


        }

        holder.itemBinding.buttonUpdate.setOnClickListener {
            onClickUpdateItem.invoke(item.id)


        }
    }




}