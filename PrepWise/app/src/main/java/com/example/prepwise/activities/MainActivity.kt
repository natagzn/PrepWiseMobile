package com.example.prepwise.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.example.HomeFragment
import com.example.prepwise.R
import com.example.prepwise.fragments.LibraryFragment
import com.example.prepwise.fragments.LikedFragment
import com.example.prepwise.fragments.ProfileFragment
import com.example.prepwise.models.Category
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Level
import com.example.prepwise.models.People
import com.example.prepwise.models.Set
import com.example.prepwise.models.User
import com.example.prepwise.objects.DialogUtils
import com.example.prepwise.objects.LocaleHelper.loadLocale
import com.example.prepwise.objects.RetrofitInstance
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    companion object {
        var currentUser: User = User()
        val categories: ArrayList<Category> = arrayListOf()
        val levels: ArrayList<Level> = arrayListOf()

        fun getSetById(setId: Int): Set? {
            return currentUser?.sets?.find { it.id == setId }
        }

        fun getFolderById(folderId: Int): Folder? {
            return currentUser?.folders?.find { it.id == folderId }
        }

        fun getUserById(userId: Int): People? {
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

        RetrofitInstance.initRetrofit(this)

        // Перевірка авторизації
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val authToken = sharedPref.getString("auth_token", null)

        if (authToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            loadData()
        }

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

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

    private fun loadData() {
        lifecycleScope.launch {
            fetchCategories()
            fetchLevels()
            fetchUserProfile()
        }
    }

    private suspend fun fetchCategories() {
        try {
            val response = RetrofitInstance.api().getCategories()
            if (response.isSuccessful && response.body() != null) {
                categories.clear()
                categories.addAll(response.body()!!.categories.map { Category(it.category_id, it.name) })
            } else {
                Log.e("MainActivity", "Error fetching categories: ${response.message()}")
            }
        } catch (e: HttpException) {
            Log.e("MainActivity", "HttpException: ${e.message}")
        } catch (e: Exception) {
            Log.e("MainActivity", "Exception: ${e.message}")
        }
    }

    private suspend fun fetchLevels() {
        try {
            val response = RetrofitInstance.api().getLevels()
            if (response.isSuccessful && response.body() != null) {
                levels.clear()
                levels.addAll(response.body()!!.map { Level(it.level_id, it.name) })
            } else {
                Log.e("MainActivity", "Error fetching levels: ${response.message()}")
            }
        } catch (e: HttpException) {
            Log.e("MainActivity", "HttpException: ${e.message}")
        } catch (e: Exception) {
            Log.e("MainActivity", "Exception: ${e.message}")
        }
    }

    private suspend fun fetchUserProfile() {
        try {
            val response = RetrofitInstance.api().getUserProfile()
            if (response.isSuccessful && response.body() != null) {
                val userProfile = response.body()!!
                currentUser.apply {
                    id = userProfile.id
                    userImg = userProfile.userImg
                    username = userProfile.username
                    email = userProfile.email
                    bio = userProfile.bio
                    location = userProfile.location
                }
                Log.d("MainActivity", "User profile fetched successfully - " + userProfile.username)
            } else {
                Log.e("MainActivity", "Error fetching user profile: ${response.message()}")
            }
        } catch (e: HttpException) {
            Log.e("MainActivity", "HttpException: ${e.message}")
        } catch (e: Exception) {
            Log.e("MainActivity", "Exception: ${e.message}")
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