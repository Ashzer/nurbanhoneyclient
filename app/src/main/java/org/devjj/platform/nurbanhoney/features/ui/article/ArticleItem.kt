package org.devjj.platform.nurbanhoney.features.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.StateFlow

@Preview
@Composable
fun ArticleItemPreview() {
    RenderArticleItem("text1", "text2")
}

@Composable
fun ArticleItem(viewModel: ArticleViewModel = hiltViewModel()) {
    viewModel.articleId
}

@Composable
fun RenderArticleItem(text1: String, text2: String) {
    Box {
        Row{
            Column {
                Row {
                    TextField(value = text1, onValueChange = {})
                    TextField(value = text2, onValueChange = {})
                }
                Row {
                    TextField(value = text1, onValueChange = {})
                    TextField(value = text2, onValueChange = {})
                }
            }
            Card {
                AsyncImage(
                    model = "https://example.com/image.jpg",
                    contentDescription = "Translated description of what the image contains"
                )
            }
        }
    }
}

data class TextWidgetElements(val value: State<String?>)