package com.clocked.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clocked.app.data.preferences.UserPreferences
import com.clocked.app.data.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val fullName: String = "",
    val alias: String = "",
    val isSaving: Boolean = false,
    val savedMessage: String? = null,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferences,
    private val repository: ShiftRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                fullName = prefs.fullName.first(),
                alias = prefs.alias.first(),
            )
        }
    }

    fun updateFullName(v: String) { _state.value = _state.value.copy(fullName = v) }
    fun updateAlias(v: String) { _state.value = _state.value.copy(alias = v) }

    fun saveProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            prefs.saveProfile(_state.value.fullName, _state.value.alias)
            _state.value = _state.value.copy(isSaving = false, savedMessage = "Opgeslagen")
        }
    }

    fun clearSavedMessage() {
        _state.value = _state.value.copy(savedMessage = null)
    }

    fun clearDatabase(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.clearAll()
            _state.value = _state.value.copy(savedMessage = "Alle shifts verwijderd")
            onDone()
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.clearAll()
            prefs.logout()
            onDone()
        }
    }
}
