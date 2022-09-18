@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

object Solution {

    @Composable
    fun Solution(paddingValues: PaddingValues = PaddingValues()) {
        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TODO
            item {
                SolutionUnit(
                    title = "타이틀",
                    subTitle = "서브타이틀",
                    items = listOf("아이템 1", "아이템 2", "아이템 3"),
                    onItemClick = { _, _ -> }
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
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
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
                                .height(IntrinsicSize.Min)
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