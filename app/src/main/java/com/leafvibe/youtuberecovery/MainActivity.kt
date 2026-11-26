package com.leafvibe.youtuberecovery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.*
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
data class Channel(val id:String,val name:String,val url:String,val category:String,val notes:String="",val videos:String="",val playlists:String="",val favorite:Boolean=false,val collection:String="",val description:String="")

class MainActivity:ComponentActivity(){
 private val prefs by lazy{getSharedPreferences("leaf_youtube_recovery",MODE_PRIVATE)}
 private fun readCategories():List<String> = runCatching{val a=JSONArray(prefs.getString("categories",null)?:return defaults);(0 until a.length()).map{a.getString(it)}}.getOrDefault(defaults)
 private fun saveCategories(v:List<String>){prefs.edit().putString("categories",JSONArray(v).toString()).apply()}
 private fun load():List<Channel> = runCatching{val a=JSONArray(prefs.getString("channels","[]"));(0 until a.length()).map{a.getJSONObject(it).let{o->Channel(o.getString("id"),o.getString("name"),o.getString("url"),o.optString("category","Non classées"),o.optString("notes"),o.optString("videos"),o.optString("playlists"),o.optBoolean("favorite",false),o.optString("collection"),o.optString("description"))}}}.getOrDefault(emptyList())
 private fun save(v:List<Channel>){val a=JSONArray();v.forEach{c->a.put(JSONObject().put("id",c.id).put("name",c.name).put("url",c.url).put("category",c.category).put("notes",c.notes).put("videos",c.videos).put("playlists",c.playlists).put("favorite",c.favorite).put("collection",c.collection).put("description",c.description))};prefs.edit().putString("channels",a.toString()).apply()}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{
  var cats by remember{mutableStateOf(readCategories())};var channels by remember{mutableStateOf(load())};var selected by remember{mutableStateOf<Channel?>(null)}
  MaterialTheme(colorScheme=darkColorScheme(primary=Green,background=Navy,surface=Panel,onPrimary=Navy,onBackground=Color.White,onSurface=Color.White)){
   if(selected==null) Home(cats,channels,{cats=it;saveCategories(it)},{channels=it;save(it)},{selected=it},{q->startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/results?search_query="+Uri.encode(q))))})
   else Detail(selected!!,cats,{selected=null},{u->channels=channels.map{if(it.id==u.id)u else it};save(channels);selected=u},{channels=channels.filterNot{it.id==selected!!.id};save(channels);selected=null},{u->startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(if(u.startsWith("http"))u else "https://www.youtube.com/$u")))})
  }
 }}
}

@Composable private fun Home(cats:List<String>,channels:List<Channel>,saveCats:(List<String>)->Unit,saveChannels:(List<Channel>)->Unit,open:(Channel)->Unit,search:(String)->Unit){
 var filter by remember{mutableStateOf("Toutes")};var query by remember{mutableStateOf("")}
 var catDialog by remember{mutableStateOf(false)};var editing by remember{mutableStateOf<String?>(null)};var catName by remember{mutableStateOf("")}
 var addDialog by remember{mutableStateOf(false)};var name by remember{mutableStateOf("")};var url by remember{mutableStateOf("")};var newCat by remember{mutableStateOf("Non classées")}
 val shown=channels.filter{(filter=="Toutes"||it.category==filter)&&(query.isBlank()||it.name.contains(query,true)||it.url.contains(query,true))}
 Scaffold(containerColor=Navy,floatingActionButton={FloatingActionButton(onClick={addDialog=true},containerColor=Green,contentColor=Navy){Icon(Icons.Default.Add,"Ajouter")}}){p->
 Column(Modifier.fillMaxSize().padding(p).padding(16.dp)){
 Text("LEAF COLOUR",color=Green,fontWeight=FontWeight.Bold);Text("YoutubeRecovery",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text("Bibliothèque de chaînes YouTube",color=Color.LightGray)
 OutlinedTextField(query,{query=it},Modifier.fillMaxWidth(),placeholder={Text("Rechercher mes chaînes")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true)
 TextButton(onClick={search(query)},enabled=query.isNotBlank()){Text("Rechercher sur YouTube")}
 Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){Text("Catégories",style=MaterialTheme.typography.titleLarge);Spacer(Modifier.weight(1f));TextButton(onClick={editing=null;catName="";catDialog=true}){Icon(Icons.Default.Add,null);Text("Ajouter")}}
 LazyColumn(Modifier.heightIn(max=210.dp)){
 item{FilterChip(filter=="Toutes",{filter="Toutes"},label={Text("Toutes ("+channels.size+")")})}
 items(cats){c->Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){FilterChip(selected=filter==c,onClick={filter=c},label={Text(c+" ("+channels.count{it.category==c}+")")},modifier=Modifier.weight(1f));IconButton(onClick={editing=c;catName=c;catDialog=true}){Icon(Icons.Default.Edit,"Renommer")};if(c!="Non classées")IconButton(onClick={saveChannels(channels.map{if(it.category==c)it.copy(category="Non classées")else it});saveCats(cats.filterNot{it==c});if(filter==c)filter="Non classées"}){Icon(Icons.Default.Delete,"Supprimer")}}}
 }
 Text("Chaînes ("+shown.size+")",style=MaterialTheme.typography.titleLarge)
 LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){items(shown,key={it.id}){c->Card(Modifier.fillMaxWidth().clickable{open(c)},colors=CardDefaults.cardColors(containerColor=Panel)){Column(Modifier.padding(14.dp)){Text(c.name,fontWeight=FontWeight.Bold);Text(c.category,color=Green);Text(c.url,color=Color.LightGray,maxLines=1);if(c.collection.isNotBlank())Text("Collection : "+c.collection,color=Color.LightGray)};IconButton(onClick={saveChannels(channels.map{if(it.id==c.id)it.copy(favorite=!it.favorite)else it})}){Icon(if(c.favorite)Icons.Default.Favorite else Icons.Default.FavoriteBorder,"Favori",tint=if(c.favorite)Green else Color.LightGray)}}}}
 }}
 if(catDialog)AlertDialog(onDismissRequest={catDialog=false},title={Text(if(editing==null)"Ajouter une catégorie" else "Renommer la catégorie")},text={OutlinedTextField(catName,{catName=it},label={Text("Nom de catégorie")})},confirmButton={TextButton(onClick={val n=catName.trim();if(n.isNotBlank()&&n!="Non classées"){if(editing==null){if(n !in cats)saveCats(cats+n)}else if(n !in cats){saveCats(cats.map{if(it==editing)n else it});saveChannels(channels.map{if(it.category==editing)it.copy(category=n)else it});if(filter==editing)filter=n}};catDialog=false}){Text("Enregistrer")}},dismissButton={TextButton(onClick={catDialog=false}){Text("Annuler")}})
 if(addDialog)AlertDialog(onDismissRequest={addDialog=false},title={Text("Ajouter une chaîne existante")},text={Column{OutlinedTextField(name,{name=it},label={Text("Nom")});OutlinedTextField(url,{url=it},label={Text("URL ou identifiant YouTube")});Text("Catégorie");cats.forEach{c->Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){RadioButton(newCat==c,{newCat=c});Text(c)}};TextButton(onClick={search(name)},enabled=name.isNotBlank()){Text("Chercher sur YouTube")}}},confirmButton={TextButton(enabled=name.isNotBlank()&&url.isNotBlank(),onClick={saveChannels(channels+Channel(System.currentTimeMillis().toString(),name.trim(),url.trim(),newCat));name="";url="";addDialog=false}){Text("Ajouter")}},dismissButton={TextButton(onClick={addDialog=false}){Text("Annuler")}})
}

