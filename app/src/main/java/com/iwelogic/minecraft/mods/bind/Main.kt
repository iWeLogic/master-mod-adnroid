package com.iwelogic.minecraft.mods.bind

import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.main.mods.ModAdapter


object Main {

    @BindingAdapter("sort", "onSelect")
    @JvmStatic
    fun setSort(view: ImageView, sort: String?, onSelect: (String) -> Unit) {
        view.setOnClickListener {
            val popup = PopupMenu(view.context, view, Gravity.END)
            popup.inflate(R.menu.sort)
            when (sort) {
                "likes" -> popup.menu.findItem(R.id.sort_likes).isChecked = true
                "installs" -> popup.menu.findItem(R.id.sort_installs).isChecked = true
                "date" -> popup.menu.findItem(R.id.sort_date).isChecked = true
                else -> popup.menu.findItem(R.id.sort_default).isChecked = true
            }
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_default -> {
                        onSelect.invoke("default")
                        item.isChecked = !item.isChecked
                    }
                    R.id.sort_likes -> {
                        onSelect.invoke("likes")
                        item.isChecked = !item.isChecked
                    }
                    R.id.sort_installs -> {
                        onSelect.invoke("installs")
                        item.isChecked = !item.isChecked
                    }
                    R.id.sort_date -> {
                        onSelect.invoke("date")
                        item.isChecked = !item.isChecked
                    }
                }
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
        if(layoutManager.spanCount == 2){
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    view.adapter?.let {
                        Log.w("myLog", "getSpanSize: XXX")
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
}