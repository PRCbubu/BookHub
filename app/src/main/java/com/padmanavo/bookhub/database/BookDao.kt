package com.padmanavo.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao
{
    @Insert
    fun insertBook(bookEntities: BookEntities)

    @Delete
    fun deleteBook(bookEntities: BookEntities)

    @Query("SELECT*FROM books")
    fun getAllBooks(): List<BookEntities>

    @Query("SELECT*FROM books WHERE book_id = :bookId")
    fun getBookById(bookId: String): BookEntities
}