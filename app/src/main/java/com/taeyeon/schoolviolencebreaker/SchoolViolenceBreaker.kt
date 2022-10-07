@file:OptIn(ExperimentalAnimationApi::class)
@file:Suppress(
    "OPT_IN_IS_NOT_ENABLED",
    "EXPERIMENTAL_IS_NOT_ENABLED"
)

package com.taeyeon.schoolviolencebreaker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import com.taeyeon.core.Core
import com.taeyeon.core.Settings
import com.taeyeon.core.Utils
import kotlinx.coroutines.delay
import org.jsoup.Jsoup
import java.io.ByteArrayOutputStream
import java.net.URL

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
        "피해자", "피해자 보호자", "가해자", "가해자 보호자", "목격자"
    )
    val reporterNoticeList = listOf(
        "피해자의 유의사항은 익명성이 보장되므로, 최대한 솔직하게 신고해야 한다는 점입니다.",
        "피해자 보호자의 유의사항은 피해자를 고통에서 해방시키기 위하여, 피해자의 정서를 보다듬어 주고, 정확하게 신고해야 한다는 점입니다.",
        "가해자의 유의사항은 사죄하는 마음을 가지고, 잘못을 늬우치며, 최대한 솔직하게 답해야 한다는 점입니다.",
        "가해자 보호자의 유의사항은 가해자에 대한 우대를 멀리하고 객관적으로 솔직히 답변해야 한다는 점입니다.",
        "목격자의 유의사항은 피해자를 위하여, 정확하게 신고해야한다는 점입니다."
    )
    val reportingList = listOf(
        Reporting(
            title = "117",
            organization = "교육부, 여성가족부, 경찰청",
            receptionDetails = "학교폭력 예방교육 및 전화·문자 상담",
            description = "경찰청 117학교·여성폭력 및 성매매피해 신고센터에서는 전국에서 발생되는 학교폭력, 가정폭력, 성폭력 및 성매매 피해자 신고를 접수하여, 즉시 긴급구조, 수사지시, 법률상담, One-Stop 또는 NGO 단체 연계 업무를 합니다.",
            shortcut = "117",
            type = ReportingType.Call
        ),
        Reporting(
            title = "1388",
            organization = "청소년 사이버상담센터",
            receptionDetails = "청소년 가출, 학업중단, 인터넷 중독, 고민 상담",
            description = "청소년사이버상담센터는 지난 10여 년간 수십만 명의 청소년들과 함께 아래와 같이 성장해 왔습니다. 1999년, PC 보급과 인터넷 발전이라는 시대적 상황에 발맞춰 우리나라 사이버상담의 일대 도약의 문을 열게 되었습니다. 2011년, 청소년사이버상담센터의 개소로 보다 많은 청소년들이 이용할 수 있도록 접근성을 높였으며, 최근에는 누리소통망(SNS)의 확산과 비대면 일상생활의 사회적 현상을 고려한 찾아가는 상담(사이버아웃리치)과 카카오톡 및 문자 메시지상담까지 확대 운영하고 있습니다.\n",
            shortcut = "1388",
            type = ReportingType.Call
        ),
        Reporting(
            title = "02-2285-1318",
            organization = "서울시청소년상담복지센터",
            receptionDetails = "자녀 학교·가정생활, 특수교육 상담",
            description = "서울시 청소년안전망은 지역사회 내 청소년 관련 기관 및 시설의 상호연계를 통해 맞춤형 서비스를 지원하고 있습니다.",
            shortcut = "0222851318",
            type = ReportingType.Call
        ),
        Reporting(
            title = "1588-9128",
            organization = "푸른나무재단",
            receptionDetails = "학교폭력 전화상담, 인터넷 상담, 개인 및 집단상담",
            description = "학교폭력으로 고통 받는 청소년, 부모님, 학교 선생님들께 푸른나무재단에서 \"구원의 팔\"을 내밀어 드립니다. 푸른나무재단에서는 1995년부터 전국 학교폭력 상담전화 1588-9128(구원의팔)을 운영하고 있습니다. 학교폭력 피·가해 위기상담 및 심리·정서적 지원, 학교폭력 발생 시 대처 및 해결을 위한 조언, 전문 정보제공, 학교폭력 사안처리에 대한 고충 상담을 제공합니다.",
            shortcut = "15889128",
            type = ReportingType.Call
        ),
        Reporting(
            title = "02-3141-6191",
            organization = "탁틴내일 | 아동청소년성폭력상담소",
            receptionDetails = "성폭력·성착취·디지털성범죄 피해상담",
            description = "여성과 아동, 청소년이 스스로 중심이 되어 주체성을 가지고 자신의 꿈을 현실화하고, 봉사로서 사회를 더욱 건강하고 밝게 변화시키는 장이 되고자 1995년 3월 1일 창립한 여성과 아동, 청소년을 위한 사회단체로 청소년 성상담 및 성교육 활동, 청소년 문화사업, 학교 폭력예방활동 우리농산물 학교급식운동, 청소년자원봉사활동, 그리고 임산부 기체조 운동 양성평등 가족만들기 운동 등을 진행하고 있습니다.",
            shortcut = "0231416191",
            type = ReportingType.Call
        ),
        Reporting(
            title = "안전Dream 학교폭력 신고센터",
            organization = "경찰청",
            receptionDetails = "학교폭력 신고 게시판",
            description = "경찰청 117학교·여성폭력 및 성매매피해 신고센터에서는 전국에서 발생되는 학교폭력, 가정폭력, 성폭력 및 성매매 피해자 신고를 접수하여, 즉시 긴급구조, 수사지시, 법률상담, One-Stop 또는 NGO 단체 연계 업무를 합니다.",
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
                        if (itemIndex == 0 && autoReport) {
                            MyView.ItemUnit(
                                content = {
                                    ConstraintLayout(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        val (t, st) = createRefs()

                                        Text(
                                            text = reporting.title,
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.bodyLarge,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.constrainAs(t) {
                                                top.linkTo(parent.top)
                                                bottom.linkTo(parent.bottom)
                                                start.linkTo(parent.start)
                                                end.linkTo(st.start, margin = 8.dp)
                                                width = Dimension.fillToConstraints
                                            }
                                        )


                                        Box(
                                            modifier = Modifier.constrainAs(st) {
                                                top.linkTo(parent.top)
                                                bottom.linkTo(parent.bottom)
                                                end.linkTo(parent.end)
                                                height = Dimension.fillToConstraints
                                            }
                                        ) {
                                            AnimatedContent(
                                                targetState = leftTime,
                                                transitionSpec = {
                                                        slideInVertically { height -> -height } + fadeIn() with
                                                                slideOutVertically { height -> height } + fadeOut()
                                                }
                                            ) {
                                                Text(
                                                    text = if (leftTime <= 0) "" else leftTime.toString(),
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }

                                    }
                                },
                                onClick = { reportingIndex = 0 },
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f).compositeOver(MaterialTheme.colorScheme.background)
                            )
                        } else {
                            MyView.ItemUnit(
                                text = reporting.title,
                                onClick = { reportingIndex = itemIndex }
                            )
                        }
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
                            if (autoReport) {
                                MyView.ItemUnit(
                                    content = {
                                        ConstraintLayout(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            val (t, st) = createRefs()

                                            Text(
                                                text = "전화 열기",
                                                color = MaterialTheme.colorScheme.primary,
                                                style = MaterialTheme.typography.bodyLarge,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.constrainAs(t) {
                                                    top.linkTo(parent.top)
                                                    bottom.linkTo(parent.bottom)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(st.start, margin = 8.dp)
                                                    width = Dimension.fillToConstraints
                                                }
                                            )


                                            Box(
                                                modifier = Modifier.constrainAs(st) {
                                                    top.linkTo(parent.top)
                                                    bottom.linkTo(parent.bottom)
                                                    end.linkTo(parent.end)
                                                    height = Dimension.fillToConstraints
                                                }
                                            ) {
                                                AnimatedContent(
                                                    targetState = leftTime,
                                                    transitionSpec = {
                                                        slideInVertically { height -> -height } + fadeIn() with
                                                                slideOutVertically { height -> height } + fadeOut()
                                                    }
                                                ) {
                                                    Text(
                                                        text = if (leftTime <= 0) "" else leftTime.toString(),
                                                        color = MaterialTheme.colorScheme.secondary,
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )
                                                }
                                            }

                                        }
                                    },
                                    onClick = {
                                        dial(reporting.shortcut)
                                        reporterIndex = null
                                        reportingIndex = null
                                    },
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f).compositeOver(MaterialTheme.colorScheme.background)
                                )
                            } else {
                                MyView.ItemUnit(
                                    text = "전화 열기",
                                    onClick = {
                                        dial(reporting.shortcut)
                                        reporterIndex = null
                                        reportingIndex = null
                                    }
                                )
                            }
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



object Action {

    data class ActionCategory(
        val name: String,
        val actionList: List<Action>
    )

    data class Action(
        val name: String,
        val content: String
    )

    val actionCategoryList = listOf(
        ActionCategory(
            name = "피해자",
            actionList = listOf(
                Action(
                    name = "심리상담 및 조언",
                    content = "학교폭력으로 받은 정신적·심리적 충격으로부터 회복할 수 있도록 하기 위해 학교 내의 전문상담교사나 학교폭력 관련 기관의 전문가에게 심리상담 및 조언을 받도록 함"
                ),
                Action(
                    name = "일시보호",
                    content = "지속적인 학교폭력이나 보복의 우려가 있는 경우에 청소년 쉼터, 피해학생보호센터 등에서 일시적으로 보호를 받을 수 있도록 함"
                ),
                Action(
                    name = "치료 및 치료를 위한 요양",
                    content = "학교폭력으로 발생한 신체적·정신적 피해를 치료하기 위해서 학교에 출석하지 않고 의료기관 등에서 치료를 받거나 요양할 수 있도록 함 (가해자 비용 부담)"
                ),
                Action(
                    name = "학급교체",
                    content = "지속적인 학교폭력의 불안감에서 벗어나도록 하기 위해서 피해학생을 동일 학교 내의 다른 학급으로 옮기도록 함"
                ),
                Action(
                    name = "그 밖에 피해학생의 보호를 위해 필요한 조치",
                    content = "피해학생의 보호를 위해 필요하다고 판단되는 조치를 실시함"
                ),
                Action(
                    name = "장애학생 보호",
                    content = "자치위원회는 학교폭력으로 피해를 입은 장애학생의 보호를 위해서 장애인전문 상담가의 상담 또는 장애인전문 치료기관의 요양 조치를 학교의 장에게 요청할 수 있음"
                )
            )
        ),
        ActionCategory(
            name = "가해자",
            actionList = listOf(
                Action(
                    name = "피해학생에 대한 서면사과",
                    content = "가해학생이 피해학생에게 사과편지 등을 써서 화해할 수 있도록 함"
                ),
                Action(
                    name = "피해학생 및 신고·고발 학생에 대한 접촉, 협박 및 보복행위의 금지",
                    content = "가해학생이 피해학생 및 신고·고발 학생에게 접근하는 것을 막아 폭력이나 협박, 보복행위를 더 이상 할 수 없도록 함"
                ),
                Action(
                    name = "교내봉사",
                    content = "교내 청소, 교사업무보조 등 교내에서 일정시간 동안 봉사하도록 함"
                ),
                Action(
                    name = "사회봉사",
                    content = "지역의 교통안내, 요양기관 봉사, 지역 청소 등 교외에서 일정시간 동안 봉사하도록 함"
                ),
                Action(
                    name = "학내외 전문가에 의한 특별 교육이수 또는 심리치료",
                    content = "교내의 전문상담교사나 교외의 학교폭력 관련 전문가에게 특별 교육을 이수하게 하거나 심리치료를 받도록 함"
                ),
                Action(
                    name = "출석정지",
                    content = "피해학생과 가해학생을 격리시켜 피해학생을 보호하고 가해학생이 반성할 수 있도록 가해학생이 학교에 출석하지 못하게 함"
                ),
                Action(
                    name = "학급교체",
                    content = "가해학생을 피해학생으로부터 격리시켜 더 이상의 폭력행위를 막고 피해학생이 불안감을 느끼지 않도록 가해학생을 동일 학교 내의 다른 학급으로 옮기도록 함 [4]"
                ),
                Action(
                    name = "전학",
                    content = "지속적인 폭력행위가 단절될 수 있도록 가해학생이 다른 학교에 강제로 전학을 가도록 함"
                ),
                Action(
                    name = "퇴학",
                    content = "학생의 신분을 상실시켜 교육을 받을 수 없도록 함 "
                )
            )
        ),
        ActionCategory(
            name = "목격자, 주변 인물",
            actionList = listOf(
                Action(
                    name = "정서적 반응",
                    content = "학교폭력을 목격하거나 폭력 현장에 있음으로 인해 심리적·정서적 충격을 받은 간접 피해자도 유사한 문제 반응이 나타날 수 있습니다."
                ),
                Action(
                    name = "심리적 완화",
                    content = "주변학생들의 현장 접근을 통제하고, 특히 초등학교 저학년의 경우 동화책 읽어주기, 종이접기 등 흥미 있는 활동으로 주의를 돌려 심리적 충격을 완화시킵니다."
                ),
                Action(
                    name = "주변인에게 상황 인식",
                    content = "사안에 관련된 학생 및 목격한 학생들에게 상황을 인식시키고, 차후 유사한 폭력상황이 벌어지지 않도록 예방교육을 합니다."
                ),
                Action(
                    name = "낙인 금지",
                    content = "사안에 관련된 학생들에 대해 낙인을 찍어 따돌리거나, 사안과 관련하여 사실과 다른 소문을 퍼뜨리지 않도록 주의시킵니다.사안에 관련된 학생들에 대해 낙인을 찍어 따돌리거나, 사안과 관련하여 사실과 다른 소문을 퍼뜨리지 않도록 주의시킵니다."
                )
            )
        )
    )

    @Composable
    fun ShowActionCategory(
        actionCategoryIndex: Int = 0,
        onDismissAdditionalAction: () -> Unit
    ) {
        var index by rememberSaveable { mutableStateOf<Int?>(null) }
        var actionIndex by rememberSaveable { mutableStateOf<Int?>(null) }

        LaunchedEffect(actionCategoryIndex) {
            index = actionCategoryIndex
        }

        LaunchedEffect(index) {
            if (index == null) onDismissAdditionalAction()
        }

        index?.let {
            val actionCategory = actionCategoryList[it]

            MyView.ListDialog(
                onDismissRequest = { index = null },
                icon = { Icon(imageVector = Icons.Filled.Handshake, contentDescription = actionCategory.name) },
                title = { Text(text = actionCategory.name) },
                items = actionCategory.actionList,
                itemContent = { itemIndex, action ->
                    Box(
                        modifier = Modifier.padding(
                            top = if (itemIndex == 0) 0.dp else 8.dp,
                            bottom = if (itemIndex == actionCategory.actionList.size - 1) 0.dp else 8.dp,
                        )
                    ) {
                        MyView.ItemUnit(
                            text = action.name,
                            onClick = { actionIndex = itemIndex }
                        )
                    }
                },
                button = {
                    TextButton(onClick = { index = null }) {
                        Text(text = stringResource(id = R.string.close))
                    }
                }
            )

            actionIndex?.let {
                ShowAction(
                    action = actionCategory.actionList[actionIndex!!],
                    onDismissRequest = { actionIndex = null }
                )
            }
        }
    }

    @Composable
    fun ShowAction(
        action: Action,
        onDismissRequest: () -> Unit
    ) {
        MyView.MessageDialog(
            onDismissRequest = onDismissRequest,
            icon = { Icon(imageVector = Icons.Filled.Handshake, contentDescription = null) },
            title = { Text(text = action.name) },
            text = {
                val scrollState = rememberScrollState()
                Surface(
                    shape = MaterialTheme.shapes.medium
                ) {
                    val cornerRadius = getCornerSize(MaterialTheme.shapes.medium)

                    Column(
                        modifier = Modifier.verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(cornerRadius))
                        SelectionContainer {
                            Text(text = action.content)
                        }
                        Spacer(modifier = Modifier.height(cornerRadius))
                    }
                }
            },
            button = {
                MyView.DialogButtonRow {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.close))
                    }
                    TextButton(onClick = {
                        Utils.copy(action.name, action.content)
                        onDismissRequest()
                    }) {
                        Text(text = stringResource(id = R.string.solution_copy))
                    }
                }
            }
        )
    }

}



