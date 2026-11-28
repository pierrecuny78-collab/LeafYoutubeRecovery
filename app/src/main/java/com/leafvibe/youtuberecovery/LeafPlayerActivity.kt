package com.leafvibe.youtuberecovery

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.EditText
import android.widget.LinearLayout.LayoutParams

class LeafPlayerActivity : Activity() {
    private val navy = Color.rgb(15, 27, 43)
    private val panel = Color.rgb(23, 40, 59)
    private val green = Color.rgb(34, 197, 94)
    private lateinit var tools: MutableSet<String>
    private lateinit var toolRow: LinearLayout
    private var currentVideoId: String = ""
    private val toolDescriptions = linkedMapOf(
        "LeafTranscript" to "Consulter une transcription lorsque YouTube la rend disponible. L'import et l'affichage avancé seront ajoutés dans une prochaine version.",
        "LeafTranslate" to "Traduire une transcription ou une description. Le moteur de traduction sera connecté dans une prochaine version.",
        "LeafSummary" to "Préparer un résumé à partir d'une transcription accessible. Le résumé automatique sera ajouté ensuite.",
        "LeafChapters" to "Conserver des repères temporels et retrouver les passages importants.",
        "LeafLinks" to "Noter et organiser les liens utiles associés à cette vidéo.",
        "LeafVocabulary" to "Enregistrer des mots et expressions à retenir.",
        "LeafNotes" to "Écrire des notes personnelles liées à cette vidéo.",
        "LeafSimilar" to "Regrouper des vidéos et chaînes similaires.",
        "LeafDiscover" to "Rechercher et découvrir des chaînes et vidéos YouTube.",
        "LeafPlaylist" to "Organiser des collections de vidéos et playlists.",
        "LeafGraph" to "Visualiser les relations entre chaînes et contenus.",
        "LeafStats" to "Consulter les statistiques de ta bibliothèque.",
        "LeafPlayer" to "Lire la vidéo avec le lecteur YouTube intégré."
    )
    private fun saveTools() {
        getSharedPreferences("leaf_youtube_recovery", MODE_PRIVATE).edit()
            .putStringSet("active_video_tools", tools.toSet()).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("leaf_youtube_recovery", MODE_PRIVATE)
        tools = (prefs.getStringSet("active_video_tools", null) ?: prefs.getStringSet("home_tools", null) ?: setOf("LeafPlayer", "LeafTranscript", "LeafTranslate", "LeafSummary", "LeafNotes")).toMutableSet()
        val url = intent.getStringExtra("video_url").orEmpty()
        val id = extractVideoId(url)
        currentVideoId = id ?: url

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(navy)
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(12, 8, 12, 8)
            setBackgroundColor(navy)
        }
        header.addView(Button(this).apply { text = "← Retour"; setOnClickListener { finish() } })
        header.addView(TextView(this).apply {
            text = "   LEAFPLAYER"
            textSize = 21f
            setTextColor(green)
            setPadding(8, 12, 0, 0)
        })
        root.addView(header)

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(14, 4, 14, 20)
        }
        val player = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
        }
        content.addView(player, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(230)))
        if (id != null) {
            val html = """
                <!doctype html><html><head><meta name="viewport" content="width=device-width, initial-scale=1"></head>
                <body style="margin:0;background:#000"><iframe width="100%" height="100%"
                style="position:absolute;inset:0;border:0"
                src="https://www.youtube.com/embed/$id?playsinline=1&rel=0"
                title="YouTube video player" frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe></body></html>
            """.trimIndent()
            player.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
        } else {
            content.addView(TextView(this).apply {
                text = "Lien vidéo non reconnu. Ajoute une URL YouTube valide (youtube.com/watch?v=… ou youtu.be/…)."
                setTextColor(Color.WHITE)
            })
        }

        content.addView(TextView(this).apply {
            text = "MES APPLICATIONS VIDÉO"
            textSize = 17f
            setTextColor(green)
            setPadding(0, 18, 0, 8)
        })
        content.addView(TextView(this).apply {
            text = "Appuie sur un outil pour ouvrir sa fiche. Tu peux personnaliser cette barre."
            textSize = 12f
            setTextColor(Color.LTGRAY)
        })
        val toolScroll = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false }
        toolRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; setPadding(0, 8, 0, 8) }
        toolScroll.addView(toolRow)
        content.addView(toolScroll)
        val manageRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        manageRow.addView(Button(this).apply {
            text = "+ Ajouter une application"
            setOnClickListener { showAddToolDialog() }
        }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        manageRow.addView(Button(this).apply {
            text = "− Retirer"
            setOnClickListener { showRemoveToolDialog() }
        }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        content.addView(manageRow)
        content.addView(TextView(this).apply {
            text = "INFORMATIONS"
            textSize = 16f
            setTextColor(green)
            setPadding(0, 20, 0, 6)
        })
        content.addView(TextView(this).apply {
            text = "La lecture utilise le lecteur officiel YouTube. Les outils sont installables dans cette barre ; leurs fonctions avancées seront activées progressivement."
            setTextColor(Color.LTGRAY)
            textSize = 13f
        })
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
        refreshTools()
        intent.getStringExtra("open_tool")?.let { requestedTool ->
            if (requestedTool in setOf("LeafNotes", "LeafChapters", "LeafLinks")) showTool(requestedTool)
        }
    }

    private fun refreshTools() {
        toolRow.removeAllViews()
        if (tools.isEmpty()) {
            toolRow.addView(TextView(this).apply { text = "Aucun outil ajouté"; setTextColor(Color.WHITE); setPadding(8, 12, 8, 12) })
        }
        tools.forEach { name ->
            toolRow.addView(Button(this).apply {
                text = name.removePrefix("Leaf")
                setTextColor(navy)
                setBackgroundColor(green)
                setOnClickListener { showTool(name) }
            }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(46)).apply { setMargins(4, 0, 4, 0) })
        }
    }

    private fun showTool(name: String) {
        if (name == "LeafNotes") { showSavedTextTool("notes", "Notes de cette vidéo"); return }
        if (name == "LeafChapters") { showSavedTextTool("chapters", "Chapitres et horodatages"); return }
        if (name == "LeafLinks") { showSavedTextTool("links", "Liens associés à cette vidéo"); return }
        AlertDialog.Builder(this).setTitle(name)
            .setMessage(toolDescriptions[name] ?: "Outil Leaf")
            .setPositiveButton("Compris", null).show()
    }

    private fun showSavedTextTool(kind: String, title: String) {
        val prefs = getSharedPreferences("leaf_youtube_recovery", MODE_PRIVATE)
        val key = "${kind}_${currentVideoId}"
        val input = EditText(this).apply {
            minLines = 5
            setText(prefs.getString(key, ""))
            hint = when(kind) { "chapters" -> "00:00 Introduction\\n02:35 Sujet suivant"; "links" -> "https://…"; else -> "Mes notes sur cette vidéo…" }
        }
        AlertDialog.Builder(this).setTitle(title).setView(input)
            .setPositiveButton("Enregistrer") { _, _ -> prefs.edit().putString(key, input.text.toString()).apply() }
            .setNegativeButton("Annuler", null).show()
    }

    private fun showAddToolDialog() {
        val available = toolDescriptions.keys.filterNot { it in tools }
        if (available.isEmpty()) {
            Toast.makeText(this, "Tous les outils sont déjà ajoutés", Toast.LENGTH_SHORT).show()
            return
        }
        AlertDialog.Builder(this).setTitle("Ajouter une application")
            .setItems(available.toTypedArray()) { _, which ->
                tools.add(available[which]); saveTools(); refreshTools()
            }.show()
    }

    private fun showRemoveToolDialog() {
        val current = tools.toList()
        if (current.isEmpty()) return
        AlertDialog.Builder(this).setTitle("Retirer une application")
            .setItems(current.toTypedArray()) { _, which ->
                tools.remove(current[which]); saveTools(); refreshTools()
            }.show()
    }

    private fun extractVideoId(raw: String): String? {
        val value = raw.trim()
        if (value.matches(Regex("^[A-Za-z0-9_-]{11}$"))) return value
        return runCatching {
            val uri = Uri.parse(if (value.startsWith("http://") || value.startsWith("https://")) value else "https://$value")
            val host = uri.host.orEmpty().lowercase()
            val path = uri.path.orEmpty().trim('/').split('/').filter { it.isNotBlank() }
            val id = when {
                host == "youtu.be" || host.endsWith(".youtu.be") -> path.firstOrNull()
                path.firstOrNull() in setOf("embed", "shorts", "live", "v") -> path.getOrNull(1)
                uri.getQueryParameter("v") != null -> uri.getQueryParameter("v")
                else -> null
            }
            id?.takeIf { it.matches(Regex("^[A-Za-z0-9_-]{11}$")) }
        }.getOrNull()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}