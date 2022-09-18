@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

object Solution {

    data class Solution(
        val icon: ImageVector,
        val iconContentDescription: String? = null,
        val title: String,
        val subTitle: String? = null,
        val items: List<String>,
        val onItemClick: (index: Int, item: String) -> Unit
    )

    @Composable
    fun Solution(paddingValues: PaddingValues = PaddingValues()) {
        var showingDialog by rememberSaveable { mutableStateOf<Int?>(null) }
        var showingDialogIndex by rememberSaveable { mutableStateOf(0) }

        val reporter = listOf(
            "피해자", "피해자 보호자", "가해자", "가해자 보호자", "관측자"
        )

        when (showingDialog) {
            0 -> {
                MyView.BaseDialog(
                    onDismissRequest = { showingDialog = null },
                    icon = { Icon(imageVector = Icons.Filled.Warning, contentDescription = stringResource(id = R.string.report)) },
                    title = { Text(text = "${stringResource(id = R.string.report)} - ${reporter[showingDialogIndex]}") },
                    text = { Text(text = "~의 신고시 유의 사항은... 입니다") },
                    content = {
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
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f).compositeOver(MaterialTheme.colorScheme.surface),
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { }
                            ) {
                                Text(
                                    text = "투두",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(cornerRadius + 8.dp)
                                )
                            }

                        }

                    },
                    button = {
                        TextButton(onClick = { showingDialog = null }) {
                            Text(text = stringResource(id = R.string.close))
                        }
                    }
                )
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

        }

        val  solutionList = listOf(
            Solution(
                icon = Icons.Filled.Warning,
                iconContentDescription = "신고",
                title = "신고",
                subTitle = "당신은?",
                items = reporter,
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
        /*
         * 당신은?
         * -피해자
         * -피해자 보호자
         * -가해자
         * -가해자 보호자
         * -관측자
         *
         * 조치
         *
         * 법률
         *
         * 기타
         */
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
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f).compositeOver(MaterialTheme.colorScheme.surface),
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(index, item) }
                        ) {
                            Text(
                                text = item,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(cornerRadius + 8.dp)
                            )
                        }
                    }

                }

            }
        }
    }

}