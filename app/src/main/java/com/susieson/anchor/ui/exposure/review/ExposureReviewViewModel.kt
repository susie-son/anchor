package com.susieson.anchor.ui.exposure.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Review
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposureReviewViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    fun addReview(
        userId: String,
        exposureId: String,
        review: Review,
    ) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, review)
        }
    }
}
