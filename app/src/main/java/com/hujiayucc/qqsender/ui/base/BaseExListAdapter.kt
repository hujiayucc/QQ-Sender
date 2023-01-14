package com.hujiayucc.qqsender.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.utils.Const.Companion.qqlist
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

/**
 * ExListAdapter
 * @param context 上下文
 * @param group 分组名称
 * @param child 共有两个List 第一个是组别，第二个是组别中的数据
 */
class BaseExListAdapter(
    val context: Context,
    val group: List<GroupBean>,
    val child: List<List<ItemsBean>>
) : BaseExpandableListAdapter() {

    /** 父项的个数 */
    override fun getGroupCount(): Int {
        return group.size
    }

    /** 某个父项的子项的个数 */
    override fun getChildrenCount(groupPosition: Int): Int {
        return child[groupPosition].size //需要注意
    }

    /** 获得某个父项 */
    override fun getGroup(groupPosition: Int): Any {
        return group[groupPosition]
    }

    /** 获得某个子项 */
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return child[childPosition]
    }

    /** 父项的Id */
    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    /** 子项的id */
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    /** 获取父项的view */
    @SuppressLint("InflateParams")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertViews: View?, parent: ViewGroup?): View {
        var convertView = convertViews
        val viewHolder: GroupHolder
        if (convertView == null) {
            viewHolder = GroupHolder()
            convertView = LayoutInflater.from(context).inflate(R.layout.robot_group, null)
            //对viewHolder的属性进行赋值
            viewHolder.qqName = convertView!!.findViewById(R.id.qq_name)
            viewHolder.checkBox = convertView.findViewById<View>(R.id.check_group) as CheckBox
            //使用setTag缓存起来方便多次重用
            convertView.tag = viewHolder
        } else {
            //如果缓存池中有对应的缓存，则直接通过getTag取出viewHolder
            viewHolder = convertView.tag as GroupHolder
        }

        val info = group[groupPosition]
        viewHolder.run {
            qqName.text = group[groupPosition].name
            checkBox.setOnClickListener {
                info.check = !info.check
                info.checkBox!!.isChecked = info.check
                if (info.check) {
                    for (child in child.get(groupPosition).toList()) {
                        if (!child.check) {
                            qqlist.add(child.friend)
                            child.check = true
                            child.checkBox?.isChecked = child.check
                        }
                    }
                } else {
                    for (child in child.get(groupPosition).toList()) {
                        if (child.check) {
                            qqlist.remove(child.friend)
                            child.check = false
                            child.checkBox?.isChecked = child.check
                        }
                    }
                }
            }

            for (g in group.toList()) {
                g.checkBox?.isChecked = g.check
            }

            checkBox.isChecked = info.check
            group[groupPosition].checkBox = checkBox
        }
        return convertView
    }

    /** 获取子项的view */
    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertViews: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertViews
        val viewHolder: ItemsHolder
        if (convertView == null) {
            viewHolder = ItemsHolder()
            convertView = LayoutInflater.from(context).inflate(R.layout.robot_child, null)
            //对viewHolder的属性进行赋值
            viewHolder.qqName = convertView!!.findViewById<View>(R.id.qq_name) as TextView
            viewHolder.qqFace = convertView.findViewById<View>(R.id.qq_face) as ImageView
            viewHolder.qqId = convertView.findViewById<View>(R.id.qq_id) as TextView
            viewHolder.checkBox = convertView.findViewById<View>(R.id.check_child) as CheckBox
            //使用setTag缓存起来方便多次重用
            convertView.tag = viewHolder
        } else {
            //如果缓存池中有对应的缓存，则直接通过getTag取出viewHolder
            viewHolder = convertView.tag as ItemsHolder
        }
        val info = child[groupPosition][childPosition]
        viewHolder.run {
            qqId.text = "${info.qq}"
            qqName.text = info.name
            Picasso.with(context).load(info.friend.avatarUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .transform(CircleTransform()).into(qqFace)
            checkBox.isChecked = info.check
            info.checkBox = checkBox
        }
        return convertView
    }

    /** 子项是否可选中,如果要设置子项的点击事件,需要返回true */
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    /** 用于缓存Group控件 */
    internal inner class GroupHolder {
        lateinit var qqName: TextView
        lateinit var checkBox: CheckBox
    }

    /** 用于缓存items控件 */
    internal inner class ItemsHolder {
        lateinit var qqFace: ImageView
        lateinit var qqName: TextView
        lateinit var qqId: TextView
        lateinit var checkBox: CheckBox
    }
}