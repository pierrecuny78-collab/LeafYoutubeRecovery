package com.leafvibe.youtuberecovery

import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import coil.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import org.json.JSONObject

private val Navy=Color(0xFF0F1B2B)
private val Green=Color(0xFF22C55E)
private val Panel=Color(0xFF17283B)
private val defaults=listOf("Divertissement","Jeux vidéo","Jeux de société","Mode","Technologie","Musique","Cuisine","Voyages","Sport","Éducation","Autres","Non classées")
data class Channel(val id:String,val name:String,val url:String,val category:String,val notes:String="",val videos:String="",val playlists:String="",val favorite:Boolean=false,val collection:String="",val description:String="",val videoFolders:String="{}")

class MainActivity:ComponentActivity(){
 private val prefs by lazy{getSharedPreferences("leaf_youtube_recovery",MODE_PRIVATE)}
 private fun readCategories():List<String> = runCatching{val a=JSONArray(prefs.getString("categories",null)?:return defaults);(0 until a.length()).map{a.getString(it)}}.getOrDefault(defaults)
 private fun saveCategories(v:List<String>){prefs.edit().putString("categories",JSONArray(v).toString()).apply()}
 private fun load():List<Channel> = runCatching{val a=JSONArray(prefs.getString("channels","[]"));(0 until a.length()).map{a.getJSONObject(it).let{o->Channel(o.getString("id"),o.getString("name"),o.getString("url"),o.optString("category","Non classées"),o.optString("notes"),o.optString("videos"),o.optString("playlists"),o.optBoolean("favorite",false),o.optString("collection"),o.optString("description"),o.optString("videoFolders","{}"))}}}.getOrDefault(emptyList())
 private fun save(v:List<Channel>){val a=JSONArray();v.forEach{c->a.put(JSONObject().put("id",c.id).put("name",c.name).put("url",c.url).put("category",c.category).put("notes",c.notes).put("videos",c.videos).put("playlists",c.playlists).put("favorite",c.favorite).put("collection",c.collection) .put("description",c.description).put("videoFolders",c.videoFolders))};prefs.edit().putString("channels",a.toString()).apply()}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);if(!prefs.getBoolean("dock_v04_migrated",false)){val all=setOf("LeafPlayer","LeafTranscript","LeafTranslate","LeafSummary","LeafSubtitles","LeafChapters","LeafLinks","LeafVocabulary","LeafNotes","LeafSimilar","LeafDiscover","LeafPlaylist","LeafGraph","LeafStats","LeafSound","LeafColour","LeafBridge","LeafTracker","LeafWallpaper","LeafSearch","LeafExport");prefs.edit().putStringSet("home_tools",(prefs.getStringSet("home_tools",emptySet())?:emptySet())+all).putBoolean("dock_v04_migrated",true).apply()};setContent{
  var cats by remember{mutableStateOf(readCategories())};var channels by remember{mutableStateOf(load())};var selected by remember{mutableStateOf<Channel?>(null)};var showVideoLibrary by remember{mutableStateOf(false)}
  MaterialTheme(colorScheme=darkColorScheme(primary=Green,background=Navy,surface=Panel,onPrimary=Navy,onBackground=Color.White,onSurface=Color.White)){
   if(showVideoLibrary) VideoLibrary(channels,{updated->channels=updated;save(updated)},{showVideoLibrary=false},{u->startActivity(Intent(this@MainActivity,LeafPlayerActivity::class.java).putExtra("video_url",u))},{channelId->selected=channels.firstOrNull{it.id==channelId};showVideoLibrary=false})
   else if(selected==null) Home(cats,channels,{cats=it;saveCategories(it)},{channels=it;save(it)},{selected=it},{q->startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+Uri.encode(q))))},{showVideoLibrary=true})
   else Detail(selected!!,cats,{selected=null},{u->channels=channels.map{if(it.id==u.id)u else it};save(channels);selected=u},{channels=channels.filterNot{it.id==selected!!.id};save(channels);selected=null},{u->startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(if(u.startsWith("http"))u else "https://www.youtube.com/$u")))})
  }
 }}
}

