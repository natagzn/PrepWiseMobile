package com.example

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.prepwise.R
import com.example.prepwise.ResourceListProvider
import com.example.prepwise.SetListProvider
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.NotificationActivity
import com.example.prepwise.activities.PremiumActivity
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.adapters.CalendarAdapter
import com.example.prepwise.adapters.Day
import com.example.prepwise.fragments.CalendarFragment
import com.example.prepwise.models.Question
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set
import java.time.LocalDate

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

class HomeFragment : Fragment() {

    private var adapterSet: AdapterSet? = null
    private lateinit var recyclerViewSet: RecyclerView

    private var adapterResourse: AdapterResource? = null
    private lateinit var recyclerViewResourse: RecyclerView

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        adapterSet = AdapterSet(SetListProvider.setList, requireContext(), parentFragmentManager, "without access")
        recyclerViewSet.adapter = adapterSet

        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))

        // Ініціалізуємо RecyclerView
        recyclerViewResourse = view.findViewById(R.id.resource_list)
        recyclerViewResourse.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterResourse = AdapterResource(ResourceListProvider.resourceList, requireContext())
        recyclerViewResourse.adapter = adapterResourse

        recyclerViewResourse.addItemDecoration(SpaceItemDecoration(spacingInPx))

        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.calendarFragmentContainer, CalendarFragment())
            }
        }


        return view
    }
}