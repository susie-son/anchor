package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    @Assisted("userId") private val userId: String,
    @Assisted("exposureId") private val exposureId: String,
    private val storageService: StorageService,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("userId") userId: String, @Assisted("exposureId") exposureId: String): ExposurePreparationViewModel
    }

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    val thoughts = mutableStateListOf<String>()
    val interpretations = mutableStateListOf<String>()
    val behaviors = mutableStateListOf<String>()
    val actions = mutableStateListOf<String>()

    var showDiscardDialog by mutableStateOf(false)

    val isValid = derivedStateOf {
        title.isNotBlank() && description.isNotBlank()
            && thoughts.isNotEmpty() && interpretations.isNotEmpty()
            && behaviors.isNotEmpty() && actions.isNotEmpty()
    }
    val isEmpty by derivedStateOf {
        title.isBlank() && description.isBlank()
            && thoughts.isEmpty() && interpretations.isEmpty()
            && behaviors.isEmpty() && actions.isEmpty()
    }

    fun onClose() {
        if (isEmpty) {
            deleteExposure()
        } else {
            showDiscardDialog = true
        }
    }

    fun addPreparation() {
        viewModelScope.launch {
            val preparation = Preparation(thoughts, interpretations, behaviors, actions)
            storageService.updateExposure(userId, exposureId, title, description, preparation)
        }
    }

    fun deleteExposure() {
        viewModelScope.launch {
            storageService.deleteExposure(userId, exposureId)
        }
    }

    fun onTitleChange(title: String) {
        this.title = title
    }

    fun onDescriptionChange(description: String) {
        this.description = description
    }

    fun onThoughtAdded(thought: String) {
        thoughts.add(thought)
    }

    fun onThoughtRemoved(thought: String) {
        thoughts.remove(thought)
    }

    fun onInterpretationAdded(interpretation: String) {
        interpretations.add(interpretation)
    }

    fun onInterpretationRemoved(interpretation: String) {
        interpretations.remove(interpretation)
    }

    fun onBehaviorAdded(behavior: String) {
        behaviors.add(behavior)
    }

    fun onBehaviorRemoved(behavior: String) {
        behaviors.remove(behavior)
    }

    fun onActionAdded(action: String) {
        actions.add(action)
    }

    fun onActionRemoved(action: String) {
        actions.remove(action)
    }

    fun onShowDiscardDialogChange(showDiscardDialog: Boolean) {
        this.showDiscardDialog = showDiscardDialog
    }
}
