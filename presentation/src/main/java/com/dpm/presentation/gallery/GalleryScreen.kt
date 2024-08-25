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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.depromeet.presentation.R
import com.dpm.designsystem.compose.ui.SpotTheme
import com.dpm.presentation.util.MultiStyleText
import com.dpm.presentation.util.ScreenType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GalleryScreen(
    screenType: String,
    onImagesSelected: (List<Uri>) -> Unit,
    onBackPressed: () -> Unit = { }
) {
    CustomGallery(
        screenType = screenType,
        onImagesSelected = onImagesSelected,
        onBackPressed = onBackPressed
    )
}

@Composable
fun CustomGallery(
    screenType: String,
    onImagesSelected : (List<Uri>) -> Unit,
    onBackPressed: () -> Unit = { }
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
            Toast.makeText(context, "권한이 거절되어 있습니다.", Toast.LENGTH_SHORT).show()
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "갤러리",
                        style = SpotTheme.typography.subtitle02,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(start = 14.dp, top = 8.dp, bottom = 8.dp)
                            .clickable { onBackPressed() },
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left),
                        contentDescription = "back"
                    )
                },
                backgroundColor = Color.White,
                actions = {
                    Box(modifier = Modifier.width(60.dp)) {
                        if (selectedItems.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp),
                                text = "선택",
                                style = SpotTheme.typography.body02,
                            )
                        } else {
                            if (screenType == ScreenType.REVIEW.name) {
                                MultiStyleText(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 16.dp)
                                        .clickable {
                                            onImagesSelected(selectedItems.map { it.imageResource })
                                            onBackPressed()
                                        },
                                    style = SpotTheme.typography.body02,
                                    textWithColors = arrayOf(
                                        Pair(
                                            "${selectedItems.size} 선택".substring(0, 1),
                                            SpotTheme.colors.actionEnabled
                                        ),
                                        Pair(
                                            "${selectedItems.size} 선택".substring(
                                                1,
                                                "${selectedItems.size} 선택".length
                                            ), SpotTheme.colors.foregroundBodySebtext
                                        )
                                    )
                                )
                            } else {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 16.dp)
                                        .clickable {
                                            onImagesSelected(selectedItems.map { it.imageResource })
                                            onBackPressed()
                                        },
                                    text = "선택",
                                    style = SpotTheme.typography.body02,
                                    color = SpotTheme.colors.actionEnabled
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = SpotTheme.colors.actionEnabled
                )
            } else if (galleryItems.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                ) {
                    item(
                        span = { GridItemSpan(3) },
                        key = "gallery_top_title"
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = if (screenType == ScreenType.REVIEW.name) {
                                    "\uD83D\uDC40 잊지 못할 직관 후기 사진을 남겨보세요 ⚾️"
                                } else {
                                    "프로필 사진을 선택해 주세요"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                style = SpotTheme.typography.subtitle02,
                                textAlign = TextAlign.Center,
                                color = SpotTheme.colors.foregroundBodySubtitle
                            )
                            Text(
                                text = if (screenType == ScreenType.REVIEW.name) {
                                    "사진, 영상 포함 최대 3장, 15mb까지 올릴 수 있어요"
                                } else {
                                   "최대 5MB까지 올릴 수 있어요"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                style = SpotTheme.typography.body03,
                                textAlign = TextAlign.Center,
                                color = SpotTheme.colors.strokePositivePrimary
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                        }
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
            .padding(0.5.dp)
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
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .background(SpotTheme.colors.strokePositivePrimary, shape = CircleShape)
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
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .border(2.dp, Color.White, CircleShape)
                    .background(SpotTheme.colors.transferBlack01, shape = CircleShape)
            )
        }
    }
}

data class GalleryItem(val id: Int, val imageResource: Uri)
