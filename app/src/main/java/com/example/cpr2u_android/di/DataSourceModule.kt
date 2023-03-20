package com.example.cpr2u_android.di

import com.example.cpr2u_android.data.datasource.auth.AuthDataSource
import com.example.cpr2u_android.data.datasource.auth.AuthRemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<AuthDataSource> { AuthRemoteDataSource(get()) }
}
