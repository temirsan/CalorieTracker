package com.tatam.tracker_domain.use_case

import com.tatam.core.domain.models.ActivityLevel
import com.tatam.core.domain.models.Gender
import com.tatam.core.domain.models.GoalType
import com.tatam.core.domain.models.UserInfo
import com.tatam.core.domain.preferences.Preferences
import com.tatam.tracker_domain.model.MealType
import com.tatam.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt

class CalculateMealNutrients(
    private val preferences: Preferences,
) {

    operator fun invoke(trackedFoods: List<TrackedFood>): Result {
        val allNutrients = trackedFoods
            .groupBy { it.mealType }
            .mapValues { entry ->
                val type = entry.key
                val foods = entry.value
                MealNutrients(
                    carbs = foods.sumOf { it.carbs },
                    proteins = foods.sumOf { it.proteins },
                    fats = foods.sumOf { it.fats },
                    calories = foods.sumOf { it.calories },
                    mealType = type
                )
            }
        val totalCarbs = allNutrients.values.sumOf { it.carbs }
        val totalProteins = allNutrients.values.sumOf { it.proteins }
        val totalFats = allNutrients.values.sumOf { it.fats }
        val totalCalories = allNutrients.values.sumOf { it.calories }

        val userInfo = preferences.loadUserInfo()
        val caloriesGoal = dailyCalorieRequirement(userInfo)
        val carbsGoal = (caloriesGoal * userInfo.carbRatio / 4f).roundToInt()
        val proteinsGoal = (caloriesGoal * userInfo.proteinRatio / 4f).roundToInt()
        val fatsGoal = (caloriesGoal * userInfo.fatRatio / 9f).roundToInt()

        return Result(
            carbsGoal = carbsGoal,
            proteinsGoal = proteinsGoal,
            fatsGoal = fatsGoal,
            caloriesGoal = caloriesGoal,
            totalCarbs = totalCarbs,
            totalProteins = totalProteins,
            totalFats = totalFats,
            totalCalories = totalCalories,
            mealNutrients = allNutrients
        )
    }

    //bmr - basal metabolic rate formula
    private fun bmr(userInfo: UserInfo): Int {
        return when (userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight +
                        5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }

            is Gender.Female -> {
                (665.09f + 9.56f * userInfo.weight +
                        1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }

    private fun dailyCalorieRequirement(userInfo: UserInfo): Int {
        val activityFactor = when (userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }
        val calorieExtra = when (userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }
        return (bmr(userInfo) * activityFactor + calorieExtra).roundToInt()
    }

    data class MealNutrients(
        val carbs: Int,
        val proteins: Int,
        val fats: Int,
        val calories: Int,
        val mealType: MealType
    )

    data class Result(
        val carbsGoal: Int,
        val proteinsGoal: Int,
        val fatsGoal: Int,
        val caloriesGoal: Int,
        val totalCarbs: Int,
        val totalProteins: Int,
        val totalFats: Int,
        val totalCalories: Int,
        val mealNutrients: Map<MealType, MealNutrients>
    )
}