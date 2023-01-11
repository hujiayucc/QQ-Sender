package com.hujiayucc.qqsender.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.ui.base.BaseActivity
import com.hujiayucc.qqsender.ui.base.BaseExListAdapter
import com.hujiayucc.qqsender.ui.base.GroupBean
import com.hujiayucc.qqsender.ui.base.ItemsBean
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.qqlist
import com.hujiayucc.qqsender.utils.Toast


class MainActivity : BaseActivity() {

    private lateinit var listView: ExpandableListView
    private lateinit var refresh: SwipeRefreshLayout
    private var groups = java.util.ArrayList<GroupBean>()
    private var items = java.util.ArrayList<ItemsBean>()
    private var child = java.util.ArrayList<java.util.ArrayList<ItemsBean>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listView = binding.listView
        refresh = binding.refresh
        refresh.setColorSchemeColors(getColor(android.R.color.holo_orange_light))
        refresh.setOnRefreshListener {
            runOnUiThread {
                try {
                    initData()
                    refresh.isRefreshing = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        listView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            try {
                val info = child[groupPosition][childPosition]
                info.check = !info.check
                info.checkBox!!.isChecked = info.check
                if (info.check) qqlist.add(info.friend)
                else qqlist.remove(info.friend)
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
                AlertDialog.Builder(this@MainActivity)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //导入菜单布局
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun initData() {
        groups.clear()
        child.clear()
        items.clear()
        refresh.isRefreshing = true
        for (friendGroup in bot!!.friendGroups.asCollection().toList()) {
            groups.add(GroupBean("${friendGroup.name}  (${friendGroup.count})"))
            for (friend in friendGroup.friends.toList()) {
                items.add(ItemsBean(friend, friend.id, friend.remark))
            }
            child.add(ArrayList(items))
            items.clear()
        }
        listView.setAdapter(BaseExListAdapter(this, groups, child))
        refresh.isRefreshing = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_account -> {
                bot!!.close()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.menu_exit -> {
                bot!!.close()
                finish()
                System.exit(0)
            }

            R.id.send -> {
                if (qqlist.size < 1) {
                    Toast.Long("先选择发送目标吧")
                } else {
                    val intent = Intent(applicationContext, SendActivity::class.java)
                    startActivity(intent)
                }
            }

            else -> {}
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}