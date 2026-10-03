package com.piodev.habityfit.features.feed.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.piodev.database.model.FeedEventType
import com.piodev.habityfit.R

@Composable
fun FeedEventImage(
    eventType: FeedEventType,
    modifier: Modifier = Modifier
) {
    val imageRes = when (eventType) {
        FeedEventType.GENERAL -> R.drawable.general
        FeedEventType.START_SESSION -> R.drawable.start_session
        FeedEventType.CHALLENGE_WIN -> R.drawable.challenge_win
        FeedEventType.NEW_RECORD -> R.drawable.new_record
    }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = stringResource(R.string.feed_event_image_cd) + eventType,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f),
        contentScale = ContentScale.Crop
    )
}