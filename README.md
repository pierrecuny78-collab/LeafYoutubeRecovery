# LeafYoutubeRecovery

Application Android native de gestion personnelle de chaînes YouTube.

## Version 0.1.0 — base initiale
- Catégories personnalisables : ajouter, renommer et supprimer.
- La suppression d'une catégorie déplace ses chaînes vers « Non classées » : aucune chaîne n'est supprimée.
- Ajout manuel de chaînes YouTube existantes et classement par catégorie.
- Déplacement d'une chaîne depuis sa fiche.
- Fiche individuelle : liens vidéo, playlists et notes personnelles LeafNotes.
- Recherche dans la bibliothèque et ouverture de la recherche YouTube.
- Données conservées localement sur l'appareil.
- Identité visuelle LeafColour : bleu nuit #0F1B2B et vert #22C55E.
- Icône vectorielle Android incluse.

## Limites de cette première version
La recherche YouTube s'ouvre dans le navigateur. L'import automatique des métadonnées, des vidéos et playlists publiques, ainsi que les suggestions de chaînes similaires, nécessitent l'intégration de YouTube Data API v3. L'application ne télécharge aucune vidéo et ne peut pas restaurer une chaîne supprimée de YouTube.

## Compilation
JDK 17 et Android SDK 35 requis.
`gradle :app:assembleDebug`
APK : `app/build/outputs/apk/debug/app-debug.apk`

Package Android : `com.leafvibe.youtuberecovery`
