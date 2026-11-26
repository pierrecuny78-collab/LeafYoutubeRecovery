package com.leafvibe.youtuberecovery

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.view.ViewGroup
import android.graphics.Color
import android.view.Gravity
import android.widget.Toast
import android.app.AlertDialog

class LeafPlayerActivity : Activity() {
    private val navy = Color.rgb(15, 27, 43)
    private val green = Color.rgb(34, 197, 94)
    private val tools = linkedSetOf("LeafTranslate", "LeafTranscript", "LeafSummary", "LeafChapters", "LeafLinks", "LeafVocabulary", "LeafNotes", "LeafSimilar")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("video_url").orEmpty()
        val videoId = extractVideoId(url)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(navy)
            setPadding(18, 18, 18, 12)
        }
        val back = Button(this).apply {
            text = "← Retour à LeafYoutubeRecovery"
            setOnClickListener { finish() }
        }
        root.addView(back, LinearLayout.LayoutParams(-1, -2))
        root.addView(TextView(this).apply {
            text = "LEAFPLAYER"
            textSize = 22f
            setTextColor(green)
            setPadding(0, 14, 0, 8)
        })
        root.addView(TextView(this).apply {
            text = "Lecteur YouTube intégré"
            textSize = 16f
            setTextColor(Color.WHITE)
        })
        val web = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
        }
        root.addView(web, LinearLayout.LayoutParams(-1, 0, 1f))
        if (videoId != null) {
            val html = """
                <!doctype html><html><head><meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"></head>
                <body style="margin:0;background:#000">
                <iframe width="100%" height="100%" style="position:absolute;inset:0;border:0"
                src="https://www.youtube.com/embed/$videoId?playsinline=1&rel=0"
                title="YouTube video player" frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
                </body></html>
            """.trimIndent()
            web.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
        } else {
            web.loadUrl("https://www.youtube.com")
        }

        val toolsTitle = TextView(this).apply {
            text = "MES OUTILS VIDÉO"
            textSize = 15f
            setTextColor(green)
            setPadding(0, 14, 0, 4)
        }
        root.addView(toolsTitle)
        val toolsText = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 13f
            setPadding(0, 4, 0, 8)
        }
        fun refreshTools() { toolsText.text = if (tools.isEmpty()) "Aucun outil ajouté" else tools.joinToString("   •   ") }
        root.addView(toolsText)
        refreshTools()
        val actions = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
        val manage = Button(this).apply {
            text = "+ Ajouter un outil"
            setOnClickListener {
                val available = listOf("LeafTranslate", "LeafTranscript", "LeafSummary", "LeafChapters", "LeafLinks", "LeafVocabulary", "LeafNotes", "LeafSimilar")
                    .filterNot { it in tools }
                if (available.isEmpty()) {
                    Toast.makeText(this@LeafPlayerActivity, "Tous les outils sont déjà ajoutés", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this@LeafPlayerActivity)
                        .setTitle("Ajouter un outil")
                        .setItems(available.toTypedArray()) { _, which -> tools.add(available[which]); refreshTools() }
                        .show()
                }
            }
        }
        val remove = Button(this).apply {
            text = "− Supprimer"
            setOnClickListener {
                val current = tools.toList()
                if (current.isNotEmpty()) AlertDialog.Builder(this@LeafPlayerActivity)
                    .setTitle("Supprimer un outil")
                    .setItems(current.toTypedArray()) { _, which -> tools.remove(current[which]); refreshTools() }
                    .show()
            }
        }
        actions.addView(manage, LinearLayout.LayoutParams(0, -2, 1f))
        actions.addView(remove, LinearLayout.LayoutParams(0, -2, 1f))
        root.addView(actions)
        setContentView(root)
    }

    private fun extractVideoId(raw: String): String? {
        val patterns = listOf(
            Regex("(?:v=|youtu\\.be/|embed/|shorts/)([A-Za-z0-9_-]{11})"),
            Regex("^([A-Za-z0-9_-]{11})$")
        )
        return patterns.firstNotNullOfOrNull { it.find(raw)?.groupValues?.getOrNull(1) }
    }

    override fun onPause() {
        super.onPause()
    }
}