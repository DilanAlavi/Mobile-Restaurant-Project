package com.ucb.data.order

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ucb.domain.Order
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : IOrderRepository {

    private val sharedPreferences = context.getSharedPreferences("orders_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val ordersKey = "saved_orders"

    override suspend fun saveOrder(order: Order) {
        val currentOrders = getAllOrders().toMutableList()
        currentOrders.add(0, order) // Agregar al inicio (más reciente primero)

        val ordersJson = gson.toJson(currentOrders)
        sharedPreferences.edit().putString(ordersKey, ordersJson).apply()
    }

    override suspend fun getAllOrders(): List<Order> {
        val ordersJson = sharedPreferences.getString(ordersKey, null)
        return if (ordersJson != null) {
            val type = object : TypeToken<List<Order>>() {}.type
            gson.fromJson(ordersJson, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun clearOrders() {
        sharedPreferences.edit().remove(ordersKey).apply()
    }
}