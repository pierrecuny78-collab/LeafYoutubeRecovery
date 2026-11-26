# LeafYoutubeRecovery

Application Android native de gestion personnelle de chaînes YouTube, avec lecteur intégré LeafPlayer.

## Version 0.2.0 — LeafPlayer et outils modulaires
- Lecteur vidéo YouTube intégré via le lecteur officiel embarqué (WebView / iframe).
- Depuis la fiche d'une chaîne, lancement de la première URL vidéo enregistrée dans LeafPlayer.
- Barre « Mes outils vidéo » en bas du lecteur.
- Ajout et suppression des modules disponibles, avec sélection conservée localement : LeafTranslate, LeafTranscript, LeafSummary, LeafChapters, LeafLinks, LeafVocabulary, LeafNotes et LeafSimilar.
- Identité visuelle LeafColour : bleu nuit #0F1B2B et vert #22C55E.

**Important :** cette étape met en place le lecteur et le gestionnaire de modules. Les fonctions de traduction, transcription, résumé et recommandations ne sont pas encore implémentées ; elles nécessiteront leurs propres services/intégrations. Le lecteur utilise le lecteur YouTube officiel et ne télécharge pas les vidéos.

## Fonctionnalités de bibliothèque
- Catégories personnalisables : ajouter, renommer et supprimer.
- La suppression d'une catégorie déplace ses chaînes vers « Non classées ».
- Ajout manuel de chaînes existantes, recherche locale et liens vidéo/playlists/notes.
- Données conservées localement sur l'appareil.

## Compilation
JDK 17 et Android SDK 35 requis.
`gradle :app:assembleDebug`
APK : `app/build/outputs/apk/debug/app-debug.apk`

Package Android : `com.leafvibe.youtuberecovery`
