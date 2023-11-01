package com.example.books

import android.content.Context
import android.widget.ListView
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsActions.Dismiss
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Dao
import androidx.room.Database
import com.example.books.database.BookDatabase
import com.example.books.database.models.Books
import com.example.books.database.models.Playlists
import com.google.errorprone.annotations.Var
import java.util.logging.Logger.global

private lateinit var db: BookDatabase

@Composable

fun PlaylistsScreen(
    navController: NavController
){


    //Keeps track of dialog
    val isDialogOpen = remember { mutableStateOf(false) }


    val context =LocalContext.current;
    db = BookDatabase.getDatabase(context)
    val booksDao = db.BooksDao()


/*
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
*/

    ElevatedButton(
        onClick = {isDialogOpen.value = true}
    ){
        Text(text = "Create New Playlist")
    }

    //Retrieve list of books
    val listOfBooks = booksDao.getAllPlaylists()
    LazyColumn(contentPadding = PaddingValues(8.dp)
    ) {
        items(listOfBooks) {item ->
            PlaylistCard(contentDescription = "vlag", title = item.playlistName)
            Divider()
        }
    }



    //Dialog box
    if(isDialogOpen.value)
    {
        //Dialog box variables
        var newPlaylistName by rememberSaveable { mutableStateOf("") }

        //Text error
        var error = false

        fun newPlaylistCreated(name: String)
        {
            //Needs error handling

            //Close Dialog

            //Inserts new playlist
            if(name.length > 0)
            {
                val size = (booksDao.getAllPlaylists().size) + 1
                //Create new instance of playlists
                booksDao.createPlaylist(Playlists(uid = size, playlistName = "${newPlaylistName}"))
                isDialogOpen.value = false;
            }

        }

        Dialog(onDismissRequest = { isDialogOpen }) {
            Card(
                modifier = Modifier
                    .wrapContentWidth(),
                shape = RoundedCornerShape(15.dp),
            ){
                Box(modifier = Modifier
                    .height(150.dp)
                    .width(200.dp)

                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Text(text = "Create New Playlist",
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Row{

                            OutlinedTextField(
                                modifier = Modifier
                                    .width(125.dp)
                                    .padding(top = 10.dp)

                                ,
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

@Composable

fun Playlist(){
    val context =LocalContext.current;
    Card(
        modifier = Modifier
            .wrapContentWidth(),
        shape = RoundedCornerShape(15.dp),
    ){
        //text and iamge
        Box(modifier = Modifier
            .height(150.dp)
            .width(150.dp)

        ){
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text(text = "Create New Playlist",
                    modifier = Modifier
                        .padding(top = 5.dp))
            }

        }
        Button(
            onClick = {
                val text = "Hello toast!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, duration) // in Activity
                toast.show()
            }
        ){
            Text(text = "Create")
        }
    }

}