@Composable private fun Detail(c:Channel,cats:List<String>,back:()->Unit,save:(Channel)->Unit,delete:()->Unit,openLink:(String)->Unit){
 val context=LocalContext.current
 var category by remember(c.id){mutableStateOf(c.category)};var notes by remember(c.id){mutableStateOf(c.notes)};var videos by remember(c.id){mutableStateOf(c.videos)};var playlists by remember(c.id){mutableStateOf(c.playlists)};var collection by remember(c.id){mutableStateOf(c.collection)};var description by remember(c.id){mutableStateOf(c.description)};var favorite by remember(c.id){mutableStateOf(c.favorite)}
 Column(Modifier.fillMaxSize().background(Navy).padding(16.dp)){TextButton(onClick=back){Text("← Retour")};Text(c.name,style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text(c.url,color=Color.LightGray);if(c.description.isNotBlank())Text(c.description,color=Color.LightGray);Button(onClick={openLink(c.url)}){Text("Ouvrir sur YouTube")}
 LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){item{Text("Description de la chaîne",color=Green);OutlinedTextField(description,{description=it},Modifier.fillMaxWidth(),minLines=3,label={Text("Présentation, contenu, thèmes…")})};item{Text("Catégorie",color=Green);cats.forEach{RadioButton(category==it,{category=it});Text(it)}};item{Text("Vidéos — un lien par ligne",color=Green);OutlinedTextField(videos,{videos=it},Modifier.fillMaxWidth(),minLines=3);val firstVideo=videos.lines().firstOrNull{it.isNotBlank()};Button(onClick={if(firstVideo!=null)context.startActivity(Intent(context,LeafPlayerActivity::class.java).putExtra("video_url",firstVideo.trim()))},enabled=firstVideo!=null,modifier=Modifier.fillMaxWidth()){Text("Lire la première vidéo avec LeafPlayer")}};item{Text("Collection personnelle",color=Green);OutlinedTextField(collection,{collection=it},Modifier.fillMaxWidth())};item{Row(verticalAlignment=androidx.compose.ui.Alignment.CenterVertically){Checkbox(favorite,{favorite=it});Text("Ajouter aux favoris")}};item{Text("Playlists",color=Green);OutlinedTextField(playlists,{playlists=it},Modifier.fillMaxWidth(),minLines=2)};item{Text("LeafNotes de cette chaîne",color=Green);OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),minLines=5)};item{Button(onClick={save(c.copy(category=category,notes=notes,videos=videos,playlists=playlists,favorite=favorite,collection=collection,description=description))},Modifier.fillMaxWidth()){Text("Enregistrer")};OutlinedButton(onClick=delete,Modifier.fillMaxWidth()){Text("Supprimer cette fiche")}}}
 }
}
