# LeafYoutubeRecovery 0.9.0

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


## Version 0.7.0 — Playlists communes
- Création de playlists globales qui regroupent des vidéos de plusieurs chaînes.
- Ajout et retrait d'une vidéo d'une ou plusieurs playlists depuis sa carte.
- Filtrage de la bibliothèque par playlist.
- Export JSON version 3 incluant les playlists communes.
- Import JSON avec fusion des playlists et déduplication des URL.


## Version 0.7.1 — Fiabilité des sauvegardes
- Correction du format JSON des playlists globales dans les exports.
- Import compatible avec les exports précédents où les playlists étaient sérialisées sous forme de texte JSON.


## Version 0.8.0 — Connexions vidéo et sauvegarde complète
- Gestion des playlists communes : création, renommage, suppression et classement des vidéos de plusieurs chaînes.
- Reconnaissance plus fiable des liens YouTube standards, courts, Shorts, directs et intégrés.
- Export JSON des notes, chapitres et liens enregistrés par vidéo.
- Import fusionné des outils vidéo : les notes déjà présentes sur l'appareil ne sont pas écrasées.


## Version 0.8.1 — Correction de compilation
- Correction du parcours des clés JSON lors de la suppression d'une playlist.
- Build Android relancé après correction.


## Version 0.9.0 — Navigation et outils vidéo reliés
- Depuis chaque vidéo d'une fiche de chaîne, ouvrir directement ses Notes, Chapitres ou Liens dans LeafPlayer.
- Depuis la bibliothèque vidéo globale, revenir directement à la fiche de la chaîne d'origine.
- Afficher les indicateurs Notes, Chapitres et Liens lorsqu'un contenu existe déjà pour la vidéo.
- Utiliser le même extracteur d'identifiant YouTube dans les fiches et la bibliothèque, y compris pour les URL Shorts, live, embed et youtu.be.


## Version 0.9.1 — Favoris et historique vidéo
- Ajouter et retirer une vidéo des favoris depuis la bibliothèque globale.
- Filtrer la bibliothèque par favoris et historique des vidéos ouvertes.
- Enregistrer automatiquement l'ouverture d'une vidéo dans l'historique du lecteur.
- Actualiser les états après retour du lecteur.
- Inclure favoris et historique dans les sauvegardes JSON (format 5), avec fusion non destructive à l'import.


## Version 0.9.2 — Statuts de visionnage
- Statut individuel par vidéo : À regarder, En cours ou Terminé.
- Filtres croisés avec les favoris, l'historique, les catégories et les playlists.
- Sauvegarde JSON des statuts avec fusion non destructive à l'import.


## Version 0.9.3 — Fiches et historique intelligent
- Fiche vidéo regroupant chaîne, catégorie, classement, statut, favoris, lien, notes, chapitres et liens associés.
- Date de dernière ouverture enregistrée automatiquement par LeafPlayer.
- Filtre Historique trié de la vidéo la plus récemment ouverte à la plus ancienne.
- Compteurs des vidéos en cours et terminées.
- Export/import JSON des dates d'historique avec fusion conservant la date la plus récente.


## Version 0.9.4 — Outils vidéo réellement utilisables
- Les modules Transcription, Traduction, Résumé, Sous-titres et Vocabulaire ouvrent désormais un éditeur de texte sauvegardé par identifiant vidéo.
- Les outils complémentaires sont accessibles depuis les fiches de vidéos dans la bibliothèque globale et les fiches de chaînes.
- Les fiches vidéo affichent également les contenus enregistrés dans ces nouveaux outils.
- L’export/import JSON inclut tous les contenus texte par vidéo (notes, chapitres, liens, transcription, traduction, résumé, sous-titres et vocabulaire).
- Les modules de la barre d’accueil orientent vers la bibliothèque vidéo pour sélectionner un contenu. Les fonctions automatiques externes ne sont pas simulées : elles restent explicitement identifiées comme non intégrées.


## Version 0.9.5 — Connexions des modules internes
- Les modules vidéo de la barre Leaf ouvrent leur éditeur après sélection d'une vidéo dans la bibliothèque.
- LeafStats affiche les statistiques locales (chaînes, catégories, vidéos, favoris, historique, statuts et playlists).
- LeafGraph présente les relations entre catégories et chaînes enregistrées.
- LeafSimilar regroupe les chaînes par catégorie pour faciliter leur comparaison.
- LeafDiscover lance une recherche YouTube depuis son écran ; LeafSearch recherche localement dans les chaînes enregistrées.
- LeafPlaylist ouvre le gestionnaire de playlists multi-chaînes existant.
- Les applications Leaf autonomes restent à connecter via leurs identifiants Android officiels : aucune connexion fictive n'est affichée.


## Version 0.9.6 — Fiabilisation des chaînes
- Validation des liens de chaîne YouTube lors de l'ajout (URL de chaîne, @handle ou ID UC…).
- Normalisation des URL de chaînes et refus des doublons exacts.
- Recherche étendue aux descriptions et notes des chaînes.
- Les liens non reconnus affichent une explication au lieu d'enregistrer une fiche inutilisable.
