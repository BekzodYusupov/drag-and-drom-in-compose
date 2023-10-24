package com.example.droganddrop.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var isCurrentlyDragging by mutableStateOf(false)
        private set

    var item by mutableStateOf(emptyList<PersonalItem>())
        private set

    var addedPersons = mutableStateListOf<PersonalItem>()
        private set

    init {
        item = listOf(
            PersonalItem("Elmurod", "1", Color.Gray),
            PersonalItem("Ali", "2", Color.Green),
            PersonalItem("Mark", "3", Color.Red)
        )
    }

    fun startDragging() {
        isCurrentlyDragging = true
    }

    fun stopDragging() {
        isCurrentlyDragging = false
    }

    fun addPerson(personalItem: PersonalItem) {
        addedPersons.add(personalItem)
    }
}