package com.ucb.ucbtest.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import com.ucb.usecases.SearchMealsByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMealsByName: SearchMealsByName
) : ViewModel() {

    sealed class SearchState {
        object Initial : SearchState()
        object Loading : SearchState()
        data class Success(val meals: List<Meal>) : SearchState()
        data class Error(val message: String) : SearchState()
        object Empty : SearchState()
    }

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchState: StateFlow<SearchState> = _searchState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchState.Initial
            return
        }

        _searchState.value = SearchState.Loading
        viewModelScope.launch {
            when (val result = searchMealsByName.invoke(query.trim())) {
                is NetworkResult.Success -> {
                    if (result.data.isEmpty()) {
                        _searchState.value = SearchState.Empty
                    } else {
                        _searchState.value = SearchState.Success(result.data)
                        // Agregar a búsquedas recientes
                        addToRecentSearches(query.trim())
                    }
                }
                is NetworkResult.Error -> {
                    _searchState.value = SearchState.Error(result.error)
                }
            }
        }
    }

    private fun addToRecentSearches(query: String) {
        val currentSearches = _recentSearches.value.toMutableList()
        // Remover si ya existe
        currentSearches.remove(query)
        // Agregar al inicio
        currentSearches.add(0, query)
        // Mantener solo las últimas 5 búsquedas
        if (currentSearches.size > 5) {
            currentSearches.removeAt(currentSearches.size - 1)
        }
        _recentSearches.value = currentSearches
    }

    fun clearRecentSearches() {
        _recentSearches.value = emptyList()
    }

    fun searchFromRecent(query: String) {
        _searchQuery.value = query
        search(query)
    }
}
