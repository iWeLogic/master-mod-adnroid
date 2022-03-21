package com.iwelogic.minecraft.mods.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fm: FragmentManager, lifeCycle: Lifecycle) : FragmentStateAdapter(fm, lifeCycle) {

    var fragments: MutableList<Fragment?> = ArrayList()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]!!
}