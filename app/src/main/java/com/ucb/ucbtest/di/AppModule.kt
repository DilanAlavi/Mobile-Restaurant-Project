package com.ucb.ucbtest.di
import android.content.Context
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
}