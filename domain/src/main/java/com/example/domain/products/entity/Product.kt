package com.example.domain.products.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey val id: String,
    val name: String,
    val price: Float,
    val type: String,
    var quantity: Int = 0,
) : Parcelable