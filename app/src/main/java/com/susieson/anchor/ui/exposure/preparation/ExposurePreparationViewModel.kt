package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import com.susieson.anchor.ui.components.Action
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

    fun onThoughtChange(type: Action, thought: String) {
        when (type) {
            Action.ADD -> thoughts.add(0, thought)
            Action.REMOVE -> thoughts.remove(thought)
        }
    }

    fun onInterpretationChange(type: Action, interpretation: String) {
        when (type) {
            Action.ADD -> interpretations.add(0, interpretation)
            Action.REMOVE -> interpretations.remove(interpretation)
        }
    }

    fun onBehaviorChange(type: Action, behavior: String) {
        when (type) {
            Action.ADD -> behaviors.add(0, behavior)
            Action.REMOVE -> behaviors.remove(behavior)
        }
    }

    fun onActionChange(type: Action, action: String) {
        when (type) {
            Action.ADD -> actions.add(0, action)
            Action.REMOVE -> actions.remove(action)
        }
    }

    fun onShowDiscardDialogChange(showDiscardDialog: Boolean) {
        this.showDiscardDialog.value = showDiscardDialog
    }
}
