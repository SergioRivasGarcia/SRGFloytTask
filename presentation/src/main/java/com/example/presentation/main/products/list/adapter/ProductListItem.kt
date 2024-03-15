package com.example.presentation.main.products.list.adapter

open class ProductListItem(
    val itemType: Int
) {
    companion object {
        const val TYPE_GROUP = 0
        const val TYPE_PRODUCT = 1
    }
}
