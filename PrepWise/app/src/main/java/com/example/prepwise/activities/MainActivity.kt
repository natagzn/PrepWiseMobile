package com.example.prepwise.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.HomeFragment
import com.example.prepwise.DialogUtils
import com.example.prepwise.FolderListProvider
import com.example.prepwise.LocaleHelper.loadLocale
import com.example.prepwise.R
import com.example.prepwise.ResourceListProvider
import com.example.prepwise.SetListProvider
import com.example.prepwise.SharedSetListProvider
import com.example.prepwise.fragments.LibraryFragment
import com.example.prepwise.fragments.LikedFragment
import com.example.prepwise.fragments.ProfileFragment
import com.example.prepwise.models.Folder
import com.example.prepwise.models.People
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set
import com.example.prepwise.models.SharedSet
import com.example.prepwise.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null

        val userList: ArrayList<People> = arrayListOf()

        fun getSetById(setId: Int): Set? {
            return currentUser?.sets?.find { it.id == setId }
        }

        fun getFolderById(folderId: Int): Folder? {
            return currentUser?.folders?.find { it.id == folderId }
        }

        fun getUserById(userId: Int): People? {
            // Спочатку шукаємо у загальному списку користувачів
            userList.find { it.id == userId }?.let { return it }

            // Далі шукаємо серед друзів, підписників та підписок поточного користувача
            currentUser?.let { currentUser ->
                currentUser.friends.find { it.id == userId }?.let { return it }
                currentUser.followers.find { it.id == userId }?.let { return it }
                currentUser.following.find { it.id == userId }?.let { return it }
            }

            // Якщо користувача не знайдено
            return null
        }


        fun dpToPx(dp: Int, context: Context): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Перевірка авторизації
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val authToken = sharedPref.getString("auth_token", null)

        if (authToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)
        }

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        userList.addAll(SharedSetListProvider.peopleList)
        currentUser = User(
            id = 1,
            userImg = "https://example.com/image.jpg",
            username = "john_doe",
            description = "This is a sample description for the user.",
            email = "john.doe@example.com",
            location = "Ukraine",
            sets = SetListProvider.setList,
            sharedSets = SharedSetListProvider.sharedSetList,
            resouces = ResourceListProvider.resourceList,
            folders = FolderListProvider.folderList,
            friends = arrayListOf(
                People(
                    id = 1,
                    userImg = "img_anna",
                    username = "AnnaNahalkaaaaaaaa",
                    status = "Friends",
                    numberOfFollowing = 150,
                    numberOfFollowers = 300,
                    description = "Loves teaching math",
                    email = "anna@example.com",
                    location = "Kyiv, Ukraine",
                    sets = arrayListOf(),
                    resouces = arrayListOf()
                ),
                People(
                    id = 2,
                    userImg = "img_john",
                    username = "John",
                    status = "Follower",
                    numberOfFollowing = 200,
                    numberOfFollowers = 500,
                    description = "History enthusiast",
                    email = "john@example.com",
                    location = "Lviv, Ukraine",
                    sets = arrayListOf(),
                    resouces = arrayListOf()
                )
            ),
            followers = arrayListOf(
                People(
                    id = 3,
                    userImg = "img_anna",
                    username = "AnnaNahalkaaaaaaaa",
                    status = "Friends",
                    numberOfFollowing = 150,
                    numberOfFollowers = 300,
                    description = "Loves teaching math",
                    email = "anna@example.com",
                    location = "Kyiv, Ukraine",
                    sets = arrayListOf(),
                    resouces = arrayListOf()
                ),
                People(
                    id = 4,
                    userImg = "img_paul",
                    username = "Paul",
                    status = "Follower",
                    numberOfFollowing = 220,
                    numberOfFollowers = 430,
                    description = "Physics enthusiast",
                    email = "paul@example.com",
                    location = "Kharkiv, Ukraine",
                    sets = arrayListOf(),
                    resouces = arrayListOf()
                )
            ),
            following = arrayListOf(
                People(
                    id = 5,
                    userImg = "img_john",
                    username = "John",
                    status = "Follower",
                    numberOfFollowing = 200,
                    numberOfFollowers = 500,
                    description = "History enthusiast",
                    email = "john@example.com",
                    location = "Lviv, Ukraine",
                    sets = arrayListOf(),
                    resouces = arrayListOf()
                ),
                People(
                    id = 6,
                    userImg = "img_nina",
                    username = "Nina",
                    status = "Following",
                    numberOfFollowing = 180,
                    numberOfFollowers = 320,
                    description = "Biology lover",
                    email = "nina@example.com",
                    location = "Odesa, Ukraine",
                    sets = arrayListOf(),
                    resouces = arrayListOf()
                )
            ),
            premium = false
        )

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE

        // Перенаправленнян на фрагмент
        val fragmentToOpen = intent.getStringExtra("openFragment")
        if (fragmentToOpen == "ProfileFragment") replaceFragment(ProfileFragment())
        else if (fragmentToOpen == "LibratyFragment") replaceFragment(LibraryFragment())
        else if (fragmentToOpen == "LikedFragment") replaceFragment(LikedFragment())
        else replaceFragment(HomeFragment())

        // Клік на меню навігації
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    intent.putExtra("openFragment", "HomeFragment")
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.bottom_liked -> {
                    intent.putExtra("openFragment", "LikedFragment")
                    replaceFragment(LikedFragment())
                    true
                }

                R.id.bottom_library -> {
                    intent.putExtra("openFragment", "LibraryFragment")
                    replaceFragment(LibraryFragment())
                    true
                }

                R.id.bottom_profile -> {
                    intent.putExtra("openFragment", "ProfileFragment")
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }

        // Відриття меню для додавання
        val addBtn: FloatingActionButton = findViewById(R.id.add_btn)
        addBtn.setOnClickListener {
            showBottomDialog()
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_add)

        val addQuestion: LinearLayout = dialog.findViewById(R.id.add_question)
        val addSet: LinearLayout = dialog.findViewById(R.id.add_set)
        val addResource: LinearLayout = dialog.findViewById(R.id.add_resource)
        val addFolder: LinearLayout = dialog.findViewById(R.id.add_folder)
        val close: ImageView = dialog.findViewById(R.id.close)

        addQuestion.setOnClickListener{
            val intent = Intent(this, NewQuestionActivity::class.java)
            startActivity(intent)
        }

        addSet.setOnClickListener{
            if(!currentUser!!.premium && currentUser!!.folders.size > 19){
                DialogUtils.showPremiumDialog(this)
            }
            else{
                val intent = Intent(this, NewSetActivity::class.java)
                startActivity(intent)
            }
        }

        addResource.setOnClickListener{
            val intent = Intent(this, NewResourceActivity::class.java)
            startActivity(intent)
        }

        addFolder.setOnClickListener{
            if(!currentUser!!.premium && currentUser!!.folders.size > 4){
                DialogUtils.showPremiumDialog(this)
            }
            else{
                val intent = Intent(this, NewFolderActivity::class.java)
                startActivity(intent)
            }
        }

        close.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}