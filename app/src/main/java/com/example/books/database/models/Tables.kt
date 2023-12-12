package com.example.books.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey


@Entity(tableName = "Contribution_Type")
data class Contribution_Type(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "contribution_role_name") val roleName: String
)

@Entity(tableName = "Contributor",
    foreignKeys = [
        ForeignKey(entity = Contribution_Type::class,
            parentColumns = ["uid"],
            childColumns = ["contribution_type_id"],
            onDelete = CASCADE)
    ])
data class Contributor(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "contribution_type_id") val contribution_type_id: Int
)



@Entity(tableName = "Playlists")
data class Playlists(

    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "playlist_name") val playlistName: String
)

//Book table
@Entity(tableName = "Books",
     /*foreignKeys = [
        ForeignKey(entity = Contributor::class,
            parentColumns = ["uid"],
            childColumns = ["contributor_id"],
            onDelete = CASCADE
        )
    ] */)
data class Books(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subjects") val subjects: String,
    @ColumnInfo(name = "isbn_10") val isbn_10: String,
    @ColumnInfo(name = "isbn_13") val isbn_13: String,
    @ColumnInfo(name = "number_of_pages") val number_of_pages: Int,
    @ColumnInfo(name = "publish_date") val publish_date: String,
    //@ColumnInfo(name = "contributor_id") val contributor_id: Int,
)

@Entity(tableName = "Book_Details")
data class Book_Details(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "book_id") val book_id: Int,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "current_page_number") val current_page_number: Int,
    @ColumnInfo(name = "total_page_number") val total_page_number: Int
)

@Entity(tableName = "Playlists_Books",
    foreignKeys = [
        ForeignKey(entity = Playlists::class,
            parentColumns = ["uid"],
            childColumns = ["playlist_id"],
            onDelete = CASCADE
        ),
        ForeignKey(entity = Books::class,
            parentColumns = ["uid"],
            childColumns = ["book_id"],
            onDelete = CASCADE)
    ])
data class Playlists_Books(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "playlist_id") val playlist_id: Int,
    @ColumnInfo(name = "book_id") val book_id: Int
)

