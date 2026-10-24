package com.example.week5

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ViewQuiz(
    quizViewModel: ViewModelQuiz = viewModel(),
) {
    val isStarted = quizViewModel.isStarted.value
    val isFinished = quizViewModel.isFinished.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5),
    ) {
        if (!isStarted) {
            StartScreen(quizViewModel)
        } else if (isFinished) {
            ResultScreen(quizViewModel)
        } else {
            QuestionScreen(quizViewModel)
        }
    }
}

@Composable
fun StartScreen(viewModel: ViewModelQuiz) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "QUIZ TIME",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF263B70)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Choose how many questions you want to answer.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.questionCountInput.value,
            onValueChange = {
                viewModel.questionCountInput.value = it
                viewModel.errorMessage.value = ""
            },
            label = { Text("Number of questions") },
            placeholder = { Text("Enter 1 to 5") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.errorMessage.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = viewModel.errorMessage.value,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.startQuiz() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "There are 5 questions available.",
            color = Color.Gray
        )
    }
}

@Composable
fun QuestionScreen(viewModel: ViewModelQuiz) {
    val questionIndex = viewModel.currentQuestionIndex.intValue
    val question = viewModel.questions[questionIndex]
    val selectedAnswer = viewModel.selectedAnswer.intValue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "QUESTION ${questionIndex + 1} OF ${viewModel.questions.size}",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF263B70)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        question.options.forEachIndexed { index, option ->
            val isSelected = selectedAnswer == index

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        viewModel.selectAnswer(index)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        Color(0xFFD5E2FF)
                    } else {
                        Color.White
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${'A' + index}.",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.submitAnswer() },
            enabled = selectedAnswer != -1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (questionIndex == viewModel.questions.lastIndex) {
                    "Finish Quiz"
                } else {
                    "Next Question"
                }
            )
        }
    }
}

@Composable
fun ResultScreen(viewModel: ViewModelQuiz) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "QUIZ RESULT",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF263B70)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your score: ${viewModel.getScore()} / ${viewModel.questions.size}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        val wrongAnswers = viewModel.answers.filter {
            it.selectedAnswer != it.question.correctAnswer
        }

        if (wrongAnswers.isEmpty()) {
            Text(
                text = "Excellent! All your answers are correct!",
                color = Color(0xFF238636),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(
                text = "Incorrect Answers",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(12.dp))

            wrongAnswers.forEachIndexed { index, answer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${index + 1}. ${answer.question.question}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your answer: ${
                                answer.question.options[answer.selectedAnswer]
                            }",
                            color = Color.Red
                        )

                        Text(
                            text = "Correct answer: ${
                                answer.question.options[
                                    answer.question.correctAnswer
                                ]
                            }",
                            color = Color(0xFF238636)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.restartQuiz() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restart Quiz")
        }
    }
}
