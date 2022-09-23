package com.taeyeon.schoolviolencebreaker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.core.Utils
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

var fullScreenMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.FullScreenMode)
var screenAlwaysOn by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ScreenAlwaysOn)
var darkMode by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DarkMode)
var dynamicColor by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.DynamicColor)
var showPopupTip by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowPopupTip)
var popupTipDisappearTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.PopupTipDisappearTime)
var showTip by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowTip)
var shakeToReport by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShakeToReport)
var shakeTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShakeTime)
var autoReportOnMain by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.AutoReportOnMain)
var waitTime by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.WaitTime)
var reportDoubleCheck by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ReportDoubleCheck)
var showSubTitle by mutableStateOf(Settings.INITIAL_SETTINGS_DATA.ShowSubTitle)

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
    autoReportOnMain = Settings.settingsData.AutoReportOnMain
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
    Settings.settingsData.AutoReportOnMain = autoReportOnMain
    Settings.settingsData.WaitTime = waitTime
    Settings.settingsData.ReportDoubleCheck = reportDoubleCheck
    Settings.settingsData.ShowSubTitle = showSubTitle

    Settings.saveSettings()
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

    var autoReport by mutableStateOf(false)
    var reporter by mutableStateOf(0)
    var reporting by mutableStateOf(false)

    @Composable
    fun ReportRunning() {
        if (reporting) {
            Report(
                reporter = reporter,
                onDismissAdditionalAction = { reporting = false },
                autoReport = autoReport
            )
        }
    }

    @Composable
    fun Report(
        reporter: Int = 0,
        onDismissAdditionalAction: () -> Unit = {},
        autoReport: Boolean = false
    ) {
        var reporterIndex by rememberSaveable { mutableStateOf<Int?>(null) }
        var reportingIndex by rememberSaveable { mutableStateOf<Int?>(null) }

        LaunchedEffect(reporter) {
            reporterIndex = reporter
        }

        LaunchedEffect(reporterIndex) {
            if (reporterIndex == null) onDismissAdditionalAction()
        }

        reporterIndex?.let { index ->
            var leftTime by rememberSaveable { mutableStateOf(waitTime) }
            if (autoReport) {
                LaunchedEffect(leftTime) {
                    if (leftTime > 0) {
                        delay(1000)
                        leftTime--
                    } else {
                        reportingIndex = 0
                    }
                }
            }

            MyView.ListDialog(
                onDismissRequest = { reporterIndex = null },
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
                            subText = if (itemIndex == 0 && autoReport && leftTime > 0) leftTime.toString() else "",
                            onClick = { reportingIndex = itemIndex }
                        )
                    }
                },
                button = {
                    TextButton(onClick = { reporterIndex = null }) {
                        Text(text = stringResource(id = R.string.close))
                    }
                }
            )
        }

        reportingIndex?.let { index ->
            val reporting = reportingList[index]
            var leftTime by rememberSaveable { mutableStateOf(waitTime) }
            if (autoReport) {
                LaunchedEffect(leftTime) {
                    if (leftTime > 0) {
                        delay(1000)
                        leftTime--
                    } else {
                        if (reporting.type == ReportingType.Call) {
                            dial(reporting.shortcut)
                        } else if (reporting.type == ReportingType.Link) {
                            openLink(reporting.shortcut)
                        }
                        reporterIndex = null
                        reportingIndex = null
                    }
                }
            }

            MyView.BaseDialog(
                onDismissRequest = { reportingIndex = null },
                icon = { Icon(imageVector = if (reporting.type == ReportingType.Call) Icons.Filled.Call else if (reporting.type == ReportingType.Link) Icons.Filled.OpenInBrowser else Icons.Filled.Error, contentDescription = reporting.title) },
                title = { Text(text = reporting.title) },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val iconSize = with(LocalDensity.current) { MyView.DialogDefaults.TextTextStyle.fontSize.toPx().toDp() }

                        reporting.organization?.let {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                var textOverFlow by remember { mutableStateOf(false) }

                                Icon(
                                    imageVector = Icons.Filled.Workspaces,
                                    contentDescription = "기관",
                                    modifier = Modifier.size(iconSize)
                                )
                                Text(
                                    text = "기관: $it",
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    onTextLayout = { textLayoutResult ->
                                        textOverFlow = textLayoutResult.hasVisualOverflow
                                    },
                                    modifier = Modifier
                                        .clickable(
                                            enabled = textOverFlow,
                                            onClick = {
                                                Utils.toast(it)
                                            }
                                        )
                                )
                            }
                        }

                        reporting.receptionDetails?.let {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                var textOverFlow by remember { mutableStateOf(false) }

                                Icon(
                                    imageVector = Icons.Filled.QuestionMark,
                                    contentDescription = "담당하는 것",
                                    modifier = Modifier.size(iconSize)
                                )

                                Text(
                                    text = "담당하는 것: $it",
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    onTextLayout = { textLayoutResult ->
                                        textOverFlow = textLayoutResult.hasVisualOverflow
                                    },
                                    modifier = Modifier
                                        .clickable(
                                            enabled = textOverFlow,
                                            onClick = {
                                                Utils.toast(it)
                                            }
                                        )
                                )
                            }
                        }
                        Text(text = reporting.description)
                    }
                },
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        if (reporting.type == ReportingType.Call) {
                            val hasPermission = checkPermission()
                            MyView.ItemUnit(
                                text = "전화 열기",
                                subText = if (autoReport && leftTime > 0) leftTime.toString() else "",
                                onClick = {
                                    dial(reporting.shortcut)
                                    reporterIndex = null
                                    reportingIndex = null
                                }
                            )
                            MyView.ItemUnit(
                                text = "전화하기",
                                onClick = if (hasPermission)
                                    { ->
                                        call(reporting.shortcut)
                                        reporterIndex = null
                                        reportingIndex = null
                                    }
                                else null,
                                textColor = if (hasPermission) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        } else if (reporting.type == ReportingType.Link) {
                            MyView.ItemUnit(
                                text = "링크 열기",
                                subText = if (autoReport && leftTime > 0) leftTime.toString() else "",
                                onClick = {
                                    openLink(reporting.shortcut)
                                    reporterIndex = null
                                    reportingIndex = null
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

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(Core.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }

    private fun dial(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        Core.getActivity().startActivity(intent)
    }

    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
        Core.getActivity().startActivity(intent)
    }

}



object Law {

    data class Law(
        val name: String,
        val content: String,
        val link: String?
    )

    private val getRaw: (id: Int) -> String = { id ->
        val inputStream = Core.getContext().resources.openRawResource(id)
        val outputStream = ByteArrayOutputStream()
        var index: Int = inputStream.read()
        while (index != -1) {
            outputStream.write(index)
            index = inputStream.read()
        }
        inputStream.close()
        String(outputStream.toByteArray(), Charsets.UTF_8)
    }

    val lawList = listOf(
        Law(
            name = "학교폭력예방 및 대책에 관한 법률",
            content = getRaw(R.raw.act_on_the_prevention_and_measures_of_school_violence),
            link = "https://www.law.go.kr/법령/학교폭력예방및대책에관한법률"
        ),
        Law(
            name = "학교폭력예방 및 대책에 관한 법률 시행령",
            content = getRaw(R.raw.enforcement_decree_of_the_act_on_the_prevention_and_measures_of_school_violence),
            link = "https://www.law.go.kr/법령/학교폭력예방및대책에관한법률시행령"
        )
    )

    @Composable
    fun ShowLaw(
        lawIndex: Int = 0,
        onDismissAdditionalAction: () -> Unit = {}
    ) {
        var index by rememberSaveable { mutableStateOf<Int?>(null) }

        LaunchedEffect(lawIndex) {
            index = lawIndex
        }

        LaunchedEffect(index) {
            if (index == null) onDismissAdditionalAction()
        }

        index?.let {
            val law = lawList[it]

            MyView.MessageDialog(
                onDismissRequest = { index = null },
                icon = { Icon(imageVector = Icons.Filled.Book, contentDescription = null) },
                title = { Text(text = law.name) },
                text = {
                    val scrollState = rememberScrollState()
                    Surface(
                        shape = MaterialTheme.shapes.medium
                    ) {
                        val cornerRadius = MaterialTheme.shapes.medium.let {
                            var cornerRadius: Dp = 0.dp
                            val size = Size.Unspecified
                            with(LocalDensity.current) {
                                val corners = listOf(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
                                corners.forEach { corner ->
                                    cornerRadius += corner.toPx(size, this).toDp() / corners.size
                                }
                            }
                            cornerRadius
                        }

                        Column(
                            modifier = Modifier.verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(cornerRadius))
                            SelectionContainer {
                                Text(text = law.content)
                            }
                            Spacer(modifier = Modifier.height(cornerRadius))
                        }
                    }
                },
                button = {
                    MyView.DialogButtonRow {
                        TextButton(onClick = { index = null }) {
                            Text(text = stringResource(id = R.string.close))
                        }
                        TextButton(onClick = {
                            Utils.copy(law.name, law.content)
                            index = null
                        }) {
                            Text(text = stringResource(id = R.string.solution_copy))
                        }
                        law.link?.let {
                            TextButton(
                                onClick = {
                                    openLink(it)
                                    index = null
                                }
                            ) {
                                Text(text = stringResource(id = R.string.solution_browse))
                            }
                        }
                    }
                }
            )
        }
    }

}



fun openLink(link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    Core.getActivity().startActivity(intent)
}