@Composable private fun Home(cats:List<String>,channels:List<Channel>,saveCats:(List<String>)->Unit,saveChannels:(List<Channel>)->Unit,open:(Channel)->Unit,search:(String)->Unit,openLibrary:()->Unit){
 var filter by remember{mutableStateOf("Toutes")};var query by remember{mutableStateOf("")};val context=LocalContext.current;var toolDialog by remember{mutableStateOf(false)};var selectedTool by remember{mutableStateOf("LeafPlayer")};var backupDialog by remember{mutableStateOf(false)};var backupText by remember{mutableStateOf("")};var tools by remember{mutableStateOf(context.getSharedPreferences("leaf_youtube_recovery",Context.MODE_PRIVATE).getStringSet("home_tools",setOf("LeafPlayer","LeafTranscript","LeafTranslate","LeafSummary","LeafSubtitles","LeafChapters","LeafLinks","LeafVocabulary","LeafNotes","LeafSimilar","LeafDiscover","LeafPlaylist","LeafGraph","LeafStats","LeafSound","LeafColour","LeafBridge","LeafTracker","LeafWallpaper","LeafSearch","LeafExport"))!!.toSet())};val availableTools=listOf("LeafPlayer","LeafTranscript","LeafTranslate","LeafSummary","LeafSubtitles","LeafChapters","LeafLinks","LeafVocabulary","LeafNotes","LeafSimilar","LeafDiscover","LeafPlaylist","LeafGraph","LeafStats","LeafSound","LeafColour","LeafBridge","LeafTracker","LeafWallpaper","LeafSearch","LeafExport")
 var catDialog by remember{mutableStateOf(false)};var editing by remember{mutableStateOf<String?>(null)};var catName by remember{mutableStateOf("")}
 var addDialog by remember{mutableStateOf(false)};var name by remember{mutableStateOf("")};var url by remember{mutableStateOf("")};var newCat by remember{mutableStateOf("Non classées")}
 val shown=channels.filter{(filter=="Toutes"||it.category==filter)&&(query.isBlank()||it.name.contains(query,true)||it.url.contains(query,true))}
 Scaffold(containerColor=Navy,bottomBar={Surface(color=Panel,tonalElevation=8.dp){Column(Modifier.fillMaxWidth().padding(vertical=6.dp)){Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal=8.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){tools.sortedBy{availableTools.indexOf(it)}.forEach{name->Surface(onClick={selectedTool=name;toolDialog=true},shape=MaterialTheme.shapes.medium,color=if(name=="LeafPlayer")Green else Navy){Column(Modifier.padding(horizontal=12.dp, vertical=8.dp),horizontalAlignment=androidx.compose.ui.Alignment.CenterHorizontally){Text(name.removePrefix("Leaf"),color=if(name=="LeafPlayer")Navy else Color.White,fontWeight=FontWeight.SemiBold,maxLines=1);Text("Leaf",color=if(name=="LeafPlayer")Navy else Green,style=MaterialTheme.typography.labelSmall)}}}};Row(Modifier.fillMaxWidth().padding(horizontal=12.dp),horizontalArrangement=Arrangement.End){TextButton(onClick={toolDialog=true;selectedTool="Gérer les applications"}){Text("Gérer les applications",color=Green)}}}}},floatingActionButton={FloatingActionButton(onClick={addDialog=true},containerColor=Green,contentColor=Navy){Icon(Icons.Default.Add,"Ajouter")}}){p->
 Column(Modifier.fillMaxSize().padding(p).padding(16.dp)){
 Text("LEAF COLOUR",color=Green,fontWeight=FontWeight.Bold);Text("YoutubeRecovery",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text("Bibliothèque de chaînes YouTube",color=Color.LightGray)
 Button(onClick=openLibrary,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=Green,contentColor=Navy)){Text("Ouvrir la bibliothèque vidéo globale  ·  "+channels.sumOf{it.videos.lines().count{line->line.isNotBlank()}}+" vidéos")}
 OutlinedTextField(query,{query=it},Modifier.fillMaxWidth(),placeholder={Text("Rechercher mes chaînes")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true)
 TextButton(onClick={search(query)},enabled=query.isNotBlank()){Text("Rechercher sur YouTube")}
 Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){Text("Catégories",style=MaterialTheme.typography.titleLarge);Spacer(Modifier.weight(1f));TextButton(onClick={editing=null;catName="";catDialog=true}){Icon(Icons.Default.Add,null);Text("Ajouter")}}
 LazyColumn(Modifier.heightIn(max=210.dp)){
 item{FilterChip(filter=="Toutes",{filter="Toutes"},label={Text("Toutes ("+channels.size+")")})}
 items(cats){c->Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){FilterChip(selected=filter==c,onClick={filter=c},label={Text(c+" ("+channels.count{it.category==c}+")")},modifier=Modifier.weight(1f));IconButton(onClick={editing=c;catName=c;catDialog=true}){Icon(Icons.Default.Edit,"Renommer")};if(c!="Non classées")IconButton(onClick={saveChannels(channels.map{if(it.category==c)it.copy(category="Non classées")else it});saveCats(cats.filterNot{it==c});if(filter==c)filter="Non classées"}){Icon(Icons.Default.Delete,"Supprimer")}}}
 }
 Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){Text("Chaînes ("+shown.size+")",style=MaterialTheme.typography.titleLarge);Spacer(Modifier.weight(1f));TextButton(onClick={val a=JSONArray();channels.forEach{a.put(JSONObject().put("id",it.id).put("name",it.name).put("url",it.url).put("category",it.category).put("notes",it.notes).put("videos",it.videos).put("playlists",it.playlists).put("favorite",it.favorite).put("collection",it.collection).put("description",it.description).put("videoFolders",it.videoFolders))};val playerPrefs=context.getSharedPreferences("leaf_youtube_recovery",Context.MODE_PRIVATE);val savedVideoTools=JSONObject();playerPrefs.all.filterKeys{listOf("notes_","chapters_","links_","transcript_","translation_","summary_","subtitles_","vocabulary_").any{prefix->it.startsWith(prefix)}}.forEach{(key,value)->if(value is String)savedVideoTools.put(key,value)};val root=JSONObject().put("format","LeafYoutubeRecovery").put("version",5).put("categories",JSONArray(cats)).put("channels",a).put("globalVideoPlaylists",JSONObject(playerPrefs.getString("global_video_playlists","{}") ?: "{}")).put("videoTools",savedVideoTools).put("videoFavorites",JSONArray((playerPrefs.getStringSet("video_favorites",emptySet()) ?: emptySet()).toList())).put("videoHistory",JSONArray((playerPrefs.getStringSet("video_history",emptySet()) ?: emptySet()).toList())).put("videoHistoryTimestamps",JSONObject(playerPrefs.getString("video_history_timestamps","{}") ?: "{}")).put("videoWatchStatuses",JSONObject(playerPrefs.getString("video_watch_statuses","{}") ?: "{}"));val send=Intent(Intent.ACTION_SEND).setType("application/json").putExtra(Intent.EXTRA_SUBJECT,"Sauvegarde LeafYoutubeRecovery").putExtra(Intent.EXTRA_TEXT,root.toString(2));context.startActivity(Intent.createChooser(send,"Exporter ma bibliothèque"))}){Icon(Icons.Default.Share,null);Text("Exporter")};TextButton(onClick={backupText="";backupDialog=true}){Text("Importer")}}
 LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){items(shown,key={it.id}){c->Card(Modifier.fillMaxWidth().clickable{open(c)},colors=CardDefaults.cardColors(containerColor=Panel)){Column(Modifier.padding(14.dp)){Text(c.name,fontWeight=FontWeight.Bold);Text(c.category,color=Green);Text(c.url,color=Color.LightGray,maxLines=1);if(c.description.isNotBlank())Text(c.description,color=Color.LightGray,maxLines=2);if(c.collection.isNotBlank())Text("Collection : "+c.collection,color=Color.LightGray)};IconButton(onClick={saveChannels(channels.map{if(it.id==c.id)it.copy(favorite=!it.favorite)else it})}){Icon(if(c.favorite)Icons.Default.Favorite else Icons.Default.FavoriteBorder,"Favori",tint=if(c.favorite)Green else Color.LightGray)}}}}

 }}
 if(toolDialog)AlertDialog(onDismissRequest={toolDialog=false},title={Text(if(selectedTool=="Gérer les applications")"Gérer les applications Leaf" else selectedTool)},text={Column(Modifier.heightIn(max=420.dp).verticalScroll(rememberScrollState())){if(selectedTool!="Gérer les applications"){Text(when(selectedTool){"LeafPlayer"->"Lecteur vidéo intégré YouTube.";"LeafTranscript"->"Espace de transcription des vidéos.";"LeafTranslate"->"Outils de traduction des sous-titres et descriptions.";"LeafSummary"->"Préparer des résumés à partir des transcriptions.";"LeafSubtitles"->"Gestion et lecture des sous-titres.";"LeafChapters"->"Repères temporels et chapitres.";"LeafLinks"->"Liens associés aux vidéos.";"LeafVocabulary"->"Vocabulaire à retenir.";"LeafNotes"->"Notes personnelles par chaîne et vidéo.";"LeafSimilar"->"Chaînes et vidéos similaires.";"LeafDiscover"->"Découverte de nouvelles chaînes.";"LeafPlaylist"->"Organisation des playlists.";"LeafGraph"->"Relations entre chaînes et thèmes.";"LeafStats"->"Statistiques de la bibliothèque.";else->"Module de l'écosystème Leaf."});Text("Depuis la bibliothèque, ouvre une vidéo puis utilise les outils liés à son contenu. Les textes enregistrés restent associés à son identifiant YouTube. Les fonctions automatiques nécessitant un service externe ne sont pas simulées.")
                OutlinedButton(onClick={toolDialog=false;openLibrary()},modifier=Modifier.fillMaxWidth()){Text("Choisir une vidéo dans la bibliothèque")}}else{availableTools.forEach{name->Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){Checkbox(checked=name in tools,onCheckedChange={checked->tools=if(checked)tools+name else tools-name;context.getSharedPreferences("leaf_youtube_recovery",Context.MODE_PRIVATE).edit().putStringSet("home_tools",tools).apply()});Text(name)}}}}},confirmButton={TextButton(onClick={toolDialog=false}){Text("Fermer")}})
 if(backupDialog)AlertDialog(onDismissRequest={backupDialog=false},title={Text("Importer une sauvegarde JSON")},text={Column{Text("Colle ici un export LeafYoutubeRecovery.");OutlinedTextField(backupText,{backupText=it},Modifier.fillMaxWidth(),minLines=6)}} ,confirmButton={TextButton(onClick={runCatching{val root=JSONObject(backupText);val arr=root.getJSONArray("channels");val imported=(0 until arr.length()).map{i->val o=arr.getJSONObject(i);Channel(o.optString("id",System.currentTimeMillis().toString()+i),o.optString("name"),o.optString("url"),o.optString("category","Non classées"),o.optString("notes"),o.optString("videos"),o.optString("playlists"),o.optBoolean("favorite"),o.optString("collection"),o.optString("description"),o.optString("videoFolders","{}"))};val merged=(channels+imported).distinctBy{it.id};saveChannels(merged);val playerPrefs=context.getSharedPreferences("leaf_youtube_recovery",Context.MODE_PRIVATE);val importedHistoryTimes=root.optJSONObject("videoHistoryTimestamps");if(importedHistoryTimes!=null){val currentTimes=runCatching{JSONObject(playerPrefs.getString("video_history_timestamps","{}")?:"{}")}.getOrDefault(JSONObject());val mergedTimes=JSONObject(currentTimes.toString());importedHistoryTimes.keys().forEach{key->val incoming=importedHistoryTimes.optLong(key,0L);if(incoming>mergedTimes.optLong(key,0L))mergedTimes.put(key,incoming)};playerPrefs.edit().putString("video_history_timestamps",mergedTimes.toString()).apply()};val importedStatuses=root.optJSONObject("videoWatchStatuses");if(importedStatuses!=null){val currentStatuses=runCatching{JSONObject(playerPrefs.getString("video_watch_statuses","{}")?:"{}")}.getOrDefault(JSONObject());val mergedStatuses=JSONObject(currentStatuses.toString());importedStatuses.keys().forEach{key->if(!mergedStatuses.has(key))mergedStatuses.put(key,importedStatuses.optString(key,"À regarder"))};playerPrefs.edit().putString("video_watch_statuses",mergedStatuses.toString()).apply()};val importedVideoTools=root.optJSONObject("videoTools");if(importedVideoTools!=null){val editor=playerPrefs.edit();importedVideoTools.keys().forEach{key->if(!playerPrefs.contains(key))editor.putString(key,importedVideoTools.optString(key))};editor.apply()};val mergedFavorites=((playerPrefs.getStringSet("video_favorites",emptySet()) ?: emptySet()) + (0 until (root.optJSONArray("videoFavorites")?.length() ?: 0)).map{root.optJSONArray("videoFavorites")!!.optString(it)}).filter{it.isNotBlank()}.toSet();val mergedHistory=((playerPrefs.getStringSet("video_history",emptySet()) ?: emptySet()) + (0 until (root.optJSONArray("videoHistory")?.length() ?: 0)).map{root.optJSONArray("videoHistory")!!.optString(it)}).filter{it.isNotBlank()}.toSet();playerPrefs.edit().putStringSet("video_favorites",mergedFavorites).putStringSet("video_history",mergedHistory).apply();val importedPlaylists=root.optJSONObject("globalVideoPlaylists") ?: runCatching{JSONObject(root.optString("globalVideoPlaylists","{}"))}.getOrNull();if(importedPlaylists!=null){val current=runCatching{JSONObject(context.getSharedPreferences("leaf_youtube_recovery",Context.MODE_PRIVATE).getString("global_video_playlists","{}")?:"{}")}.getOrDefault(JSONObject());val combined=JSONObject(current.toString());importedPlaylists.keys().forEach{name->val currentItems=combined.optJSONArray(name)?:JSONArray();val incoming=importedPlaylists.optJSONArray(name)?:JSONArray();val urls=((0 until currentItems.length()).map{currentItems.optString(it)}+(0 until incoming.length()).map{incoming.optString(it)}).filter{it.isNotBlank()}.distinct();combined.put(name,JSONArray(urls))};context.getSharedPreferences("leaf_youtube_recovery",Context.MODE_PRIVATE).edit().putString("global_video_playlists",combined.toString()).apply()};val importedCats=root.optJSONArray("categories");if(importedCats!=null){val newCats=(0 until importedCats.length()).map{importedCats.getString(it)};saveCats((cats+newCats).distinct())}}.onSuccess{backupDialog=false}.onFailure{backupText="JSON invalide : "+(it.message?: "erreur")}}){Text("Importer")}},dismissButton={TextButton(onClick={backupDialog=false}){Text("Annuler")}})
 if(catDialog)AlertDialog(onDismissRequest={catDialog=false},title={Text(if(editing==null)"Ajouter une catégorie" else "Renommer la catégorie")},text={OutlinedTextField(catName,{catName=it},label={Text("Nom de catégorie")})},confirmButton={TextButton(onClick={val n=catName.trim();if(n.isNotBlank()&&n!="Non classées"){if(editing==null){if(n !in cats)saveCats(cats+n)}else if(n !in cats){saveCats(cats.map{if(it==editing)n else it});saveChannels(channels.map{if(it.category==editing)it.copy(category=n)else it});if(filter==editing)filter=n}};catDialog=false}){Text("Enregistrer")}},dismissButton={TextButton(onClick={catDialog=false}){Text("Annuler")}})
 if(addDialog)AlertDialog(onDismissRequest={addDialog=false},title={Text("Ajouter une chaîne existante")},text={Column{OutlinedTextField(name,{name=it},label={Text("Nom")});OutlinedTextField(url,{url=it},label={Text("URL ou identifiant YouTube")});Text("Catégorie");cats.forEach{c->Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){RadioButton(newCat==c,{newCat=c});Text(c)}};TextButton(onClick={search(name)},enabled=name.isNotBlank()){Text("Chercher sur YouTube")}}},confirmButton={TextButton(enabled=name.isNotBlank()&&url.isNotBlank(),onClick={saveChannels(channels+Channel(System.currentTimeMillis().toString(),name.trim(),url.trim(),newCat));name="";url="";addDialog=false}){Text("Ajouter")}},dismissButton={TextButton(onClick={addDialog=false}){Text("Annuler")}})
}

