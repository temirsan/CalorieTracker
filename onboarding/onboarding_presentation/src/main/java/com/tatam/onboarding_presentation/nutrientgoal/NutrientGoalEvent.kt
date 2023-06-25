package com.tatam.onboarding_presentation.nutrientgoal

sealed class NutrientGoalEvent {
    data class OnCarbsRationEnter(val ratio : String) : NutrientGoalEvent()
    data class OnProteinRationEnter(val ratio : String) : NutrientGoalEvent()
    data class OnFatRationEnter(val ratio : String) : NutrientGoalEvent()
    object OnNextClick: NutrientGoalEvent()
}
