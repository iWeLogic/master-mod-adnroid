package com.iwelogic.minecraft.mods.ui.main.mods

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ItemModBinding
import com.iwelogic.minecraft.mods.models.Mod

class ModAdapter(private val onClick: (Mod) -> Unit) : ListAdapter<Mod, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Mod>() {
            override fun areItemsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem === newItem
            override fun areContentsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ModViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mod, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ModViewHolder) {
            holder.bind(getItem(position))
        }
    }

    internal inner class ModViewHolder(private val binding: ItemModBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Mod) {
            binding.item = item
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }
            binding.executePendingBindings()
        }
    }
}