object Misunderstanding {

    data class Misunderstanding(
        val name: String,
        val content: String
    )

    val misunderstandingList = listOf(
        Misunderstanding(
            name = "학교폭력의 심각성",
            content = """
                교사의 40.6%는 '학교폭력의 수준이 심각하지 않다.'라고 생각했고 학생의 33.7%가 '학교폭력의 수준이 심각하지 않다.'라고 생각했다.
                이는 교사와 학생이 학교폭력에 대한 심각성을 다르게 인식함을 나타낸다.
            """.trimIndent()
        ),
        Misunderstanding(
            name = "학업성취도와 관련성?",
            content = """
                흔히 학교폭력은 교육 환경이 낮은 곳에서 발생한다고 생각할 수 있으나, 실상은 이와 다르게 교육 환경이 좋은 지역에서도 학교폭력이 빈번하게 발생한다.
                교육 환경이 낮은 곳에서는 폭력적으로 일어나는 경우가 많고, 교육 환경이 좋은 지역에서는 학교폭력을 은폐하는 경우가 많기 때문에 이런 오해가 발생하는 것이다.
            """.trimIndent()
        ),
        Misunderstanding(
            name = "학창시절에만 발생하는가?",
            content = """
                학교폭력예방 및 대책에 관한 법률 제2조 1항, 2항에 따르면, 초ㆍ중등교육법상의 학교에서 일어난 사건 만을 의미한다.
            """.trimIndent()
        ),
        Misunderstanding(
            name = "피해자에게도 문제가 있을까?",
            content = """
                피해자에게 문제가 없어도 학교폭력은 일어날 수 있으며, 학교폭력 피해자에게 문제가 있든 없든 간에 학교폭력은 정당화될 수 없다.
                피해자에게 문제가 있을 수 있다. 설령 그렇다 해도 폭행의 가해자가 되어서는 안 된다.
            """.trimIndent()
        ),
        Misunderstanding(
            name = "가해자는 학생뿐인가?",
            content = """
                학교폭력이란 학생을 대상으로 일어나는 폭력행위를 말한다. 따라서 학교폭력의 가해자는 학생 뿐만 아니라, 학교, 교사와 같은 주체도 될 수 있다.
            """.trimIndent()
        )
    )

