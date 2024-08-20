package com.dpm.presentation.gallery

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.depromeet.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GalleryScreen(
    onImagesSelected: (List<Uri>) -> Unit
) {
    CustomGallery(onImagesSelected)
}

@Composable
fun CustomGallery(
    onImagesSelected : (List<Uri>) -> Unit
) {
    var galleryItems by remember { mutableStateOf<List<GalleryItem>>(emptyList()) }
    val selectedItems = remember { mutableStateListOf<GalleryItem>() }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            isLoading = true
            loadImages(context) { items ->
                galleryItems = items
                isLoading = false
            }
        } else {
            Toast.makeText(context, "권한이 거절되어있어용~", Toast.LENGTH_SHORT).show()
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        checkAndRequestPermission(context, permissionLauncher) {
            loadImages(context) { items ->
                galleryItems = items
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("갤러리", style = MaterialTheme.typography.h6)
            Button(
                onClick = {
                    onImagesSelected(selectedItems.map { it.imageResource })
                }
            ) {
                Text("${selectedItems.size} 선택")
            }
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (galleryItems.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
            ) {
                item(
                    span = { GridItemSpan(3) },
                    key = "gallery_top_title"
                ) {
                    Text("갤러리 제목입니당~~", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(30.dp))
                }

                items(galleryItems) { item ->
                    GalleryItemView(
                        item = item,
                        isSelected = selectedItems.contains(item),
                        selectedIndex = selectedItems.indexOf(item) + 1,
                        onClick = {
                            if (selectedItems.contains(item)) {
                                selectedItems.remove(item)
                            } else if (selectedItems.size < 3) {
                                selectedItems.add(item)
                            }
                        }
                    )
                }
            }
        } else {
            Text("No images found", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

private fun checkAndRequestPermission(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    onPermissionGranted: () -> Unit
) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            when (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)) {
                PackageManager.PERMISSION_GRANTED -> onPermissionGranted()
                else -> permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            onPermissionGranted()
        }
        else -> {
            when (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PackageManager.PERMISSION_GRANTED -> onPermissionGranted()
                else -> permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}

fun loadImages(context: Context, onComplete: (List<GalleryItem>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val contentResolver = context.contentResolver
        val galleryItems = mutableListOf<GalleryItem>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var count = 0
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                galleryItems.add(GalleryItem(galleryItems.size, contentUri))
                count++
            }
        }
        withContext(Dispatchers.Main) {
            onComplete(galleryItems)
        }
    }
}

@Composable
fun GalleryItemView(
    item: GalleryItem,
    isSelected: Boolean,
    selectedIndex: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = item.imageResource,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Red, shape = CircleShape)
            ) {
                Text(
                    text = selectedIndex.toString(),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .border(2.dp, Color.Gray, CircleShape)
            )
        }
    }
}

data class GalleryItem(val id: Int, val imageResource: Uri)
