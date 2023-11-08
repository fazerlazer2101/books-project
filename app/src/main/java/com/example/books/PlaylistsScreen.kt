package com.example.books

import android.annotation.SuppressLint
import android.widget.GridLayout
import android.widget.Toast
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
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.books.database.BookDatabase
import com.example.books.database.daos.BooksDao
import com.example.books.database.models.Playlists


private lateinit var db: BookDatabase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun PlaylistsScreen(
    navController: NavController,
    innerPadding: PaddingValues
){
    //Keeps track of dialog
    val isDialogOpen = remember { mutableStateOf(false) }

    //Database variables
    val context =LocalContext.current
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
                }
            )
        }
    ) {innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        ) {
            //Retrieve list of books
            val listOfBooks = booksDao.getAllPlaylists()
            LazyColumn(modifier = Modifier
            ) {

                items(listOfBooks) {item ->
                    PlaylistCard(contentDescription = "Card", title = item.playlistName, navController = navController, booksDao = booksDao, playlistItem = item )
                    Divider(modifier = Modifier)
                }

            }
        }
    }

    //Dialog box
    if(isDialogOpen.value)
    {
        //Dialog box variables
        var newPlaylistName by rememberSaveable { mutableStateOf("") }

        //Needs text validation

        fun newPlaylistCreated(name: String)
        {
            //Needs error handling

            //Close Dialog

            //Inserts new playlist
            if(name.length > 0)
            {
                Toast.makeText(context, newPlaylistName, Toast.LENGTH_SHORT)
                    .show()
                val size = (booksDao.getAllPlaylists().size) + 1

                //Create new instance of playlists
                booksDao.createPlaylist(Playlists(uid = 0,playlistName = newPlaylistName))
                isDialogOpen.value = false
            }

        }

        Dialog(onDismissRequest = { isDialogOpen }) {
            Card(
                modifier = Modifier
                    .wrapContentWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ){
                Box(modifier = Modifier
                    .height(150.dp)
                    .width(200.dp)

                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Text(
                                text = "Create New Playlist",
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Row{

                            OutlinedTextField(
                                modifier = Modifier
                                    .width(125.dp)
                                    .padding(top = 10.dp),
                                value = newPlaylistName,
                                onValueChange = { newPlaylistName = it },
                                singleLine = true,
                            )
                        }
                        Row{
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
        onClick = ({navController.navigate("3")})
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                        val listItems = arrayOf("Rename", "Delete")
                        val contextForToast = LocalContext.current.applicationContext
                        //Sets menu into column
                        Column(modifier=Modifier) {
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                // adding items
                                listItems.forEachIndexed { itemIndex, itemValue ->
                                    DropdownMenuItem(
                                        onClick = {
                                            //Deletes entry
                                            booksDao.deletePlaylist(playlistItem)
                                            //Refresh the composable
                                            navController.navigate("0")
                                            //Closes
                                            expanded = false
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