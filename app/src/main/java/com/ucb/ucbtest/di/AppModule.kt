package com.ucb.ucbtest.di

import android.content.Context
import com.ucb.data.PushNotificationRepository
import com.ucb.data.push.IPushDataSource
import com.ucb.framework.service.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.ucb.framework.push.FirebaseNotificationDataSource
import com.ucb.data.MealRepository
import com.ucb.framework.meal.MealRemoteDataSource
import com.ucb.data.meal.IMealRemoteDataSource
import com.ucb.usecases.GetMealByName

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMealRemoteDataSource(retrofitBuilder: RetrofitBuilder): IMealRemoteDataSource {
        return MealRemoteDataSource(retrofitBuilder)
    }

    @Provides
    @Singleton
    fun provideMealRepository(dataSource: IMealRemoteDataSource): MealRepository {
        return MealRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetMealByName(mealRepository: MealRepository): GetMealByName {
        return GetMealByName(mealRepository)
    }

    @Provides
    @Singleton
    fun providerRetrofitBuilder(@ApplicationContext context: Context) : RetrofitBuilder {
        return RetrofitBuilder(context)
    }

    @Provides
    @Singleton
    fun providePushNotificationRepository( pushDataSource: IPushDataSource): PushNotificationRepository {
        return PushNotificationRepository(pushDataSource)
    }

    @Provides
    @Singleton
    fun provideIPushDataSource(): IPushDataSource {
        return FirebaseNotificationDataSource()
    }
}