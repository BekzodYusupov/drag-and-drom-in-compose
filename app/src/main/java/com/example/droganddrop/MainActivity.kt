package com.example.droganddrop

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.droganddrop.app.DragTarget
import com.example.droganddrop.app.DragTargetInfo
import com.example.droganddrop.app.DragTargetView
import com.example.droganddrop.app.DropTarget
import com.example.droganddrop.app.FoodItem
import com.example.droganddrop.app.LocalDragTargetInfo
import com.example.droganddrop.app.Person
import com.example.droganddrop.app.foodList
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dropTargets =
                remember { mutableStateListOf<FoodItem>(FoodItem(1, "Axe", 20.0, R.drawable.img)) }
            val dragTargets = remember { mutableStateListOf<FoodItem>() }
            val state = remember { DragTargetInfo() }

//            val dragInfo = LocalDragTargetInfo.current
//            val dragPosition = dragInfo.dragPosition
//            val dragOffset = dragInfo.dragOffset
//            var isInBound by remember { mutableStateOf(false) }


            CompositionLocalProvider(
                LocalDragTargetInfo provides state
            ) {
                Box(modifier = Modifier.fillMaxSize())
                {
                    //////
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        items(items = foodList) { food ->
                            DragTarget(modifier = Modifier, foodItem = food)
                        }
                    }
//
//                    val bgColor = if (isInBound) Color.Red else Color.Green
//                    if (isInBound && !dragInfo.isDragging) dropTargets.add(dragInfo.dataToDrop!!)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
//                            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
                            /*.onGloballyPositioned {
                                Log.d("qqqq", "onGloballyPositioned")
                                it.boundsInWindow().let { rect ->
                                        isInBound = rect.contains(dragPosition + dragOffset)
                                        Log.d("qqqq", "isInBound - $isInBound")
                                    }
                            }*/,
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
//                        items(dropTargets) {
//                            DragTargetView(it)
////
//                        }

                        item{ DropTarget(person = Person(1, "Axe", R.drawable.img)) }
                    }
                }
                //////////
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


        /*  DraggableScreen(
              modifier = Modifier
                  .fillMaxSize()
                  .background(Color.Black.copy(0.8f))
          ) {
              MainScreen(viewModel = viewModel)
          }*/
        /*  Scaffold(topBar = {
              Text(text = "Multi", color = Color.Black)
          }, content = {
              Column(
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.Center,
                  horizontalAlignment = Alignment.CenterHorizontally
              ) {
                  Multi(bgColor = Color.Green, letter = "A")
              }
          })*/
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
