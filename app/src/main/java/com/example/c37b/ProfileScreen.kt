package com.example.c37b

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.c37b.ui.theme.Blue
import com.example.c37b.ui.theme.PurpleGrey40

@Composable
fun ProfileScreen(){
    Column(
        modifier = Modifier.fillMaxSize().background(PurpleGrey40)
    ) {
        Text("Profile screen")
    }
}