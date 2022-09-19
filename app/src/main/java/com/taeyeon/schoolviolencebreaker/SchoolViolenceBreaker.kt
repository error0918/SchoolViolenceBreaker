package com.taeyeon.schoolviolencebreaker

import android.graphics.Point
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import kotlinx.coroutines.*
import kotlin.random.Random

var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.FullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ScreenAlwaysOn)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DarkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DynamicColor)
var showPopupTip by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowPopupTip)
var popupTipDisappearTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.PopupTipDisappearTime)
var showTip by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowTip)
var shakeToReport by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShakeToReport)
var shakeTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShakeTime)
var waitTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.WaitTime)
var reportDoubleCheck by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ReportDoubleCheck)
var showSubTitle by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowSubTitle)

var isReporting by mutableStateOf(false)

fun load() {
    Settings.loadSettings()

    fullScreenMode = Settings.settingsData.FullScreenMode
    screenAlwaysOn = Settings.settingsData.ScreenAlwaysOn
    darkMode = Settings.settingsData.DarkMode
    dynamicColor = Settings.settingsData.DynamicColor
    showPopupTip = Settings.settingsData.ShowPopupTip
    popupTipDisappearTime = Settings.settingsData.PopupTipDisappearTime
    showTip = Settings.settingsData.ShowTip
    shakeToReport = Settings.settingsData.ShakeToReport
    shakeTime = Settings.settingsData.ShakeTime
    waitTime = Settings.settingsData.WaitTime
    reportDoubleCheck = Settings.settingsData.ReportDoubleCheck
    showSubTitle = Settings.settingsData.ShowSubTitle
}

fun save() {
    Settings.settingsData.FullScreenMode = fullScreenMode
    Settings.settingsData.ScreenAlwaysOn = screenAlwaysOn
    Settings.settingsData.DarkMode = darkMode
    Settings.settingsData.DynamicColor = dynamicColor
    Settings.settingsData.ShowPopupTip = showPopupTip
    Settings.settingsData.PopupTipDisappearTime = popupTipDisappearTime
    Settings.settingsData.ShowTip = showTip
    Settings.settingsData.ShakeToReport = shakeToReport
    Settings.settingsData.ShakeTime = shakeTime
    Settings.settingsData.WaitTime = waitTime
    Settings.settingsData.ReportDoubleCheck = reportDoubleCheck
    Settings.settingsData.ShowSubTitle = showSubTitle

    Settings.saveSettings()
}

