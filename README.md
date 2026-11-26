# LeafYoutubeRecovery

Application Android native de gestion de chaînes YouTube, avec identité visuelle LeafColour.

## Base améliorée — travail en cours
- Catégories personnalisables : ajouter, renommer et supprimer.
- Lorsqu'une catégorie est supprimée, ses chaînes sont déplacées vers « Non classées ».
- Ajout manuel de chaînes et classement par catégorie.
- Favoris par chaîne.
- Collections personnelles par chaîne.
- Fiche chaîne : vidéos (liens un par ligne), playlists et notes.
- Ouverture des vidéos enregistrées dans LeafPlayer.
- LeafPlayer utilise le lecteur YouTube officiel intégré et ne télécharge pas les vidéos.
- Gestion des outils LeafPlayer avec mémorisation locale des modules activés.
- Recherche YouTube externe et recherche dans la bibliothèque.
- Données enregistrées localement avec compatibilité de lecture des anciennes fiches.

## Limites connues
- Les modules LeafTranscript, LeafTranslate, LeafSummary, LeafChapters, LeafLinks, LeafVocabulary, LeafNotes et LeafSimilar sont des emplacements de modules ; leurs fonctions avancées doivent encore être développées.
- Les chaînes et vidéos sont ajoutées manuellement : l'import automatique des métadonnées nécessite une intégration YouTube Data API.
- L'export JSON/ZIP, l'import/restauration, les sous-catégories, la recherche avancée et la découverte automatique restent à faire.
- Le lecteur conserve les éléments et contrôles du lecteur YouTube officiel.

## Compilation
JDK 17 et Android SDK 35.
`gradle :app:assembleDebug`
APK : `app/build/outputs/apk/debug/app-debug.apk`

Package : `com.leafvibe.youtuberecovery`
