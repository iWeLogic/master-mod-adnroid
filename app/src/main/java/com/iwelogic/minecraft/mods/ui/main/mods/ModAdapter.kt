package com.iwelogic.minecraft.mods.ui.main.mods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ItemAdBinding
import com.iwelogic.minecraft.mods.databinding.ItemModBinding
import com.iwelogic.minecraft.mods.databinding.ItemSkinBinding
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.utils.dp
import com.iwelogic.minecraft.mods.utils.fromPxToDp

class ModAdapter(private val onClick: (Mod) -> Unit) : ListAdapter<Mod, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Mod>() {
            override fun areItemsTheSame(oldItem: Mod, newItem: Mod): Boolean = oldItem === newItem
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
        if (holder is ModViewHolder) {
            holder.bind(getItem(position))
        }
        if (holder is SkinViewHolder) {
            holder.bind(getItem(position))
        }
        if (holder is AdViewHolder) {
            holder.bind()
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
        fun bind() {
            val context = binding.adViewContainer.context
            val adView = AdView(context)
            adView.adUnitId = context.getString(R.string.ad_banner)
            val adWidth = context.resources.displayMetrics.widthPixels - 32.dp(context)
            val adHeight = adWidth / 3 * 2
            adView.adSize = AdSize.getInlineAdaptiveBannerAdSize(adWidth.fromPxToDp(context), adHeight.fromPxToDp(context))
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            binding.adViewContainer.removeAllViews()
            binding.adViewContainer.addView(adView)
            binding.executePendingBindings()
        }
    }

    internal class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).category) {
            "ad" -> 3
            "skins" -> 2
            "progress" -> 0
            else -> 1
        }
    }
}
