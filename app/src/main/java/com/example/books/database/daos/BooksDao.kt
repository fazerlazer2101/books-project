package com.example.books.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.books.database.models.Book_Details
import com.example.books.database.models.Books
import com.example.books.database.models.Playlists
import com.example.books.database.models.Playlists_Books

@Dao
interface BooksDao {

    //Playlists Methods

    @Query ("SELECT * FROM Playlists")
    fun getAllPlaylists(): List<Playlists>

    @Query ("SELECT * FROM Playlists WHERE playlist_name != 'Saved' ")
    fun getAllPlaylistsExcludeSaved(): List<Playlists>
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

    @Query("SELECT * FROM Books WHERE uid = :id")
    fun getDetailsOfBook(id: Int) : Books

    //Creates a book
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createBook(book: Books)

    //Deletes a book
    @Delete
    fun deleteBook(book: Books)

    @Upsert
    fun upsertBookDetails(bookDetails: Book_Details)

    @Query("DELETE FROM Book_Details WHERE book_id = :book_id")
    fun deleteBookDetails(book_id: Int)

    @Query("SELECT * FROM Book_Details WHERE book_id = :book_id")
    fun getExistingBookDetails(book_id: Int) : Book_Details

    @Query("SELECT * FROM Book_Details")
    fun getAllBookDetails() : List<Book_Details>

    //Adds books to playlists
    @Insert
    fun addBookToPlaylist(playlistsBooks: Playlists_Books)

    //Gets a the uid of a playlist
    @Query("SELECT uid FROM Playlists WHERE playlist_name = 'Saved'")
    fun getSavedPlaylist(): Int


    //Methods for a particular playlist

    //Retrieve all books of a particular playlist
    @Query("SELECT DISTINCT b.title, b.publish_date, b.number_of_pages, b.isbn_10, b.isbn_13, b.subjects, b.uid FROM playlists_books pb INNER JOIN Books b on b.uid = pb.book_id WHERE pb.playlist_id = :id")
    fun getAllBooksInPlaylist(id: Int): List<Books>

    @Query("SELECT * FROM playlists WHERE uid = :id")
    fun getDetailsOfPlaylist(id: Int) : Playlists

    @Query("SELECT * FROM playlists_books")
    fun getAllBooksAssigned() : List<Playlists_Books>

    //Determines if a book is assigned to a particular playlist
    @Query("SELECT * FROM playlists_books WHERE playlist_id = :playlist_id and book_id = :book_id")
    fun checksIfBookIsAssigned(playlist_id: Int, book_id: Int) : Boolean

    //Insert a new book in the playlist_books table
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun assignBookToPlaylist(playlistsBooks: Playlists_Books)

    //Delete a book in the playlist_books table
    @Query("DELETE FROM PLAYLISTS_BOOKS WHERE playlist_id = :playlist_id and book_id = :book_id")
    fun deleteBookInPlaylist(playlist_id: Int, book_id: Int)

    @Query("DELETE FROM Books WHERE uid = :book_id")
    fun deleteBooksFromSavedPlaylist(book_id: Int)
}