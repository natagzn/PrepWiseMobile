package com.example.prepwise

import com.example.prepwise.activities.MainActivity
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import java.time.LocalDate

object FolderListProvider {
    val folderList = arrayListOf(
        Folder(
            id = 1,
            name = "Mobile Development",
            sets = SetListProvider.setList,
            date = LocalDate.of(2023, 9, 25),
            isLiked = true
        ),
        Folder(
            id = 2,
            name = "Data Science",
            sets = SetListProvider.setList,
            date = LocalDate.of(2023, 9, 30),
            isLiked = false
        ),
        Folder(
            id = 3,
            name = "Web Development",
            sets = SetListProvider.setList,
            date = LocalDate.of(2023, 10, 1),
            isLiked = true
        ),
        Folder(
            id = 4,
            name = "Cybersecurity",
            sets = SetListProvider.setList,
            date = LocalDate.of(2023, 2, 1),
            isLiked = true
        ),
        Folder(
            id = 5,
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
