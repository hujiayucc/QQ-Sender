package com.hujiayucc.qqsender.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hujiayucc.qqsender.Applications
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.ui.activity.SendGFActivity
import com.hujiayucc.qqsender.ui.base.BaseListAdapter
import com.hujiayucc.qqsender.ui.base.GroupsBean
import com.hujiayucc.qqsender.utils.Const
import com.hujiayucc.qqsender.utils.Const.Companion.grouplist

class GroupFragment : Fragment() {
    private var groups = java.util.ArrayList<GroupsBean>()

    private lateinit var listView: ListView
    private lateinit var refresh: SwipeRefreshLayout

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group, null, false)
    }

    @SuppressLint("RtlHardcoded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        listView.setOnItemClickListener { p0, p1, p2, p3 ->
            val info = groups[p2]
            info.check = !info.check
            info.checkBox?.isChecked = info.check
            if (info.check)
                grouplist.add(groups[p2].group)
            else
                grouplist.remove(groups[p2].group)
        }

        listView.setOnItemLongClickListener { p0, p1, p2, p3 ->
            val popupMenu = PopupMenu(requireContext(), p1)
            popupMenu.menuInflater.inflate(R.menu.groups, popupMenu.menu)
            popupMenu.gravity = Gravity.RIGHT
            popupMenu.setOnMenuItemClickListener { p4 ->
                when (p4.itemId) {
                    R.id.add_all -> {
                        for (group in groups) {
                            if (!group.check) grouplist.add(group.group)
                            group.check = true
                            group.checkBox?.isChecked = true
                        }
                    }

                    R.id.clear_all -> {
                        grouplist.clear()
                        for (group in groups) {
                            if (group.check) grouplist.remove(group.group)
                            group.check = false
                            group.checkBox?.isChecked = false
                        }
                    }

                    R.id.fx -> {
                        for (group in groups) {
                            if (!group.check) grouplist.add(group.group)
                            else grouplist.remove(group.group)
                            group.check = !group.check
                            group.checkBox?.isChecked = group.check
                        }
                    }

                    R.id.open -> {
                        val intent = Intent(Applications.context, SendGFActivity::class.java)
                        intent.putExtra("qq", groups[p2].group.id)
                        startActivity(intent)
                    }

                    else -> {}
                }
                true
            }
            popupMenu.show()
            true
        }

        try {
            initData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initData() {
        grouplist.clear()
        groups.clear()
        refresh.isRefreshing = true
        for (group in Const.bot!!.groups.toList()) {
            groups.add(GroupsBean(group, group.id, group.name))
        }
        listView.adapter = BaseListAdapter(groups)
        refresh.isRefreshing = false
    }
}