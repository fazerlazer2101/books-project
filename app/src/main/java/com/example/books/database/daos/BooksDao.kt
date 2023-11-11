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

    //Gets a book using book title and returns an integer
    @Query ("SELECT * FROM Books WHERE title = :book_title")
    fun getBook(book_title: String): Books

    @Query("SELECT * FROM Books WHERE title = :book_title")
    fun checkExistingBook(book_title: String): Boolean

    //Creates a book
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createBook(book: Books)

    //Deletes a book
    @Delete
    fun deleteBook(book: Books)


    //Adds books to playlists
    @Insert
    fun addBookToPlaylist(playlistsBooks: Playlists_Books)

    //Gets a the uid of a playlist
    @Query("SELECT uid FROM Playlists WHERE playlist_name = 'Saved'")
    fun getSavedPlaylist(): Int
}