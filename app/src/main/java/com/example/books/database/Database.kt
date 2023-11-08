package com.example.books.database

import android.content.Context
import android.service.autofill.UserData
import androidx.room.RoomDatabase
import com.google.common.collect.Tables
import androidx.room.Database
import androidx.room.Room
import com.example.books.database.daos.BooksDao
import com.example.books.database.models.Books
import com.example.books.database.models.Contribution_Type
import com.example.books.database.models.Contributor
import com.example.books.database.models.Playlists
import com.example.books.database.models.Playlists_Books

@Database(
    entities = [Books::class, Contribution_Type::class, Contributor::class, Playlists::class, Playlists_Books::class], version = 1)
abstract class BookDatabase: RoomDatabase() {
    abstract fun BooksDao(): BooksDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return  instance
            }
        }

    }
}