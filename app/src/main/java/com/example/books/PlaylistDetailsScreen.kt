@file:OptIn(ExperimentalGlideComposeApi::class)

package com.example.books

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.books.database.BookDatabase
import com.example.books.database.models.Books

private lateinit var db: BookDatabase
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailsScreen(
    navController: NavController,
    param: Int
){
    //Database variables
    val context = LocalContext.current
    db = BookDatabase.getDatabase(context)
    val booksDao = db.BooksDao()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${booksDao.getDetailsOfPlaylist(param).playlistName}",
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("0") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    )
    {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Testing ${param}",
                color = Color.Black,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Column(modifier = Modifier
                .padding(it)
                .fillMaxSize()

            ) {
                //Retrieve list of books
                val listOfBooks = booksDao.getAllBooksInPlaylist(id = param)
                LazyColumn(modifier = Modifier
                ) {

                    items(listOfBooks) {item ->

                        booksCards(item)
                        Divider(modifier = Modifier)
                    }

                }
            }

        }
    }

}

//Composable that will display the each book
@Composable
fun booksCards(
    book: Books
){
    Card ( modifier = Modifier
        .width(200.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ){
        //Contains image
        Row {
            GlideImage(
                model = "https://covers.openlibrary.org/b/ISBN/${book.isbn_10}-M.jpg",
                contentDescription = "Book Cover"
            )
        }


    }
}