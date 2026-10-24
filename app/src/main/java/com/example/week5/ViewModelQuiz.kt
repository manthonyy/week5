package com.example.week5

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ViewModelQuiz : ViewModel() {

    private val allQuestions = listOf(
        QuizQuestion(
            "What is the capital of Indonesia?",
            listOf("Bandung", "Jakarta", "Surabaya", "Medan"),
            1,
        ),
        QuizQuestion(
            "Which planet is known as the Red Planet?",
            listOf("Earth", "Venus", "Mars", "Jupiter"),
            2,
        ),
        QuizQuestion(
            "How many days are there in a week?",
            listOf("5", "6", "7", "8"),
            2,
        ),
        QuizQuestion(
            "Which language is used for Android development?",
            listOf("Kotlin", "HTML", "SQL", "CSS"),
            0,
        ),
        QuizQuestion(
            "What is 5 x 6?",
            listOf("11", "25", "30", "35"),
            2,
        ),
    )

    val questions = mutableStateListOf<QuizQuestion>()
    val answers = mutableStateListOf<QuizAnswer>()

    var currentQuestionIndex = mutableIntStateOf(0)
    var selectedAnswer = mutableIntStateOf(-1)
    var isFinished = mutableStateOf(value = false)
    var isStarted = mutableStateOf(value = false)

    var questionCountInput = mutableStateOf(value = "3")
    var errorMessage = mutableStateOf(value = "")

    private var selectedQuestionCount = 3

    fun startQuiz() {
        val count = questionCountInput.value.toIntOrNull()

        when {
            (count == null) || (count < 1) -> {
                errorMessage.value = "Enter a number between 1 and ${allQuestions.size}."
            }

            count > allQuestions.size -> {
                errorMessage.value = "Maximum questions available: ${allQuestions.size}."
            }

            else -> {
                selectedQuestionCount = count
                errorMessage.value = ""
                restartQuiz()
                isStarted.value = true
            }
        }
    }

    fun selectAnswer(index: Int) {
        selectedAnswer.intValue = index
    }

    fun submitAnswer() {
        if (selectedAnswer.intValue == -1) return

        answers.add(
            QuizAnswer(
                question = questions[currentQuestionIndex.intValue],
                selectedAnswer = selectedAnswer.intValue,
            ),
        )

        if (currentQuestionIndex.intValue == questions.lastIndex) {
            isFinished.value = true
        } else {
            currentQuestionIndex.intValue++
            selectedAnswer.intValue = -1
        }
    }

    fun restartQuiz() {
        questions.clear()
        questions.addAll(
            allQuestions.shuffled().take(selectedQuestionCount),
        )

        answers.clear()
        currentQuestionIndex.intValue = 0
        selectedAnswer.intValue = -1
        isFinished.value = false
    }

    fun getScore(): Int {
        return answers.count {
            it.selectedAnswer == it.question.correctAnswer
        }
    }
}
