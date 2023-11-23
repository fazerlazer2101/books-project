[1mdiff --git a/app/src/main/java/com/example/books/MainActivity.kt b/app/src/main/java/com/example/books/MainActivity.kt[m
[1mindex bf3defe..0950762 100644[m
[1m--- a/app/src/main/java/com/example/books/MainActivity.kt[m
[1m+++ b/app/src/main/java/com/example/books/MainActivity.kt[m
[36m@@ -22,6 +22,7 @@[m [mimport androidx.compose.runtime.mutableStateOf[m
 import androidx.compose.runtime.saveable.rememberSaveable[m
 import androidx.compose.runtime.setValue[m
 import androidx.compose.ui.graphics.vector.ImageVector[m
[32m+[m[32mimport androidx.compose.ui.tooling.preview.Preview[m
 import androidx.navigation.NavType[m
 import androidx.navigation.compose.NavHost[m
 import androidx.navigation.compose.composable[m
[36m@@ -52,6 +53,7 @@[m [mclass MainActivity : ComponentActivity() {[m
 @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")[m
 @OptIn(ExperimentalMaterial3Api::class)[m
 @Composable[m
[32m+[m[32m@Preview[m
 fun bottomNavigationBar()[m
 {[m
 [m
[36m@@ -71,7 +73,7 @@[m [mfun bottomNavigationBar()[m
             route = "1"[m
         ),[m
         BottomNavItem([m
[31m-            title = "My Books",[m
[32m+[m[32m            title = "Stats",[m
             selectedIcon = Icons.Filled.Info,[m
             unselectedIcon = Icons.Outlined.Info,[m
             route = "2"[m
[1mdiff --git a/app/src/main/java/com/example/books/SearchScreen.kt b/app/src/main/java/com/example/books/SearchScreen.kt[m
[1mindex e1d472e..ce1ecee 100644[m
[1m--- a/app/src/main/java/com/example/books/SearchScreen.kt[m
[1m+++ b/app/src/main/java/com/example/books/SearchScreen.kt[m
[36m@@ -143,11 +143,15 @@[m [mfun SearchScreen([m
                 val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,[m
                     { response ->[m
 [m
[31m-                        Log.d("url response", "Value: ${response}")[m
[32m+[m[32m                        Log.d("url response", "Value: ${url}")[m
                         //Parse JSON[m
                         //Gets an array from json in the key called "covers"[m
 [m
[32m+[m[32m//Parse JSON[m
[32m+[m[32m                        //Gets an array from json in the key called "covers"[m
[32m+[m
                         var imageId: String = "No Cover Found"[m
[32m+[m[32m                        val responseJSON: JSONArray[m
 [m
                         if(response.has("covers"))[m
                         {[m
[36m@@ -161,7 +165,7 @@[m [mfun SearchScreen([m
                         {[m
                             imageURL = "None"[m
                         }[m
[31m-                        [m
[32m+[m
                         //Display book cover ID[m
                         bookTitle = response.getString("title")[m
                         if (response.has("subjects"))[m
[36m@@ -180,8 +184,6 @@[m [mfun SearchScreen([m
                         isSavedVisible = true[m
                     },//If an error occurs in fetching the data[m
                     {[m
[31m-                        Log.d("error", "${url}")[m
[31m-                        Log.d("error", "${it}")[m
                         bookTitle = "Failed to load image"[m
                         isSavedVisible = false[m
                         toast.show()[m
[36m@@ -189,7 +191,7 @@[m [mfun SearchScreen([m
 [m
                 // Add the request to the RequestQueue.[m
                 queue.add(jsonObjectRequest)[m
[31m-                queue.start()[m
[32m+[m
             }) {[m
                 Text("Search")[m
             }[m
