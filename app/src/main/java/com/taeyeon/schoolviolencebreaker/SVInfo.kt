package com.taeyeon.schoolviolencebreaker

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taeyeon.schoolviolencebreaker.MyView.Tip
import java.util.*

object SVInfo {

    @Composable
    fun SVInfo(paddingValues: PaddingValues = PaddingValues()) {
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 16.dp ,
                bottom = paddingValues.calculateBottomPadding() + 16.dp,
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var showTip by rememberSaveable { mutableStateOf(showTip) }
            var showingTip by rememberSaveable { mutableStateOf(showTip) }
            if (showTip != com.taeyeon.schoolviolencebreaker.showTip) {
                showingTip = com.taeyeon.schoolviolencebreaker.showTip
                showTip = com.taeyeon.schoolviolencebreaker.showTip
            }

            val tipInformation = listOf(
                MyView.TipInformation(
                    title = "알고 계셨나요?",
                    message = "학교 폭력은 진짜 나쁘답니다!",
                    onCloseButtonClick = {
                        showingTip = false
                    },
                    closeImageDescription = "닫기"
                ),
                MyView.TipInformation(
                    title = "알고 계셨나요?",
                    message = "학교 폭력 멈춰!",
                    onCloseButtonClick = {
                        showingTip = false
                    },
                    closeImageDescription = "닫기"
                )
            )

            if (showingTip) {
                val tipIndex by rememberSaveable { mutableStateOf(Random().nextInt(tipInformation.size)) }
                Tip(tipInformation[tipIndex])
            }

            var showing by rememberSaveable { mutableStateOf(false) }
            if (showing) {
                MyView.ListDialog(
                    onDismissRequest = { showing = false },
                    icon = { Icon(imageVector = Icons.Default.QuestionMark, contentDescription = null) },
                    title = { Text(text = "타이틀") },
                    itemContent = { index ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TextButton(
                                onClick = { /*TODO*/ },
                                shape = RectangleShape
                            ) {
                                Text(
                                    text = "아이템 $index",
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                    },
                    itemCount = 20,
                    button = {
                        MyView.DialogButtonRow {
                            TextButton(onClick = { showing = false }) {
                                Text(text = "버튼")
                            }
                        }
                    }
                )
            }

            Button(
                onClick = {
                    showing = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "테스트")
            }

            Text(
                modifier = Modifier.fillMaxSize(),
                text = "정태연 ".repeat(100)
            )
        }
    }

}