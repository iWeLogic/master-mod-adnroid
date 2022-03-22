package com.iwelogic.minecraft.mods.ui.main.mods.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ItemFilterValueBinding
import com.iwelogic.minecraft.mods.models.FilterValue

class FilterValueAdapter : ListAdapter<FilterValue, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<FilterValue>() {
            override fun areItemsTheSame(oldItem: FilterValue, newItem: FilterValue): Boolean = oldItem === newItem
            override fun areContentsTheSame(oldItem: FilterValue, newItem: FilterValue): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilterValueViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_filter_value, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterValueViewHolder) {
            holder.bind(getItem(position))
        }
    }

    internal inner class FilterValueViewHolder(private val binding: ItemFilterValueBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilterValue) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}
