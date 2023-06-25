package com.tatam.calorietracker.navigation

import androidx.navigation.NavController
import com.tatam.core.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}