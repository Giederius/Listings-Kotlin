package com.giedriusmecius.listings.di

//import com.giedriusmecius.listings.data.ProductRepositoryImpl
//import com.giedriusmecius.listings.data.remote.api.ProductApi
import com.giedriusmecius.listings.data.ProductRepositoryImpl
import com.giedriusmecius.listings.data.remote.api.ProductApi
import com.giedriusmecius.listings.data.remote.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository = ProductRepositoryImpl(api)

}