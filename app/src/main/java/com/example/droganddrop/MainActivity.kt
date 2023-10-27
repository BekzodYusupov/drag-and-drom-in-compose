package com.example.droganddrop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.droganddrop.app.BottomList
import com.example.droganddrop.app.DragData
import com.example.droganddrop.app.DragViewModel
import com.example.droganddrop.app.FoodItem

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: DragViewModel by viewModels()
        setContent {

            val screenState by viewModel.screenState.collectAsState()

            DragAndDRopView(
                viewState = screenState,
                onDragEnd = viewModel::onDragEnd,
                setTopRect = viewModel::setTopRect,
                setBottomRect = viewModel::setBottomRect
            )
        }
    }
}

@Composable
fun DragAndDRopView(
    viewState: DragData,
    onDragEnd: (dragAmount: Offset, foodItem: FoodItem) -> Unit,
    setTopRect: (Rect)->Unit,
    setBottomRect: (Rect)->Unit,
) {


    Box(modifier = Modifier.fillMaxSize())
    {
        BottomList(
            modifier = Modifier.align(Alignment.TopCenter),
            foodItems = viewState.topFoodItems,
            onRectChange = setTopRect,
            onDragEnd = onDragEnd
        )
        BottomList(
            modifier = Modifier.align(Alignment.BottomCenter),
            foodItems = viewState.bottomFoodItems,
            onRectChange = setBottomRect,
            onDragEnd = onDragEnd
        )
    }
}
