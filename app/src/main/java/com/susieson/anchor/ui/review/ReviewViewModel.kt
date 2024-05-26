package com.susieson.anchor.ui.review

import androidx.lifecycle.ViewModel
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

}