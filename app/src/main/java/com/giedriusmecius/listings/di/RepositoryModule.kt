package com.giedriusmecius.listings.di

import android.content.Context
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.ProductRepositoryImpl
import com.giedriusmecius.listings.data.remote.api.ProductApi
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import com.giedriusmecius.listings.utils.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository = ProductRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideUserSharedPreferences(@ApplicationContext context: Context): UserPreferences =
        UserPreferences(
            context.getSharedPreferences(
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE
            )
        )

}