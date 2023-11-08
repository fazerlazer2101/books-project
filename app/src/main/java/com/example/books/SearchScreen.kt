package com.example.books

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.engage.common.datamodel.Image
import org.json.JSONArray
import android.view.View
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import com.example.books.database.BookDatabase
import com.example.books.database.models.Books
import com.example.books.database.models.Playlists
import com.example.books.database.models.Playlists_Books

private lateinit var db: BookDatabase

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SearchScreen(
    navController: NavController
){
    val context = LocalContext.current;
    val queue = Volley.newRequestQueue(context)
    val focusManager = LocalFocusManager.current
    val padding = 16.dp
    var searchable by remember {
        mutableStateOf("")
    }
    var imageURL by remember {
        mutableStateOf("")
    }
    var bookTitle by remember {
        mutableStateOf("")
    }
    var subjects by remember {
        mutableStateOf("")
    }
    var isbn10 by remember {
        mutableStateOf("")
    }
    var isbn13 by remember {
        mutableStateOf("")
    }
    var numberOfPages by remember {
        mutableIntStateOf(0)
    }
    var publishDate by remember {
        mutableStateOf("")
    }
    var isSavedVisible by remember {
        mutableStateOf(false)
    }

    db = BookDatabase.getDatabase(context);
    val bookDao = db.BooksDao();

    fun createBook() {
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT)
            .show()
        bookDao.createBook(Books(uid = 0, title = bookTitle, subjects = subjects, isbn_10 = isbn10.toLong(), isbn_13 = isbn13.toLong(), number_of_pages = numberOfPages, publish_date = publishDate))

        var playlists = bookDao.getAllPlaylists();
        var saved = false;
        for (playlist in playlists) {
            if (playlist.playlistName == "Saved")
            {
                bookDao.addBookToPlaylist(Playlists_Books(uid = 0, playlist_id = playlist.uid, book_id = bookDao.getAllBooks().size))
                saved = true;
            }
        }
        if (!saved)
        {
            bookDao.createPlaylist(Playlists(uid = 0, playlistName = "Saved"))
            bookDao.addBookToPlaylist(Playlists_Books(uid = 0, playlist_id = bookDao.getAllPlaylists().size, book_id = bookDao.getAllBooks().size))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.TopCenter,
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchable,
                placeholder = { Text("ISBN...") },
                onValueChange = { searchable = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                focusManager.clearFocus()

                val url = "https://openlibrary.org/isbn/${searchable.trim()}.json"

                // Loading image
                imageURL = "https://media1.giphy.com/media/6036p0cTnjUrNFpAlr/giphy.gif?cid=ecf05e479j2w1xbpa3tk0fx0b5mo6nax6c74nd8ct4mk6b64&ep=v1_gifs_search&rid=giphy.gif&ct=g"

                val text = "Failed to load image"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, duration) // in Activity

                //Retrieves jsonObject
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->

                        //Parse JSON
                        //Gets an array from json in the key called "covers"
                        val responseJSON: JSONArray = response.getJSONArray("covers")

                        //Stores the cover ID
                        val imageId: String = responseJSON.getString(0)
                        imageURL = "https://covers.openlibrary.org/b/id/${imageId}-L.jpg"
                        
                        //Display book cover ID
                        bookTitle = response.getString("title")
                        subjects = response.getJSONArray("subjects").toString()
                        isbn10 = response.getJSONArray("isbn_10")[0].toString()
                        isbn13 = response.getJSONArray("isbn_13")[0].toString()
                        numberOfPages = response.getInt("number_of_pages")
                        publishDate = response.getString("publish_date")

                        isSavedVisible = true
                    },//If an error occurs in fetching the data
                    {
                        bookTitle = "Failed to load image"
                        isSavedVisible = false
                        toast.show()
                    })

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest)
            }) {
                Text("Search")
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlideImage(
                        model = imageURL,
                        contentDescription = "Book Cover"
                    )
                    Text(text = bookTitle)
                    Button(
                        onClick = { createBook() },
                        modifier = Modifier.alpha(if (isSavedVisible) 1f else 0f)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}