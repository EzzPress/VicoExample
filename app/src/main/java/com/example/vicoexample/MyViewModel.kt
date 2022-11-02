package com.example.vicoexample


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MyViewModel : ViewModel() {

    private var yLow = 400f
    private var yHigh = 1200f

    private val entries: MutableList<FloatEntry> = MutableList(0) { FloatEntry(0f, 0f) }
    val producer = ChartEntryModelProducer(entries)

    private val _range: MutableStateFlow<Pair<Float, Float>> = MutableStateFlow(0.0f to 120.0f)
    val xRange = _range.asStateFlow()
    private val _yRange: MutableStateFlow<Pair<Float, Float>> = MutableStateFlow(yLow to yHigh)
    val yRange = _yRange.asStateFlow()
    val rnd = Random(System.currentTimeMillis())
    var counter = 0.0f

    init {
        viewModelScope.launch {
            while (true) {
                entries.add(FloatEntry(counter, rnd.nextFloat() * 1000))
                counter += rnd.nextFloat() // grid breaks
                //counter += 1f - grid safe
                _yRange.value = yLow to yHigh
                producer.setEntries(entries)
                delay(1000)
            }
        }
    }
}

