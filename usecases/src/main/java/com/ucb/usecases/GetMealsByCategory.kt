package com.ucb.usecases

import com.ucb.data.MealRepository
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import javax.inject.Inject

class GetMealsByCategory @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(categoryName: String): NetworkResult<List<Meal>> {
        // Validación de entrada
        if (categoryName.isBlank()) {
            return NetworkResult.Error("El nombre de la categoría no puede estar vacío")
        }

        return try {
            mealRepository.getMealsByCategory(categoryName)
        } catch (e: Exception) {
            NetworkResult.Error("Error al obtener productos: ${e.message}")
        }
    }
}