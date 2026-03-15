package com.example.quranoffline.ui.reciters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import com.example.quranoffline.ui.components.shimmerEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.R
import com.example.quranoffline.Reciter
import com.example.quranoffline.ui.components.ComposeReciterItem
import androidx.compose.ui.res.stringResource

@Composable
fun ReciterListScreen(
    modifier: Modifier,
    navController: NavController,
    reciterViewModel: ReciterViewModel = hiltViewModel()
) {
    val resultState by reciterViewModel.resultState.collectAsState()
    val searchQuery by reciterViewModel.searchQuery.collectAsState()

    LaunchedEffect(reciterViewModel) {
        reciterViewModel.fetchReciters()
    }


    when (resultState) {
        is RecitationsResultState.Loading -> {
            LazyColumn(modifier.fillMaxSize()) {
                repeat(10) {
                    item {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }
        }

        is RecitationsResultState.Failure -> {
            val message = (resultState as RecitationsResultState.Failure).e.message
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message ?: stringResource(R.string.error_loading_data))
            }
        }

        is RecitationsResultState.Success -> {
            val reciters = (resultState as RecitationsResultState.Success).response.reciters
            val filteredReciters = reciters.filter { 
                it.name.contains(searchQuery, ignoreCase = true) 
            }
            
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(R.string.reciters),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        text = stringResource(R.string.reciters_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    TextField(
                        value = searchQuery,
                        onValueChange = { reciterViewModel.onSearchQueryChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { 
                            Text(
                                text = stringResource(R.string.search_reciters), 
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            ) 
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search, 
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(100),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }

                items(filteredReciters) { reciter ->
                    ComposeReciterItem(reciter) {
                        navController.navigate(Reciter(reciter.id.toString()))
                    }
                }
            }
        }
    }
}
