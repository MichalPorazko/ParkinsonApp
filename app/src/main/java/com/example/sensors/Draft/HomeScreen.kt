package com.example.sensors.Draft

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensors.R


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            // Header Content
            HeaderContent()

            Spacer(modifier = Modifier.height(32.dp))

            // Schedule Content
            ScheduleContent()

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                value = "",
                placeholder = {
                    Text(
                        text = "Search doctor or health issue",
                        fontWeight = FontWeight.W400,
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.patient_list_24px),
                        contentDescription = "Icon Search"
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                onValueChange = {},
            )

            // Menu Home
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            }

            // Near Content
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = "Near Doctor",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(3) {
//                    NearDoctorCard()
                }
            }
        }
    }
}

@Composable
private fun HeaderContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello,",
                fontWeight = FontWeight.W400,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Hi, Shidiq",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Image(
            modifier = Modifier.size(56.dp),
            painter = painterResource(id = R.drawable.stethoscope_24px),
            contentDescription = "Image Header Content"
        )
    }
}

@Composable
private fun ScheduleContent(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.Blue,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.chat_24px),
                    contentDescription = "Image Doctor"
                )

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Dr. Ibnu Sina",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "General Doctor",
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }

                Image(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow Next",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .alpha(0.2f),
                color = Color.White
            )

            // Schedule Time Content

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}