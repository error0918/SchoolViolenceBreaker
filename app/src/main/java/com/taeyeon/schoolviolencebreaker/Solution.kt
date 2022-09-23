@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
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

        when (showingDialog) {
            0 -> {
                Report.Report(
                    reporter = showingDialogIndex,
                    onDismissAdditionalAction = {
                        showingDialog = null
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
                Law.ShowLaw(
                    lawIndex = showingDialogIndex,
                    onDismissAdditionalAction = {
                        showingDialog = null
                    }
                )
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
                items = Report.reporterList,
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
                items = Law.lawList.map { it.name },
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
                        bottom = if(solutionList.lastIndexOf(solution) == solutionList.size - 1) 16.dp else 0.dp
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