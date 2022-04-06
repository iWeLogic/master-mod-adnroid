package com.iwelogic.minecraft.mods.ui.main.mods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ItemAdBinding
import com.iwelogic.minecraft.mods.databinding.ItemModBinding
import com.iwelogic.minecraft.mods.databinding.ItemSkinBinding
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.utils.catchAll

class ModAdapter(private val onClick: (Mod) -> Unit) : ListAdapter<Mod, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Mod>() {
            override fun areItemsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            3 -> AdViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_ad, parent, false))
            2 -> SkinViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_skin, parent, false))
            1 -> ModViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mod, parent, false))
            else -> ProgressHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ModViewHolder -> holder.bind(getItem(position))
            is SkinViewHolder -> holder.bind(getItem(position))
            is AdViewHolder -> holder.bind(getItem(position))
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

    inner class SkinViewHolder(private val binding: ItemSkinBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Mod) {
            binding.item = item
            binding.root.setOnClickListener {
                onClick.invoke(item)
            }
            binding.executePendingBindings()
        }
    }

    inner class AdViewHolder(private val binding: ItemAdBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Mod) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    internal class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type?.cellType ?: 0
    }
}
