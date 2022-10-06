@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED", "OPT_IN_USAGE_FUTURE_ERROR")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.taeyeon.core.Core
import com.taeyeon.core.Utils
import com.taeyeon.schoolviolencebreaker.MyView.Tip
import kotlinx.coroutines.launch
import java.util.*

object SVInfo {

    data class SVInfo(
        val title: String,
        val message: String,
        val buttonTitle: String? = null,
        val onButtonClick: (() -> Unit)? = null,
        val hasButtonBar: Boolean = true
    )

    private val svInfoList = listOf(
        SVInfo(
            title = "학교폭력",
            message = "“학교폭력”이란 학교 내외에서 학생을 대상으로 발생한 상해, 폭행, 감금, 협박, 약취ㆍ유인, 명예훼손ㆍ모욕, 공갈, 강요ㆍ강제적인 심부름 및 성폭력, 따돌림, 사이버 따돌림, 정보통신망을 이용한 음란ㆍ폭력 정보 등에 의하여 신체ㆍ정신 또는 재산상의 피해를 수반하는 행위를 말한다.\n" +
                    "학교폭력예방 및 대책에 관한 법률 2조",
            hasButtonBar = true
        ),
        SVInfo(
            title = "학교폭력대책자치위원회",
            message = "학폭위에서 가해학생의 학교폭력 사실이 인정되어 징계조치를 받더라도, 이와 동시에 검찰, 경찰등 수사기관을 통한 형사처벌이 가능합니다.\n" +
                    "학교폭력이 발생하면, 그것이 아무리 경미하다 하더라도 학교에 설치된 학교폭력대책자치위원회에서 사건을 담당합니다.\n" +
                    "학교폭력대책자치위원회에서는 사건을 조사한 후 심의를 통해 학교폭력 피해학생과 학교폭력 가해학생에게 적절한 조치를 내리고, 이를 학교의 장이 이행할 것을 요청할 수 있습니다.\n" +
                    "피해학생과 가해학생 사이 또는 그 보호자 사이의 손해배상에 관련된 합의조정이 필요하거나, 자치위원회가 필요하다고 인정하는 조정사항이 있으면 자치위원회는 분쟁조정절차에 들어갑니다. 이 분쟁조정절차는 심의와 동시에 진행될 수도, 별개로 진행될 수도 있으며, 심의가 끝난 이후에 진행될 수도 있습니다.",
            hasButtonBar = true
        ),
        SVInfo(
            title = "학교폭력이 근절되지 않는 이유",
            message = "학교폭력이 근절되지 않는 이유는 가해자가 죄책감을 느끼지 못하며 학교폭력을 범죄가 아닌 놀이로 인식하기 때문이다. 가해자는 재미있어서 하는 행동이지만 피해자의 인생이 망가진다. 이 때문에 학교폭력에 대해서는 미성년자 신분을 고려하지 않고 엄격하고 무서운 형벌을 내려야만 근절된다.",
            hasButtonBar = true
        ),
        SVInfo(
            title = "가해학생 조치",
            message = "1. 피해학생에 대한 서면사과\n" +
                    "2. 피해학생 및 신고ㆍ고발 학생에 대한 접촉, 협박 및 보복행위의 금지\n" +
                    "3. 학교에서의 봉사\n" +
                    "4. 사회봉사\n" +
                    "5. 학내외 전문가에 의한 특별 교육이수 또는 심리치료\n" +
                    "6. 출석정지\n" +
                    "7. 학급교체\n" +
                    "8. 전학\n" +
                    "9. 퇴학처분\n" +
                    "학교폭력예방 및 대책에 관한 법률 제17조",
            hasButtonBar = true
        ),
        SVInfo(
            title = "관련 작품",
            message = "우상의 눈물(1980), 우리들의 일그러진 영웅[24](1987), 검정고무신 일부 에피소드, 전사의 후예(1996), 공포의 쓴맛(2000), 릴리 슈슈의 모든 것(2001), 꼬마 독재자(2002), 로스트 저지먼트: 심판받지 않은 기억(2021), 양파의 왕따 일기(2005), 여고괴담(1998), 여고괴담 3 - 여우 계단(2003), 봄비(2004), 여왕의 교실(2005), 여왕의 교실(MBC)(2013), 열혈초등학교(2008), 꽃보다 남자(한국 드라마)(2009), 파수꾼(2011), 돼지의 왕(2011), 돼지의 왕(드라마)(2022), 돈 크라이 마미(2012), 호루라기(2013), 행복을 빼앗는 괴물 폭력 (2013), 마인드 스쿨 시리즈(2013~), 방황하는 칼날(2014), 패션왕(2014), 우아한 거짓말(2014), 결계녀(2015), 후아유 - 학교 2015(2015), 앵그리맘(2015), 폭력의 법칙: 나쁜 피 두 번째 이야기(2016), 학교 2017(2017), 지렁이(영화)(2017), 자살 소년(2017), 내일(웹툰)(2017~연재 중), 내일(MBC)(2022), 사랑은 뷰티풀 인생은 원더풀(2019), 소년시절의 너(2019), 아름다운 세상(2019), 인생존망(2019~2020), 참교육(웹툰)(2020), 펜트하우스 시리즈(2020~2021), 관계의 종말(2020), 경이로운 소문(2020), 모범택시(드라마)(2021), 존잘주의(2021), 엽총소년(2021), 다크홀(드라마)(2021), 더 글로리(2022)",
            hasButtonBar = true
        ),
        SVInfo(
            title = "추가 정보",
            message = "신고 절차, 오해, 학교폭력실태조사 등의 추가 정보가 필요하신가요?",
            buttonTitle = "해결",
            onButtonClick = {
                Main.scope.launch {
                    Main.pagerState.scrollToPage(1)
                }
            },
            hasButtonBar = true
        ),
        SVInfo(
            title = "도움되는 곳",
            message = "도움을 받고 싶으신가요?",
            buttonTitle = "도움되는 곳",
            onButtonClick = {
                Main.scope.launch {
                    Main.pagerState.scrollToPage(2)
                }
            },
            hasButtonBar = true
        )
    )

