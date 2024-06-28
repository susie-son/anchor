package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var title = mutableStateOf("")
    var description = mutableStateOf("")
    val thoughts = mutableStateListOf<String>()
    val interpretations = mutableStateListOf<String>()
    val behaviors = mutableStateListOf<String>()
    val actions = mutableStateListOf<String>()

    var showDiscardDialog = mutableStateOf(false)

    val isValid = derivedStateOf {
        title.value.isNotBlank() && description.value.isNotBlank() &&
            thoughts.isNotEmpty() && interpretations.isNotEmpty() &&
            behaviors.isNotEmpty() && actions.isNotEmpty()
    }
    val isEmpty = derivedStateOf {
        title.value.isBlank() && description.value.isBlank() &&
            thoughts.isEmpty() && interpretations.isEmpty() &&
            behaviors.isEmpty() && actions.isEmpty()
    }

    fun addPreparation() {
        viewModelScope.launch {
            val exposure = storageService.addExposure(userId)
            val preparation = Preparation(thoughts, interpretations, behaviors, actions)
            storageService.updateExposure(userId, exposure.id, title.value, description.value, preparation)
        }
    }

    fun onTitleChange(title: String) {
        this.title.value = title
    }

    fun onDescriptionChange(description: String) {
        this.description.value = description
    }

    fun onThoughtAdded(thought: String) {
        thoughts.add(0, thought)
    }

    fun onThoughtRemoved(thought: String) {
        thoughts.remove(thought)
    }

    fun onInterpretationAdded(interpretation: String) {
        interpretations.add(0, interpretation)
    }

    fun onInterpretationRemoved(interpretation: String) {
        interpretations.remove(interpretation)
    }

    fun onBehaviorAdded(behavior: String) {
        behaviors.add(0, behavior)
    }

    fun onBehaviorRemoved(behavior: String) {
        behaviors.remove(behavior)
    }

    fun onActionAdded(action: String) {
        actions.add(0, action)
    }

    fun onActionRemoved(action: String) {
        actions.remove(action)
    }

    fun onShowDiscardDialogChange(showDiscardDialog: Boolean) {
        this.showDiscardDialog.value = showDiscardDialog
    }
}
