package com.ucb.ucbtest.categorymeal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.CategoryMeal
import com.ucb.usecases.GetCategoriesMeal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMealViewModel @Inject constructor(
    private val getCategoriesMeal: GetCategoriesMeal
) : ViewModel() {

    sealed class CategoryMealState {
        object Initial : CategoryMealState()
        object Loading : CategoryMealState()
        data class Success(val categories: List<CategoryMeal>) : CategoryMealState()
        data class Error(val message: String) : CategoryMealState()
    }

    private val _state = MutableStateFlow<CategoryMealState>(CategoryMealState.Initial)
    val state: StateFlow<CategoryMealState> = _state

    fun getCategories() {
        _state.value = CategoryMealState.Loading
        viewModelScope.launch {
            when (val result = getCategoriesMeal.invoke()) {
                is NetworkResult.Success -> {
                    _state.value = CategoryMealState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _state.value = CategoryMealState.Error(result.error)
                }
            }
        }
    }
}