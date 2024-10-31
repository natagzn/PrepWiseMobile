package com.example.prepwise.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.prepwise.DialogUtils
import com.example.prepwise.LocaleHelper.loadLocale
import com.example.prepwise.LocaleHelper.setLocale
import com.example.prepwise.R
import com.example.prepwise.activities.LoginActivity
import com.example.prepwise.activities.PeopleActivity
import com.example.prepwise.activities.PremiumActivity

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        loadLocale(requireContext())

        // Зміна мови
        val changeLanguage: LinearLayout = view.findViewById(R.id.language)
        changeLanguage.setOnClickListener {
            showChangeLang()
        }

        // Вихід з системи
        val logOut: LinearLayout = view.findViewById(R.id.log_out)
        logOut.setOnClickListener{
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                message = getString(R.string.are_you_sure_you_want_to_log_out),
                positiveButtonText = getString(R.string.log_out),
                negativeButtonText = getString(R.string.cancel)
            ) { confirmed ->
                if (confirmed) {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        // Видалення акаунту
        val deleteAccount: LinearLayout = view.findViewById(R.id.delete_account)
        deleteAccount.setOnClickListener{
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                message = getString(R.string.are_you_sure_you_want_to_delete_account),
                positiveButtonText = getString(R.string.Delete),
                negativeButtonText = getString(R.string.cancel)
            ) { confirmed ->
            }
        }

        // Редагування профілю
        val editProfile: ImageView = view.findViewById(R.id.edit)
        editProfile.setOnClickListener{
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val targetFragment = EditProfileFragment()
            fragmentTransaction.replace(R.id.fragment_container, targetFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        // Преміум
        val premium: LinearLayout = view.findViewById(R.id.premium)
        premium.setOnClickListener{
            val intent = Intent(requireActivity(), PremiumActivity::class.java)
            startActivity(intent)
        }

        // Лист із запитанням
        val askQuestionLayout: LinearLayout = view.findViewById(R.id.ask_a_question)
        askQuestionLayout.setOnClickListener {
            DialogUtils.showAnswerDialog(requireContext())
        }

        // Відкриття списку друзів
        val myPeople: LinearLayout = view.findViewById(R.id.my_people)
        myPeople.setOnClickListener{
            val intent = Intent(requireActivity(), PeopleActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun showChangeLang() {
        val listItems = arrayOf("Українська", "English")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle(getString(R.string.choose_language))
        mBuilder.setItems(listItems) { dialog, which ->
            when (which) {
                0 -> {
                    setLocale("uk", requireContext())
                    requireActivity().recreate()
                }
                1 -> {
                    setLocale("en", requireContext())
                    requireActivity().recreate()
                }
            }
            dialog.dismiss()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }
}