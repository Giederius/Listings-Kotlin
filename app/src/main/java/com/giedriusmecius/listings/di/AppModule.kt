package com.giedriusmecius.listings.di

import android.content.Context
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.utils.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule