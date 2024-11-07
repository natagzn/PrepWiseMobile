package com.example.prepwise.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.MainActivity.Companion.currentUser
import com.example.prepwise.adapters.ViewPagerUserAdapter
import com.example.prepwise.models.People
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserProfileFragment : Fragment() {
    private var userId: Int? = null
    private var user: People? = null
    private lateinit var userResources: ArrayList<Resource>
    private lateinit var userSets: ArrayList<Set>

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int): UserProfileFragment {
            val fragment = UserProfileFragment()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var setUsername: TextView
    private lateinit var setNumberOfFollowing: TextView
    private lateinit var setNumberOfFollower: TextView
    private lateinit var setStatus: TextView
    private lateinit var statusBtn: LinearLayout
    private lateinit var setDescription: TextView
    private lateinit var setEmail: TextView
    private lateinit var setLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(ARG_USER_ID)
        }

        userId?.let {
            user = MainActivity.getUserById(it)
            userResources = user?.resouces ?: arrayListOf()
            userSets = user?.sets ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        setUsername = view.findViewById(R.id.username)
        setNumberOfFollowing = view.findViewById(R.id.number_of_following)
        setNumberOfFollower = view.findViewById(R.id.number_of_followers)
        setStatus = view.findViewById(R.id.people_status)
        setDescription = view.findViewById(R.id.description)
        setEmail = view.findViewById(R.id.email)
        setLocation = view.findViewById(R.id.location)
        statusBtn = view.findViewById(R.id.status_btn)

        user?.let{
            setUsername.text = it.username
            setDescription.text = it.description
            setEmail.text = it.email
            setLocation.text = it.location

            if(it.status == "Friends") setStatus.text = getString(R.string.friends)
            else if(it.status == "Follower") setStatus.text = getString(R.string.follower)
            else if(it.status == "Following") setStatus.text = getString(R.string.following)
            else {
                setStatus.text = getString(R.string.follow)
                statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.gray_rounded_stroke)
            }
        }

        statusBtn.setOnClickListener {
            if (user?.status == "Following") {
                DialogUtils.showConfirmationDialog(
                    requireContext(),
                    message = getString(R.string.are_you_sure_you_want_to_unsubscribe),
                    positiveButtonText = getString(R.string.unfollow),
                    negativeButtonText = getString(R.string.cancel)
                ) { confirmed ->
                    if (confirmed) {
                        user!!.status = "none"
                        setStatus.text = getString(R.string.follow)
                        statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.gray_rounded_stroke)
                        setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
            if (user?.status == "Friends") {
                DialogUtils.showConfirmationDialog(
                    requireContext(),
                    message = getString(R.string.are_you_sure_you_want_to_unsubscribe),
                    positiveButtonText = getString(R.string.unfollow),
                    negativeButtonText = getString(R.string.cancel)
                ) { confirmed ->
                    if (confirmed) {
                        user!!.status = "Follower"
                        setStatus.text = getString(R.string.follower)
                        statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_rounded_background)
                        setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                }
            }
            else if (user?.status == "Follower") {
                user!!.status = "Friends"
                setStatus.text = getString(R.string.friends)
                statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_rounded_background)
                setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            else if (user?.status!!.toLowerCase() == "none") {
                user!!.status = "Following"
                setStatus.text = getString(R.string.following)
                statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_rounded_background)
                setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val adapter = ViewPagerUserAdapter(userSets, userResources, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.sets)
                1 -> tab.text = getString(R.string.resources)
            }
        }.attach()

        val backButton: ImageView = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Встановлення фото профілю
        val (initials, backgroundColor) = generateAvatar(currentUser.username)
        val userInitialsView:TextView = view.findViewById(R.id.user_initials)
        userInitialsView.text = initials

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            color = ColorStateList.valueOf(backgroundColor)
        }
        userInitialsView.background = drawable

        return view
    }

    fun generateAvatar(username: String): Pair<String, Int> {
        val initials = if (username.isNotEmpty()) username.take(2).uppercase() else "N/A"

        // Генерація кольору на основі хешу імені
        val hash = username.fold(0) { acc, char -> acc + char.code }
        val hue = hash % 360
        val color = Color.HSVToColor(floatArrayOf(hue.toFloat(), 0.3f, 0.7f)) // Колір у форматі HSL

        return Pair(initials, color)
    }
}