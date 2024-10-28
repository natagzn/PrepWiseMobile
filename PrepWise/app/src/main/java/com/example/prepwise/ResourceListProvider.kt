package com.example.prepwise

import com.example.prepwise.models.Resourse
import java.time.LocalDate

object ResourceListProvider {

    val resourceList = arrayListOf(
        Resourse(
            articleBook = "Effective Java",
            description = "A comprehensive guide to best practices in Java programming.",
            level = "Senior",
            category = "Java",
            date = LocalDate.of(2023, 1, 15),
            username = "john_doe",
            isLiked = true,
            isDisLiked = false,
            numberOfLikes = 15,
            numberOfDislikes = 3
        ),
        Resourse(
            articleBook = "Clean Code",
            description = "Learn how to write cleaner and more maintainable code.",
            level = "Junior",
            category = "Software Development",
            date = LocalDate.of(2022, 6, 20),
            username = "anna_smith",
            isLiked = false,
            isDisLiked = true,
            numberOfLikes = 10,
            numberOfDislikes = 7
        ),
        Resourse(
            articleBook = "Kotlin for Android Developers",
            description = "A practical guide to learning Kotlin in Android development.",
            level = "Intermediate",
            category = "Kotlin",
            date = LocalDate.of(2023, 3, 5),
            username = "alex_king",
            isLiked = false,
            isDisLiked = false,
            numberOfLikes = 8,
            numberOfDislikes = 2
        ),
        Resourse(
            articleBook = "Kotlin for Android Developers",
            description = "A practical guide to learning Kotlin in Android development.",
            level = "Intermediate",
            category = "Kotlin",
            date = LocalDate.of(2023, 3, 5),
            username = "alex_king",
            isLiked = false,
            isDisLiked = false,
            numberOfLikes = 8,
            numberOfDislikes = 2
        ),
        Resourse(
            articleBook = "Kotlin for Android Developers",
            description = "A practical guide to learning Kotlin in Android development.",
            level = "Intermediate",
            category = "Kotlin",
            date = LocalDate.of(2023, 3, 5),
            username = "alex_king",
            isLiked = false,
            isDisLiked = false,
            numberOfLikes = 8,
            numberOfDislikes = 2
        )
    )
}