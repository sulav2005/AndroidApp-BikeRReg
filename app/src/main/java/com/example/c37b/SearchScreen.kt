package com.example.c37b

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.c37b.ui.theme.Blue

@Composable
fun SearchScreen(){
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Green)
    ) {
        Text("Search screen")
    }
}