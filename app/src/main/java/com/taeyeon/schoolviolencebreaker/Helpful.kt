@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import com.taeyeon.core.Core

object Helpful {

    data class Helpful(
        val imageBitmap: ImageBitmap,
        val title: String,
        val description: String,
        val link: String,
    )

    private val helpfulList by lazy {
        val drawableToImageBitmap = { id: Int ->
            val drawable = ContextCompat.getDrawable(Core.getContext(), id)
            val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap.asImageBitmap()
        }

        val todo = Helpful(
            imageBitmap = drawableToImageBitmap(R.drawable.ic_launcher_round),
            title = "a",
            description = """
                     aaa
                """.trimIndent(),
            link = "https://naver.com/"
        )
        listOf(
            Helpful(
                imageBitmap = drawableToImageBitmap(R.drawable.ic_launcher_round),
                title = "학교폭력예방교육지원센터",
                description = """
                     학교폭력예방교육지원센터는 교육부의 지정을 받아 2018년 5월부터 한국청소년정책연구원의 부설센터로 운영되고 있습니다.
                     본 센터는 학교폭력 예방교육 어울림 프로그램 개발·보급·운영 및 학교폭력 예방교육 지원체계 구축을 위해 어울림 프로그램의 재구조화, 학교폭력 예방 교과연계 원격연수 프로그램 개발, 학교폭력 예방교육 프로그램 적용 효과성 분석, 학교폭력 추세 대응을 위한 교육자료 제작 및 보급 그리고 학부모를 위한 학교폭력 예방 소식지 발간 등의 사업을 수행하고 있습니다.
                     또한 학교폭력 예방을 위한 전문가 컨설팅 및 교육연수, 학생참여 학교폭력 예방활동 지원을 위한 어울림 학생서포터즈단 운영 그리고 시도 교육청 단위 학교폭력 예방 협의체 구성 및 단위 별 사업운영 지원을 통해 활발한 현장 중심의 맞춤형 사업을 수행하고 있습니다.
                     본 센터는 학생들의 자발적 참여·활동을 기반으로 지역 주도의 학교폭력 예방활동과 함께 학교 교육과정에 초점을 맞추어 학생, 교사, 학부모 및 지역 사회가 함께 참여하는 학교폭력 예방교육 사업을 추진·수행하고 있습니다.
                """.trimIndent(),
                link = "https://www.stopbullying.re.kr/mps"
            ),
            todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo, todo,
            Helpful(
                imageBitmap = drawableToImageBitmap(R.drawable.ic_launcher_round),
                title = "에듀넷·티-클리어",
                description = """
                    에듀넷·티-클리어는 약자로 교육과정과 교육정책 전반의 정보를 통합 제공하고, 협업 소통을 지원하는 교육정보 통합 지원 서비스입니다.
                """.trimIndent(),
                link = "https://doran.edunet.net/main/mainForm.do"
            )
        )
    }

    @Composable
    fun Helpful(paddingValues: PaddingValues = PaddingValues()) {
        LazyColumn(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(helpfulList) { helpful ->
                HelpfulUnit(
                    imageBitmap = helpful.imageBitmap,
                    imageBitmapDescription = helpful.title,
                    title = helpful.title,
                    description = helpful.description,
                    link = helpful.link,
                    modifier = Modifier.padding(
                        top = if(helpfulList.indexOf(helpful) == 0) 16.dp else 0.dp,
                        bottom = if(helpfulList.lastIndexOf(helpful) == helpfulList.size - 1) 16.dp else 0.dp
                    )
                )
            }
        }
    }

    @SuppressLint("ModifierParameter")
    @Composable
    fun HelpfulUnit(
        imageBitmap: ImageBitmap? = null,
        imageBitmapDescription: String? = null,
        title: String,
        description: String,
        link: String,
        modifier: Modifier = Modifier
    ) {
        val hasImage = imageBitmap != null

        Card(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        Core.getActivity().startActivity(intent)
                    }
            )
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
            var textMoreSee by rememberSaveable { mutableStateOf(false) }

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cornerRadius)
            ) {
                val (image, titleText, descriptionText, seeMoreButton) = createRefs()

                if (hasImage) {
                    var imageWidth by remember { mutableStateOf(0) }
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = imageBitmapDescription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(with (LocalDensity.current) { imageWidth.toDp() / 3 })
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .onSizeChanged { intSize ->
                                imageWidth = intSize.width
                            }
                            .background(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .constrainAs(titleText) {
                            top.linkTo(if (hasImage) image.bottom else parent.top, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = if (textMoreSee) 100 else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(descriptionText) {
                            top.linkTo(titleText.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

                Button(
                    onClick = { textMoreSee = !textMoreSee },
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .constrainAs(seeMoreButton) {
                            top.linkTo(descriptionText.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Icon(
                        imageVector = if (textMoreSee) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (textMoreSee) stringResource(id = R.string.close) else stringResource(id = R.string.open),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    }

}