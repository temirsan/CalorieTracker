package com.tatam.tracker_data.mapper

import com.tatam.tracker_data.remote.dto.Product
import com.tatam.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

fun Product.toTrackableFood(): TrackableFood? {
    val carbsPer100g = nutriments.carbohydrates100g.roundToInt()
    val proteinPer100g = nutriments.proteins100g.roundToInt()
    val fatsPer100g = nutriments.fats100g.roundToInt()
    val caloriesPer100g = nutriments.energyKcal100g.roundToInt()
    return TrackableFood(
        name = productName ?: return null,
        caloriesPer100g = caloriesPer100g,
        carbsPer100g = carbsPer100g,
        proteinsPer100g = proteinPer100g,
        fatsPer100g = fatsPer100g,
        imageUrl = imageFrontThumbUrl
    )
}
