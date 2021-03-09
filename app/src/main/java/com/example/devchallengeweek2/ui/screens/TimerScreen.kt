package com.example.devchallengeweek2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.devchallengeweek2.MainViewModel
import com.example.devchallengeweek2.utils.GreenProgress
import com.example.devchallengeweek2.utils.toFormattedTime

@ExperimentalAnimationApi
@Composable
fun TimerScreen(viewModel: MainViewModel = viewModel()) {
    val timeStr by viewModel.time.observeAsState((75000L).toFormattedTime())
    val progress by viewModel.progress.observeAsState(0f)
    val progressColor by viewModel.progressColor.observeAsState(GreenProgress)
    val isRunning by viewModel.isRunning.observeAsState(false)
    val lastTenSeconds by viewModel.lastTenSeconds.observeAsState(false)
    val animatedProgress: Float by animateFloatAsState(targetValue = progress)
    val timeValue = remember { mutableStateOf("30") }
    val animatedTimeSize: Int by animateIntAsState(
        targetValue = if (lastTenSeconds.not()) 60 else 80,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = keyframes {
                durationMillis = 500
                70 at 0
                80 at 200
            }
        )
    )

    Box(
        contentAlignment = Alignment.BottomStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animatedProgress)
                .background(color = Color(progressColor))
        )

        Image(
            painter = painterResource(id = R.drawable.ic_outline_alarm_24),
            contentDescription = "Alarm",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center),
            colorFilter = ColorFilter.tint(Color.LightGray),
            alpha = 0.3f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Enter Timer Value In Sec..",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                    .background(
                        color = Color(0xffb9b9b9),
                        shape = RoundedCornerShape(percent = 10)
                    ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                value = timeValue.value,
                onValueChange = {
                    timeValue.value = it
                }
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = viewModel.STR,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = timeStr,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = if (!isRunning || lastTenSeconds.not()) 60.sp else animatedTimeSize.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (isRunning) {
                        viewModel.cancelTimer()
                    } else {
                        viewModel.startTimer(timeInMs = timeValue.value.toInt() * 1000L)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isRunning.not()) Color(0xFF009688) else Color(0xFFFF5722)
                ),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                AnimatedVisibility(
                    visible = isRunning.not(),
                    enter = slideInHorizontally(
                        initialOffsetX = { -100 },
                        animationSpec = tween(
                            durationMillis = 800,
                            delayMillis = 0,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Timer",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Text(
                    text = if (isRunning.not()) "Start Timer" else "Cancel Timer",
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 17.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

