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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.utils.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.MainActivity.Companion.currentUser
import com.example.prepwise.adapters.ViewPagerUserAdapter
import com.example.prepwise.models.People
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.example.prepwise.repositories.PeopleRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private var userId: Int? = null
    private var user: People? = null
    private var userResources: ArrayList<Resource> = arrayListOf()
    private var userSets: ArrayList<Set> = arrayListOf()

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
    private lateinit var setLocation: TextView

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var contentPage: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(ARG_USER_ID)
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
        setLocation = view.findViewById(R.id.location)
        statusBtn = view.findViewById(R.id.status_btn)

        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        contentPage = view.findViewById(R.id.content)

        userId?.let {
            lifecycleScope.launch {
                loadingProgressBar.visibility = View.VISIBLE
                contentPage.visibility = View.GONE

                user = PeopleRepository.getPeopleById(it)

                loadingProgressBar.visibility = View.GONE
                contentPage.visibility = View.VISIBLE

                userResources = user?.resouces ?: arrayListOf()
                userSets = user?.sets ?: arrayListOf()

                user?.let{
                    setUsername.text = it.username
                    setDescription.text = it.description
                    setLocation.text = it.location
                    setNumberOfFollowing.text = it.numberOfFollowing.toString()
                    setNumberOfFollower.text = it.numberOfFollowers.toString()

                    if(it.status == "friends") setStatus.text = getString(R.string.friends)
                    else if(it.status == "follower") setStatus.text = getString(R.string.follower)
                    else if(it.status == "following") setStatus.text = getString(R.string.following)
                    else {
                        setStatus.text = getString(R.string.follow)
                        statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.gray_rounded_stroke)
                        setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }

                    // Встановлення фото профілю
                    val (initials, backgroundColor) = generateAvatar(it.username)
                    val userInitialsView:TextView = view.findViewById(R.id.user_initials)
                    userInitialsView.text = initials

                    val drawable = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        color = ColorStateList.valueOf(backgroundColor)
                    }
                    userInitialsView.background = drawable
                }

                val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
                val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

                val adapter = ViewPagerUserAdapter(userSets, userResources, requireActivity())
                viewPager.adapter = adapter

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.text = user!!.sets.size.toString() + " " + getString(R.string.sets)
                        1 -> tab.text = user!!.resouces.size.toString() + " " + getString(R.string.resources)
                    }
                }.attach()
            }
        }

        statusBtn.setOnClickListener {
            if (user?.status == "following") {
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
            if (user?.status == "friends") {
                DialogUtils.showConfirmationDialog(
                    requireContext(),
                    message = getString(R.string.are_you_sure_you_want_to_unsubscribe),
                    positiveButtonText = getString(R.string.unfollow),
                    negativeButtonText = getString(R.string.cancel)
                ) { confirmed ->
                    if (confirmed) {
                        user!!.status = "follower"
                        setStatus.text = getString(R.string.follower)
                        statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_rounded_background)
                        setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                }
            }
            else if (user?.status == "follower") {
                user!!.status = "friends"
                setStatus.text = getString(R.string.friends)
                statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_rounded_background)
                setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            else if (user?.status!!.toLowerCase() == "none") {
                user!!.status = "following"
                setStatus.text = getString(R.string.following)
                statusBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_rounded_background)
                setStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }

        val backButton: ImageView = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

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