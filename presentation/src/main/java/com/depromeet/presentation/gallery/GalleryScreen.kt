package com.depromeet.presentation.gallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter

data class GalleryImage(val id: Long, val uri: Uri)

@Composable
fun GalleryScreen(

) {
    CustomGallery()
}


@Composable
fun CustomGallery() {
    val context = LocalContext.current
    var showGallery by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf(listOf<GalleryImage>()) }
    var galleryImages by remember { mutableStateOf(listOf<GalleryImage>()) }

    val permissions = remember {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    var permissionsGranted by remember {
        mutableStateOf(
            permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        permissionsGranted = permissionsMap.values.all { it }
        if (permissionsGranted) {
            showGallery = true
            galleryImages = loadGalleryImages(context)
        }
    }

    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            showGallery = true
            galleryImages = loadGalleryImages(context)
        }
    }

    Column {
        Button(onClick = {
            if (!permissionsGranted) {
                launcher.launch(permissions.toTypedArray())
            } else {
                showGallery = true
                galleryImages = loadGalleryImages(context)
            }
        }) {
            Text("Open Gallery")
        }

        if (permissionsGranted && showGallery) {
            GalleryGrid(
                images = galleryImages,
                selectedImages = selectedImages,
                onImageSelected = { image ->
                    selectedImages = when {
                        selectedImages.contains(image) -> selectedImages - image
                        selectedImages.size < 3 -> selectedImages + image
                        else -> selectedImages
                    }
                }
            )
        } else if (!permissionsGranted) {
            // 권한 없음
        }
    }
}

@Composable
fun GalleryGrid(
    images: List<GalleryImage>,
    selectedImages: List<GalleryImage>,
    onImageSelected: (GalleryImage) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(images) { image ->
            GalleryItem(
                image = image,
                isSelected = selectedImages.contains(image),
                selectionOrder = selectedImages.indexOf(image) + 1,
                onImageSelected = onImageSelected
            )
        }
    }
}

@Composable
fun GalleryItem(
    image: GalleryImage,
    isSelected: Boolean,
    selectionOrder: Int,
    onImageSelected: (GalleryImage) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable { onImageSelected(image) }
    ) {
        Image(
            painter = rememberImagePainter(data = image.uri),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            color = if (isSelected) Color.Blue else Color.White,
            shape = CircleShape,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            if (isSelected) {
                Text(
                    text = selectionOrder.toString(),
                    color = Color.White,
                    modifier = Modifier.wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}

fun loadGalleryImages(context: Context): List<GalleryImage> {
    val images = mutableListOf<GalleryImage>()
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATE_TAKEN
    )
    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} 내림차순"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            images.add(GalleryImage(id, contentUri))
        }
    }
    return images
}