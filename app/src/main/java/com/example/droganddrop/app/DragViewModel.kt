package com.example.droganddrop.app

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.example.droganddrop.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DragViewModel : ViewModel() {
    private val _screenState = MutableStateFlow(DragData())
    val screenState = _screenState.asStateFlow()

    init {
        _screenState.update { it.copy(topFoodItems = topList, bottomFoodItems = bottomList) }
    }

    fun onDragEnd(dragAmount: Offset, foodItem: FoodItem) {
        Log.d("ttt", "foodItem -$foodItem")

        val shouldAddBottom = _screenState.value.topFoodItems.contains(foodItem)
        val exists = _screenState.value.topFoodItems.find { it.id==foodItem.id }
        Log.d("ttt", "shouldAddBottom -$shouldAddBottom, and $exists")

        Log.d("initial", "addBottom")
        screenState.value.topFoodItems.forEach {
            Log.d("TTT","top - $it")
        }
        Log.d("TTT","******************")

        screenState.value.bottomFoodItems.forEach {
            Log.d("TTT","bottom - $it")
        }

        if (shouldAddBottom||exists!=null) {
            Log.d("initial", "addBottom")
            Log.d("initial", "bottomRect - ${_screenState.value.bottomRect}")
            Log.d("initial", "dragAmount.y ${dragAmount.y}")
            val isInBounds = _screenState.value.bottomRect?.contains(dragAmount)
            if (isInBounds == true) {
                Log.d("initial", "Bottom Bounds")
                _screenState.update {
                    val bottomFoodItems = _screenState.value.bottomFoodItems.toMutableList()
                    val topFoodItems = _screenState.value.topFoodItems.toMutableList()
                    bottomFoodItems.add(foodItem)
                    topFoodItems.remove(foodItem)
                    it.copy(
                        bottomFoodItems = bottomFoodItems.toMutableList(),
                        topFoodItems = topFoodItems.toMutableList()
                    )
                }
            }
        } else {
            Log.d("initial", "addTop")
            Log.d("initial", "topRect - ${_screenState.value.topRect}")
            Log.d("initial", "dragAmount.y ${dragAmount.y}")
            val isInBounds = _screenState.value.topRect?.contains(dragAmount)
            if (isInBounds == true) {
                Log.d("initial", "Top Bounds")
                _screenState.update {
                    val bottomFoodItems = _screenState.value.bottomFoodItems.toMutableList()
                    val topFoodItems = _screenState.value.topFoodItems.toMutableList()
                    topFoodItems.add(foodItem)
                    bottomFoodItems.remove(foodItem)
                    it.copy(
                        topFoodItems = topFoodItems.toMutableList(),
                        bottomFoodItems = bottomFoodItems.toMutableList()
                    )
                }
            }
        }
    }

    fun setTopRect(rect: Rect) {
        Log.d("initial", "top rect $rect")
        _screenState.update { it.copy(topRect = rect) }
    }

    fun setBottomRect(rect: Rect) {
        Log.d("initial", "bottom rect $rect")
        _screenState.update { it.copy(bottomRect = rect) }
    }

}

data class DragData(
    val topFoodItems: MutableList<FoodItem> = mutableListOf(),
    val bottomFoodItems: MutableList<FoodItem> = mutableListOf(),
    val dragItem: FoodItem? = null,
    val topRect: Rect? = null,
    val bottomRect: Rect? = null,
)

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    @DrawableRes val image: Int
)

val topList = mutableListOf(
    FoodItem(1, "Pizza", 20.0, R.drawable.food_pizza),
    FoodItem(2, "French toast", 10.05, R.drawable.food_toast),
    FoodItem(3, "Chocolate cake", 12.99, R.drawable.food_cake),
)


val bottomList = mutableListOf(
    FoodItem(4, "Silencer", 20.0, R.drawable.img),
)