package com.taeyeon.schoolviolencebreaker

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.imageResource
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taeyeon.core.Utils
import com.taeyeon.schoolviolencebreaker.MyView.Tip

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
            var showingTip by rememberSaveable { mutableStateOf(showTip) }

            if (showingTip) {
                Tip(
                    tip = "asfddfsa",
                    tipImageDescription = "",
                    title = "adsf",
                    message = "adfs".repeat(10),
                    onCloseButtonClick = {
                        showingTip = false
                    },
                    closeImageDescription = "daf",
                    imageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_launcher_round),
                    imageBitmapDescription = "",
                    actionButtonTitle = "sasdfA",
                    onActionButtonClick = {
                        Utils.toast("adsf")
                    }
                )
            }

            Text(
                modifier = Modifier.fillMaxSize(),
                text = "정태연 ".repeat(100)
            )
        }
    }

}

@Preview(
    name = "SVInfo",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4_XL
)
@Composable
fun SVInfoPreview() {
    SVInfo.SVInfo()
}