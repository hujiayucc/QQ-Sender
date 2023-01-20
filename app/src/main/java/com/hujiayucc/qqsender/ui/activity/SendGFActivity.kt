package com.hujiayucc.qqsender.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.databinding.ActivitySendgfBinding
import com.hujiayucc.qqsender.ui.base.BaseListAdapter2
import com.hujiayucc.qqsender.ui.base.MemberBean
import com.hujiayucc.qqsender.utils.Const
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.numberlist
import com.hujiayucc.qqsender.utils.Toast
import net.mamoe.mirai.contact.isFriend
import net.mamoe.mirai.contact.nameCardOrNick

/**
 * 发送群成员消息
 */
class SendGFActivity : AppCompatActivity() {
    private var friends = java.util.ArrayList<MemberBean>()
    lateinit var binding: ActivitySendgfBinding
    lateinit var refresh: SwipeRefreshLayout
    lateinit var listView: ListView
    var qq: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        initView()
    }

    @SuppressLint("RtlHardcoded")
    private fun initView() {
        qq = intent.extras?.getLong("qq") ?: 0L
        if (qq == 0L) finish()
        binding = ActivitySendgfBinding.inflate(layoutInflater)
        refresh = binding.refresh
        listView = binding.listView
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = bot!!.getGroup(qq)?.name
        supportActionBar?.subtitle = "${bot!!.getGroup(qq)?.members?.size}人"
        refresh.setColorSchemeColors(applicationContext.getColor(android.R.color.holo_orange_light))
        refresh.setOnRefreshListener {
            try {
                initData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        listView.setOnItemClickListener { p0, p1, p2, p3 ->
            val info = friends[p2]
            info.check = !info.check
            info.checkBox?.isChecked = info.check
            if (info.check)
                numberlist.add(info.member)
            else
                numberlist.remove(info.member)
        }

        listView.setOnItemLongClickListener { p0, p1, p2, p3 ->
            val popupMenu = PopupMenu(this, p1)
            popupMenu.menuInflater.inflate(R.menu.groups, popupMenu.menu)
            popupMenu.menu.removeItem(R.id.open)
            popupMenu.gravity = Gravity.RIGHT
            popupMenu.setOnMenuItemClickListener { p4 ->
                when (p4.itemId) {
                    R.id.add_all -> {
                        for (friend in friends) {
                            if (!friend.check) numberlist.add(friend.member)
                            friend.check = true
                            friend.checkBox?.isChecked = true
                        }
                    }

                    R.id.clear_all -> {
                        numberlist.clear()
                        for (friend in friends) {
                            if (friend.check) numberlist.remove(friend.member)
                            friend.check = false
                            friend.checkBox?.isChecked = false
                        }
                    }

                    R.id.fx -> {
                        for (friend in friends) {
                            if (!friend.check) numberlist.add(friend.member)
                            else numberlist.remove(friend.member)
                            friend.check = !friend.check
                            friend.checkBox?.isChecked = friend.check
                        }
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
        numberlist.clear()
        friends.clear()
        refresh.isRefreshing = true
        for (number in bot!!.getGroup(qq)!!.members.toList()) {
            if (number.isFriend) {
                val friend = bot!!.getFriend(number.id)
                friends.add(MemberBean(number, number.id, friend?.remark ?: "获取好友备注失败"))
            } else friends.add(MemberBean(number, number.id, number.nameCardOrNick))
        }

        listView.adapter = BaseListAdapter2(friends)
        refresh.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_friend, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.send -> {
                if (numberlist.size < 1) Toast.Long("先选择发送目标吧")
                else {
                    val intent = Intent(applicationContext, SendActivity::class.java)
                    intent.putExtra("type", Const.send_type_group_friend)
                    startActivity(intent)
                }
            }

            else -> {}
        }
        return true
    }
}