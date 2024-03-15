package com.example.data.products.entity

import com.example.domain.products.entity.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Float,
    @SerializedName("type") val type: String,
)

fun ProductResponse.toEntity(): Product =
    Product(
        id = id,
        name = name,
        price = price,
        type = type,
    )