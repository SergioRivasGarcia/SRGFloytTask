package com.example.presentation.main.products.list.adapter

import com.example.domain.products.entity.Product

class ProductItem(
    val id: String,
    val name: String,
    val price: Float,
    val type: String,
    var quantity: Int = 0,
) : ProductListItem(TYPE_PRODUCT) {
    fun toProduct(): Product {
        return Product(id, name, price, type, quantity)
    }
}
