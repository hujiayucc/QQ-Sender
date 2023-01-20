package com.hujiayucc.qqsender.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.hujiayucc.qqsender.R
import com.hujiayucc.qqsender.ui.base.BaseActivity
import com.hujiayucc.qqsender.ui.base.BaseAdapter
import com.hujiayucc.qqsender.ui.fragment.FriendFragment
import com.hujiayucc.qqsender.ui.fragment.GroupFragment
import com.hujiayucc.qqsender.utils.Const.Companion.bot
import com.hujiayucc.qqsender.utils.Const.Companion.grouplist
import com.hujiayucc.qqsender.utils.Const.Companion.qqlist
import com.hujiayucc.qqsender.utils.Const.Companion.send_type_friend
import com.hujiayucc.qqsender.utils.Const.Companion.send_type_group
import com.hujiayucc.qqsender.utils.Toast


class MainActivity : BaseActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private val fragmentList = ArrayList<Fragment>()
    private lateinit var adapter: BaseAdapter
    private var type = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        fragmentList.add(FriendFragment())
        fragmentList.add(GroupFragment())
        tabLayout.setupWithViewPager(viewPager)
        adapter = BaseAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = adapter
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                type = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //导入菜单布局
        menuInflater.inflate(R.menu.main, menu)
        return true
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
                when (type) {
                    send_type_friend -> {
                        if (qqlist.size < 1) {
                            Toast.Long("先选择发送目标吧")
                        } else {
                            val intent = Intent(applicationContext, SendActivity::class.java)

                            startActivity(intent)
                        }
                    }

                    send_type_group -> {
                        if (grouplist.size < 1) {
                            Toast.Long("先选择发送目标吧")
                        } else {
                            val intent = Intent(applicationContext, SendActivity::class.java)
                            intent.putExtra("type", type)
                            startActivity(intent)
                        }
                    }

                    else -> {}
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