@file:Suppress("DEPRECATION")

package com.hujiayucc.qqsender.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class BaseAdapter(fragmentManager: FragmentManager, private var fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fragmentManager) {
    private val title = arrayOf("好友", "群聊")
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return title[position]
    }
}
