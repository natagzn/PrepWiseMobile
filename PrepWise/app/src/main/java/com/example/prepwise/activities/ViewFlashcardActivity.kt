package com.example.prepwise.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prepwise.R
import com.example.prepwise.models.Question

class ViewFlashcardActivity : AppCompatActivity() {
    private lateinit var flashcard: CardView
    private lateinit var flashcardText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var leftCount: TextView
    private lateinit var rightCount: TextView
    private var flashcardIndex = 0
    private var showingQuestion = true

    private var setId: Int = 0
    private lateinit var flashcardsAll: List<Question>
    private lateinit var flashcards: List<Question>
    private var counter: Int = 0

    private var downX = 0f
    private val SWIPE_THRESHOLD = 300

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_flashcard)

        findViewById<ImageView>(R.id.close).setOnClickListener{
            finish()
        }

        setId = intent.getIntExtra("setId", -1)
        val set = MainActivity.getSetById(setId)
        if (set != null) {
            flashcards = set.questions
            flashcards = flashcards.shuffled()
        } else {
            flashcardsAll = emptyList()
        }

        flashcard = findViewById(R.id.flashcard)
        flashcardText = findViewById(R.id.question_text)
        progressBar = findViewById(R.id.progress_bar)
        leftCount = findViewById(R.id.left)
        rightCount = findViewById(R.id.right)

        updateProgress()
        updateFlashcard()

        flashcard.setOnClickListener {
            if (flashcard.translationX == 0f) {
                flipCard()
            }
        }

        flashcard.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - downX
                    view.translationX = deltaX

                    flashcard.setBackgroundResource(R.drawable.default_border)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - downX

                    if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                        goToNextCard()
                        updateProgress()
                    } else {
                        view.performClick()
                    }
                    view.animate()
                        .translationX(0f)
                        .setDuration(300)
                        .withEndAction {
                            flashcard.setBackgroundResource(R.drawable.default_border)
                        }
                        .start()
                    true
                }
                else -> false
            }
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            if (flashcardIndex > 0) {
                flashcardIndex--
                counter--

                showingQuestion = true
                updateFlashcard()
                updateProgress()
            }
        }

        findViewById<ImageView>(R.id.restart).setOnClickListener {
            flashcardIndex = 0
            counter = 0
            showingQuestion = true

            val intent = Intent(this, ViewFlashcardActivity::class.java)
            intent.putExtra("setId", setId)
            startActivity(intent)
            finish()
        }

    }

    private fun flipCard() {

        val flipOut = ObjectAnimator.ofFloat(flashcard, "rotationY", 0f, 90f)
        flipOut.duration = 150

        val flipIn = ObjectAnimator.ofFloat(flashcard, "rotationY", -90f, 0f)
        flipIn.duration = 150

        flipOut.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                flashcard.visibility = View.INVISIBLE
                flashcardText.text = if (showingQuestion) flashcards[flashcardIndex].answer else flashcards[flashcardIndex].content
                showingQuestion = !showingQuestion
                flashcard.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        flipIn.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        AnimatorSet().apply {
            playSequentially(flipOut, flipIn)
            start()
        }
    }

    private fun goToNextCard() {
        if (flashcardIndex < flashcards.size - 1) {
            flashcardIndex++
            showingQuestion = true
            counter++
            updateProgress()
            updateFlashcard()
        }
        else{
//            val intent = Intent(this, ResultStudyingActivity::class.java)
//            intent.putExtra("rightCount", flashcards.size)
//            startActivity(intent)
//            finish()
        }
    }

    private fun updateFlashcard() {
        flashcardText.text = flashcards[flashcardIndex].content
        leftCount.text = (counter+1).toString()
        rightCount.text = flashcards.size.toString()
    }

    private fun updateProgress() {
        val progress = (counter.toDouble() / flashcards.size * 100).toInt()

        progressBar.progress = progress
    }

}