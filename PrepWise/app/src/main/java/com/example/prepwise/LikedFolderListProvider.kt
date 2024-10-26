package com.example.prepwise

import com.example.prepwise.models.Folder
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import java.time.LocalDate

object LikedFolderListProvider {
    val folderList = arrayListOf(
        Folder(
            name = "Mobile Development",
            sets = arrayListOf(
                Set(
                    id = 1,
                    name = "Android Basics",
                    level = "Junior",
                    categories = arrayListOf("Android", "Kotlin"),
                    access = "public",
                    date = LocalDate.of(2023, 9, 20),
                    questions = arrayListOf(
                        Question("What is Activity?", "A UI component", false),
                        Question("What is Intent?", "An action to perform", false)
                    ),
                    username = "mobile_dev",
                    isLiked = true
                ),
                Set(
                    id = 2,
                    name = "iOS Fundamentals",
                    level = "Intermediate",
                    categories = arrayListOf("iOS", "Swift"),
                    access = "private",
                    date = LocalDate.of(2023, 8, 15),
                    questions = arrayListOf(
                        Question(
                            "What is ViewController?",
                            "UI management component in iOS", false
                        ), Question("What is SwiftUI?", "A declarative UI framework", false)
                    ),
                    username = "ios_master",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 9, 25),
            isLiked = true
        ),
        Folder(
            name = "Data Science",
            sets = arrayListOf(
                Set(
                    id = 3,
                    name = "Data Analysis with Python",
                    level = "Senior",
                    categories = arrayListOf("Python", "Pandas"),
                    access = "public",
                    date = LocalDate.of(2022, 4, 10),
                    questions = arrayListOf(
                        Question(
                            "What is DataFrame?",
                            "A table-like data structure", false
                        ), Question("What is NumPy?", "A library for numerical operations", false)
                    ),
                    username = "data_expert",
                    isLiked = true
                ),
                Set(
                    id = 4,
                    name = "Machine Learning Basics",
                    level = "Intermediate",
                    categories = arrayListOf("Machine Learning", "Algorithms"),
                    access = "public",
                    date = LocalDate.of(2023, 5, 8),
                    questions = arrayListOf(
                        Question(
                            "What is a Decision Tree?",
                            "A supervised learning algorithm",
                            false
                        ),
                        Question(
                            "What is Overfitting?",
                            "When the model fits training data too well", false
                        )
                    ),
                    username = "ml_guru",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 9, 30),
            isLiked = true
        ),
        Folder(
            name = "Web Development",
            sets = arrayListOf(
                Set(
                    id = 5,
                    name = "JavaScript for Beginners",
                    level = "Junior",
                    categories = arrayListOf("JavaScript", "Web"),
                    access = "public",
                    date = LocalDate.of(2021, 7, 12),
                    questions = arrayListOf(
                        Question(
                            "What is a function?",
                            "A reusable block of code", false
                        ), Question("What is an array?", "A collection of elements", false)
                    ),
                    username = "web_dev",
                    isLiked = true
                ),
                Set(
                    id = 6,
                    name = "React Advanced",
                    level = "Senior",
                    categories = arrayListOf("React", "Web"),
                    access = "private",
                    date = LocalDate.of(2023, 3, 17),
                    questions = arrayListOf(
                        Question(
                            "What is a component?",
                            "A reusable UI element", false
                        ), Question("What is JSX?", "A syntax extension for JavaScript", false)
                    ),
                    username = "react_pro",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 10, 1),
            isLiked = true
        ),
        Folder(
            name = "Cloud Computing",
            sets = arrayListOf(
                Set(
                    id = 7,
                    name = "AWS Basics",
                    level = "Intermediate",
                    categories = arrayListOf("AWS", "Cloud"),
                    access = "public",
                    date = LocalDate.of(2022, 11, 21),
                    questions = arrayListOf(
                        Question("What is EC2?", "A virtual server on AWS", false),
                        Question("What is S3?", "Object storage service", false)
                    ),
                    username = "cloud_enthusiast",
                    isLiked = false
                ),
                Set(
                    id = 8,
                    name = "Azure Fundamentals",
                    level = "Junior",
                    categories = arrayListOf("Azure", "Cloud"),
                    access = "private",
                    date = LocalDate.of(2023, 6, 12),
                    questions = arrayListOf(
                        Question(
                            "What is Azure Blob Storage?",
                            "Storage for unstructured data", false
                        ), Question("What is Virtual Network?", "Network in Azure", false)
                    ),
                    username = "azure_expert",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 10, 5),
            isLiked = true
        )
    )

}
