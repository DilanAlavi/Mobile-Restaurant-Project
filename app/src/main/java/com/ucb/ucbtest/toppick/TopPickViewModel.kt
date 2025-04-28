package com.ucb.ucbtest.toppick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.TopPick
import com.ucb.usecases.GetTopPicks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopPickViewModel @Inject constructor(
    private val getTopPicks: GetTopPicks
) : ViewModel() {

    sealed class TopPickState {
        object Initial : TopPickState()
        object Loading : TopPickState()
        data class Success(val topPicks: List<TopPick>) : TopPickState()
        data class Error(val message: String) : TopPickState()
    }

    private val _state = MutableStateFlow<TopPickState>(TopPickState.Initial)
    val state: StateFlow<TopPickState> = _state

    fun getTopPicks() {
        _state.value = TopPickState.Loading
        viewModelScope.launch {
            when (val result = getTopPicks.invoke()) {
                is NetworkResult.Success -> {
                    _state.value = TopPickState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _state.value = TopPickState.Error(result.error)
                }
            }
        }
    }
}