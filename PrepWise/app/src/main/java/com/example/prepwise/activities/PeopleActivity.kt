package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.R
import com.example.prepwise.adapters.ViewPagerPeopleAdapter
import com.example.prepwise.models.People
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PeopleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_people)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val followingList = MainActivity.currentUser!!.following
        val followersList = MainActivity.currentUser!!.followers
        val friendsList = MainActivity.currentUser!!.friends

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = ViewPagerPeopleAdapter(followingList, followersList, friendsList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.following)
                1 -> getString(R.string.followers)
                2 -> getString(R.string.friends)
                else -> getString(R.string.following)
            }
        }.attach()

        // Закриття сторінки
        val close: ImageView = findViewById(R.id.close)
        close.setOnClickListener {
            finish()
        }

    }
}