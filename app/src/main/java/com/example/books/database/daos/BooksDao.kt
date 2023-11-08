package com.example.books.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.books.database.models.Books
import com.example.books.database.models.Playlists
import com.example.books.database.models.Playlists_Books

@Dao
interface BooksDao {

    //Playlists Methods

    @Query ("SELECT * FROM Playlists")
    fun getAllPlaylists(): List<Playlists>
    //Inserts new Playlist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createPlaylist(playlist: Playlists)

    @Delete
    fun deletePlaylist(playlist: Playlists)

    @Query ("SELECT * FROM Books")
    fun getAllBooks(): List<Books>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createBook(book: Books)

    @Delete
    fun deleteBook(book: Books)

    @Insert
    fun addBookToPlaylist(playlistsBooks: Playlists_Books)
}