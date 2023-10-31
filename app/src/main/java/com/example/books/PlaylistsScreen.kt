package com.example.books

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.room.Database
import com.example.books.database.BookDatabase
import com.example.books.database.models.Books
import com.example.books.database.models.Playlists

private lateinit var db: BookDatabase
@Composable
fun PlaylistsScreen(
    navController: NavController
){
    val context =LocalContext.current;
    db = BookDatabase.getDatabase(context)


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Home",
            color = Color.Black,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
        val Painter = painterResource(id = R.drawable.ic_launcher_foreground)
        val Description = "Create Playlist"
        val test = "Create Playlist"
        PlaylistCard(painter = Painter, contentDescription = Description, title = test)
    }

    ElevatedButtonExample()



}

@Composable
fun ElevatedButtonExample() {
    val bookDao = db.BooksDao()
    val context =LocalContext.current;
    fun toast()
    {
        val newBook = Playlists(uid= 1, playlistName = "test")
        bookDao.createPlaylist(newBook)
    }

    ElevatedButton(onClick = { toast() }) {
        Text("Elevated")
    }
}