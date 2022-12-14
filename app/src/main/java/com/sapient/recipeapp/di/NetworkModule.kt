package com.sapient.recipeapp.di

import com.sapient.recipeapp.data.remote.interceptor.HeaderInterceptor
import com.sapient.recipeapp.data.remote.api.RecipeApi
import com.sapient.recipeapp.data.remote.builder.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: RetrofitBuilder, headerInterceptor: HeaderInterceptor): Retrofit = retrofitBuilder
        .addInterceptors(headerInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRecipeService(retrofit: Retrofit): RecipeApi = retrofit.create(RecipeApi::class.java)

}