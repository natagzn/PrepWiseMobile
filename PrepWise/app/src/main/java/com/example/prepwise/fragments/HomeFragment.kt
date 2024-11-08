package com.example

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.NotificationActivity
import com.example.prepwise.activities.PremiumActivity
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.fragments.CalendarFragment
import com.example.prepwise.models.Question
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import java.time.LocalDate

import androidx.fragment.app.commit
import com.example.prepwise.fragments.SearchFragment
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level

class HomeFragment : Fragment() {

    private var adapterSet: AdapterSet? = null
    private lateinit var recyclerViewSet: RecyclerView

    private var adapterResourse: AdapterResource? = null
    private lateinit var recyclerViewResourse: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Створення тестових рівнів та категорій
    val level1 = Level(1, "Junior")
    val level2 = Level(2, "Middle")
    val level3 = Level(3, "Senior")

    val category1 = Category(1, "Programming")
    val category2 = Category(2, "Math")
    val category3 = Category(3, "Science")

    // Створення трьох тестових об'єктів Set
    val sets = arrayListOf(
        Set(
            id = 1,
            name = "Java Basics",
            level = level1,
            categories = arrayListOf(category1),
            access = "Public",
            date = LocalDate.of(2024, 1, 1),
            questions = arrayListOf(
                Question(1, "What is Java?", "A programming language", false),
                Question(2, "What is a class?", "A blueprint for creating objects", false)
            ),
            username = "john_doe",
            isLiked = true
        ),
        Set(
            id = 2,
            name = "Algebra 101",
            level = level2,
            categories = arrayListOf(category2),
            access = "Private",
            date = LocalDate.of(2024, 2, 1),
            questions = arrayListOf(
                Question(3, "What is a polynomial?", "An expression with multiple terms", false),
                Question(4, "What is a factor?", "A number that divides another number", false)
            ),
            username = "jane_doe",
            isLiked = false
        ),
        Set(
            id = 3,
            name = "Physics Basics",
            level = level3,
            categories = arrayListOf(category3),
            access = "Public",
            date = LocalDate.of(2024, 3, 1),
            questions = arrayListOf(
                Question(5, "What is gravity?", "A force of attraction between objects", true),
                Question(6, "What is velocity?", "The speed of an object in a given direction", true)
            ),
            username = "alex_smith",
            isLiked = true
        )
    )

    // Створення трьох тестових об'єктів Resource
    val resources = arrayListOf(
        Resource(
            id = 1,
            articleBook = "Java Programming Book",
            description = "A comprehensive guide to Java programming",
            level = level1,
            category = category1,
            date = LocalDate.of(2024, 1, 5),
            username = "john_doe",
            isLiked = true,
            isDisLiked = false,
            numberOfLikes = 120,
            numberOfDislikes = 5,
            isAuthor = false
        ),
        Resource(
            id = 2,
            articleBook = "Algebra Essentials",
            description = "An introduction to algebra concepts",
            level = level2,
            category = category2,
            date = LocalDate.of(2024, 2, 15),
            username = "jane_doe",
            isLiked = false,
            isDisLiked = true,
            numberOfLikes = 85,
            numberOfDislikes = 10,
            isAuthor = false
        ),
        Resource(
            id = 3,
            articleBook = "Physics Fundamentals",
            description = "Basics of physics for beginners",
            level = level3,
            category = category3,
            date = LocalDate.of(2024, 3, 10),
            username = "alex_smith",
            isLiked = true,
            isDisLiked = false,
            numberOfLikes = 150,
            numberOfDislikes = 3,
            isAuthor = false
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Знаходимо ImageView та встановлюємо обробник кліків
        val notify: ImageView = view.findViewById(R.id.go_to_notify)
        notify.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }

        // Преміум
        val premium: TextView = view.findViewById(R.id.free_trial)
        premium.setOnClickListener{
            val intent = Intent(requireActivity(), PremiumActivity::class.java)
            startActivity(intent)
        }

        // Ініціалізуємо RecyclerView
        recyclerViewSet = view.findViewById(R.id.set_list)
        recyclerViewSet.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterSet = AdapterSet(sets, requireContext(), parentFragmentManager, "without access")
        recyclerViewSet.adapter = adapterSet

        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))

        // Ініціалізуємо RecyclerView
        recyclerViewResourse = view.findViewById(R.id.resource_list)
        recyclerViewResourse.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterResourse = AdapterResource(resources, requireContext())
        recyclerViewResourse.adapter = adapterResourse

        recyclerViewResourse.addItemDecoration(SpaceItemDecoration(spacingInPx))

        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.calendarFragmentContainer, CalendarFragment())
            }
        }

        var searchInput: EditText
        searchInput = view.findViewById(R.id.input_search)

        // Додати обробник для клавіші "Пошук" на клавіатурі
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                openSearchFragment(searchInput.text.toString())
                true
            } else {
                false
            }
        }

        return view
    }

    private fun openSearchFragment(query: String) {
        val searchFragment = SearchFragment.newInstance(query)

        // Замінюємо поточний фрагмент на SearchFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchFragment)
            .addToBackStack(null)
            .commit()
    }
}