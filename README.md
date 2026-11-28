# LeafYoutubeRecovery 0.5.0

Application Android native de gestion de chaînes et de vidéos YouTube. Identité LeafColour : bleu nuit #0F1B2B et vert #22C55E.

## Bibliothèque de chaînes
- Catégories de chaînes personnalisables : ajouter, renommer, supprimer.
- Fiches avec description, notes, favoris, playlists et collections.
- Recherche locale et raccourci de recherche YouTube.
- Export/import JSON.

## Bibliothèque de vidéos (0.5.0)
- Plusieurs liens vidéo par chaîne.
- Miniatures YouTube lorsque l'identifiant vidéo est extrait du lien.
- Lecture de chaque vidéo dans LeafPlayer, intégré avec le lecteur officiel YouTube (connexion Internet requise).
- Classement individuel dans : À classer, À regarder, À revoir, Favoris, Tutoriels, Musique, Divertissement, Jeux vidéo, Autres.
- Filtres par catégorie et sauvegarde des classements avec la fiche de chaîne.
- Les classements sont inclus dans l'export/import JSON.

## Modules Leaf
Barre inférieure horizontale configurable : LeafPlayer, LeafTranscript, LeafTranslate, LeafSummary, LeafSubtitles, LeafChapters, LeafLinks, LeafVocabulary, LeafNotes, LeafSimilar, LeafDiscover, LeafPlaylist, LeafGraph, LeafStats, LeafSound, LeafColour, LeafBridge, LeafTracker, LeafWallpaper, LeafSearch, LeafExport. Plusieurs sont encore des espaces d'intégration et ne disposent pas tous d'un moteur complet.

## Limites
- Lecture en streaming via le lecteur officiel ; aucun téléchargement vidéo ni lecture hors ligne.
- Les titres des vidéos et descriptions de chaîne ne sont pas encore récupérés automatiquement.
- Transcription, traduction, résumé et recommandations restent à connecter.

## Build
JDK 17, Gradle 8.9, Android SDK 35. `gradle :app:assembleDebug`.
Application ID : `com.leafvibe.youtuberecovery`


## Version 0.6.0 — Bibliothèque vidéo globale
- Bibliothèque unique regroupant les vidéos enregistrées dans toutes les chaînes.
- Recherche par URL ou nom de chaîne.
- Filtres par catégorie vidéo : à classer, à regarder, à revoir, favoris, tutoriels, musique, divertissement, jeux vidéo et autres.
- Classement d'une vidéo depuis la bibliothèque centrale, avec sauvegarde dans la fiche de sa chaîne.
- Lecture depuis la bibliothèque via LeafPlayer.


## Version 0.6.1 — Connexions entre les outils vidéo
- Depuis la bibliothèque vidéo globale, ouvrir directement les notes, chapitres ou liens d'une vidéo.
- LeafPlayer reçoit l'outil demandé et ouvre directement son espace associé à la vidéo.
- Les notes, chapitres et liens restent stockés par identifiant de vidéo dans les préférences locales.
