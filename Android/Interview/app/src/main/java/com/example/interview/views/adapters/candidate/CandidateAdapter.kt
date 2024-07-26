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
    }

    fun deleteItem(position: Int) {

        if (position >= 0 && position < list.size) {
            list.removeAt(position)
            notifyItemRemoved(position)

            if (list.isEmpty()) {
                notifyItemRangeRemoved(0, list.size)
            }
        }
    }


}