@file:OptIn(ExperimentalGlideComposeApi::class)

package com.example.books

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.books.database.BookDatabase
import com.example.books.database.daos.BooksDao
import com.example.books.database.models.Books
import com.example.books.database.models.Playlists
import com.example.books.database.models.Playlists_Books
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


private lateinit var db: BookDatabase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun PlaylistsScreen(
    navController: NavController,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    //Keeps track of dialog
    val isDialogOpen = remember { mutableStateOf(false) }
    val isDialogSharedBookOpen = remember { mutableStateOf(false) }
    var sharedISBN = remember { mutableStateOf("") }

    //Retrieves QR content
    val getContent = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.getContents() == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                context,
                "Scanned: " + result.getContents(),
                Toast.LENGTH_LONG
            ).show();

            sharedISBN .value = result.getContents();
            isDialogSharedBookOpen.value = true;
        }
    }

    //Scanner/camera option
    val options = ScanOptions()
    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
    options.setCameraId(3)
    options.setBeepEnabled(false)
    options.setOrientationLocked(false)

    //Database variables

    db = BookDatabase.getDatabase(context)
    val booksDao = db.BooksDao()

//Top Bar
    Scaffold(modifier = Modifier.padding(innerPadding),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Playlists",
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { isDialogOpen.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = {

                        getContent.launch(ScanOptions())
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            //Retrieve list of books
            val listOfBooks = booksDao.getAllPlaylists()
            LazyColumn(
                modifier = Modifier
            ) {

                items(listOfBooks) { item ->
                    PlaylistCard(
                        contentDescription = "Card",
                        title = item.playlistName,
                        navController = navController,
                        booksDao = booksDao,
                        playlistItem = item
                    )
                    Divider(modifier = Modifier)
                }

            }
        }
    }

    //Dialog box
    if (isDialogOpen.value) {
        //Dialog box variables
        var newPlaylistName by rememberSaveable { mutableStateOf("") }

        //Needs text validation

        fun newPlaylistCreated(name: String) {
            //Needs error handling

            //Close Dialog

            //Inserts new playlist
            if (name.length > 0) {
                Toast.makeText(context, "Created ${newPlaylistName}", Toast.LENGTH_SHORT).show()

                //Create new instance of playlists
                booksDao.createPlaylist(Playlists(uid = 0, playlistName = newPlaylistName))
                isDialogOpen.value = false
            }

        }
        //Create playlist
        Dialog(onDismissRequest = { isDialogOpen }) {
            Card(
                modifier = Modifier
                    .wrapContentWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .width(200.dp)

                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            Text(
                                text = "Create New Playlist",
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Row {

                            OutlinedTextField(
                                modifier = Modifier
                                    .width(125.dp)
                                    .padding(top = 10.dp),
                                value = newPlaylistName,
                                onValueChange = { newPlaylistName = it },
                                singleLine = true,
                            )
                        }
                        Row {
                            TextButton(onClick = { isDialogOpen.value = false }) {
                                Text(text = "Cancel")
                            }
                            TextButton(onClick = { newPlaylistCreated(newPlaylistName) }) {
                                Text(text = "Create")
                            }
                        }
                    }
                }
            }
        }
    }

    //QR Book
    //Dialog box
    if (isDialogSharedBookOpen.value) {
        //Variables
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
        var alreadyRan = false

        fun createBook() {

            if(!booksDao.checkExistingBook(book_title = bookTitle))
            {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT)
                    .show()
                var playlists = booksDao.getAllPlaylists();
                var saved = false;
                for (playlist in playlists) {
                    if (playlist.playlistName == "Saved")
                    {
                        booksDao.createBook(Books(uid = 0, title = bookTitle, subjects = subjects, isbn_10 = isbn10, isbn_13 = isbn13, number_of_pages = numberOfPages, publish_date = publishDate))
                        booksDao.addBookToPlaylist(Playlists_Books(uid =0, playlist_id = booksDao.getSavedPlaylist(), book_id = booksDao.getBook(bookTitle).uid))
                        saved = true;
                    }
                }
                if (!saved)
                {
                    booksDao.createPlaylist(Playlists(uid = 0, playlistName = "Saved"))
                    booksDao.createBook(Books(uid = 0, title = bookTitle, subjects = subjects, isbn_10 = isbn10 , isbn_13 = isbn13, number_of_pages = numberOfPages, publish_date = publishDate))
                    booksDao.addBookToPlaylist(Playlists_Books(uid =0, playlist_id = booksDao.getSavedPlaylist(), book_id = booksDao.getBook(bookTitle).uid))
                }
            }
            else
            {
                Toast.makeText(context, "Book Already exists", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val queue = Volley.newRequestQueue(context)
        Dialog(
            onDismissRequest = { isDialogSharedBookOpen.value = false },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(400.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp)
                ) {
                    if(!alreadyRan)
                    {
                        alreadyRan = true
                        //Volley data
                        Log.d("sharedISBN", "${sharedISBN.value}")
                        val url = "https://openlibrary.org/isbn/${sharedISBN.value}.json"

                        // Loading image
                        imageURL = "https://media1.giphy.com/media/6036p0cTnjUrNFpAlr/giphy.gif?cid=ecf05e479j2w1xbpa3tk0fx0b5mo6nax6c74nd8ct4mk6b64&ep=v1_gifs_search&rid=giphy.gif&ct=g"

                        val text = "Failed to load image"
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText(context, text, duration) // in Activity

                        //Retrieves jsonObject
                        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://openlibrary.org/isbn/${sharedISBN.value}.json", null,
                            { response ->

                                Log.d("url response", "Value: ${response}")
                                //Parse JSON
                                //Gets an array from json in the key called "covers"

                                var imageId: String = "No Cover Found"



                                if (response.has("covers")) {
                                    imageId = response.getJSONArray("covers").getString(0);
                                    var imageurlCheck: String = "https://covers.openlibrary.org/b/id/${imageId}-L.jpg"

                                    Log.d("Image response", response.has("covers").toString())
                                    imageURL = imageurlCheck
                                    Log.d("Image response", imageURL)
                                } else {
                                    imageURL = "None"
                                }

                                //Display book cover ID
                                bookTitle = response.getString("title")
                                if (response.has("subjects")) {
                                    subjects = response.getJSONArray("subjects").toString()
                                } else {
                                    subjects = ""
                                }
                                if(response.has("isbn_10"))
                                {
                                    isbn10 =response.getJSONArray("isbn_10")[0].toString()
                                }


                                if(response.has("isbn_13"))
                                {
                                    isbn13 = response.getJSONArray("isbn_13")[0].toString()
                                }
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
    }
}
//Common Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistCard(
    //Parameters
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier,
    booksDao: BooksDao,
    playlistItem: Playlists,
    navController: NavController
) {

    //Determines state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    //Actual content
    //Card
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxWidth()
            .padding(15.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = ({ navController.navigate("3/${playlistItem.uid}") })
    ) {
        //text and iamge
        Box(
            modifier = Modifier
                .height(150.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                //Spaces elements from start to end
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${title}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 28.sp,
                        modifier = Modifier.width(300.dp)
                    )

                    //Icon Button
                    IconButton({ expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description"
                        )
                        //List of options
                        val listItems = arrayOf("Delete")
                        val contextForToast = LocalContext.current.applicationContext
                        //Sets menu into column
                        Column(modifier = Modifier) {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }) {
                                // adding items
                                listItems.forEachIndexed { itemIndex, itemValue ->
                                    DropdownMenuItem(
                                        onClick = {
                                            if (playlistItem.playlistName != "Saved") {
                                                //Deletes entry
                                                booksDao.deletePlaylist(playlistItem)
                                                //Refresh the composable
                                                navController.navigate("0")
                                                //Closes
                                                expanded = false
                                            } else {
                                                Toast.makeText(
                                                    contextForToast,
                                                    "Can't delete default playlist",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        },
                                    ) {
                                        Text(text = itemValue)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}