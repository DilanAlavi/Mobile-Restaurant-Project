package com.ucb.domain

import java.io.Serializable

data class Order(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val gst: Double = 5.0,
    val deliveryFee: Double = 25.0,
    val total: Double = subtotal + gst + deliveryFee,
    val paymentMethod: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Completado"
) : Serializable