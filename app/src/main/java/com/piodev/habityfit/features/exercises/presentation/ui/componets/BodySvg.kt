package com.piodev.habityfit.features.exercises.presentation.ui.componets

import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BodySvg(
    selectedMuscleId: String?,
    modifier: Modifier = Modifier,
    onMuscleClick: (String) -> Unit
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WebView(ctx).apply {
                isClickable = true
                isFocusable = true
                setLayerType(View.LAYER_TYPE_HARDWARE, null)

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }

                setBackgroundColor(android.graphics.Color.TRANSPARENT)

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onMuscleTouch(muscleId: String) {
                        onMuscleClick(muscleId)
                        post {
                            evaluateJavascript(
                                "highlightMuscle('$muscleId');",
                                null
                            )
                        }

                    }
                }, "AndroidInterface")

                val svgContent = ctx.assets
                    .open("img/body.svg")
                    .bufferedReader()
                    .use { it.readText() }

                val html = """
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>

                            html, body {
                                height: 100%;
                                width: auto;
                                margin: 0;
                                padding: 0;
                            }
                            svg {
                                width: 88%;
                                margin: 0 auto;
                                height: auto;
                                display: flex;                             
                            }
                        </style>
                    </head>
                    <body>
                        $svgContent
                        <script>
                            const activeColor = "#CCFF00";
                            const defaultColor = "#DEAA87";
                        
                            let currentMuscle = null;
                        
                            document.addEventListener('click', function(event) {
                                let target = event.target;
                                while (target && target.tagName !== 'svg') {
                        
                                    if (target.id && target.id.length >= 36) { 
                                        AndroidInterface.onMuscleTouch(target.id);
                                        return;
                                    }
                                    target = target.parentElement;
                                }
                            });
                        
                            function setColor(element, color) {
                                const paths = element.tagName === 'g'
                                    ? element.querySelectorAll('path')
                                    : [element];
                        
                                paths.forEach(p => {
                                    p.style.fill = color;
                                });
                            }
                        
                            function highlightMuscle(id) {
                                if (currentMuscle) {
                                    const prev = document.getElementById(currentMuscle);
                                    if (prev) setColor(prev, defaultColor);
                                    currentMuscle = null;
                                }
                                if (!id) return;
                                const el = document.getElementById(id);
                                if (el) {
                                    setColor(el, activeColor);
                                    currentMuscle = id;
                                }
                            }
                        </script>
                    </body>
                    </html>
                """.trimIndent()

                loadDataWithBaseURL(
                    null,
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        update = { webView ->
            val jsId = if (selectedMuscleId != null) "'$selectedMuscleId'" else "null"
            webView.evaluateJavascript("highlightMuscle($jsId);", null)
        }
    )
}