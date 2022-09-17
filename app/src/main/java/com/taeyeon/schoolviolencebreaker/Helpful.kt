@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.taeyeon.schoolviolencebreaker

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.taeyeon.core.Core
import com.taeyeon.core.SharedPreferencesManager
import com.taeyeon.core.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.net.URL
import kotlin.math.roundToInt

object Helpful {

    data class Helpful(
        val imageBitmap: ImageBitmap,
        val imageBitmapBackground: Color? = null,
        val title: String,
        val description: String,
        val link: String,
    )

    val helpfulList by lazy {
        val getImageFromWeb = { link: String ->
            var bitmap: Bitmap? = null
            val thread = Thread {
                bitmap = BitmapFactory.decodeStream(
                    URL(link).openConnection().inputStream
                )
            }
            thread.start()
            thread.join()
            bitmap!!
        }

        val getImage = { link: String ->
            val getStringFromBitmap = { bitmap: Bitmap ->
                val byteArrayBitmapStream = ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.PNG, 100,
                    byteArrayBitmapStream
                )
                val b = byteArrayBitmapStream.toByteArray()
                Base64.encodeToString(b, Base64.DEFAULT)
            }

            val getBitmapFromString = { string: String ->
                val decodedString = Base64.decode(string, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            }

            val sharedPreferencesManagerName = "LINK_IMAGE"
            val sharedPreferencesManager = SharedPreferencesManager(sharedPreferencesManagerName)

            val defaultBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            var bitmap = defaultBitmap
            if (sharedPreferencesManager.contains(link)) {
                bitmap = getBitmapFromString(sharedPreferencesManager.getString(link, getStringFromBitmap(defaultBitmap)))
            }
            if (!sharedPreferencesManager.contains(link) || bitmap == defaultBitmap) {
                bitmap = getImageFromWeb(link)
                sharedPreferencesManager.putString(link, getStringFromBitmap(bitmap))
            }
            bitmap.asImageBitmap()
        }

        listOf(
            Helpful(
                imageBitmap = getImage("https://www.police.go.kr/resources/common/images/sub/txt_kcc.png"),
                imageBitmapBackground = Color.White,
                title = "경찰청",
                description = """
                     대한민국의 국가경찰기관. 치안경찰에 관한 사무를 총괄하기 위해 행정안전부 장관 소속 아래에 둔 기관.
                """.trimIndent(),
                link = "https://www.police.go.kr/index.do"
            ),
            Helpful(
                imageBitmap = getImage("https://www.moe.go.kr/img/2021Renewal/content/mi_2_1.png"),
                imageBitmapBackground = Color.White,
                title = "교육부",
                description = """
                     대한민국의 인적자원 개발정책과 학교교육·평생교육 및 학술에 관한 사무를 관장하는 중앙행정기관.
                """.trimIndent(),
                link = "https://www.moe.go.kr/main.do?s=moe"
            ),
            Helpful(
                imageBitmap = getImage("https://www.safe182.go.kr/static/home/new_images/logo.png"),
                imageBitmapBackground = Color.White,
                title = "안전Dream",
                description = """
                     경찰청은 해마다 증가하는 사회적 약자 대상 범죄에 대한 피해신고접수와 신속한 구조활동을 지원하기 위해, 기존 실종아동찾기 센터, 117학교·여성폭력 및 성매매피해자 긴급지원센터(117센터)등 관련 홈페이지를 「안전Dream」이라는 명칭으로 통합하였습니다.
                     「안전Dream」은 다양한 신고서비스와 온라인 상담, 범죄예방 컨텐츠 등을 제공하며, 경찰청 내부업무시스템인 「프로파일링시스템」과의 연계로 보다 빠르고 신뢰성 있는 치안서비스를 제공하고 되었습니다.
                """.trimIndent(),
                link = "https://www.safe182.go.kr/index.do"
            ),
            Helpful(
                imageBitmap = getImage("https://www.edunet.net/nedu/images/common_c/edunet_newlogo.png"),
                imageBitmapBackground = Color.White,
                title = "에듀넷·티-클리어",
                description = """
                    에듀넷·티-클리어는 약자로 교육과정과 교육정책 전반의 정보를 통합 제공하고, 협업 소통을 지원하는 교육정보 통합 지원 서비스입니다.
                """.trimIndent(),
                link = "https://doran.edunet.net/main/mainForm.do"
            ),
            Helpful(
                imageBitmap = getImage("https://www.btf.or.kr/images/common/logo02.png"),
                imageBitmapBackground = Color.White,
                title = "푸른나무재단",
                description = """
                     전국 학교폭력 상담전화 1588-9128(구원의팔)
                     학교폭력으로 고통 받는 청소년, 부모님, 학교 선생님들께 푸른나무재단에서 "구원의 팔"을 내밀어 드립니다.
                     푸른나무재단에서는 1995년부터 전국 학교폭력 상담전화 1588-9128(구원의팔)을 운영하고 있습니다. 학교폭력 피·가해 위기상담 및 심리·정서적 지원, 학교폭력 발생 시 대처 및 해결을 위한 조언, 전문 정보제공, 학교폭력 사안처리에 대한 고충 상담을 제공합니다.
                """.trimIndent(),
                link = "https://www.btf.or.kr"
            ),
            Helpful(
                imageBitmap = getImage("https://www.cyber1388.kr:447/images/common/logo_renew.png"),
                imageBitmapBackground = Color.White,
                title = "청소년사이버상담센터",
                description = """
                     청소년사이버상담센터는 지난 10여 년간 수십만 명의 청소년들과 함께 아래와 같이 성장해 왔습니다. 1999년, PC 보급과 인터넷 발전이라는 시대적 상황에 발맞춰 우리나라 사이버상담의 일대 도약의 문을 열게 되었습니다. 2011년, 청소년사이버상담센터의 개소로 보다 많은 청소년들이 이용할 수 있도록 접근성을 높였으며, 최근에는 누리소통망(SNS)의 확산과 비대면 일상생활의 사회적 현상을 고려한 찾아가는 상담(사이버아웃리치)과 카카오톡 및 문자 메시지상담까지 확대 운영하고 있습니다.
                """.trimIndent(),
                link = "https://www.cyber1388.kr:447"
            ),
            Helpful(
                imageBitmap = getImage("https://www.law.go.kr/LSW/images/common/poplogo.gif"),
                imageBitmapBackground = Color(0xFF328DDF),
                title = "학교폭력예방 및 대책에 관한 법률",
                description = """
                     법제처 국가법력정보센터 학교폭력예방 및 대책에 관한 법률 (약칭 학교폭력예방법)
                """.trimIndent(),
                link = "https://www.law.go.kr/%EB%B2%95%EB%A0%B9/%ED%95%99%EA%B5%90%ED%8F%AD%EB%A0%A5%EC%98%88%EB%B0%A9%EB%B0%8F%EB%8C%80%EC%B1%85%EC%97%90%EA%B4%80%ED%95%9C%EB%B2%95%EB%A5%A0"
            ),
            Helpful(
                imageBitmap = getImage("https://www.kyci.or.kr/userSite/images/common/logo.png"),
                imageBitmapBackground = Color.White,
                title = "한국청소년상담복지개발원",
                description = """
                     한국청소년상담복지개발원은 전국 600여개의 청소년상담복지센터, 학교밖청소년지원센터(청소년지원센터 꿈드림), 청소년복지시설(청소년쉼터, 청소년자립지원관, 청소년회복지원시설)을 총괄하는 중추 기관으로서 청소년의 건강한 성장과 행복한 꿈의 실현을 위해 다양한 상담복지사업을 수행하고 있는 여성가족부 산하 공공기관입니다.
                     특히 학교 밖 청소년, 미디어 과의존 청소년 등 위기청소년 지원과 함께 청소년 정책연구 및 프로그램 개발, 상담복지 전문인력 양성 등 다양한 사업을 수행하고 있습니다.
                """.trimIndent(),
                link = "https://www.kyci.or.kr/userSite/index.asp"
            )
        )
    }

