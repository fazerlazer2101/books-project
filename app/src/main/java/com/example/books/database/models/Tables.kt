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
    foreignKeys = [
        ForeignKey(entity = Contributor::class,
            parentColumns = ["uid"],
            childColumns = ["contributor_id"],
            onDelete = CASCADE
        ),
        ForeignKey(entity = Playlists_Books::class,
            parentColumns = ["uid"],
            childColumns = ["playlists_books_id"],
            onDelete = CASCADE)
    ])
data class Books(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subjects") val subjects: String,
    @ColumnInfo(name = "isbn_10") val isbn_10: Int,
    @ColumnInfo(name = "isbn_13") val isbn_13: Int,
    @ColumnInfo(name = "number_of_pages") val number_of_pages: Int,
    @ColumnInfo(name = "publish_date") val publish_date: Long,
    @ColumnInfo(name = "contributor_id") val contributor_id: Int,
    @ColumnInfo(name = "playlists_books_id") val playlists_books_id: Int,
)

@Entity(tableName = "Playlists_Books",
    foreignKeys = [
        ForeignKey(entity = Playlists::class,
            parentColumns = ["uid"],
            childColumns = ["playlist_id"],
            onDelete = CASCADE)
    ])
data class Playlists_Books(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "playlist_id") val playlist_id: Int,
    @ColumnInfo(name = "book_id") val book_id: Int
)

