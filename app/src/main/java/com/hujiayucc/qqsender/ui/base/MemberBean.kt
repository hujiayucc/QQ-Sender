package com.hujiayucc.qqsender.ui.base

import android.widget.CheckBox
import net.mamoe.mirai.contact.NormalMember

/**
 * 好友数据Bean
 * @param member 群成员
 * @param qq QQ号
 * @param name 群名片或昵称
 */
class MemberBean(val member: NormalMember, val qq: Long, val name: String) {
    var checkBox: CheckBox? = null
    var check = false
}