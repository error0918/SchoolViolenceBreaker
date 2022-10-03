package com.taeyeon.schoolviolencebreaker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import com.taeyeon.schoolviolencebreaker.MyView.Tip
import java.util.*

object SVInfo {

    @ExperimentalMaterial3Api
    @Suppress("UNUSED_VALUE")
    @Composable
    fun SVInfo(paddingValues: PaddingValues = PaddingValues()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

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

                val tipType = Random().nextInt(tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize + helpfulListSize)
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
                    val tipIndex = tipType - (tipInformationListSize + reporterListSize + reportingListSize)
                    var actionPeople = ""
                    var action = Action.Action(name = "", content = "")
                    var finished = false
                    var totalIndex = 0
                    Action.actionCategoryList.forEach { actionCategory ->
                        totalIndex += actionCategory.actionList.size
                        if (tipIndex <= totalIndex && !finished) {
                            actionPeople = actionCategory.name
                            action = actionCategory.actionList[totalIndex - actionCategory.actionList.size]
                            finished = true
                        }
                    }
                    MyView.TipInformation(
                        title = "$actionPeople 조치: ${action.name}",
                        message = action.content
                    )
                } else if (tipType in tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize until tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize) {
                    val tipIndex = tipType - (tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize)
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
                    val tipIndex = tipType - (tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize)
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
                    val tipIndex = tipType - (tipInformationListSize + reporterListSize + reportingListSize + actionListSize + misunderstandingListSize + lawListSize + etcListSize + lawListSize)
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

            ////////////////////////////////////////////

            Card(
                modifier = Modifier.fillMaxWidth()
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
                                text = "타이틀",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )

                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "닫기" else "열기",
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )

                        }
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Surface(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(top = getCornerSize(shape = MaterialTheme.shapes.medium))
                        ) {
                            Column(
                                modifier = Modifier.padding(getCornerSize(shape = MaterialTheme.shapes.medium))
                            ) {

                                SelectionContainer {
                                    Text(
                                        text = "안녕하세요 ".repeat(10),
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }

                                val dividerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                Canvas(
                                    modifier = Modifier
                                        .padding(top = getCornerSize(shape = MaterialTheme.shapes.medium))
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
                                    TextButton(
                                        onClick = { /*TODO*/ },
                                        contentPadding = PaddingValues(horizontal = getCornerSize(shape = MaterialTheme.shapes.small)),
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    ) {
                                        Text(text = "asffffffff")
                                    }
                                    IconButton(
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Share,
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ContentCopy,
                                            contentDescription = null 
                                        )
                                    }
                                }
                            }

                        }
                    }

                }
            }

            ///////////////////////////////////////////

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                text = "정태연 ".repeat(100)
            )

        }
    }

}