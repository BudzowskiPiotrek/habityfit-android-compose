package com.piodev.habityfit.features.feed.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.features.feed.domain.model.FeedEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val EMPTY_USER_ID = "00000000-0000-0000-0000-000000000000"

@Composable
fun FeedEventCard(
    event: FeedEvent,
    modifier: Modifier = Modifier,
    exerciseImageUrl: String? = null
) {
    Log.d("FeedEventCard", "Exercise image URL: $exerciseImageUrl")
    val isSystemEvent = event.userId.isBlank() || event.userId == EMPTY_USER_ID

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        val dateFormatter = remember {
            SimpleDateFormat("dd MMM", Locale.getDefault())
        }

        if (isSystemEvent) {
            Column(modifier = Modifier.fillMaxWidth()) {
                FeedEventImage(eventType = event.eventType)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val text = event.customText ?: ""
                    val urlRegex = Regex("""https?://\S+""")
                    val match = urlRegex.find(text)

                    if (match != null) {
                        val annotated = buildAnnotatedString {
                            append(text.substring(0, match.range.first))
                            pushLink(
                                LinkAnnotation.Url(
                                    match.value,
                                    TextLinkStyles(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    )
                                )
                            )
                            append(stringResource(R.string.feed_card_link_text))
                            pop()
                            append(text.substring(match.range.last + 1))
                        }
                        Text(
                            text = annotated,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Justify
                        )
                    } else {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.weight(3f)) {
                    Text(
                        text = event.customText ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = dateFormatter.format(Date(event.createdAt)),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}