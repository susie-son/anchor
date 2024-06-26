package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.runtime.derivedStateOf
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
    @Assisted("userId") private val userId: String,
    @Assisted("exposureId") private val exposureId: String,
    private val storageService: StorageService,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("userId") userId: String, @Assisted("exposureId") exposureId: String): ExposurePreparationViewModel
    }

    val basicState = mutableStateOf(BasicFormSectionState())
    val prepState = mutableStateOf(PreparationFormSectionState())

    val basicListener = derivedStateOf {
        BaseBasicFormSectionListener(basicState)
    }
    val prepListener = derivedStateOf {
        BasePreparationFormSectionListener(prepState)
    }

    val showDiscardDialog = mutableStateOf(false)

    val isValid = derivedStateOf {
        basicState.value.isValid && prepState.value.isValid
    }
    val isEmpty = derivedStateOf {
        basicState.value.isEmpty && prepState.value.isEmpty
    }

    fun onClose() {
        if (isEmpty.value) {
            deleteExposure(userId, exposureId)
        } else {
            setShowDiscardDialog(true)
        }
    }

    fun setShowDiscardDialog(show: Boolean) {
        showDiscardDialog.value = show
    }

    fun addPreparation() {
        viewModelScope.launch {
            val title = basicState.value.title
            val description = basicState.value.description
            val thoughts = prepState.value.thoughts
            val interpretations = prepState.value.interpretations
            val behaviors = prepState.value.behaviors
            val actions = prepState.value.actions
            val preparation = Preparation(thoughts, interpretations, behaviors, actions)

            storageService.updateExposure(userId, exposureId, title, description, preparation)
        }
    }

    fun deleteExposure(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.deleteExposure(userId, exposureId)
        }
    }
}
