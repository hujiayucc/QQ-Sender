package com.hujiayucc.qqsender.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.hujiayucc.qqsender.Applications.Companion.context
import com.hujiayucc.qqsender.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class BaseListAdapter(
    val groupList: List<GroupsBean>
) : BaseAdapter() {
    override fun getCount(): Int {
        return groupList.size
    }

    override fun getItem(position: Int): GroupsBean {
        return groupList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.robot_child, null)
        val info = getItem(position)
        val qqFace: ImageView = view.findViewById(R.id.qq_face)
        val qqName: TextView = view.findViewById(R.id.qq_name)
        val qqId: TextView = view.findViewById(R.id.qq_id)
        val checkBox: CheckBox = view.findViewById(R.id.check_child)
        qqName.text = info.group.name
        qqId.text = info.group.id.toString()
        checkBox.isChecked = info.check
        Picasso.with(context).load(info.group.avatarUrl)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .transform(CircleTransform()).into(qqFace)
        info.checkBox = checkBox
        return view
    }

}