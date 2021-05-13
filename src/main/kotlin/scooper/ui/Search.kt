package scooper.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent.get
import scooper.util.cursorHand
import scooper.util.cursorInput
import scooper.util.onHover
import scooper.viewmodels.AppsViewModel

@Composable
fun SearchBox() {
    Surface(
        Modifier.fillMaxWidth().height(80.dp),
        elevation = 3.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SearchBar()
        }
    }
}

@Composable
fun SearchBar() {
    val appsViewModel: AppsViewModel = get(AppsViewModel::class.java)
    val modifier = Modifier.border(1.dp, MaterialTheme.colors.primary, shape = MaterialTheme.shapes.medium)
        .onHover { on ->
            if (on) border(
                2.dp,
                MaterialTheme.colors.secondary,
                shape = MaterialTheme.shapes.medium
            )
        }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val state by appsViewModel.container.stateFlow.collectAsState()
        val buckets = mutableListOf("")
        buckets.addAll(state.buckets.map { it.name })

        var expand by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf(-1) }
        var bucket by remember { mutableStateOf("") }

        Spacer(Modifier.width(10.dp))

        Row(
            Modifier.cursorHand().clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { expand = true }, verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownMenu(
                expand, onDismissRequest = { expand = false },
                Modifier.width(120.dp).cursorHand(),
                offset = DpOffset(x = (-10).dp, y = 6.dp)
            ) {
                buckets.forEachIndexed() { idx, title ->
                    var hover by remember { mutableStateOf(false) }
                    DropdownMenuItem(
                        onClick = {
                            expand = false
                            selectedItem = idx
                            bucket = title
                        },
                        modifier = Modifier.sizeIn(maxHeight = 30.dp)
                            .background(color = if (hover) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.surface)
                            .onHover { hover = it }
                    ) {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (hover) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface,
                        )
                    }
                }
            }

            val color =
                if (selectedItem > 0) MaterialTheme.colors.onSurface else Color.LightGray

            Text(
                bucket.ifBlank { "Select bucket" },
                modifier = Modifier.width(100.dp),
                style = MaterialTheme.typography.body1.copy(color = color),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                Icons.TwoTone.KeyboardArrowDown,
                "",
            )
        }
        // val query = remember { mutableStateOf("") }
        var query by remember { mutableStateOf(TextFieldValue()) }
        @OptIn(ExperimentalFoundationApi::class)
        BasicTextField(
            query,
            onValueChange = { query = it },
            modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                .defaultMinSize(120.dp).fillMaxWidth(0.4f)
                .cursorInput().pointerInput(Unit) {

                },
            singleLine = true,
        )
        Button(
            onClick = {
                appsViewModel.getApps(query.text, bucket = bucket)
            },
            modifier = Modifier.padding(horizontal = 0.dp).width(100.dp).cursorHand(),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 4.dp,
                bottomEnd = 4.dp
            )
        ) {
            Icon(
                Icons.TwoTone.Search,
                "",
                modifier = Modifier.size(18.dp)
            )
            Text("Search")
        }
    }
}