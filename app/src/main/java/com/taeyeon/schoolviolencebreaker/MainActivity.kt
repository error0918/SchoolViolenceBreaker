@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.pager.*
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.core.Utils
import com.taeyeon.schoolviolencebreaker.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    var shakeTime = 0L
    var shake = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofPropertyValuesHolder(
                    splashScreenView.iconView,
                    PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2f, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2f, 1f)
                ).run {
                    interpolator = AnticipateInterpolator()
                    duration = 100L
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }
        installSplashScreen()

        Core.initialize(applicationContext)
        Helpful.helpfulList // 초기화

        super.onCreate(savedInstanceState)

        Core.activityCreated(this)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager.registerListener(
            object: SensorEventListener {
                override fun onSensorChanged(sensorEvent: SensorEvent?) {
                    if (sensorEvent != null && sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        val x = sensorEvent.values[0] / SensorManager.GRAVITY_EARTH
                        val y = sensorEvent.values[1] / SensorManager.GRAVITY_EARTH
                        val z = sensorEvent.values[2] / SensorManager.GRAVITY_EARTH

                        val f = (x * y * z).pow(2)
                        val g = sqrt(f)

                        if (g > 2.7f) {
                            val currentTime = System.currentTimeMillis()
                            if (shakeTime + 500f <= currentTime) {
                                shakeTime = currentTime
                                if (++shake >= shakeTime) {
                                    if (shakeToReport) {
                                        Utils.vibrate(50)
                                        // TODO
                                    }
                                    shake = 0
                                }
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, p1: Int) { }
            },
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        setContent {
            Theme {
                //
                val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) Utils.toast("됨")
                    else Utils.toast("안됨")
                }
                SideEffect {
                    if (ContextCompat.checkSelfPermission(Core.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) launcher.launch(Manifest.permission.CALL_PHONE)
                }

                Main.Main()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Settings.applyFullScreenMode()
        Settings.applyScreenAlwaysOn()
    }

    override fun onRestart() {
        super.onRestart()
        Core.activityCreated(this)
    }
}

object Main {
    private var position by mutableStateOf(0)
    private val data by lazy {
        listOf(
            Data(Icons.Filled.Info, Icons.Outlined.Info, Core.getContext().getString(R.string.svinfo)) { paddingValues -> SVInfo.SVInfo(paddingValues) },
            Data(Icons.Filled.ReportProblem, Icons.Outlined.ReportProblem, Core.getContext().getString(R.string.solution)) { paddingValues -> Solution.Solution(paddingValues) },
            Data(Icons.Filled.Help, Icons.Outlined.HelpOutline, Core.getContext().getString(R.string.helpful)) { paddingValues -> Helpful.Helpful(paddingValues) },
        )
    }

    private lateinit var scope: CoroutineScope
    private lateinit var pagerState: PagerState

    data class Data(
        val filledIcon: ImageVector,
        val outlinedIcon: ImageVector,
        val title: String,
        val composable: @Composable (PaddingValues) -> Unit
    )
    
    @Composable
    fun Main() {
        scope = rememberCoroutineScope()
        pagerState = rememberPagerState(initialPage = 0)

        load()

        Scaffold(
            topBar = { Toolbar() },
            floatingActionButton = { Fab() },
            bottomBar = { NavigationBar() }
        ) { paddingValues ->
            var showPopupTip by rememberSaveable { mutableStateOf(com.taeyeon.schoolviolencebreaker.showPopupTip) }
            var showingPopupTip by rememberSaveable { mutableStateOf(com.taeyeon.schoolviolencebreaker.showPopupTip) }
            if (showPopupTip != com.taeyeon.schoolviolencebreaker.showPopupTip) {
                showingPopupTip = com.taeyeon.schoolviolencebreaker.showPopupTip
                showPopupTip = com.taeyeon.schoolviolencebreaker.showPopupTip
            }

            if(showingPopupTip) {
                MyView.PopupTip(
                    message = "우왕!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
                    hasBottomBar = true,
                    onClose = { showingPopupTip = false },
                    disappearTime = popupTipDisappearTime
                )
            }

            MainContent(paddingValues = paddingValues)
        }
    }

    @Composable
    fun Toolbar() {
        LargeTopAppBar(
            title = {
                Column {
                    Text(text = stringResource(id = R.string.app_name))
                    if (showSubTitle) {
                        Text(
                            text = stringResource(id = R.string.main_subtitle),
                            style = MaterialTheme.typography.titleSmall
                                .copy(
                                    fontWeight = FontWeight((MaterialTheme.typography.titleSmall.fontWeight!!.weight * 0.5f).toInt())
                                )
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        val intent = Intent(Core.getContext(), SettingsActivity::class.java)
                        Core.getActivity().startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.settings),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .zIndex(1f)
                .shadow(10.dp)
        )
    }

    @Composable
    fun Fab() {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = stringResource(id = R.string.report)
                )
            },
            text = {
                   Text(text = stringResource(id = R.string.report))
            },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            onClick = {
                //isReporting = true
            }
        )
    }

    @Composable
    fun NavigationBar() {
        NavigationBar {
            data.forEachIndexed { index, item ->
                val selected = index == position
                NavigationBarItem(
                    selected = selected && !pagerState.isScrollInProgress,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected && !pagerState.isScrollInProgress) item.filledIcon else item.outlinedIcon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }

    @Composable
    fun MainContent(paddingValues: PaddingValues) {
        HorizontalPager(
            count = data.size,
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) {
            position = this.currentPage
            data[position].composable(paddingValues)
        }
    }

}