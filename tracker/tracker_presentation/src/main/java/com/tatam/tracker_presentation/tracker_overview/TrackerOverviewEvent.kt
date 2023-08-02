package com.tatam.tracker_presentation.tracker_overview

import com.tatam.tracker_domain.model.TrackedFood

sealed class TrackerOverviewEvent {
    object OnNextDayClick: TrackerOverviewEvent()
    object OnPreviousDayClick: TrackerOverviewEvent()
    data class OnToggleMealClick(val meal: Meal): TrackerOverviewEvent()
    data class OnDeleteTrackedFoodClick(val trackFood: TrackedFood): TrackerOverviewEvent()
    data class OnAddFoodFoodClick(val meal: Meal): TrackerOverviewEvent()
}
