package com.susieson.anchor.di

import com.susieson.anchor.service.AuthService
import com.susieson.anchor.service.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthService(authServiceImpl: AuthServiceImpl): AuthService
}