    @Suppress("UNUSED_VALUE")
    @Composable
    fun SVInfo(paddingValues: PaddingValues = PaddingValues()) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                var showTip by rememberSaveable { mutableStateOf(com.taeyeon.schoolviolencebreaker.showTip) }
                var showingTip by rememberSaveable { mutableStateOf(com.taeyeon.schoolviolencebreaker.showTip) }
                if (showTip != com.taeyeon.schoolviolencebreaker.showTip) {
                    showingTip = com.taeyeon.schoolviolencebreaker.showTip
                    showTip = com.taeyeon.schoolviolencebreaker.showTip
                }

                val tipInformationList = listOf(
                    MyView.TipInformation(
                        title = "앱 버전",
                        message = com.taeyeon.core.Info.getVersionName()
                    )
                )

                AnimatedVisibility(visible = showingTip) {
                    val tipInformationListSize = tipInformationList.size
                    val reporterListSize = Report.reporterList.size
                    val reportingListSize = Report.reportingList.size
                    val actionListSize by lazy {
                        var size = 0
                        Action.actionCategoryList.forEach { actionCategory ->
                            size += actionCategory.actionList.size
                        }
                        size
                    }
                    val misunderstandingListSize = Misunderstanding.misunderstandingList.size
                    val lawListSize = Law.lawList.size
                    val etcListSize = Etc.etcList.size
                    val helpfulListSize = Helpful.helpfulList.size

                    val tipType by rememberSaveable {
                        mutableStateOf(
                            Random().nextInt(
                                tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize + helpfulListSize
                            )
                        )
                    }
                    val tipInformation = if (tipType in 0 until tipInformationListSize) {
                        tipInformationList[tipType]
                    } else if (tipType in tipInformationListSize until tipInformationListSize + reporterListSize) {
                        val tipIndex = tipType - tipInformationListSize
                        MyView.TipInformation(
                            title = "${Report.reporterList[tipIndex]} 유의사항",
                            message = Report.reporterNoticeList[tipIndex]
                        )
                    } else if (tipType in tipInformationListSize + reporterListSize until tipInformationListSize + reporterListSize + reportingListSize) {
                        val tipIndex = tipType - (tipInformationListSize + reporterListSize)
                        MyView.TipInformation(
                            title = Report.reportingList[tipIndex].title,
                            message = """
                            기관: ${Report.reportingList[tipIndex].organization}
                            담당하는 것: ${Report.reportingList[tipIndex].receptionDetails},
                            ${Report.reportingList[tipIndex].description}
                        """.trimIndent()
                        )
                    } else if (tipType in tipInformationListSize + reporterListSize + reportingListSize + actionListSize until tipInformationListSize + reporterListSize + reportingListSize + actionListSize) {
                        val tipIndex =
                            tipType - (tipInformationListSize + reporterListSize + reportingListSize)
                        var actionPeople = ""
                        var action = Action.Action(name = "", content = "")
                        var finished = false
                        var totalIndex = 0
                        Action.actionCategoryList.forEach { actionCategory ->
                            totalIndex += actionCategory.actionList.size
                            if (tipIndex <= totalIndex && !finished) {
                                actionPeople = actionCategory.name
                                action =
                                    actionCategory.actionList[totalIndex - actionCategory.actionList.size]
                                finished = true
                            }
                        }
                        MyView.TipInformation(
                            title = "$actionPeople 조치: ${action.name}",
                            message = action.content
                        )
                    } else if (tipType in tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize until tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize) {
                        val tipIndex =
                            tipType - (tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize)
                        val law = Law.lawList[tipIndex]
                        if (law.link != null) {
                            MyView.TipInformation(
                                title = law.name,
                                message = "이 법에 대해 알아보시겠습니까?",
                                actionButtonTitle = "알아보기",
                                onActionButtonClick = {
                                    openLink(law.link)
                                }
                            )
                        } else {
                            tipInformationList[Random().nextInt(tipInformationListSize)]
                        }
                    } else if (tipType in tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize until tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize) {
                        val tipIndex =
                            tipType - (tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize)
                        val etc = Etc.etcList[tipIndex]
                        if (etc.link != null) {
                            MyView.TipInformation(
                                title = etc.name,
                                message = "이 법에 대해 알아보시겠습니까?",
                                actionButtonTitle = "알아보기",
                                onActionButtonClick = {
                                    openLink(etc.link)
                                }
                            )
                        } else {
                            tipInformationList[Random().nextInt(tipInformationListSize)]
                        }
                    } else if (tipType in tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize + lawListSize until tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize + helpfulListSize) {
                        val tipIndex =
                            tipType - (tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize + lawListSize)
                        val helpful = Helpful.helpfulList[tipIndex]
                        MyView.TipInformation(
                            title = helpful.title,
                            message = helpful.description,
                            imageBitmap = helpful.imageBitmap,
                            imageBitmapBackground = helpful.imageBitmapBackground,
                            imageBitmapDescription = helpful.title,
                            actionButtonTitle = "방문하기",
                            onActionButtonClick = {
                                openLink(helpful.link)
                            }
                        )
                    } else {
                        tipInformationList[Random().nextInt(tipInformationListSize)]
                    }

                    Tip(
                        tipInformation = tipInformation.run {
                            onClose = {
                                showingTip = false
                            }
                            closeImageDescription = "닫기"
                            modifier = Modifier.padding(top = 16.dp)
                            this
                        }
                    )
                }
            }

            items(svInfoList.size) {
                SVInfoUnit(
                    svInfo = svInfoList[it],
                    modifier = Modifier.padding(bottom = if (svInfoList.size - 1 == it) 16.dp else 0.dp)
                )
            }

        }
    }

    @Composable
    fun SVInfoUnit(
        svInfo: SVInfo,
        modifier: Modifier = Modifier
    ) {
        SVInfoUnit(
            title = svInfo.title,
            message = svInfo.message,
            buttonTitle = svInfo.buttonTitle,
            onButtonClick = svInfo.onButtonClick,
            hasButtonBar = svInfo.hasButtonBar,
            modifier = modifier
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun SVInfoUnit(
        title: String,
        message: String,
        buttonTitle: String? = null,
        onButtonClick: (() -> Unit)? = null,
        hasButtonBar: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        val hasButton = buttonTitle != null && onButtonClick != null
        Card(
            modifier = modifier.then(Modifier.fillMaxWidth())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(getCornerSize(MaterialTheme.shapes.medium))
            ) {
                var isExpanded by rememberSaveable { mutableStateOf(true) }

                Surface(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.medium,
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                    ) {

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )

                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (isExpanded) stringResource(id = R.string.svinfo_fold) else stringResource(id = R.string.svinfo_unfold),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )

                    }
                }

                AnimatedVisibility(visible = isExpanded) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f).compositeOver(MaterialTheme.colorScheme.background),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(top = getCornerSize(shape = MaterialTheme.shapes.medium))
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(getCornerSize(shape = MaterialTheme.shapes.medium)),
                            modifier = Modifier.padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                        ) {

                            SelectionContainer {
                                Text(
                                    text = message,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }

                            if (hasButtonBar) {

                                val dividerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                ) {
                                    drawLine(
                                        color = dividerColor,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                                    )
                                }

                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (hasButton) {
                                        TextButton(
                                            onClick = onButtonClick!!,
                                            contentPadding = PaddingValues(horizontal = getCornerSize(shape = MaterialTheme.shapes.small)),
                                            modifier = Modifier.align(Alignment.CenterStart)
                                        ) {
                                            Text(text = buttonTitle!!)
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_SEND)
                                            intent.type = "text/plain"
                                            intent.putExtra(Intent.EXTRA_TEXT, message)
                                            val chooserIntent = Intent.createChooser(intent, "share")
                                            chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            Core.getContext().startActivity(chooserIntent)
                                        },
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Share,
                                            contentDescription = stringResource(id = R.string.svinfo_share)
                                        )
                                    }
                                    IconButton(
                                        onClick = { Utils.copy(title, message) },
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ContentCopy,
                                            contentDescription = stringResource(id = R.string.svinfo_copy)
                                        )
                                    }
                                }
                            }

                        }

                    }
                }

            }
        }
    }

}