package com.hujiayucc.qqsender.ui.base

import android.widget.CheckBox
import net.mamoe.mirai.contact.Friend

/**
 * 好友数据Bean
 * @param friend QQ好友
 * @param qq QQ号
 * @param name 好友备注
 * @param checkBox 复选框
 * @param checked 选中状态
 */
class ItemsBean(val friend: Friend, val qq: Long, val name: String) {
    var checkBox: CheckBox? = null
    var check = false
}