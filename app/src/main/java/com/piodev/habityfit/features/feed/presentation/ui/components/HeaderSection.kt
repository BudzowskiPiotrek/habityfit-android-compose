package com.piodev.habityfit.features.feed.presentation.ui.components

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.core.config.LimitsConfig.EMAIL_APP

@Composable
fun HeaderSection() {
    val context = LocalContext.current

    val inputStream = context.assets.open("dev.jpg")
    val bitmap = BitmapFactory.decodeStream(inputStream)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.feed_header_title_text),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.feed_header_body_text),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = EMAIL_APP,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$EMAIL_APP")
                    }
                    context.startActivity(intent)
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = stringResource(R.string.feed_header_image_cd),
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
        )
    }
}