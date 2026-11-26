# LeafYoutubeRecovery — 0.3.0

Application Android native de gestion de chaînes et vidéos YouTube, identité LeafColour (#0F1B2B / #22C55E).

## Fonctionnalités présentes

### Bibliothèque
- Création, renommage et suppression de catégories.
- Déplacement automatique des chaînes vers « Non classées » lors de la suppression d'une catégorie.
- Ajout manuel de chaînes, description personnalisée, catégorie, collection et favoris.
- Recherche locale par nom et URL ; raccourci de recherche publique YouTube.
- Fiche de chaîne avec notes, playlists et plusieurs URL vidéo.
- Aperçus miniatures YouTube (hqdefault) lorsque l'URL contient un identifiant vidéo reconnu.
- Lecture individuelle dans LeafPlayer.
- Export de bibliothèque via partage Android au format JSON ; import depuis un JSON collé dans l'application.

### LeafPlayer et modules
- Lecture intégrée via iframe du lecteur YouTube officiel.
- Barre horizontale d'outils vidéo personnalisable.
- Préférences des outils synchronisées entre accueil et lecteur.
- LeafNotes, LeafChapters et LeafLinks : éditeurs de texte enregistrés localement par identifiant de vidéo.
- Catalogue : LeafPlayer, LeafTranscript, LeafTranslate, LeafSummary, LeafChapters, LeafLinks, LeafVocabulary, LeafNotes, LeafSimilar, LeafDiscover, LeafPlaylist, LeafGraph et LeafStats.

## Limites / intégrations restantes
- Les noms et descriptions des chaînes sont saisis manuellement ; l'import automatique de métadonnées de chaîne nécessite une intégration YouTube Data API et une clé API.
- Les miniatures sont obtenues depuis l'URL de miniature YouTube ; les titres vidéo ne sont pas encore récupérés automatiquement.
- LeafTranscript, LeafTranslate, LeafSummary, LeafVocabulary, LeafSimilar, LeafDiscover, LeafPlaylist, LeafGraph et LeafStats sont des entrées de catalogue / fiches explicatives, pas encore des moteurs complets.
- Pas de téléchargement vidéo ni lecture hors ligne. LeafPlayer s'appuie sur le lecteur officiel YouTube et nécessite Internet.
- Les données sont stockées localement dans les préférences Android ; l'export JSON permet une sauvegarde manuelle. La sauvegarde ZIP et le stockage SD restent à faire.

## Compilation
JDK 17, Gradle 8.9, Android SDK 35.
`gradle :app:assembleDebug`
APK : `app/build/outputs/apk/debug/app-debug.apk`
Application ID : `com.leafvibe.youtuberecovery`
