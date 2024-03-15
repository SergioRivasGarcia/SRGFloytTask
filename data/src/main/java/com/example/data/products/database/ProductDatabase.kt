package com.example.data.products.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.products.entity.Product
import com.example.domain.products.repository.IProductDBRepository

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun iProductDBRepository(): IProductDBRepository
}
