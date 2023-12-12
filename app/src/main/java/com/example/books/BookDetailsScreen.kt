package com.example.books

import android.annotation.SuppressLint
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.books.database.BookDatabase
import com.example.books.database.models.Book_Details

private lateinit var db: BookDatabase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun BookDetailsScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    book_id: Int,
    playlist_id: Int
){
    val context = LocalContext.current
    db = BookDatabase.getDatabase(context)
    val booksDao = db.BooksDao()

    val book = booksDao.getDetailsOfBook(book_id)
    val bookDetails = booksDao.getExistingBookDetails(book_id)

    var status by remember {
        mutableStateOf("Unread")
    }
    var pageNumber by remember {
        mutableStateOf("${bookDetails.current_page_number}")
    }

    //Determines Intial state of status
    status = bookDetails.status

    fun upsertBookDetails() {
        if (bookDetails != null)
        {
            booksDao.upsertBookDetails(Book_Details(uid = bookDetails.uid, book_id = book_id, status = status, current_page_number = pageNumber.toInt(), total_page_number = book.number_of_pages))
        }
        else
        {
            booksDao.upsertBookDetails(Book_Details(uid = 0, book_id = book_id, status = status, current_page_number = pageNumber.toInt(), total_page_number = book.number_of_pages))
        }
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT)
            .show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book.title,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("3/${playlist_id}") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to Playlist"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Card(
                    modifier = Modifier
                        .height(250.dp)
                        .padding(15.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp)
                            .padding(0.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Contains image
                        GlideImage(
                            modifier = Modifier
                                .width(150.dp)
                                .height(225.dp),
                            model = "https://covers.openlibrary.org/b/ISBN/${book.isbn_10}-M.jpg",
                            contentDescription = "Book Cover",
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Status: ${status}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Pages Read: ")
                    TextField(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(end = 8.dp),
                        value = pageNumber,
                        placeholder = { Text("Page Number...") },
                        onValueChange = {
                            pageNumber = it
                            status = if (it == "0") {
                                "Unread"
                            } else if (it == "${book.number_of_pages}") {
                                "Read"
                            } else {
                                "In Progress"
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(text = "/${book.number_of_pages}")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { upsertBookDetails() }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }


}