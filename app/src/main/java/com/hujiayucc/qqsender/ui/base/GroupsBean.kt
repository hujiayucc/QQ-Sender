package com.hujiayucc.qqsender.ui.base

import android.widget.CheckBox
import net.mamoe.mirai.contact.Group

/**
 * 好友数据Bean
 * @param group QQ群
 * @param qq QQ群号
 * @param name 群名
 * @param checkBox 复选框
 * @param checked 选中状态
 */
class GroupsBean(
    val group: Group,
    private val qq: Long,
    private val name: String
) {
    var checkBox: CheckBox? = null
    var check = false
}