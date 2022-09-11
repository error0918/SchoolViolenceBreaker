package com.taeyeon.schoolviolencebreaker

import android.graphics.Point
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import kotlinx.coroutines.*

var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.FullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ScreenAlwaysOn)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DarkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DynamicColor)
var shakeToReport by mutableStateOf(true) //TODO
var shakeTime by mutableStateOf(3)
var waitTime by mutableStateOf(5)
var reportDoubleCheck by mutableStateOf(true)

var isReporting by mutableStateOf(false)

fun load() {
    Settings.loadSettings()

    fullScreenMode = Settings.settingsData.FullScreenMode
    screenAlwaysOn = Settings.settingsData.ScreenAlwaysOn
    darkMode = Settings.settingsData.DarkMode
    dynamicColor = Settings.settingsData.DynamicColor
}

fun save() {
    Settings.settingsData.FullScreenMode = fullScreenMode
    Settings.settingsData.ScreenAlwaysOn = screenAlwaysOn
    Settings.settingsData.DarkMode = darkMode
    Settings.settingsData.DynamicColor = dynamicColor

    Settings.saveSettings()
}

@Composable
fun Report() {
    var isDoubleChecking by remember { mutableStateOf(false) }

    if (isReporting) {
        isDoubleChecking = false

        Dialog(onDismissRequest = { isReporting = false }) {
            val display = Core.getActivity().windowManager.defaultDisplay
            val size = Point()
            display.getRealSize(size)
            val displayWidth = with (LocalDensity.current) { size.x.toDp() }
            Surface(
                modifier = Modifier
                    .requiredWidthIn(
                        min = if (displayWidth >= 280.dp) 280.dp else 0.dp,
                        max = if (displayWidth >= 560.dp) 560.dp else displayWidth
                    ),
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "투두",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text( 
                        text = "투두$size",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "투두${with (LocalDensity.current) { size.x.toDp() }}".repeat(30),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(6.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val remainingTime = remember { mutableStateOf(waitTime) }
                    wait(rememberCoroutineScope(), remainingTime)

                    for (i in 1..10) {
                        TextButton(
                            onClick = {
                                /*TODO*/
                                isReporting = false
                                if (reportDoubleCheck) isDoubleChecking = true
                            },
                            contentPadding = PaddingValues(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "sjfslkhfs${remainingTime.value}",
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                if (true) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                        Divider(modifier = Modifier.fillMaxWidth())
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = { isReporting = false },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(vertical = 6.dp)
                    ) {
                        Text(text = "닫기")
                    }
                }
            }
        }
    }

    if (isDoubleChecking) {
        //
    }
}

fun wait(coroutineScope: CoroutineScope, remainingTime: MutableState<Int>) {
    coroutineScope.launch {
        while (remainingTime.value > 0) {
            delay(1000)
            remainingTime.value--
        }
        if (remainingTime.value < 0) remainingTime.value = 0
    }
}

object Report {

    @Composable
    fun a() {
        //
    }

    private fun call(phone: String) {
        // TODO
    }

}