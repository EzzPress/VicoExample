package com.example.vicoexample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HelloChart(viewModel: MyViewModel = viewModel()) {
    val xRange = viewModel.xRange.collectAsState()
    val yRange = viewModel.yRange.collectAsState()
    var markers by remember {
        mutableStateOf(hashMapOf(0f to getMarker("Start")))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

       StyledChart(
            xRange = xRange,
            yRange = yRange,
            markers = markers,
            producer = viewModel.producer
        )
    }

}