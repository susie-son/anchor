package com.susieson.anchor.di

import com.susieson.anchor.service.StorageService
import com.susieson.anchor.service.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class StorageModule {
    @Binds
    abstract fun bindStorageService(storageService: StorageServiceImpl): StorageService
}