@Composable
private fun Detail(
    c: Channel,
    cats: List<String>,
    back: () -> Unit,
    save: (Channel) -> Unit,
    delete: () -> Unit,
    openLink: (String) -> Unit
) {
    val context = LocalContext.current
    var category by remember(c.id) { mutableStateOf(c.category) }
    var notes by remember(c.id) { mutableStateOf(c.notes) }
    var videos by remember(c.id) { mutableStateOf(c.videos) }
    var playlists by remember(c.id) { mutableStateOf(c.playlists) }
    var collection by remember(c.id) { mutableStateOf(c.collection) }
    var description by remember(c.id) { mutableStateOf(c.description) }
    val videoFolders = remember(c.id) {
        mutableStateMapOf<String, String>().apply {
            runCatching { JSONObject(c.videoFolders) }.getOrDefault(JSONObject()).let { saved ->
                saved.keys().forEach { key -> put(key, saved.optString(key, "À classer")) }
            }
        }
    }
    var videoCategoryFilter by remember(c.id) { mutableStateOf("Toutes") }
    var favorite by remember(c.id) { mutableStateOf(c.favorite) }
    val folderNames = listOf(
        "À classer", "À regarder", "À revoir", "Favoris",
        "Tutoriels", "Musique", "Divertissement", "Jeux vidéo", "Autres"
    )
    val videoList = videos.lines().map { it.trim() }.filter { it.isNotBlank() }

    Column(
        modifier = Modifier.fillMaxSize().background(Navy).padding(16.dp)
    ) {
        TextButton(onClick = back) { Text("← Retour") }
        Text(c.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(c.url, color = Color.LightGray)
        if (c.description.isNotBlank()) Text(c.description, color = Color.LightGray)
        Button(onClick = { openLink(c.url) }) { Text("Ouvrir sur YouTube") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text("Description de la chaîne", color = Green)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    label = { Text("Présentation, contenu, thèmes…") }
                )
            }
            item {
                Text("Catégorie", color = Green)
                cats.forEach { cat ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(selected = category == cat, onClick = { category = cat })
                        Text(cat)
                    }
                }
            }
            item {
                Text("Vidéos enregistrées", color = Green)
                Text(
                    "Colle une URL YouTube par ligne. Les vidéos sont lues dans LeafPlayer sans téléchargement.",
                    color = Color.LightGray
                )
                OutlinedTextField(
                    value = videos,
                    onValueChange = { videos = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    label = { Text("Une URL par ligne") }
                )
            }
            item {
                Text("Collections de vidéos", color = Green)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = videoCategoryFilter == "Toutes",
                        onClick = { videoCategoryFilter = "Toutes" },
                        label = { Text("Toutes") }
                    )
                    folderNames.forEach { folder ->
                        FilterChip(
                            selected = videoCategoryFilter == folder,
                            onClick = { videoCategoryFilter = folder },
                            label = { Text(folder) }
                        )
                    }
                }
                val visibleCount = videoList.count {
                    videoCategoryFilter == "Toutes" ||
                        (videoFolders[it] ?: "À classer") == videoCategoryFilter
                }
                Text("$visibleCount vidéo(s)", color = Color.LightGray)
            }
            items(
                videoList.filter {
                    videoCategoryFilter == "Toutes" ||
                        (videoFolders[it] ?: "À classer") == videoCategoryFilter
                },
                key = { it }
            ) { videoUrl ->
                val videoIndex = videoList.indexOf(videoUrl) + 1
                val videoId = extractYouTubeVideoId(videoUrl)
                var menuExpanded by remember(videoUrl) { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Panel)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            if (videoId != null) {
                                AsyncImage(
                                    model = "https://img.youtube.com/vi/$videoId/hqdefault.jpg",
                                    contentDescription = "Miniature vidéo",
                                    modifier = Modifier.width(104.dp).height(60.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f).padding(start = 8.dp)
                            ) {
                                Text("Vidéo $videoIndex", color = Color.White, fontWeight = FontWeight.Bold)
                                Text(
                                    (videoFolders[videoUrl] ?: "À classer"),
                                    color = Green,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(context, LeafPlayerActivity::class.java)
                                            .putExtra("video_url", videoUrl)
                                    )
                                }
                            ) { Text("Lire") }
                        }
                        TextButton(onClick = { menuExpanded = true }) {
                            Text("Ranger dans une catégorie")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            folderNames.forEach { folder ->
                                DropdownMenuItem(
                                    text = { Text(folder) },
                                    onClick = {
                                        videoFolders[videoUrl] = folder
                                        menuExpanded = false
                                    }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            TextButton(onClick = { playTool(context, videoUrl, "LeafNotes") }) { Text("Notes") }
                            TextButton(onClick = { playTool(context, videoUrl, "LeafChapters") }) { Text("Chapitres") }
                            TextButton(onClick = { playTool(context, videoUrl, "LeafLinks") }) { Text("Liens") }
                            TextButton(onClick = { playTool(context, videoUrl, "LeafTranscript") }) { Text("Transcription") }
                            TextButton(onClick = { playTool(context, videoUrl, "LeafSummary") }) { Text("Résumé") }
                        }
                    }
                }
            }
            item {
                Text("Collection personnelle", color = Green)
                OutlinedTextField(
                    value = collection,
                    onValueChange = { collection = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = favorite, onCheckedChange = { favorite = it })
                    Text("Ajouter aux favoris")
                }
            }
            item {
                Text("Playlists", color = Green)
                OutlinedTextField(
                    value = playlists,
                    onValueChange = { playlists = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
            item {
                Text("LeafNotes de cette chaîne", color = Green)
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5
                )
            }
            item {
                Button(
                    onClick = {
                        save(
                            c.copy(
                                category = category,
                                notes = notes,
                                videos = videos,
                                playlists = playlists,
                                favorite = favorite,
                                collection = collection,
                                description = description,
                                videoFolders = JSONObject().apply { videoFolders.forEach { (url, folder) -> put(url, folder) } }.toString()
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Enregistrer") }
                OutlinedButton(
                    onClick = delete,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Supprimer cette fiche") }
            }
        }
    }
}

private data class LibraryVideo(val channelId:String,val channelName:String,val channelCategory:String,val url:String,val folder:String)

@Composable
private fun VideoLibrary(channels:List<Channel>,saveChannels:(List<Channel>)->Unit,back:()->Unit,play:(String)->Unit,openChannel:(String)->Unit) {
    var query by remember { mutableStateOf("") }
    var folderFilter by remember { mutableStateOf("Toutes") }
    var quickFilter by remember { mutableStateOf("Toutes") }
    var watchFilter by remember { mutableStateOf("Tous les statuts") }
    val context = LocalContext.current
    val videoPrefs = context.getSharedPreferences("leaf_youtube_recovery", Context.MODE_PRIVATE)
    var watchStatuses by remember { mutableStateOf(runCatching { JSONObject(videoPrefs.getString("video_watch_statuses", "{}") ?: "{}") }.getOrDefault(JSONObject())) }
    var favoriteVideoIds by remember { mutableStateOf(videoPrefs.getStringSet("video_favorites", emptySet())?.toSet() ?: emptySet()) }
    var historyVideoIds by remember { mutableStateOf(videoPrefs.getStringSet("video_history", emptySet())?.toSet() ?: emptySet()) }
    var historyTimestamps by remember { mutableStateOf(runCatching { JSONObject(videoPrefs.getString("video_history_timestamps", "{}") ?: "{}") }.getOrDefault(JSONObject())) }
    var selectedVideoDetails by remember { mutableStateOf<LibraryVideo?>(null) }
    var globalPlaylists by remember { mutableStateOf(runCatching { JSONObject(context.getSharedPreferences("leaf_youtube_recovery", Context.MODE_PRIVATE).getString("global_video_playlists", "{}") ?: "{}") }.getOrDefault(JSONObject())) }
    var playlistDialogVideo by remember { mutableStateOf<LibraryVideo?>(null) }
    var createPlaylistDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }
    var playlistFilter by remember { mutableStateOf("Toutes") }
    var managePlaylistsDialog by remember { mutableStateOf(false) }
    var renamePlaylistName by remember { mutableStateOf<String?>(null) }
    var renamedPlaylistText by remember { mutableStateOf("") }
    var deletePlaylistName by remember { mutableStateOf<String?>(null) }
    val folders = listOf("À classer","À regarder","À revoir","Favoris","Tutoriels","Musique","Divertissement","Jeux vidéo","Autres")
    val playlistNames = globalPlaylists.keys().asSequence().toList().sorted()
    fun savePlaylists(updated: JSONObject) {
        globalPlaylists = updated
        context.getSharedPreferences("leaf_youtube_recovery", Context.MODE_PRIVATE).edit().putString("global_video_playlists", updated.toString()).apply()
    }
    fun playlistUrls(name: String): Set<String> {
        val arr = globalPlaylists.optJSONArray(name) ?: JSONArray()
        return (0 until arr.length()).mapNotNull { arr.optString(it).takeIf { value -> value.isNotBlank() } }.toSet()
    }
    val allVideos = channels.flatMap { channel ->
        val saved = runCatching { JSONObject(channel.videoFolders) }.getOrDefault(JSONObject())
        channel.videos.lines().map { it.trim() }.filter { it.isNotBlank() }.distinct().map { url ->
            LibraryVideo(channel.id,channel.name,channel.category,url,saved.optString(url,"À classer"))
        }
    }
    val filteredVideos = allVideos.filter { video ->
        val id = extractYouTubeVideoId(video.url) ?: video.url
        (folderFilter=="Toutes" || video.folder==folderFilter) &&
        (playlistFilter=="Toutes" || video.url in playlistUrls(playlistFilter)) &&
        (quickFilter=="Toutes" || (quickFilter=="Favoris" && id in favoriteVideoIds) || (quickFilter=="Historique" && id in historyVideoIds)) &&
        (watchFilter=="Tous les statuts" || watchStatuses.optString(id, "À regarder")==watchFilter) &&
        (query.isBlank() || video.url.contains(query,true) || video.channelName.contains(query,true))
    }
    val shown = if(quickFilter=="Historique") filteredVideos.sortedByDescending { video -> historyTimestamps.optLong(extractYouTubeVideoId(video.url) ?: video.url, 0L) } else filteredVideos
    val watchingCount = watchStatuses.keys().asSequence().count { watchStatuses.optString(it)=="En cours" }
    val finishedCount = watchStatuses.keys().asSequence().count { watchStatuses.optString(it)=="Terminé" }
    Column(Modifier.fillMaxSize().background(Navy).padding(16.dp)) {
        TextButton(onClick=back) { Text("← Retour aux chaînes") }
        Text("Bibliothèque vidéo",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
        Text("${allVideos.size} vidéos · ${watchingCount} en cours · ${finishedCount} terminées",color=Color.LightGray)
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            Button(onClick={createPlaylistDialog=true}, modifier=Modifier.weight(1f)) { Text("+ Nouvelle playlist") }
            OutlinedButton(onClick={managePlaylistsDialog=true}, enabled=playlistNames.isNotEmpty()) { Text("Gérer") }
            OutlinedButton(onClick={
                favoriteVideoIds=videoPrefs.getStringSet("video_favorites",emptySet())?.toSet() ?: emptySet()
                historyVideoIds=videoPrefs.getStringSet("video_history",emptySet())?.toSet() ?: emptySet()
                historyTimestamps=runCatching { JSONObject(videoPrefs.getString("video_history_timestamps", "{}") ?: "{}") }.getOrDefault(JSONObject())
                watchStatuses=runCatching { JSONObject(videoPrefs.getString("video_watch_statuses", "{}") ?: "{}") }.getOrDefault(JSONObject())
            }) { Text("Actualiser") }
            OutlinedButton(onClick={playlistFilter="Toutes";folderFilter="Toutes";quickFilter="Toutes";watchFilter="Tous les statuts";query=""}) { Text("Réinitialiser") }
        }
        OutlinedTextField(value=query,onValueChange={query=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text("Rechercher une vidéo ou une chaîne")},leadingIcon={Icon(Icons.Default.Search,null)})
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            listOf("Toutes","Favoris","Historique").forEach { mode ->
                FilterChip(selected=quickFilter==mode,onClick={quickFilter=mode},label={Text(when(mode){"Favoris"->"★ Favoris (${favoriteVideoIds.size})";"Historique"->"Récemment ouvertes (${historyVideoIds.size})";else->"Toutes les vidéos"})})
            }
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            listOf("Tous les statuts","À regarder","En cours","Terminé").forEach { status ->
                FilterChip(selected=watchFilter==status,onClick={watchFilter=status},label={Text(status)})
            }
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            FilterChip(selected=playlistFilter=="Toutes",onClick={playlistFilter="Toutes"},label={Text("Toutes les playlists")})
            playlistNames.forEach { name -> FilterChip(selected=playlistFilter==name,onClick={playlistFilter=name},label={Text("$name (${playlistUrls(name).size})")}) }
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            FilterChip(selected=folderFilter=="Toutes",onClick={folderFilter="Toutes"},label={Text("Toutes (${allVideos.size})")})
            folders.forEach { folder -> FilterChip(selected=folderFilter==folder,onClick={folderFilter=folder},label={Text("$folder (${allVideos.count{it.folder==folder}})")}) }
        }
        Text("${shown.size} résultat(s)",color=Green)
        LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)) {
            items(shown,key={it.channelId+"|"+it.url}) { video ->
                val videoId=extractYouTubeVideoId(video.url)
                var menuExpanded by remember(video.channelId,video.url) { mutableStateOf(false) }
                var statusMenuExpanded by remember(video.channelId,video.url) { mutableStateOf(false) }
                Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=Panel)) {
                    Column(Modifier.padding(10.dp)) {
                        Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                            if(videoId!=null) AsyncImage(model="https://img.youtube.com/vi/$videoId/hqdefault.jpg",contentDescription="Miniature vidéo",modifier=Modifier.width(104.dp).height(60.dp))
                            Column(Modifier.weight(1f).padding(start=8.dp)) {
                                Text(video.channelName,fontWeight=FontWeight.Bold,color=Color.White)
                                Text(video.folder,color=Green,style=MaterialTheme.typography.bodySmall)
                                Text(video.url,maxLines=1,color=Color.LightGray,style=MaterialTheme.typography.labelSmall)
                            }
                            Button(onClick={play(video.url)}) { Text("Lire") }
                        }
                        val videoKey = extractYouTubeVideoId(video.url) ?: video.url
                        Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                            TextButton(onClick={selectedVideoDetails=video}) { Text("Fiche") }
                            TextButton(onClick={statusMenuExpanded=true}) { Text("Statut : ${watchStatuses.optString(videoKey, "À regarder")}") }
                            DropdownMenu(expanded=statusMenuExpanded,onDismissRequest={statusMenuExpanded=false}) {
                                listOf("À regarder","En cours","Terminé").forEach { status ->
                                    DropdownMenuItem(text={Text(status)},onClick={
                                        val updated=JSONObject(watchStatuses.toString()).put(videoKey,status)
                                        watchStatuses=updated
                                        videoPrefs.edit().putString("video_watch_statuses",updated.toString()).apply()
                                        statusMenuExpanded=false
                                    })
                                }
                            }
                        }
                        Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                            TextButton(onClick={
                                val next = favoriteVideoIds.toMutableSet()
                                if(videoKey in next) next.remove(videoKey) else next.add(videoKey)
                                favoriteVideoIds=next
                                videoPrefs.edit().putStringSet("video_favorites",next).apply()
                            }) { Text(if(videoKey in favoriteVideoIds) "★ Favori" else "☆ Favori") }
                            TextButton(onClick={
                                val next = historyVideoIds + videoKey
                                historyVideoIds=next
                                videoPrefs.edit().putStringSet("video_history",next).apply()
                            }) { Text(if(videoKey in historyVideoIds) "Déjà ouverte" else "Marquer ouverte") }
                            TextButton(onClick={menuExpanded=true}) { Text("Classer") }
                            TextButton(onClick={playTool(context,video.url,"LeafNotes")}) { Text("Notes") }
                            TextButton(onClick={playTool(context,video.url,"LeafChapters")}) { Text("Chapitres") }
                            TextButton(onClick={playTool(context,video.url,"LeafLinks")}) { Text("Liens") }
                            TextButton(onClick={playTool(context,video.url,"LeafTranscript")}) { Text("Texte") }
                            TextButton(onClick={playTool(context,video.url,"LeafTranslate")}) { Text("Traduire") }
                            TextButton(onClick={playTool(context,video.url,"LeafSummary")}) { Text("Résumé") }
                            TextButton(onClick={playTool(context,video.url,"LeafSubtitles")}) { Text("Sous-titres") }
                            TextButton(onClick={playTool(context,video.url,"LeafVocabulary")}) { Text("Vocabulaire") }
                            TextButton(onClick={playlistDialogVideo=video}) { Text("Playlist") }
                        }
                        val toolVideoKey = videoId ?: video.url
                        val hasNotes = !videoPrefs.getString("notes_$toolVideoKey", "").isNullOrBlank()
                        val hasChapters = !videoPrefs.getString("chapters_$toolVideoKey", "").isNullOrBlank()
                        val hasLinks = !videoPrefs.getString("links_$toolVideoKey", "").isNullOrBlank()
                        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                            if(hasNotes) Text("Notes ✓",color=Green,style=MaterialTheme.typography.labelSmall)
                            if(hasChapters) Text("Chapitres ✓",color=Green,style=MaterialTheme.typography.labelSmall)
                            if(hasLinks) Text("Liens ✓",color=Green,style=MaterialTheme.typography.labelSmall)
                            val hasTranscript=!videoPrefs.getString("transcript_$toolVideoKey","").isNullOrBlank()
                            val hasSummary=!videoPrefs.getString("summary_$toolVideoKey","").isNullOrBlank()
                            if(hasTranscript) Text("Transcription ✓",color=Green,style=MaterialTheme.typography.labelSmall)
                            if(hasSummary) Text("Résumé ✓",color=Green,style=MaterialTheme.typography.labelSmall)
                        }
                        Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                            Text("Chaîne : ${video.channelCategory}",color=Color.LightGray,style=MaterialTheme.typography.labelSmall,modifier=Modifier.weight(1f))
                            TextButton(onClick={openChannel(video.channelId)}) { Text("Fiche chaîne") }
                        }
                        DropdownMenu(expanded=menuExpanded,onDismissRequest={menuExpanded=false}) {
                            folders.forEach { folder -> DropdownMenuItem(text={Text(folder)},onClick={
                                val target=channels.firstOrNull{it.id==video.channelId}
                                if(target!=null) {
                                    val updatedFolders=runCatching{JSONObject(target.videoFolders)}.getOrDefault(JSONObject())
                                    updatedFolders.put(video.url,folder)
                                    saveChannels(channels.map{if(it.id==video.channelId)it.copy(videoFolders=updatedFolders.toString())else it})
                                }
                                menuExpanded=false
                            }) }
                        }
                    }
                }
            }
        }
    }
    selectedVideoDetails?.let { detailVideo ->
        val detailKey=extractYouTubeVideoId(detailVideo.url) ?: detailVideo.url
        AlertDialog(onDismissRequest={selectedVideoDetails=null},
            title={Text("Fiche vidéo")},
            text={Column(Modifier.heightIn(max=460.dp).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                Text(detailVideo.channelName,fontWeight=FontWeight.Bold,color=Green)
                Text("Catégorie : ${detailVideo.channelCategory}")
                Text("Classement : ${detailVideo.folder}")
                Text("Statut : ${watchStatuses.optString(detailKey,"À regarder")}")
                Text(if(detailKey in favoriteVideoIds) "★ Dans les favoris" else "☆ Pas dans les favoris")
                Text(if(detailKey in historyVideoIds) "Déjà ouverte" else "Pas encore ouverte")
                val lastOpened=historyTimestamps.optLong(detailKey,0L)
                Text(if(lastOpened>0L) "Dernière ouverture : ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(lastOpened))}" else "Aucune date d'ouverture enregistrée",color=Color.LightGray)
                Text("Lien : ${detailVideo.url}",style=MaterialTheme.typography.bodySmall)
                val notes=videoPrefs.getString("notes_$detailKey","").orEmpty()
                val chapters=videoPrefs.getString("chapters_$detailKey","").orEmpty()
                val links=videoPrefs.getString("links_$detailKey","").orEmpty()
                val transcript=videoPrefs.getString("transcript_$detailKey","").orEmpty()
                val translation=videoPrefs.getString("translation_$detailKey","").orEmpty()
                val summary=videoPrefs.getString("summary_$detailKey","").orEmpty()
                val subtitles=videoPrefs.getString("subtitles_$detailKey","").orEmpty()
                val vocabulary=videoPrefs.getString("vocabulary_$detailKey","").orEmpty()
                Text("Notes : ${if(notes.isBlank()) "Aucune" else notes}",style=MaterialTheme.typography.bodySmall)
                Text("Chapitres : ${if(chapters.isBlank()) "Aucun" else chapters}",style=MaterialTheme.typography.bodySmall)
                Text("Liens : ${if(links.isBlank()) "Aucun" else links}",style=MaterialTheme.typography.bodySmall)
                Text("Transcription : ${if(transcript.isBlank()) "Aucune" else transcript}",style=MaterialTheme.typography.bodySmall)
                Text("Traduction : ${if(translation.isBlank()) "Aucune" else translation}",style=MaterialTheme.typography.bodySmall)
                Text("Résumé : ${if(summary.isBlank()) "Aucun" else summary}",style=MaterialTheme.typography.bodySmall)
                Text("Sous-titres : ${if(subtitles.isBlank()) "Aucun" else subtitles}",style=MaterialTheme.typography.bodySmall)
                Text("Vocabulaire : ${if(vocabulary.isBlank()) "Aucun" else vocabulary}",style=MaterialTheme.typography.bodySmall)
            }},
            confirmButton={TextButton(onClick={selectedVideoDetails=null;play(detailVideo.url)}){Text("Lire")}},
            dismissButton={TextButton(onClick={selectedVideoDetails=null}){Text("Fermer")}})
    }
    if (createPlaylistDialog) AlertDialog(onDismissRequest={createPlaylistDialog=false}, title={Text("Créer une playlist commune")}, text={OutlinedTextField(value=newPlaylistName,onValueChange={newPlaylistName=it},label={Text("Nom de la playlist")},singleLine=true)}, confirmButton={TextButton(onClick={
        val name=newPlaylistName.trim()
        if(name.isNotBlank() && !globalPlaylists.has(name)) {
            val updated=JSONObject(globalPlaylists.toString()).put(name,JSONArray())
            savePlaylists(updated); playlistFilter=name
        }
        newPlaylistName=""; createPlaylistDialog=false
    }) { Text("Créer") }}, dismissButton={TextButton(onClick={createPlaylistDialog=false}) { Text("Annuler") }})
    if (managePlaylistsDialog) AlertDialog(
        onDismissRequest={managePlaylistsDialog=false},
        title={Text("Gérer les playlists")},
        text={Column(Modifier.heightIn(max=420.dp).verticalScroll(rememberScrollState())) {
            if(playlistNames.isEmpty()) Text("Aucune playlist créée.")
            playlistNames.forEach { name ->
                Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(name,fontWeight=FontWeight.SemiBold)
                        Text("${playlistUrls(name).size} vidéo(s)",color=Color.LightGray,style=MaterialTheme.typography.labelSmall)
                    }
                    TextButton(onClick={renamedPlaylistText=name;renamePlaylistName=name;managePlaylistsDialog=false}) { Text("Renommer") }
                    TextButton(onClick={deletePlaylistName=name;managePlaylistsDialog=false}) { Text("Supprimer",color=MaterialTheme.colorScheme.error) }
                }
            }
        }},
        confirmButton={TextButton(onClick={managePlaylistsDialog=false}) { Text("Fermer") }}
    )
    renamePlaylistName?.let { oldName ->
        AlertDialog(
            onDismissRequest={renamePlaylistName=null},
            title={Text("Renommer la playlist")},
            text={OutlinedTextField(value=renamedPlaylistText,onValueChange={renamedPlaylistText=it},singleLine=true,label={Text("Nouveau nom")})},
            confirmButton={TextButton(onClick={
                val newName=renamedPlaylistText.trim()
                if(newName.isNotBlank() && (newName==oldName || !globalPlaylists.has(newName))) {
                    val updated=JSONObject()
                    globalPlaylists.keys().forEach { key -> updated.put(if(key==oldName)newName else key,globalPlaylists.optJSONArray(key) ?: JSONArray()) }
                    savePlaylists(updated)
                    if(playlistFilter==oldName) playlistFilter=newName
                }
                renamePlaylistName=null
            }) { Text("Enregistrer") }},
            dismissButton={TextButton(onClick={renamePlaylistName=null}) { Text("Annuler") }}
        )
    }
    deletePlaylistName?.let { name ->
        AlertDialog(
            onDismissRequest={deletePlaylistName=null},
            title={Text("Supprimer « $name » ?")},
            text={Text("Seule la playlist sera supprimée. Les vidéos resteront dans leurs chaînes et dans la bibliothèque.")},
            confirmButton={TextButton(onClick={
                val updated=JSONObject()
                globalPlaylists.keys().asSequence().filterNot{it==name}.forEach{key->updated.put(key,globalPlaylists.optJSONArray(key)?:JSONArray())}
                savePlaylists(updated)
                if(playlistFilter==name) playlistFilter="Toutes"
                deletePlaylistName=null
            }) { Text("Supprimer") }},
            dismissButton={TextButton(onClick={deletePlaylistName=null}) { Text("Annuler") }}
        )
    }
    playlistDialogVideo?.let { video ->
        AlertDialog(onDismissRequest={playlistDialogVideo=null}, title={Text("Ajouter à une playlist")}, text={Column(Modifier.heightIn(max=360.dp).verticalScroll(rememberScrollState())) {
            if(playlistNames.isEmpty()) Text("Crée d’abord une playlist commune.")
            playlistNames.forEach { name ->
                val selectedUrl=video.url in playlistUrls(name)
                Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked=selectedUrl,onCheckedChange={checked->
                        val updated=JSONObject(globalPlaylists.toString())
                        val urls=playlistUrls(name).toMutableSet()
                        if(checked) urls.add(video.url) else urls.remove(video.url)
                        updated.put(name,JSONArray(urls.toList())); savePlaylists(updated)
                    })
                    Text(name)
                }
            }
        }}, confirmButton={TextButton(onClick={playlistDialogVideo=null}) { Text("Terminé") }})
    }
}

private fun extractYouTubeVideoId(raw: String): String? {
    val value = raw.trim()
    if (value.matches(Regex("^[A-Za-z0-9_-]{11}$"))) return value
    return runCatching {
        val uri = Uri.parse(if (value.startsWith("http://") || value.startsWith("https://")) value else "https://$value")
        val host = uri.host.orEmpty().lowercase()
        val path = uri.path.orEmpty().trim('/').split('/').filter { it.isNotBlank() }
        when {
            host == "youtu.be" || host.endsWith(".youtu.be") -> path.firstOrNull()
            path.firstOrNull() in setOf("embed", "shorts", "live", "v") -> path.getOrNull(1)
            uri.getQueryParameter("v") != null -> uri.getQueryParameter("v")
            else -> null
        }?.takeIf { it.matches(Regex("^[A-Za-z0-9_-]{11}$")) }
    }.getOrNull()
}

private fun playTool(context: android.content.Context, url: String, tool: String) {
    context.startActivity(Intent(context, LeafPlayerActivity::class.java)
        .putExtra("video_url", url)
        .putExtra("open_tool", tool))
}
