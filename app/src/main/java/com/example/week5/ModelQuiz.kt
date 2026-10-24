package com.example.week5

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

data class QuizAnswer(
    val question: QuizQuestion,
    val selectedAnswer: Int
)