package com.ucb.ucbtest.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import com.ucb.usecases.GetMealByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val getMealByName: GetMealByName
) : ViewModel() {

    sealed class MealState {
        object Initial : MealState()
        object Loading : MealState()
        data class Success(val meal: Meal) : MealState()
        data class Error(val message: String) : MealState()
    }

    private val _state = MutableStateFlow<MealState>(MealState.Initial)
    val state: StateFlow<MealState> = _state

    fun getMeal(name: String) {
        _state.value = MealState.Loading
        viewModelScope.launch {
            when (val result = getMealByName.invoke(name)) {
                is NetworkResult.Success -> {
                    _state.value = MealState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _state.value = MealState.Error(result.error)
                }
            }
        }
    }
}