    @Composable
    fun ShowMisunderstanding(
        misunderstandingIndex: Int = 0,
        onDismissAdditionalAction: () -> Unit = {}
    ) {
        var index by rememberSaveable { mutableStateOf<Int?>(null) }

        LaunchedEffect(misunderstandingIndex) {
            index = misunderstandingIndex
        }

        LaunchedEffect(index) {
            if (index == null) onDismissAdditionalAction()
        }

        index?.let {
            val misunderstanding = misunderstandingList[it]

            MyView.MessageDialog(
                onDismissRequest = { index = null },
                icon = { Icon(imageVector = Icons.Filled.Error, contentDescription = null) },
                title = { Text(text = misunderstanding.name) },
                text = {
                    val scrollState = rememberScrollState()
                    Surface(
                        shape = MaterialTheme.shapes.medium
                    ) {
                        val cornerRadius = getCornerSize(MaterialTheme.shapes.medium)

                        Column(
                            modifier = Modifier.verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            SelectionContainer {
                                Text(text = misunderstanding.content)
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
                    }
                }
            )
        }
    }

}



object Law {

    data class Law(
        val name: String,
        val content: String,
        val link: String?
    )

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
                        val cornerRadius = getCornerSize(MaterialTheme.shapes.medium)

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



object Etc {

    data class Etc(
        val name: String,
        val content: String,
        val link: String? = null
    )

    @Suppress("RedundantLambdaOrAnonymousFunction")
    val etcList by lazy {
        listOf(
            Etc(
                name = "학교폭력 실태조사",
                content = {
                    var result = ""
                    val thread = Thread {
                        result = Jsoup.connect("http://survey.eduro.go.kr").get()
                            .select("#contents > div.top_txt > p.top_mg.fw7.fc_n.text_l.tablet > span")
                            .text()
                    }
                    thread.start()
                    thread.join()
                    result
                }(),
                link = "http://survey.eduro.go.kr"
            )
        )
    }

    @Composable
    fun ShowEtc(
        etcIndex: Int = 0,
        onDismissAdditionalAction: () -> Unit = {}
    ) {
        var index by rememberSaveable { mutableStateOf<Int?>(null) }

        LaunchedEffect(etcIndex) {
            index = etcIndex
        }

        LaunchedEffect(index) {
            if (index == null) onDismissAdditionalAction()
        }

        index?.let {
            val etc = etcList[it]

            MyView.MessageDialog(
                onDismissRequest = { index = null },
                icon = Icons.Filled.MoreVert,
                title = etc.name,
                text = etc.content,
                dismissButtonText = stringResource(id = R.string.close),
                confirmButtonText = if (etc.link != null) stringResource(id = R.string.solution_browse) else null,
                onConfirmButtonClick = if (etc.link != null) {
                    {
                        openLink(etc.link)
                        index = null
                    }
                } else null
            )
        }
    }

}



@Composable
fun getCornerSize(shape: CornerBasedShape): Dp {
    var cornerRadius: Dp = 0.dp
    shape.let {
        val size = Size.Unspecified
        with(LocalDensity.current) {
            val corners = listOf(it.topStart, it.topEnd, it.bottomStart, it.bottomEnd)
            corners.forEach { corner ->
                cornerRadius += corner.toPx(size, this).toDp() / corners.size
            }
        }
    }
    return cornerRadius
}

fun getRaw(id: Int): String {
    val inputStream = Core.getContext().resources.openRawResource(id)
    val outputStream = ByteArrayOutputStream()
    var index: Int = inputStream.read()
    while (index != -1) {
        outputStream.write(index)
        index = inputStream.read()
    }
    inputStream.close()
    return String(outputStream.toByteArray(), Charsets.UTF_8)
}

fun getImageFromWeb(link: String): Bitmap {
    var bitmap: Bitmap? = null
    val thread = Thread {
        bitmap = BitmapFactory.decodeStream(
            URL(link).openConnection().inputStream
        )
    }
    thread.start()
    thread.join()
    return bitmap!!
}

fun openLink(link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    Core.getActivity().startActivity(intent)
}