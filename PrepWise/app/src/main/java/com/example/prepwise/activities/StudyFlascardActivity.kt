package com.example.prepwise.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.prepwise.R
import com.example.prepwise.models.Question


class StudyFlascardActivity : AppCompatActivity() {
    private lateinit var flashcard: CardView
    private lateinit var flashcardText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var leftCountTextView: TextView
    private lateinit var rightCountTextView: TextView
    private lateinit var knowCountTextView: TextView
    private lateinit var stillLearningCountTextView: TextView
    private var flashcardIndex = 0
    private var showingQuestion = true

    private val flashcards = listOf(
        Question("Question 1", "Answer 1", false),
        Question("Question 2", "Answer 2", false),
        Question("Question 3", "Answer 3", false),
        Question("Question 4", "Answer 4", false)
    )

    private var learnedCount = 0
    private var learningCount = 0
    private var downX = 0f
    private val SWIPE_THRESHOLD = 300

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_flascard)

        flashcard = findViewById(R.id.flashcard)
        flashcardText = findViewById(R.id.question_text)
        progressBar = findViewById(R.id.progress_bar)
        leftCountTextView = findViewById(R.id.left)
        rightCountTextView = findViewById(R.id.right)
        knowCountTextView = findViewById(R.id.number_of_know)
        stillLearningCountTextView = findViewById(R.id.number_of_still_learning)

        updateFlashcard()
        updateProgress()

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

                    if (deltaX < -SWIPE_THRESHOLD) {
                        flashcard.setBackgroundResource(R.drawable.know_border)
                    } else if (deltaX > SWIPE_THRESHOLD) {
                        flashcard.setBackgroundResource(R.drawable.learning_border)
                    } else {
                        flashcard.setBackgroundResource(R.drawable.default_border)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - downX

                    if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                        if (deltaX < 0) {
                            markAsLearned()
                        } else {
                            markAsLearning()
                        }
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

    private fun markAsLearned() {
        if (!flashcards[flashcardIndex].learned) {
            flashcards[flashcardIndex].learned = true
            learnedCount++
        }
        goToNextCard()
    }

    private fun markAsLearning() {
        if (!flashcards[flashcardIndex].learned) {
            flashcards[flashcardIndex].learned = false
            learningCount++
        }
        goToNextCard()
    }

    private fun goToNextCard() {
        if (flashcardIndex < flashcards.size - 1) {
            flashcardIndex++
            showingQuestion = true
            updateFlashcard()
        }
    }

    private fun updateFlashcard() {
        flashcardText.text = flashcards[flashcardIndex].content
        leftCountTextView.text = (learningCount+learnedCount + 1).toString()
        rightCountTextView.text = flashcards.size.toString()
    }

    private fun updateProgress() {
        val progress = ((learnedCount + learningCount).toDouble() / flashcards.size * 100).toInt()

        progressBar.progress = progress
        knowCountTextView.text = learnedCount.toString()
        stillLearningCountTextView.text = learningCount.toString()
    }

}





