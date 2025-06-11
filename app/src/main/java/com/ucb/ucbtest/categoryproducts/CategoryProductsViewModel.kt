package com.ucb.ucbtest.categoryproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import com.ucb.usecases.GetMealsByCategory
import com.ucb.usecases.GetMealById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getMealsByCategory: GetMealsByCategory,
    private val getMealById: GetMealById
) : ViewModel() {

    sealed class CategoryProductsState {
        object Initial : CategoryProductsState()
        object Loading : CategoryProductsState()
        data class Success(val meals: List<Meal>) : CategoryProductsState()
        data class Error(val message: String) : CategoryProductsState()
        object LoadingDetails : CategoryProductsState()
    }

    private val _state = MutableStateFlow<CategoryProductsState>(CategoryProductsState.Initial)
    val state: StateFlow<CategoryProductsState> = _state

    fun getMealsByCategory(categoryName: String) {
        _state.value = CategoryProductsState.Loading
        viewModelScope.launch {
            when (val result = getMealsByCategory.invoke(categoryName)) {
                is NetworkResult.Success -> {
                    _state.value = CategoryProductsState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _state.value = CategoryProductsState.Error(result.error)
                }
            }
        }
    }
    fun getMealDetails(mealId: String, onComplete: (Meal) -> Unit) {
        // Mostrar loading solo brevemente
        _state.value = CategoryProductsState.LoadingDetails

        viewModelScope.launch {
            when (val result = getMealById.invoke(mealId)) {
                is NetworkResult.Success -> {
                    // Restaurar la lista de productos
                    if (_state.value is CategoryProductsState.LoadingDetails) {
                        // Aquí podrías restaurar el estado anterior, pero por simplicidad
                        // solo ejecutamos el callback
                    }
                    onComplete(result.data)
                }
                is NetworkResult.Error -> {
                    _state.value = CategoryProductsState.Error("Error al cargar detalles: ${result.error}")
                }
            }
        }
    }
}