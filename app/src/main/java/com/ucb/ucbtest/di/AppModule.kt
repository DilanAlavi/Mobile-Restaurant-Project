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
import com.ucb.usecases.SearchMealsByName
import com.ucb.usecases.GetMealById
import com.ucb.usecases.cart.AddToCartUseCase
import com.ucb.usecases.cart.GetCartItemsUseCase
import com.ucb.usecases.cart.GetCartTotalUseCase
import com.ucb.usecases.cart.RemoveFromCartUseCase
import com.ucb.usecases.cart.UpdateCartQuantityUseCase
import com.ucb.data.cart.ICartRepository
import com.ucb.data.cart.CartRepository
import com.ucb.data.order.IOrderRepository
import com.ucb.framework.order.OrderRemoteDataSource
import com.ucb.usecases.order.GetOrdersHistoryUseCase
import com.ucb.usecases.order.SaveOrderUseCase
import com.ucb.usecases.cart.ClearCartUseCase

import com.ucb.data.repository.AuthRepository
import com.ucb.framework.repository.AuthRepositoryImpl
import com.ucb.usecases.auth.RegisterWithEmailPasswordUseCase
import com.ucb.usecases.auth.CheckAuthStateUseCase
import com.ucb.usecases.auth.GetCurrentUserUseCase
import com.ucb.usecases.auth.SignInWithGoogleUseCase
import com.ucb.usecases.auth.SignInWithEmailPasswordUseCase
import com.ucb.usecases.auth.SignOutUseCase
import com.ucb.framework.auth.InternalUserManager // ✅ CAMBIO DE IMPORT
import com.google.gson.Gson

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCartRepository(): ICartRepository {
        return CartRepository()
    }
    @Provides
    @Singleton
    fun provideOrderRepository(@ApplicationContext context: Context): IOrderRepository {
        return OrderRemoteDataSource(context)
    }

    @Provides
    @Singleton
    fun provideSaveOrderUseCase(orderRepository: IOrderRepository): SaveOrderUseCase {
        return SaveOrderUseCase(orderRepository)
    }

    @Provides
    @Singleton
    fun provideGetOrdersHistoryUseCase(orderRepository: IOrderRepository): GetOrdersHistoryUseCase {
        return GetOrdersHistoryUseCase(orderRepository)
    }

    @Provides
    @Singleton
    fun provideClearCartUseCase(cartRepository: ICartRepository): ClearCartUseCase {
        return ClearCartUseCase(cartRepository)
    }
    @Provides
    @Singleton
    fun provideAddToCartUseCase(cartRepository: ICartRepository): AddToCartUseCase {
        return AddToCartUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideGetCartItemsUseCase(cartRepository: ICartRepository): GetCartItemsUseCase {
        return GetCartItemsUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateCartQuantityUseCase(cartRepository: ICartRepository): UpdateCartQuantityUseCase {
        return UpdateCartQuantityUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveFromCartUseCase(cartRepository: ICartRepository): RemoveFromCartUseCase {
        return RemoveFromCartUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideGetCartTotalUseCase(cartRepository: ICartRepository): GetCartTotalUseCase {
        return GetCartTotalUseCase(cartRepository)
    }
    @Provides
    @Singleton
    fun provideMealRemoteDataSource(retrofitBuilder: RetrofitBuilder): IMealRemoteDataSource {
        return MealRemoteDataSource(retrofitBuilder)
    }
    @Provides
    @Singleton
    fun provideSearchMealsByName(mealRepository: MealRepository): SearchMealsByName {
        return SearchMealsByName(mealRepository)
    }
    @Provides
    @Singleton
    fun provideGetMealById(mealRepository: MealRepository): GetMealById {
        return GetMealById(mealRepository)
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

    // ✅ PROVIDER ACTUALIZADO PARA AUTHREPOSITORY
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        internalUserManager: InternalUserManager, // ✅ CAMBIO DE NOMBRE
        @ApplicationContext context: Context,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, internalUserManager, context, gson)
    }

    @Provides
    @Singleton
    fun provideSignInWithEmailPasswordUseCase(authRepository: AuthRepository): SignInWithEmailPasswordUseCase {
        return SignInWithEmailPasswordUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterWithEmailPasswordUseCase(authRepository: AuthRepository): RegisterWithEmailPasswordUseCase {
        return RegisterWithEmailPasswordUseCase(authRepository)
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

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideInternalUserManager(@ApplicationContext context: Context, gson: Gson): InternalUserManager {
        return InternalUserManager(context, gson)
    }
}