package com.example.prepwise

import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import java.time.LocalDate

object SetListProvider {
    val setList = arrayListOf(
        Set(
            id = 1,
            name = "Android Development",
            level = "Junior",
            categories = arrayListOf("Android", "Kotlin"),
            access = "public",
            date = LocalDate.of(2023, 10, 1),
            questions = arrayListOf(
                Question("What is Activity?", "A component in Android", false),
                Question("What is Fragment?", "Part of a UI", true),
                Question("What is overfitting?", "A modeling error due to excessive complexity.", false),
                Question("What is a variable?", "A container for data", false)
            ),
            username = "dev_junior",
            isLiked = false
        ),
        Set(
            id = 2,
            name = "Data Science Basics",
            level = "Intermediate",
            categories = arrayListOf("Data Science", "Python", "Statistics"),
            access = "private",
            date = LocalDate.of(2022, 5, 15),
            questions = arrayListOf(
                Question("What is DataFrame?", "A table-like structure in pandas", true),
                Question("What is a variable?", "A container for data", false)
            ),
            username = "data_guru",
            isLiked = false
        ),
        Set(
            id = 3,
            name = "Web Development with JavaScript",
            level = "Senior",
            categories = arrayListOf("JavaScript", "React"),
            access = "public",
            date = LocalDate.of(2021, 8, 30),
            questions = arrayListOf(
                Question("What is a closure?", "A function with access to outer variables", true),
                Question("What is DOM?", "Document Object Model", true)
            ),
            username = "web_master",
            isLiked = true
        ),
        Set(
            id = 4,
            name = "Machine Learning Fundamentals",
            level = "Intermediate",
            categories = arrayListOf("Machine Learning", "Python"),
            access = "public",
            date = LocalDate.of(2023, 2, 20),
            questions = arrayListOf(
                Question("What is overfitting?", "A modeling error due to excessive complexity.", false),
                Question("What is a confusion matrix?", "A table used to describe the performance of a classification model.", true)
            ),
            username = "ml_expert",
            isLiked = false
        ),
        Set(
            id = 5,
            name = "iOS App Development",
            level = "Junior",
            categories = arrayListOf("iOS", "Swift"),
            access = "public",
            date = LocalDate.of(2023, 5, 10),
            questions = arrayListOf(
                Question("What is Swift?", "A powerful programming language for iOS development.", true),
                Question("What is UIKit?", "A framework for building user interfaces in iOS.",false)
            ),
            username = "ios_dev",
            isLiked = true
        ),
        Set(
            id = 6,
            name = "iOS App Development",
            level = "Junior",
            categories = arrayListOf("iOS", "Swift"),
            access = "public",
            date = LocalDate.of(2023, 5, 10),
            questions = arrayListOf(
                Question("What is Swift?", "A powerful programming language for iOS development.", false),
                Question("What is UIKit?", "A framework for building user interfaces in iOS.", false)
            ),
            username = "ios_dev",
            isLiked = true
        )
    )
}
