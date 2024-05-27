package com.susieson.anchor.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Review
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {
    fun add(userId: String, exposureId: String, review: Review) {
        viewModelScope.launch {
            storageService.add(userId, exposureId, review)
        }
    }
}