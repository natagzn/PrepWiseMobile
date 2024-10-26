package com.example.prepwise

import com.example.prepwise.models.Folder
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import java.time.LocalDate

object FolderListProvider {
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
                            "UI management component in iOS",
                            false
                        ), Question("What is SwiftUI?", "A declarative UI framework", false)
                    ),
                    username = "ios_master",
                    isLiked = false
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
                            "A table-like data structure",
                            false
                        ), Question("What is NumPy?", "A library for numerical operations", false)
                    ),
                    username = "data_expert",
                    isLiked = false
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
                            "When the model fits training data too well",
                            false
                        )
                    ),
                    username = "ml_guru",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 9, 30),
            isLiked = false
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
                            "A reusable block of code",
                            false
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
                            "A reusable UI element",
                            false
                        ), Question("What is JSX?", "A syntax extension for JavaScript", false)
                    ),
                    username = "react_pro",
                    isLiked = false
                )
            ),
            date = LocalDate.of(2023, 10, 1),
            isLiked = true
        ),
        Folder(
            name = "Cybersecurity",
            sets = arrayListOf(
                Set(
                    id = 9,
                    name = "Network Security Basics",
                    level = "Intermediate",
                    categories = arrayListOf("Cybersecurity", "Networks"),
                    access = "public",
                    date = LocalDate.of(2023, 1, 9),
                    questions = arrayListOf(
                        Question(
                            "What is a firewall?",
                            "A network security system",
                            false
                        ), Question("What is VPN?", "A virtual private network", false)
                    ),
                    username = "security_guru",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 2, 1),
            isLiked = true
        ),
        Folder(
            name = "Game Development",
            sets = arrayListOf(
                Set(
                    id = 10,
                    name = "Unity Basics",
                    level = "Junior",
                    categories = arrayListOf("Unity", "C#"),
                    access = "public",
                    date = LocalDate.of(2021, 12, 25),
                    questions = arrayListOf(
                        Question(
                            "What is a GameObject?",
                            "A fundamental object in Unity", false
                        ), Question("What is a prefab?", "A reusable asset in Unity", false)
                    ),
                    username = "game_dev",
                    isLiked = false
                ),
                Set(
                    id = 11,
                    name = "Unreal Engine Advanced",
                    level = "Senior",
                    categories = arrayListOf("Unreal Engine", "C++"),
                    access = "private",
                    date = LocalDate.of(2023, 7, 22),
                    questions = arrayListOf(
                        Question(
                            "What is a Blueprint?",
                            "A visual scripting system in Unreal",
                            false
                        ), Question("What is LOD?", "Level of detail for rendering", false)
                    ),
                    username = "unreal_master",
                    isLiked = true
                )
            ),
            date = LocalDate.of(2023, 8, 20),
            isLiked = true
        )
    )

}
