package com.hujiayucc.qqsender.ui.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.ui.base.BaseExListAdapter
import com.hujiayucc.qqsender.ui.base.GroupBean
import com.hujiayucc.qqsender.ui.base.ItemsBean
import com.hujiayucc.qqsender.utils.Const
import com.hujiayucc.qqsender.utils.Const.Companion.qqlist

class FriendFragment : Fragment() {
    private var groups = java.util.ArrayList<GroupBean>()
    private var items = java.util.ArrayList<ItemsBean>()
    private var child = java.util.ArrayList<java.util.ArrayList<ItemsBean>>()

    private lateinit var listView: ExpandableListView
    private lateinit var refresh: SwipeRefreshLayout

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend, null, false)
    }

    override fun onStart() {
        super.onStart()
        listView = requireView().findViewById(R.id.listView)
        refresh = requireView().findViewById(R.id.refresh)
        refresh.setColorSchemeColors(requireContext().getColor(android.R.color.holo_orange_light))
        refresh.setOnRefreshListener {
            try {
                initData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        listView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            try {
                val info = child[groupPosition][childPosition]
                info.check = !info.check
                info.checkBox!!.isChecked = info.check
                if (info.check)
                    qqlist.add(info.friend)
                else
                    qqlist.remove(info.friend)
                for (i in child[groupPosition].toList()) {
                    if (!i.check) {
                        groups[groupPosition].check = false
                        groups[groupPosition].checkBox!!.isChecked = groups[groupPosition].check
                        return@setOnChildClickListener false
                    }
                    groups[groupPosition].check = true
                }
                groups[groupPosition].checkBox!!.isChecked = groups[groupPosition].check
            } catch (e: Exception) {
                AlertDialog.Builder(this@FriendFragment.requireContext())
                    .setTitle("错误信息")
                    .setMessage(e.toString())
                    .setPositiveButton("关闭", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.dismiss()
                        }
                    })
                    .create().show()
                e.printStackTrace()
            }
            false
        }

        try {
            initData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initData() {
        qqlist.clear()
        groups.clear()
        child.clear()
        items.clear()
        refresh.isRefreshing = true
        for (friendGroup in Const.bot!!.friendGroups.asCollection().toList()) {
            groups.add(GroupBean("${friendGroup.name}  (${friendGroup.count})"))
            for (friend in friendGroup.friends.toList()) {
                items.add(ItemsBean(friend, friend.id, friend.remark))
            }
            child.add(ArrayList(items))
            items.clear()
        }
        listView.setAdapter(BaseExListAdapter(this@FriendFragment.requireContext(), groups, child))
        refresh.isRefreshing = false
    }
}