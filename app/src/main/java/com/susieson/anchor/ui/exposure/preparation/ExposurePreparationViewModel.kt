package com.susieson.anchor.ui.exposure.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import com.susieson.anchor.ui.components.Operation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExposurePreparationViewModel.Factory::class)
class ExposurePreparationViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    private val storageService: StorageService,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userId: String): ExposurePreparationViewModel
    }

    private val _state = MutableStateFlow(ExposurePreparationState())
    val state: StateFlow<ExposurePreparationState> = _state.asStateFlow()

    fun addPreparation() {
        viewModelScope.launch {
            with(state.value) {
                val exposure = storageService.addExposure(userId)
                val preparation = Preparation(thoughts, interpretations, behaviors, actions)
                storageService.updateExposure(userId, exposure.id, title, description, preparation)
            }
        }
    }

    fun onTitleChanged(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun onDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun onThoughtChanged(operation: Operation, thought: String) {
        _state.update { it.copy(thoughts = updateList(it.thoughts, thought, operation)) }
    }

    fun onInterpretationChanged(operation: Operation, interpretation: String) {
        _state.update { it.copy(interpretations = updateList(it.interpretations, interpretation, operation)) }
    }

    fun onBehaviorChanged(operation: Operation, behavior: String) {
        _state.update { it.copy(behaviors = updateList(it.behaviors, behavior, operation)) }
    }

    fun onActionChanged(operation: Operation, action: String) {
        _state.update { it.copy(actions = updateList(it.actions, action, operation)) }
    }

    fun onShowDiscardDialogChanged(showDiscardDialog: Boolean) {
        _state.update { it.copy(showDiscardDialog = showDiscardDialog) }
    }

    private fun <T> updateList(list: List<T>, item: T, operation: Operation): List<T> {
        return when (operation) {
            Operation.ADD -> listOf(item) + list
            Operation.REMOVE -> list.toMutableList().also { it.remove(item) }
        }
    }
}

data class ExposurePreparationState(
    val title: String = "",
    val description: String = "",
    val thoughts: List<String> = emptyList(),
    val interpretations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val actions: List<String> = emptyList(),
    val showDiscardDialog: Boolean = false
) {
    val isEmpty = title.isBlank() && description.isBlank() && thoughts.isEmpty() &&
        interpretations.isEmpty() && behaviors.isEmpty() && actions.isEmpty()
    val isValid = title.isNotBlank() && description.isNotBlank() && thoughts.isNotEmpty() &&
        interpretations.isNotEmpty() && behaviors.isNotEmpty() && actions.isNotEmpty()
}
