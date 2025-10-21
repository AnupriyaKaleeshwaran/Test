package com.example.productlisttestapp.model

data class ApiResponse(
    val status: Boolean,
    val message: List<OrderMessage>,
    val response_code: Int
)

data class OrderMessage(
    val order: Order,
    val item: List<OrderItem>,
    val void: List<Any>,
    val qrCodeUrl: String
)

data class Order(
    val id: Int,
    val total: String,
    val tableSeat: Int,
    val tableName: String
)

data class OrderItem(
    val id: Int,
    val item_name: String,
    val qty: Int,
    val price: Double,
    val finalTotal: Double
)

data class Person(
    val name: String,
    var amount: Double = 0.0,
    var selectedItems: MutableList<OrderItem> = mutableListOf(),
    var shares: Double = 0.0,
    var percentage: Double = 50.0
)
