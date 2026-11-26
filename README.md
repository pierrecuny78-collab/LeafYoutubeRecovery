# LeafYoutubeRecovery

Application Android native de gestion de chaînes YouTube, identité visuelle LeafColour (bleu nuit #0F1B2B et vert #22C55E).

## Version 0.2.0 — bibliothèque enrichie et outils Leaf

### Bibliothèque
- Catégories personnalisables : ajouter, renommer, supprimer.
- Lorsqu'une catégorie est supprimée, ses chaînes sont déplacées vers « Non classées ».
- Ajout manuel de chaînes et classement par catégorie.
- Recherche locale et raccourci de recherche YouTube.
- Favoris, collections personnelles et descriptions éditables par chaîne.
- Fiche chaîne : vidéos (URL une par ligne), playlists et notes.
- Lecture de chaque URL vidéo dans LeafPlayer.
- Sauvegarde JSON par partage de texte et import depuis JSON collé dans l'application.

### Espace applications Leaf
- Sélection des modules visibles sur l'accueil et mémorisation locale.
- Outils disponibles : LeafPlayer, LeafTranscript, LeafTranslate, LeafSummary, LeafChapters, LeafLinks, LeafVocabulary, LeafNotes, LeafSimilar, LeafDiscover, LeafPlaylist, LeafGraph et LeafStats.
- Barre horizontale configurable dans LeafPlayer.
- LeafNotes, LeafChapters et LeafLinks disposent d'un éditeur de texte sauvegardé localement par vidéo.

### Lecture
- LeafPlayer embarque le lecteur officiel YouTube via WebView/iframe.
- Les vidéos ne sont pas téléchargées et la lecture hors ligne n'est pas proposée.

## Limites honnêtes
- Les descriptions et les liens des chaînes sont saisis manuellement.
- Les titres et miniatures YouTube ne sont pas encore importés automatiquement.
- LeafTranscript, LeafTranslate, LeafSummary, LeafVocabulary, LeafSimilar, LeafDiscover, LeafPlaylist, LeafGraph et LeafStats nécessitent encore leurs intégrations et traitements dédiés. Leur présence dans la barre ne signifie pas que leur moteur est déjà actif.
- L'import JSON actuel fusionne les chaînes par identifiant ; l'export se fait via la feuille de partage Android.

## Compilation
JDK 17, Gradle 8.9 et Android SDK 35.
`gradle :app:assembleDebug`
APK : `app/build/outputs/apk/debug/app-debug.apk`

Package : `com.leafvibe.youtuberecovery`
