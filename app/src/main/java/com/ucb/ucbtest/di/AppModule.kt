package com.ucb.ucbtest.di
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.ucb.data.CategoryMealRepository
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
import com.ucb.data.TopPickRepository
import com.ucb.data.meal.ICategoryMealRemoteDataSource
import com.ucb.data.meal.IMealRemoteDataSource
import com.ucb.data.meal.ITopPickRemoteDataSource
import com.ucb.framework.categoryMeal.CategoryMealRemoteDataSource
import com.ucb.framework.meal.MealRemoteDataSource
import com.ucb.framework.topPick.TopPickRemoteDataSource
import com.ucb.usecases.GetCategoriesMeal
import com.ucb.usecases.GetMealByName
import com.ucb.usecases.GetTopPicks


import com.ucb.data.repository.AuthRepository
import com.ucb.framework.repository.AuthRepositoryImpl
import com.ucb.usecases.auth.CheckAuthStateUseCase
import com.ucb.usecases.auth.GetCurrentUserUseCase
import com.ucb.usecases.auth.SignInWithGoogleUseCase
import com.ucb.usecases.auth.SignOutUseCase
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
    fun provideCategoryMealRemoteDataSource(retrofitBuilder: RetrofitBuilder): ICategoryMealRemoteDataSource {
        return CategoryMealRemoteDataSource(retrofitBuilder)
    }

    @Provides
    @Singleton
    fun provideCategoryMealRepository(dataSource: ICategoryMealRemoteDataSource): CategoryMealRepository {
        return CategoryMealRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetCategoryesMeal(categoryMealRepository: CategoryMealRepository): GetCategoriesMeal {
        return GetCategoriesMeal(categoryMealRepository)
    }

    // TopPick dependencies
    @Provides
    @Singleton
    fun provideTopPickRemoteDataSource(retrofitBuilder: RetrofitBuilder): ITopPickRemoteDataSource {
        return TopPickRemoteDataSource(retrofitBuilder)
    }

    @Provides
    @Singleton
    fun provideTopPickRepository(dataSource: ITopPickRemoteDataSource): TopPickRepository {
        return TopPickRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetTopPicks(topPickRepository: TopPickRepository): GetTopPicks {
        return GetTopPicks(topPickRepository)
    }

    @Provides
    @Singleton
    fun providerRetrofitBuilder(@ApplicationContext context: Context) : RetrofitBuilder {
        return RetrofitBuilder(context)
    }

    @Provides
    @Singleton
    fun providePushNotificationRepository(pushDataSource: IPushDataSource): PushNotificationRepository {
        return PushNotificationRepository(pushDataSource)
    }

    @Provides
    @Singleton
    fun provideIPushDataSource(): IPushDataSource {
        return FirebaseNotificationDataSource()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(authRepository: AuthRepository): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(authRepository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideCheckAuthStateUseCase(authRepository: AuthRepository): CheckAuthStateUseCase {
        return CheckAuthStateUseCase(authRepository)
    }


}