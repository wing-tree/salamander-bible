package com.wing.tree.salamander.bible.bible.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.wing.tree.salamander.bible.bible.viewmodel.BibleViewModel
import com.wing.tree.salamander.bible.constant.EMPTY
import com.wing.tree.salamander.bible.constant.ONE
import com.wing.tree.salamander.bible.constant.SPACE
import com.wing.tree.salamander.bible.constant.titles
import com.wing.tree.salamander.bible.extension.isOne
import com.wing.tree.salamander.bible.ui.theme.SalamanderBibleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class BibleFragment : Fragment() {
    private val viewModel by viewModels<BibleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SalamanderBibleTheme {
                    val lazyListState = rememberLazyListState()
                    val lazyPagingItems = viewModel.pager.collectAsLazyPagingItems()
                    var title by remember { mutableStateOf(EMPTY) }

                    val topAppBarSize = 56.dp
                    val topAppBarSizePixelSize = with(LocalDensity.current) {
                        topAppBarSize.roundToPx().toFloat()
                    }

                    var topAppBarOffset by remember { mutableStateOf(IntOffset(0, 0)) }

                    val nestedScrollConnection = remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                val y = topAppBarOffset.y + available.y

                                topAppBarOffset = IntOffset(
                                    0,
                                    y.coerceIn(-topAppBarSizePixelSize, 0.0F).roundToInt()
                                )

                                return Offset.Zero
                            }
                        }
                    }

                    var fontSize by remember { mutableStateOf(16.0F) }

                    Surface(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(nestedScrollConnection),
                        ) {
                            LazyColumn(
                                modifier = Modifier.pointerInput(Unit) {
                                    detectTransformGestures { _, _, zoom, _ ->
                                        fontSize *= zoom
                                        fontSize = fontSize.coerceIn(12.0F, 36.0F)
                                    }
                                },
                                state = lazyListState,
                                contentPadding = PaddingValues(start = 8.dp, top = topAppBarSize, end = 8.dp)
                            ) {
                                if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                                    item {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentWidth(Alignment.CenterHorizontally)
                                        )
                                    }
                                }

                                itemsIndexed(lazyPagingItems) { _, item ->
                                    val book = item?.book
                                    val chapter = item?.chapter
                                    val verse = item?.verse
                                    val word = item?.word

                                    title = titles[book] ?: EMPTY

                                    if (areOne(chapter, verse)) {
                                        Text(text = title)
                                    }

                                    Text(
                                        text = buildAnnotatedString {
                                            val end = if (verse.isOne) {
                                                with("$chapter") {
                                                    append(this)
                                                    length
                                                }
                                            } else {
                                                with("$verse") {
                                                    append(this)
                                                    length
                                                }
                                            }

                                            append(SPACE)

                                            addStyle(
                                                style = when(verse) {
                                                    ONE -> SpanStyle(color = Color.Cyan)
                                                    else -> SpanStyle()
                                                },
                                                start = 0,
                                                end = end
                                            )

                                            append("$word")
                                        },
                                        fontSize = fontSize.sp
                                    )
                                }

                                if (lazyPagingItems.loadState.append == LoadState.Loading) {
                                    item {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentWidth(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                            }

                            TopAppBar(
                                modifier = Modifier
                                    .height(topAppBarSize)
                                    .offset { topAppBarOffset },
                                title = {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = title
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    private fun areOne(vararg int: Int?) = int.all { it.isOne }
}