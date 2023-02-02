package com.giedriusmecius.listings.di

import com.giedriusmecius.listings.data.checkoutManager.CheckoutManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCheckoutManager() = CheckoutManager()

}