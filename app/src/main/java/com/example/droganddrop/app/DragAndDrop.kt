package com.example.droganddrop.app

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt


@Composable
fun DragTarget(
    modifier: Modifier = Modifier,
    foodItem: FoodItem,
    onDragEnd: (Offset, FoodItem) -> Unit
) {
    var globalPosition by remember { mutableStateOf(Offset.Zero) }//used to measure global drag amount
    var dragView by remember { mutableStateOf(Offset.Zero) }//used to drag item

    Box(modifier = modifier
        .offset { IntOffset(0, dragView.y.roundToInt()) }
        .onGloballyPositioned {
            globalPosition = it.localToWindow(Offset.Zero)
//            Log.d("initial", "onGloballyPositioned - ${it.localToWindow(Offset.Zero)}")
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragView += Offset(0f, dragAmount.y)
                    Log.d("qqqq", "currentDragOffset - $dragView")
                }, onDragEnd = {
                    Log.d("initial", "*********end**************")
                    onDragEnd(globalPosition, foodItem)
                    dragView = Offset.Zero
                    globalPosition = Offset.Zero
                }, onDragCancel = {
                    onDragEnd(globalPosition, foodItem)
                    Log.d("initial", "*********cancel**************")
                    dragView = Offset.Zero
                    globalPosition = Offset.Zero
                })
        }) {
        DragTargetView(foodItem)
    }
}

@Composable
fun BottomList(
    modifier: Modifier = Modifier,
    foodItems: List<FoodItem>,
    onDragEnd: (dragAmount: Offset, foodItem: FoodItem) -> Unit,
    onRectChange: (rect: Rect) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.Green, shape = RoundedCornerShape(12.dp))
            .onGloballyPositioned {
                it
                    .boundsInWindow()
                    .let { rect ->
                        onRectChange(rect)
                    }
            },
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        if (foodItems.isEmpty()) item { Spacer(modifier = Modifier.height(100.dp)) }
        items(foodItems) {
            DragTarget(foodItem = it, onDragEnd = onDragEnd)
        }
    }
}


@Composable
fun DragTargetView(foodItem: FoodItem) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {

            Image(
                painter = painterResource(id = foodItem.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = foodItem.name,
                    fontSize = 22.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$${foodItem.price}",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

