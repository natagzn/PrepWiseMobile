package com.example.prepwise.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
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

        // Встановлення фото профілю
        val userPicture: ImageView = view.findViewById(R.id.user_picture)
        userPicture.setOnLongClickListener {
            showProfilePictureOptions()
            true
        }

        return view
    }

    // Вікно для вибору дії при кліку на фото профілю
    private fun showProfilePictureOptions() {
        val options = arrayOf(getString(R.string.set_image), getString(R.string.delete_image))

        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialogTheme)
        builder.setTitle(getString(R.string.choose_an_action))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openGallery()
                1 -> removeProfilePicture()
            }
        }

        // Створення діалогу та кастомізація
        val dialog = builder.create()
        dialog.setOnShowListener {
            val titleTextView = dialog.findViewById<TextView>(android.R.id.title)
            titleTextView?.apply {
                setTextSize(20f)
                typeface = ResourcesCompat.getFont(context, R.font.bold)
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            dialog.listView?.adapter?.let { adapter ->
                for (i in 0 until adapter.count) {
                    val item = dialog.listView.getChildAt(i) as? TextView
                    item?.apply {
                        setTextSize(18f) // Розмір тексту пунктів
                        typeface = ResourcesCompat.getFont(context, R.font.regular)
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                    }
                }
            }
        }

        dialog.show()
    }


    // видалення фото
    private fun removeProfilePicture() {
        val userPicture: ImageView = view?.findViewById(R.id.user_picture) ?: return
        userPicture.setImageResource(R.drawable.user_default_avatar)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            view?.findViewById<ImageView>(R.id.user_picture)?.setImageURI(imageUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            Toast.makeText(context, "Доступ до галереї не надано", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showChangeLang() {
        val listItems = arrayOf("Українська", "English")

        val mBuilder = AlertDialog.Builder(requireContext(), R.style.RoundedDialogTheme)
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