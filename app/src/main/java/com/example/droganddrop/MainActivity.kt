package com.example.droganddrop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.droganddrop.app.BottomList
import com.example.droganddrop.app.DragTarget
import com.example.droganddrop.app.DragTargetInfo
import com.example.droganddrop.app.FoodItem
import com.example.droganddrop.app.LocalDragTargetInfo
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dragInfo = LocalDragTargetInfo.current
            val bottomListItems =
                remember { mutableStateListOf(
                    FoodItem(1, "Axe", 20.0, R.drawable.img))
                }
            val topListItems =
                remember { mutableStateListOf(
                    FoodItem(1, "Pizza", 20.0, R.drawable.food_pizza),
                    FoodItem(2, "French toast", 10.05, R.drawable.food_toast),
                    FoodItem(3, "Chocolate cake", 12.99, R.drawable.food_cake))
                }

            val state = remember { DragTargetInfo() }

            CompositionLocalProvider(
                LocalDragTargetInfo provides state
            ) {
                Box(modifier = Modifier.fillMaxSize())
                {
                    BottomList(foodItems = topListItems, modifier = Modifier.align(Alignment.TopCenter)){
                        if (!topListItems.contains(it)){
                            topListItems.add(it)
                            bottomListItems.remove(it)
                        }
                    }
                    BottomList(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        foodItems = bottomListItems
                    ) {
                        if (!bottomListItems.contains(it)) {
                            dragInfo.draggableComposable = null
                            bottomListItems.add(it)
                            topListItems.remove(it)
                        }
                    }
                }
                if (state.isDragging) {
                    var targetSize by remember { mutableStateOf(IntSize.Zero) }
                    Box(modifier = Modifier
                        .graphicsLayer {
                            val offset = (state.dragPosition + state.dragOffset)
                            translationX = offset.x.minus(targetSize.width / 2)
                            translationY = offset.y.minus(targetSize.height / 2)
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
                        }
                    ) {
                        state.draggableComposable?.invoke()
                    }
                }
            }
        }
    }
}


@Composable
fun Multi(bgColor: Color, letter: String) {
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }


    Box(modifier = Modifier
        .offset {
            Log.d("offset", "y - ${offsetY.value}")
            IntOffset(x = offsetX.value.roundToInt(), y = offsetY.value.roundToInt())

        }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                Log.d("offset", " dragAmount - ${dragAmount}")
                offsetX.value += dragAmount.x
                offsetY.value += dragAmount.y

            }
        }
        .size(80.dp)
        .clip(CircleShape)
        .background(bgColor), contentAlignment = Alignment.Center) {
        Text(
            text = letter, fontSize = 30.sp, color = Color.White
        )
    }
}
