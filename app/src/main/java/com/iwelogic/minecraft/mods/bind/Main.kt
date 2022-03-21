package com.iwelogic.minecraft.mods.bind

import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import com.iwelogic.minecraft.mods.R


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
}