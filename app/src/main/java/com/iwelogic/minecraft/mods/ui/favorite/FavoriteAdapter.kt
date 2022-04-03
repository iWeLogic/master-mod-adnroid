package com.iwelogic.minecraft.mods.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ItemModBinding
import com.iwelogic.minecraft.mods.databinding.ItemSkinFavoriteBinding
import com.iwelogic.minecraft.mods.models.Mod

class FavoriteAdapter(private val onClick: (Mod) -> Unit) : ListAdapter<Mod, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Mod>() {
            override fun areItemsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            2 -> SkinViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_skin_favorite, parent, false))
            else -> ModViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mod, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ModViewHolder) {
            holder.bind(getItem(position))
        }
        if (holder is SkinViewHolder) {
            holder.bind(getItem(position))
        }
    }

    inner class ModViewHolder(private val binding: ItemModBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Mod) {
            binding.item = item
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }
            binding.executePendingBindings()
        }
    }

    inner class SkinViewHolder(private val binding: ItemSkinFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Mod) {
            binding.item = item
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type?.cellType ?: 0
    }
}
