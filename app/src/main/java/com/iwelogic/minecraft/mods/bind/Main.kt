package com.iwelogic.minecraft.mods.bind

import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.models.FilterValue
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.models.Sort
import com.iwelogic.minecraft.mods.models.Type
import com.iwelogic.minecraft.mods.ui.favorite.FavoriteAdapter
import com.iwelogic.minecraft.mods.ui.main.mods.ModAdapter
import com.iwelogic.minecraft.mods.ui.main.mods.filter.FilterValueAdapter


object Main {

    @BindingAdapter("sort", "onSelect", "type")
    @JvmStatic
    fun setSort(view: ImageView, sort: Sort, onSelect: (Sort) -> Unit, type: Type) {
        view.setOnClickListener {
            val popup = PopupMenu(view.context, view, Gravity.END)
            popup.inflate(type.sortMenu)
            popup.menu.findItem(sort.menuId).isChecked = true
            popup.setOnMenuItemClickListener { item ->
                item.isChecked = !item.isChecked
                onSelect.invoke(Sort.findByMenuId(item.itemId))
                false
            }
            popup.show()
        }
    }

    @BindingAdapter("mods", "onClick", "onScroll", "spanCount")
    @JvmStatic
    fun showMods(view: RecyclerView, mods: List<Mod>?, onClick: (Mod) -> Unit, onScroll: (Int) -> Unit, spanCount: Int?) {
        view.adapter ?: run {
            view.adapter = ModAdapter(onClick)
            view.itemAnimator?.changeDuration = 0
            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    onScroll.invoke((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition())
                }
            })
        }
        spanCount?.let {
            val layoutManager = (view.layoutManager as GridLayoutManager)
            layoutManager.spanCount = spanCount
            if (layoutManager.spanCount >= 2) {
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        view.adapter?.let {
                            return when (it.getItemViewType(position)) {
                                Type.AD.cellType -> if (mods?.firstOrNull()?.type == Type.SKINS) 2 else 1
                                Type.SKINS.cellType -> 1
                                Type.ADDONS.cellType -> 1
                                Type.MAPS.cellType -> 1
                                Type.TEXTURES.cellType -> 1
                                Type.SEEDS.cellType -> 1
                                Type.BUILDINGS.cellType -> 1
                                else -> layoutManager.spanCount
                            }
                        } ?: run {
                            return 0
                        }
                    }
                }
            }
        }
        (view.adapter as ModAdapter).submitList(mods?.toMutableList())
    }

    @BindingAdapter("counter")
    @JvmStatic
    fun showCounter(view: TextView, counter: Long?) {
        view.text = convertBigNumbers(counter)
    }

    private fun convertBigNumbers(value: Long?): String {
        value?.let {
            return when (it) {
                in 0L..999L -> it.toString()
                in 1000L..99999L -> "${String.format("%.1f", it.toFloat() / 1000).replace(".0", "")} k"
                in 100000L..999999L -> "${it / 1000} k"
                else -> "${String.format("%.1f", it.toFloat() / 100000).replace(".0", "")} m"
            }
        } ?: run {
            return ""
        }
    }

    @BindingAdapter("filters", "spanCount")
    @JvmStatic
    fun showFilters(view: RecyclerView, filters: List<FilterValue>?, spanCount: Int) {
        view.adapter ?: run {
            view.adapter = FilterValueAdapter()
        }
        val layoutManager = (view.layoutManager as GridLayoutManager)
        layoutManager.spanCount = spanCount
        (view.adapter as FilterValueAdapter).submitList(filters?.toMutableList())
    }

    @BindingAdapter("favorites", "onClick", "spanCount")
    @JvmStatic
    fun showFavorites(view: RecyclerView, favorites: List<Mod>?, onClick: (Mod) -> Unit, spanCount: Int) {
        view.adapter ?: run {
            view.adapter = FavoriteAdapter(onClick)
        }
        val layoutManager = (view.layoutManager as GridLayoutManager)
        layoutManager.spanCount = spanCount
        (view.adapter as FavoriteAdapter).submitList(favorites?.toMutableList())
    }
}