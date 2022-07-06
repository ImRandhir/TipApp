package com.randhir.tipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val ButtonSizeModifier = Modifier.size(40.dp)

// We needed 2 buttons so we created a single one in a different file to use it twice in main
@Composable
fun RoundButton(modifier: Modifier = Modifier,
                imageVector: ImageVector,
                onClick: () -> Unit,
                tint: Color = Color.Black.copy(alpha = .8f),
                backgroundColor: Color = MaterialTheme.colors.background,
                elevation: Dp = 4.dp,
                ){


    Card(modifier = Modifier
        .padding(all = 4.dp)
        .clickable { onClick.invoke() }
        .then(ButtonSizeModifier),
            shape = CircleShape,
            backgroundColor = backgroundColor,
            elevation = elevation) {

            Icon(imageVector = imageVector, contentDescription = "Plus or Minus Icon" , tint = tint)



        }

}