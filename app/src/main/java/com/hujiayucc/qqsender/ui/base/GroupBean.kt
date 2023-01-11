package com.hujiayucc.qqsender.ui.base

import android.widget.CheckBox

/**
 * 分组数据Bean
 * @param name 分组名称
 * @param checkBox 复选框
 * @param check 选中状态
 */
class GroupBean(val name: String) {
    var checkBox: CheckBox? = null
    var check = false
}