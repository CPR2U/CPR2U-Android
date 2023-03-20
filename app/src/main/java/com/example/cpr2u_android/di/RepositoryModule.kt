package com.example.cpr2u_android.di

import com.example.cpr2u_android.data.repository.auth.AuthRepositoryImpl
import com.example.cpr2u_android.domain.repository.auth.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}
