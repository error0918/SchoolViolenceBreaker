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
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.pager.*
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.core.Utils
import com.taeyeon.schoolviolencebreaker.ui.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private val sensorListener = object: SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent?) {
            if (sensorEvent != null && sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = sensorEvent.values[0] / SensorManager.GRAVITY_EARTH
                val y = sensorEvent.values[1] / SensorManager.GRAVITY_EARTH
                val z = sensorEvent.values[2] / SensorManager.GRAVITY_EARTH

                val accel = (x * y * z).pow(2)

                if (accel * 0.9f + accel - lastAccel > 30) {
                    val currentTime = System.currentTimeMillis()
                    if (shakeTime + 300L <= currentTime) {
                        Utils.vibrate(10)
                        if (++shake >= com.taeyeon.schoolviolencebreaker.shakeTime) {
                            if (shakeToReport) {
                                Utils.vibrate(50)
                                Report.reporter = 0
                                Report.autoReport = true
                                Report.reporting = true
                            }
                            shake = 0
                        }
                        shakeTime = currentTime
                    }
                }

                lastAccel = accel
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, p1: Int) { }
    }
    private var shakeTime = 0L
    private var shake = 0
    private var lastAccel = 0f

    @Suppress("DEPRECATION")
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

        super.onCreate(savedInstanceState)

        Core.activityCreated(this)

        Main.isNetworkConnected = getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isConnectedOrConnecting ?: false  // Check Network Connected

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            load()
            Main.scope = rememberCoroutineScope()

            if (Main.isNetworkConnected) {

                Solution.solutionList
                Helpful.helpfulList // 초기화

                sensorManager.registerListener(
                    sensorListener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
                )

                Theme {
                    Main.Main()
                    Report.ReportRunning()
                }

            } else {
                Theme {
                    Main.MainContentNetworkDisconnected()
                }
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

    override fun onPause() {
        sensorManager.unregisterListener(sensorListener)
        super.onPause()
    }

    override fun onResume() {
        sensorManager.registerListener(
            sensorListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
}

object Main {
    var isNetworkConnected by mutableStateOf(false)
    private val snackbarHostState = SnackbarHostState()
    private var position by mutableStateOf(0)
    private val data by lazy {
        listOf(
            Data(Icons.Filled.Info, Icons.Outlined.Info, Core.getContext().getString(R.string.svinfo)) { paddingValues -> SVInfo.SVInfo(paddingValues) },
            Data(Icons.Filled.ReportProblem, Icons.Outlined.ReportProblem, Core.getContext().getString(R.string.solution)) { paddingValues -> Solution.Solution(paddingValues) },
            Data(Icons.Filled.Help, Icons.Outlined.HelpOutline, Core.getContext().getString(R.string.helpful)) { paddingValues -> Helpful.Helpful(paddingValues) },
        )
    }

    lateinit var scope: CoroutineScope
    lateinit var pagerState: PagerState

    data class Data(
        val filledIcon: ImageVector,
        val outlinedIcon: ImageVector,
        val title: String,
        val composable: @Composable (PaddingValues) -> Unit
    )
    
    @Suppress("UNUSED_VALUE")
    @Composable
    fun Main() {
        pagerState = rememberPagerState(initialPage = 0)

        var permissionDeniedDialog by rememberSaveable { mutableStateOf(false) }
        var permissionLaunch by rememberSaveable { mutableStateOf(true) }

        val permissionLauncher =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    permissionDeniedDialog = false
                } else {
                    permissionDeniedDialog = true
                    scope.launch {
                        snackbarHostState.showSnackbar(Core.getContext().getString(R.string.main_call_permission_denied))
                    }
                }
            }
        LaunchedEffect(permissionLaunch) {
            if (
                ContextCompat.checkSelfPermission(Core.getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED
            ) permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            permissionLaunch = false
        }

        if (permissionDeniedDialog) {
            MyView.BaseDialog(
                onDismissRequest = {
                    permissionDeniedDialog = false
                },
                icon = { Icon(imageVector = Icons.Filled.Accessibility, contentDescription = stringResource(id = R.string.main_permission)) },
                title = { Text(text = stringResource(id = R.string.main_call_permission_denied)) },
                text = { Text(text = stringResource(id = R.string.main_call_permission_message)) },
                button = {
                    MyView.DialogButtonRow {
                        TextButton(onClick = { permissionLaunch = true }) {
                            Text(text = stringResource(id = R.string.main_call_permission_request))
                        }
                        TextButton(onClick = { permissionDeniedDialog = false }) {
                            Text(text = stringResource(id = R.string.main_call_go_to_settings))
                        }
                        TextButton(onClick = { permissionDeniedDialog = false }) {
                            Text(text = stringResource(id = R.string.close))
                        }
                    }
                }
            )
        }


        Scaffold(
            topBar = { Toolbar() },
            floatingActionButton = { Fab() },
            bottomBar = { NavigationBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->

            var showPopupTip by rememberSaveable { mutableStateOf(com.taeyeon.schoolviolencebreaker.showPopupTip) }
            var showingPopupTip by rememberSaveable { mutableStateOf(com.taeyeon.schoolviolencebreaker.showPopupTip) }
            if (showPopupTip != com.taeyeon.schoolviolencebreaker.showPopupTip) {
                showingPopupTip = com.taeyeon.schoolviolencebreaker.showPopupTip
                showPopupTip = com.taeyeon.schoolviolencebreaker.showPopupTip
            }

            val popupTipInformationList = listOf(
                MyView.PopupTipInformation(
                    message = "도움되는 곳의 카드를 클릭하고, 더블 클릭하고, 길게 클릭해보세요!",
                    hasBottomBar = true,
                    onClose = { showingPopupTip = false },
                    disappearTime = popupTipDisappearTime
                ),
                MyView.PopupTipInformation(
                    message = "설정에서 테마를 설정해보세요!",
                    hasBottomBar = true,
                    onClose = { showingPopupTip = false },
                    disappearTime = popupTipDisappearTime
                ),
                MyView.PopupTipInformation(
                    message = "팝업 팁과 팁 카드는 설정에서 끌 수 있습니다.",
                    hasBottomBar = true,
                    onClose = { showingPopupTip = false },
                    disappearTime = popupTipDisappearTime
                ),
                MyView.PopupTipInformation(
                    message = "설정에서 서브타이틀을 끄고 켜보세요!",
                    hasBottomBar = true,
                    onClose = { showingPopupTip = false },
                    disappearTime = popupTipDisappearTime
                )
            )

            val randomShowPopupTip by rememberSaveable { mutableStateOf(Random().nextInt(2) == 1) }
            if (randomShowPopupTip) {
                if(showingPopupTip) {
                    MyView.PopupTip(popupTipInformationList[Random().nextInt(popupTipInformationList.size)])
                }
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
                Report.reporter = 0
                Report.autoReport = true
                Report.reporting = true
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
            data[currentPage].composable(paddingValues)
        }
    }

    @Suppress("DEPRECATION")
    @Composable
    fun MainContentNetworkDisconnected() {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxWidth()
        ) { paddingValues ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        bottom = paddingValues.calculateBottomPadding() + 16.dp,
                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                    )
                    .fillMaxSize()
            ) {
                val (closeButton, appImage, appName, message, retry) = createRefs()

                Button(
                    onClick = { Utils.shutDownApp() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier
                        .constrainAs(closeButton) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                    Text(
                        text = stringResource(id = R.string.close),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_round),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(200.dp)
                        .constrainAs(appImage) {
                            bottom.linkTo(appName.top, margin = 8.dp)
                            centerHorizontallyTo(parent)
                        }
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .constrainAs(appName) {
                            centerTo(parent)
                        }
                )

                Text(
                    text = stringResource(id = R.string.main_network_disconnected_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = MaterialTheme.shapes.medium
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                        .constrainAs(message) {
                            top.linkTo(appName.bottom)
                            bottom.linkTo(retry.top)
                            centerHorizontallyTo(parent)
                        }
                )

                OutlinedButton(
                    onClick = {
                        isNetworkConnected = Core.getContext().getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isConnectedOrConnecting ?: false  // Check Network Connected
                        scope.launch {
                            snackbarHostState.showSnackbar(if (isNetworkConnected) Core.getContext().getString(R.string.main_network_connect_success) else Core.getContext().getString(R.string.main_network_connect_fail))
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(retry) {
                            bottom.linkTo(parent.bottom)
                            centerHorizontallyTo(parent)
                        }
                ) {
                    Text(
                        text = stringResource(id = R.string.main_network_network_retry),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

}