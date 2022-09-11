package com.taeyeon.schoolviolencebreaker

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object Solution {

    @Composable
    fun Solution(paddingValues: PaddingValues = PaddingValues()) {
        //
        Text(
            modifier = Modifier.fillMaxSize(),
            text = "Solution".repeat(100)
        )
    }

}