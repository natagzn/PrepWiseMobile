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

        val emptySetList = arrayListOf<Set>()
        val emptyResourcesList = arrayListOf<Resourse>()

        val followingList = arrayListOf(
            People(
                userImg = "img1.png",
                username = "User1",
                status = "Following",
                numberOfFollowing = 100,
                numberOfFollowers = 200,
                description = "User1 description",
                email = "user1@example.com",
                location = "City1",
                sets = emptySetList,
                resouces = emptyResourcesList
            ),
            People(
                userImg = "img2.png",
                username = "User2",
                status = "Following",
                numberOfFollowing = 150,
                numberOfFollowers = 180,
                description = "User2 description",
                email = "user2@example.com",
                location = "City2",
                sets = emptySetList,
                resouces = emptyResourcesList
            ),
            People(
                userImg = "img6.png",
                username = "User6",
                status = "Friends",
                numberOfFollowing = 220,
                numberOfFollowers = 280,
                description = "User6 description",
                email = "user6@example.com",
                location = "City6",
                sets = emptySetList,
                resouces = emptyResourcesList
            )
        )

        val followersList = arrayListOf(
            People(
                userImg = "img3.png",
                username = "User3",
                status = "Followers",
                numberOfFollowing = 80,
                numberOfFollowers = 120,
                description = "User3 description",
                email = "user3@example.com",
                location = "City3",
                sets = emptySetList,
                resouces = emptyResourcesList
            ),
            People(
                userImg = "img4.png",
                username = "User4",
                status = "Followers",
                numberOfFollowing = 70,
                numberOfFollowers = 90,
                description = "User4 description",
                email = "user4@example.com",
                location = "City4",
                sets = emptySetList,
                resouces = emptyResourcesList
            ),
            People(
                userImg = "img6.png",
                username = "User6",
                status = "Friends",
                numberOfFollowing = 220,
                numberOfFollowers = 280,
                description = "User6 description",
                email = "user6@example.com",
                location = "City6",
                sets = emptySetList,
                resouces = emptyResourcesList
            )
        )

        val friendsList = arrayListOf(
            People(
                userImg = "img5.png",
                username = "User5",
                status = "Friends",
                numberOfFollowing = 200,
                numberOfFollowers = 250,
                description = "User5 description",
                email = "user5@example.com",
                location = "City5",
                sets = emptySetList,
                resouces = emptyResourcesList
            ),
            People(
                userImg = "img6.png",
                username = "User6",
                status = "Friends",
                numberOfFollowing = 220,
                numberOfFollowers = 280,
                description = "User6 description",
                email = "user6@example.com",
                location = "City6",
                sets = emptySetList,
                resouces = emptyResourcesList
            )
        )

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
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openFragment", "ProfileFragment")
            startActivity(intent)
            finish()
        }

    }
}