    @Composable
    fun Helpful(paddingValues: PaddingValues = PaddingValues()) {
        var tip by rememberSaveable { mutableStateOf(true) }
        if(tip) {
            MyView.PopupTip(
                message = "우왕!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
                hasBottomBar = true,
                onClose = { tip = false }
            )
        }

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
                    imageBitmapBackground = helpful.imageBitmapBackground,
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
        imageBitmapBackground: Color? = null,
        title: String,
        description: String,
        link: String,
        modifier: Modifier = Modifier
    ) {
        val hasImage = imageBitmap != null

        var showingActionsDialog by remember { mutableStateOf(false) }
        var showingInfoDialog by remember { mutableStateOf(false) }

        if (showingActionsDialog) {
            MyView.BaseDialog(
                onDismissRequest = { showingActionsDialog = false },
                //icon = { Icon }
            )
        }

        if (showingInfoDialog) {
            MyView.BaseDialog(
                onDismissRequest = { showingInfoDialog = false }
            )
        }

        Card(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .combinedClickable(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            Core
                                .getActivity()
                                .startActivity(intent)
                        },
                        onLongClick = {
                            Utils.vibrate(50)
                            showingActionsDialog = true
                        },
                        onDoubleClick = {
                            Utils.vibrate(50)
                            showingInfoDialog = true
                        }
                    )
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
            var textMoreSee by remember { mutableStateOf(false) }
            var didOverflowHeight by remember { mutableStateOf(false) }

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(cornerRadius)
            ) {
                val (image, titleText, showActionsButton, showInfoButton, descriptionText, seeMoreButton) = createRefs()

                if (hasImage) {
                    var imageWidth by remember { mutableStateOf(0) }
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = imageBitmapDescription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(with(LocalDensity.current) { imageWidth.toDp() / 3 })
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .onSizeChanged { intSize ->
                                imageWidth = intSize.width
                            }
                            .background(
                                color = imageBitmapBackground
                                    ?: MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(5.dp)
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

                IconButton(
                    onClick = { showingInfoDialog = true },
                    modifier = Modifier
                        .constrainAs(showInfoButton) {
                            top.linkTo(titleText.top)
                            bottom.linkTo(titleText.bottom)
                            end.linkTo(showActionsButton.start)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = { showingActionsDialog = true },
                    modifier = Modifier
                        .constrainAs(showActionsButton) {
                            top.linkTo(titleText.top)
                            bottom.linkTo(titleText.bottom)
                            end.linkTo(parent.end)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null
                    )
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = if (textMoreSee) 100 else 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        didOverflowHeight = textLayoutResult.didOverflowHeight
                    },
                    modifier = Modifier
                        .constrainAs(descriptionText) {
                            top.linkTo(titleText.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        }
                )

                if (didOverflowHeight || textMoreSee) {
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
                            contentDescription = if (textMoreSee) stringResource(id = R.string.close) else stringResource(
                                id = R.string.open
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

            }
        }
    }

}