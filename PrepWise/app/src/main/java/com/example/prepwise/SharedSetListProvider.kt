package com.example.prepwise

import com.example.prepwise.activities.MainActivity
import com.example.prepwise.models.People
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import com.example.prepwise.models.SharedSet
import java.time.LocalDate

object SharedSetListProvider {
    // Створюємо список людей (People)
    val peopleList = arrayListOf(
        People(
            id = 7,
            userImg = "img_anna",
            username = "AnnaNahalkaaaaaaaa",
            status = "Friends",
            numberOfFollowing = 150,
            numberOfFollowers = 300,
            description = "Loves teaching math",
            email = "anna@example.com",
            location = "Kyiv, Ukraine",
            sets = SetListProvider.setList,
            resouces = arrayListOf()
        ),
        People(
            id = 8,
            userImg = "img_john",
            username = "John",
            status = "Follower",
            numberOfFollowing = 200,
            numberOfFollowers = 500,
            description = "History enthusiast",
            email = "john@example.com",
            location = "Lviv, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            id = 9,
            userImg = "img_nina",
            username = "Nina",
            status = "Following",
            numberOfFollowing = 180,
            numberOfFollowers = 320,
            description = "Biology lover",
            email = "nina@example.com",
            location = "Odesa, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            id = 10,
            userImg = "img_paul",
            username = "Paul",
            status = "Follower",
            numberOfFollowing = 220,
            numberOfFollowers = 430,
            description = "Physics enthusiast",
            email = "paul@example.com",
            location = "Kharkiv, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            id = 11,
            userImg = "img_sara",
            username = "Sara",
            status = "Friends",
            numberOfFollowing = 170,
            numberOfFollowers = 290,
            description = "Chemistry teacher",
            email = "sara@example.com",
            location = "Dnipro, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        )
    )

    // Створюємо список SharedSet (5 об'єктів)
    val sharedSetList = arrayListOf(
        SharedSet(
            id = 1,
            type = "edit",
            coAuthors = arrayListOf(peopleList[0], peopleList[1]), // Anna, John
            name = "Math Basics",
            level = "Beginner",
            categories = arrayListOf("Math", "Algebra"),
            access = "Public",
            date = LocalDate.of(2024, 10, 1),
            questions = arrayListOf(Question("What is 2+2?", "4", false)),
            username = "math_guru",
            isLiked = true
        ),
        SharedSet(
            id = 2,
            type = "view",
            coAuthors = arrayListOf(peopleList[1], peopleList[2]), // John, Nina
            name = "History 101",
            level = "Intermediate",
            categories = arrayListOf("History", "Europe"),
            access = "Private",
            date = LocalDate.of(2024, 9, 28),
            questions = arrayListOf(Question("Who discovered America?", "Columbus", false)),
            username = "history_buff",
            isLiked = false
        ),
        SharedSet(
            id = 3,
            type = "edit",
            coAuthors = arrayListOf(peopleList[2], peopleList[3]), // Nina, Paul
            name = "Biology Basics",
            level = "Beginner",
            categories = arrayListOf("Biology", "Cells"),
            access = "Public",
            date = LocalDate.of(2024, 8, 15),
            questions = arrayListOf(Question("What is the powerhouse of the cell?", "Mitochondria", false)),
            username = "bio_lover",
            isLiked = true
        ),
        SharedSet(
            id = 5,
            type = "view",
            coAuthors = arrayListOf(peopleList[3], peopleList[4]), // Paul, Sara
            name = "Physics Advanced",
            level = "Advanced",
            categories = arrayListOf("Physics", "Quantum Mechanics"),
            access = "Private",
            date = LocalDate.of(2024, 10, 5),
            questions = arrayListOf(Question("What is Schrödinger's cat?", "Paradox", false)),
            username = "quantum_master",
            isLiked = false
        ),
        SharedSet(
            id = 6,
            type = "edit",
            coAuthors = arrayListOf(peopleList[4], peopleList[0]), // Sara, Anna
            name = "Chemistry for Beginners",
            level = "Beginner",
            categories = arrayListOf("Chemistry", "Elements"),
            access = "Public",
            date = LocalDate.of(2024, 10, 10),
            questions = arrayListOf(Question("What is H2O?", "Water", false)),
            username = "chem_novice",
            isLiked = true
        )
    )

}
