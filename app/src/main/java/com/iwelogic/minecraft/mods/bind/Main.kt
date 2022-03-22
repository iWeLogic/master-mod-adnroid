package com.iwelogic.minecraft.mods.bind

import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.models.FilterValue
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.models.Sort
import com.iwelogic.minecraft.mods.ui.main.mods.ModAdapter
import com.iwelogic.minecraft.mods.ui.main.mods.filter.FilterValueAdapter


object Main {

    @BindingAdapter("sort", "onSelect")
    @JvmStatic
    fun setSort(view: ImageView, sort: Sort, onSelect: (Sort) -> Unit) {
        view.setOnClickListener {
            val popup = PopupMenu(view.context, view, Gravity.END)
            popup.inflate(R.menu.sort)
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
    fun showMods(view: RecyclerView, mods: List<Mod>?, onClick: (Mod) -> Unit, onScroll: (Int) -> Unit, spanCount: Int) {
        view.adapter ?: run {
            view.adapter = ModAdapter(onClick)
            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    onScroll.invoke((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition())
                }
            })
        }
        val layoutManager = (view.layoutManager as GridLayoutManager)
        layoutManager.spanCount = spanCount
        if (layoutManager.spanCount == 2) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    view.adapter?.let {
                        return when (it.getItemViewType(position)) {
                            2 -> 1
                            1 -> 1
                            else -> 2
                        }
                    } ?: run {
                        return 0
                    }
                }
            }
        }
        (view.adapter as ModAdapter).submitList(mods?.toMutableList())
    }

    @BindingAdapter("filters")
    @JvmStatic
    fun showFilters(view: RecyclerView, filters: List<FilterValue>?) {
        view.adapter ?: run {
            view.adapter = FilterValueAdapter()
        }
        (view.adapter as FilterValueAdapter).submitList(filters?.toMutableList())
    }
}