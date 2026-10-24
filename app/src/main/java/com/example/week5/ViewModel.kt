package com.example.week5

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

    val expenses = mutableStateListOf<Model>()

    var errorMessage = mutableStateOf("")

    private var nextId = 1

    fun addExpense(name: String, amount: Double) {
        if (name.isBlank()) {
            errorMessage.value = "Nama pengeluaran tidak boleh kosong"
            return
        }

        if (amount <= 0.0) {
            errorMessage.value = "Amount harus lebih dari 0"
            return
        }

        expenses.add(
            Model(
                id = nextId++,
                name = name,
                amount = amount
            )
        )

        errorMessage.value = ""
    }

    fun deleteExpense(id: Int) {
        expenses.removeAll { it.id == id }
    }

    fun editExpense(id: Int, newAmount: Double) {
        if (newAmount <= 0.0) {
            errorMessage.value = "Amount harus lebih dari 0"
            return
        }

        val index = expenses.indexOfFirst { it.id == id }

        if (index != -1) {
            expenses[index] = expenses[index].copy(
                amount = newAmount
            )
            errorMessage.value = ""
        }
    }

    fun getTotalExpenses(): Double {
        return expenses.sumOf { it.amount }
    }
}