@Composable
fun Report() {
    /*
    var isDoubleChecking by remember { mutableStateOf(false) }

    if (isReporting) {
        isDoubleChecking = false

        Dialog(onDismissRequest = { isReporting = false }) {
            val size = Point()
            Core.getActivity().windowManager.defaultDisplay.getRealSize(size)
            val displayWidth = with (LocalDensity.current) { size.x.toDp() }
            Surface(
                modifier = Modifier
                    .requiredWidthIn(
                        min = if (displayWidth >= 280.dp) 280.dp else 0.dp,
                        max = if (displayWidth >= 560.dp) 560.dp else displayWidth
                    )
                    .padding(10.dp),
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
                                    style = MaterialTheme.typography.titleMedium,
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
                        Divider(
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.fillMaxWidth()
                        )
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
    }*/
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

    enum class ReportingType {
        Call, Link
    }

    data class Reporting(
        val title: String,
        val organization: String? = null,
        val receptionDetails: String? = null,
        val description: String = "$title ($organization) : $receptionDetails",
        val shortcut: String,
        val type: ReportingType
    )

    val reporterList = listOf(
        "피해자", "피해자 보호자", "가해자", "가해자 보호자", "관측자"
    )
    val reporterNoticeList = listOf(
        "피해자의 유의사항은 익명성이 보장되므로, 최대한 솔직하게 신고해야 사건이 더 원할하게 해결된다는 점입니다.",
        "피해자 보호자의 유의사항은 피해자를 고통에서 해방시키기 위하여, 피해자의 정서를 보다듬어 주고, 정확하게 신고해야 한다는 점입니다.",
        "가해자의 유의사항은 미안한 마음을 가지고, 잘못을 늬우치며, 최대한 솔직하고 정중하게 답해야 한다는 점입니다.",
        "가해자 보호자의 유의사항은 가해자의 미래와 인성을 위해서, 가해자에 대한 우대를 멀리하고 객관적으로 솔직히 답변해야 한다는 점입니다.",
        "관측자의 유의사항은 피해자를 위하여, 솔직하고 정확하게 신고해야한다는 점입니다."
    )
    val reportingList = listOf(
        Reporting(
            title = "117",
            organization = "교육부, 여성가족부, 경찰청",
            receptionDetails = "학교폭력 예방교육 및 전화·문자 상담",
            description = "학교",
            shortcut = "117",
            type = ReportingType.Call
        ),
        Reporting(
            title = "1388",
            organization = "청소년 사이버상담센터",
            receptionDetails = "청소년 가출, 학업중단, 인터넷 중독, 고민 상담",
            description = "학교",
            shortcut = "1388",
            type = ReportingType.Call
        ),
        Reporting(
            title = "02-2285-1318",
            organization = "서울시청소년상담복지센터 ",
            receptionDetails = "자녀 학교·가정생활, 특수교육 상담",
            description = "학교",
            shortcut = "0222851318",
            type = ReportingType.Call
        ),
        Reporting(
            title = "1588-9128",
            organization = "푸른나무재단",
            receptionDetails = "학교폭력 전화상담, 인터넷 상담, 개인 및 집단상담",
            description = "학교",
            shortcut = "15889128",
            type = ReportingType.Call
        ),
        Reporting(
            title = "02-3141-6191",
            organization = "탁틴내일",
            receptionDetails = "성폭력·성착취·디지털성범죄 피해상담",
            description = "학교",
            shortcut = "0231416191",
            type = ReportingType.Call
        ),
        Reporting(
            title = "044-203-6898 (교육부 학교폭력대책과)",
            organization = "교육부",
            receptionDetails = "TODO",
            description = "학교",
            shortcut = "0231416191",
            type = ReportingType.Call
        ),
        Reporting(
            title = "안전Dream 학교폭력 신고센터",
            organization = "경찰청",
            receptionDetails = "학교폭력 신고 게시판",
            description = "학교",
            shortcut = "https://www.safe182.go.kr/pot/selectRptList.do?rptTyGubun=09",
            type = ReportingType.Link
        )
    )

    @Composable
    fun Report(reporter: Int, onDismissAdditionalAction: () -> Unit = {}, autoReport: Boolean = false) {
        var reporterIndex by rememberSaveable { mutableStateOf<Int?>(null) }
        var reportingIndex by rememberSaveable { mutableStateOf<Int?>(null) }

        LaunchedEffect(reporter) {
            reporterIndex = reporter
        }

        reporterIndex?.let { index ->
            MyView.ListDialog(
                onDismissRequest = {
                    reporterIndex = null
                    onDismissAdditionalAction()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = stringResource(id = R.string.report)
                    )
                },
                title = { Text(text = "${stringResource(id = R.string.report)} - ${reporterList[index]}") },
                text = { Text(text = reporterNoticeList[index]) },
                items = reportingList,
                itemContent = { itemIndex, reporting ->
                    Box(
                        modifier = Modifier.padding(
                            top = if (itemIndex == 0) 0.dp else 8.dp,
                            bottom = if (itemIndex == reportingList.size - 1) 0.dp else 8.dp,
                        )
                    ) {
                        MyView.ItemUnit(
                            text = reporting.title,
                            onClick = { reportingIndex = itemIndex }
                        )
                    }
                },
                button = {
                    TextButton(onClick = {
                        reporterIndex = null
                        onDismissAdditionalAction()
                    }) {
                        Text(text = stringResource(id = R.string.close))
                    }
                }
            )
        }

        reportingIndex?.let { index ->
            val reporting = reportingList[index]
            MyView.BaseDialog(
                onDismissRequest = { reportingIndex = null },
                icon = { Icon(imageVector = if (reporting.type == ReportingType.Call) Icons.Filled.Call else if (reporting.type == ReportingType.Link) Icons.Filled.OpenInBrowser else Icons.Filled.Error, contentDescription = reporting.title) },
                title = { Text(text = "${reporting.title} - ${reporterList[reporterIndex!!]}") },
                text = { Text(text = "TODO") },
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        if (reporting.type == ReportingType.Call) {
                            val hasPermission = Random(3).nextInt() == 1
                            MyView.ItemUnit(
                                text = "전화 열기",
                                onClick = {
                                    /* TODO */
                                }
                            )
                            MyView.ItemUnit(
                                text = "전화하기",
                                onClick = if (hasPermission)
                                    { ->
                                        /* TODO */
                                    }
                                else null,
                                contentColor = if (hasPermission) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        } else if (reporting.type == ReportingType.Link) {
                            MyView.ItemUnit(
                                text = "링크 열기",
                                onClick = {
                                    /* TODO */
                                }
                            )
                        }

                    }
                },
                button = {
                    TextButton(onClick = { reportingIndex = null }) {
                        Text(text = stringResource(id = R.string.close))
                    }
                }
            )
        }
    }

    private fun dial(phone: String) {
        //
    }

    private fun call(phone: String) {
        //
    }

}