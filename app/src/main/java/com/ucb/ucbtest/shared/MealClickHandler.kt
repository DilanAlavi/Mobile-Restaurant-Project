package com.ucb.ucbtest.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import com.ucb.domain.TopPick
import com.ucb.usecases.GetMealById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealClickHandler @Inject constructor(
    private val getMealById: GetMealById
) : ViewModel() {

    // Para clicks de TopPick (mantiene precios especiales)
    fun handleTopPickClick(
        topPick: TopPick,
        onMealFound: (Meal) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val result = getMealById.invoke(topPick.idMeal)) {
                is NetworkResult.Success -> {
                    // Crear Meal híbrido: datos de API + precio de TopPick
                    val mealWithTopPickPrice = result.data.copy(
                        price = topPick.discountPrice // Mantiene precio especial del TopPick
                    )
                    onMealFound(mealWithTopPickPrice)
                }
                is NetworkResult.Error -> {
                    onError(result.error)
                }
            }
        }
    }

    // Para clicks de resultados de búsqueda (precio por defecto 50.0)
    fun handleSearchResultClick(
        mealId: String,
        onMealFound: (Meal) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (val result = getMealById.invoke(mealId)) {
                is NetworkResult.Success -> {
                    // Usa precio por defecto (50.0 Bs)
                    onMealFound(result.data)
                }
                is NetworkResult.Error -> {
                    onError(result.error)
                }
            }
        }
    }
}