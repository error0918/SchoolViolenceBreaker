package com.taeyeon.schoolviolencebreaker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.taeyeon.schoolviolencebreaker.MyView.Tip
import java.util.*

object SVInfo {

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
                    title = "알고 계셨나요?",
                    message = "학교 폭력은 진짜 나쁘답니다!"
                ),
                MyView.TipInformation(
                    title = "알고 계셨나요?",
                    message = "학교 폭력 멈춰!"
                )
            )

            AnimatedVisibility(visible = showingTip) {
                val helpfulListSize = Helpful.helpfulList.size
                val lawListSize = Law.lawList.size
                val etcListSize = Etc.etcList.size
                val tipInformationListSize = tipInformationList.size

                val tipType = Random().nextInt(helpfulListSize + lawListSize + tipInformationListSize)
                val tipInformation = if (tipType in 0 until helpfulListSize) {
                    val helpful = Helpful.helpfulList[Random().nextInt(helpfulListSize)]
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
                } else if (tipType in helpfulListSize until helpfulListSize + lawListSize) {
                    val law = Law.lawList[Random().nextInt(lawListSize)]
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
                } else if (tipType in helpfulListSize + lawListSize until helpfulListSize + lawListSize + etcListSize) {
                    val etc = Etc.etcList[Random().nextInt(etcListSize)]
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

            Text(
                modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
                text = "정태연 ".repeat(100)
            )

        }
    }

}