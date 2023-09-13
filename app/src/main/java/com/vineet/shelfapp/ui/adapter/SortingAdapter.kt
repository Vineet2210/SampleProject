package com.vineet.shelfapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vineet.shelfapp.databinding.IncludeSortingItemsBinding

class SortingAdapter(val onTapItem: (Sorting) -> Unit) :
    RecyclerView.Adapter<SortingAdapter.SortListViewHolder>() {
    private val sortList = ArrayList<Sorting>()
    private  var selectedIndex:Int?=-1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SortListViewHolder {
        return SortListViewHolder(
            IncludeSortingItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SortListViewHolder, position: Int) {
        holder.bind(sortList[position])

    }


    override fun getItemCount(): Int {
        return sortList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(filterData: ArrayList<Sorting>) {
        sortList.clear()
        sortList.addAll(filterData)
        this.notifyDataSetChanged()
    }


    inner class SortListViewHolder(val binding: IncludeSortingItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(sortType: Sorting) {
            binding.tvSort.text=sortType.sortType
            binding.tvSort.setOnClickListener {
                if (selectedIndex != adapterPosition) {
                    selectedIndex = adapterPosition
                binding.tvSort.isSelected=!binding.tvSort.isSelected
                onTapItem(sortType)
                notifyDataSetChanged()
                }
            }
            binding.tvSort.isSelected = selectedIndex==adapterPosition
        }
    }
}