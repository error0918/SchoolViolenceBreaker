@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.core.Core
import kotlin.random.Random

object Solution {

    data class Solution(
        val icon: ImageVector,
        val iconContentDescription: String? = null,
        val title: String,
        val subTitle: String? = null,
        val items: List<String>,
        val onItemClick: (index: Int, item: String) -> Unit
    )

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

    @Composable
    fun Solution(paddingValues: PaddingValues = PaddingValues()) {
        var showingDialog by rememberSaveable { mutableStateOf<Int?>(null) }
        var showingDialogIndex by rememberSaveable { mutableStateOf(0) }

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

        when (showingDialog) {
            0 -> {
                var reportingIndex by rememberSaveable { mutableStateOf<Int?>(null) }

                MyView.ListDialog(
                    onDismissRequest = { showingDialog = null },
                    icon = { Icon(imageVector = Icons.Filled.Warning, contentDescription = stringResource(id = R.string.report)) },
                    title = { Text(text = "${stringResource(id = R.string.report)} - ${reporterList[showingDialogIndex]}") },
                    text = { Text(text = reporterNoticeList[showingDialogIndex]) },
                    items = reportingList,
                    itemContent = { index, reporting ->
                        Box(
                            modifier = Modifier.padding(
                                top = if (index == 0) 0.dp else 8.dp,
                                bottom = if (index == reportingList.size - 1) 0.dp else 8.dp,
                            )
                        ) {
                            MyView.ItemUnit(
                                text = reporting.title,
                                onClick = { reportingIndex = index }
                            )
                        }
                    },
                    button = {
                        TextButton(onClick = { showingDialog = null }) {
                            Text(text = stringResource(id = R.string.close))
                        }
                    }
                )

                reportingIndex?.let { index ->
                    val reporting = reportingList[index]
                    MyView.BaseDialog(
                        onDismissRequest = { reportingIndex = null },
                        icon = { Icon(imageVector = if (reporting.type == ReportingType.Call) Icons.Filled.Call else if (reporting.type == ReportingType.Link) Icons.Filled.OpenInBrowser else Icons.Filled.Error, contentDescription = reporting.title) },
                        title = { Text(text = "${reporting.title} - ${reporterList[showingDialogIndex]}") },
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
            1 -> {
                // TODO
            }
            2 -> {
                // TODO
            }
            3 -> {
                // TODO
            }
            4 -> {
                // TODO
            }

        }

        val  solutionList = listOf(
            Solution(
                icon = Icons.Filled.Warning,
                iconContentDescription = "신고",
                title = "신고",
                subTitle = "당신은?",
                items = reporterList,
                onItemClick = { index, _ ->
                    showingDialog = 0
                    showingDialogIndex = index
                }
            ),
            Solution(
                icon = Icons.Filled.Handshake,
                iconContentDescription = "조치",
                title = "조치",
                items = listOf(
                    "피해자", "가해자"
                ),
                onItemClick = { index, _ ->
                    showingDialog = 1
                    showingDialogIndex = index
                }
            ),
            Solution(
                icon = Icons.Filled.Error,
                iconContentDescription = "오해",
                title = "오해",
                items = listOf(
                    "잘못된 해결법", "잘못된 상식"
                ),
                onItemClick = { index, _ ->
                    showingDialog = 1
                    showingDialogIndex = index
                }
            ),
            Solution(
                icon = Icons.Filled.Book,
                iconContentDescription = "법률",
                title = "법률",
                items = listOf(
                    "학교폭력에 관한 법률"
                ),
                onItemClick = { index, _ ->
                    showingDialog = 2
                    showingDialogIndex = index
                }
            ),
            Solution(
                icon = Icons.Filled.MoreVert,
                iconContentDescription = "기타",
                title = "기타",
                items = listOf(
                    "학교폭력 실태조사"
                ),
                onItemClick = { index, _ ->
                    showingDialog = 3
                    showingDialogIndex = index
                }
            )
        )

        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(solutionList) { solution ->
                SolutionUnit(
                    solution,
                    modifier = Modifier.padding(
                        top = if(solutionList.indexOf(solution) == 0) 16.dp else 0.dp,
                        bottom = if(solutionList.lastIndexOf(solution) == Helpful.helpfulList.size - 1) 16.dp else 0.dp
                    )
                )
            }
        }
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun SolutionUnit(
        solution: Solution,
        modifier: Modifier = Modifier
    ) {
        SolutionUnit(
            icon = solution.icon,
            iconContentDescription = solution.iconContentDescription,
            title = solution.title,
            subTitle = solution.subTitle,
            items = solution.items,
            onItemClick = solution.onItemClick,
            modifier = modifier
        )
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun SolutionUnit(
        icon: ImageVector,
        iconContentDescription: String? = null,
        title: String,
        subTitle: String? = null,
        items: List<String>,
        onItemClick: (index: Int, item: String) -> Unit,
        modifier: Modifier = Modifier
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

        Card(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cornerRadius)
            ) {
                val (indicatorIcon, titleText, subTitleText, actionList) = createRefs()

                Icon(
                    imageVector = icon,
                    contentDescription = iconContentDescription,
                    modifier = Modifier
                        .constrainAs(indicatorIcon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(if (subTitle == null) titleText.bottom else subTitleText.bottom)
                            start.linkTo(parent.start)
                        }
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .constrainAs(titleText) {
                            top.linkTo(parent.top)
                            start.linkTo(indicatorIcon.end, margin = 16.dp)
                        }
                )

                subTitle?.let {
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .constrainAs(subTitleText) {
                                top.linkTo(titleText.bottom)
                                start.linkTo(indicatorIcon.end, margin = 16.dp)
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .constrainAs(actionList) {
                            top.linkTo(indicatorIcon.bottom, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        },
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items.forEachIndexed { index, item ->
                        MyView.ItemUnit(
                            text = item,
                            onClick = { onItemClick(index, item) }
                        )
                    }

                }

            }
        }
    }

}