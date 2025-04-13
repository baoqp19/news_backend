package com.example.news_backend.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.news_backend.R
import com.example.news_backend.activity.main.MainActivity
import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: MyFragmentAdapter
    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2

        Log.d("AccountAccount","${DataLocalManager.getInstance().getFirstInstalled()}")
        if (!DataLocalManager.getInstance().getFirstInstalled()) {
            initTabLayoutAndViewPager()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initTabLayoutAndViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sign_in)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sign_up)))

        val fragmentManager: FragmentManager = supportFragmentManager
        adapter = MyFragmentAdapter(fragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.sign_in)
                1 -> getString(R.string.sign_up)
                else -> getString(R.string.sign_in)
            }
        }.attach()
    }

}