# LeafYoutubeRecovery roadmap

## 0.1.0 — Bibliothèque de chaînes
- [x] Catégories : ajouter, renommer et supprimer
- [x] Préserver les chaînes en les déplaçant vers « Non classées »
- [x] Ajouter une chaîne existante et la classer
- [x] Notes, liens vidéo et playlists par chaîne
- [x] Icône Android LeafColour

## 0.2.0 — LeafPlayer et gestionnaire de modules
- [x] Lecteur YouTube intégré via le lecteur officiel embarqué
- [x] Lancement d'une vidéo enregistrée depuis la fiche de chaîne
- [x] Barre d'outils en bas du lecteur
- [x] Ajouter/supprimer les modules et mémoriser la sélection
- [ ] Afficher toutes les vidéos d'une chaîne sous forme de liste de lecture
- [ ] Ouvrir les fiches des modules et gérer leurs paramètres

## 0.3.0 — Analyse et enrichissement vidéo
- [ ] LeafTranscript : récupérer/afficher les transcriptions disponibles et gérer les langues
- [ ] LeafTranslate : traduire les sous-titres et métadonnées
- [ ] LeafSummary : résumés et points clés à partir d'une transcription accessible
- [ ] LeafChapters : chapitres et repères temporels
- [ ] LeafLinks : classer les liens de description
- [ ] LeafVocabulary : enregistrer vocabulaire et expressions
- [ ] LeafNotes : notes horodatées reliées à LeafNotes

## 0.4.0 — Découverte
- [ ] YouTube Data API v3 configurable
- [ ] Import des métadonnées, vidéos et playlists publiques
- [ ] Chaînes et vidéos similaires
- [ ] LeafGraph : relations entre chaînes et créateurs

## 0.6.0 — Bibliothèque vidéo centrale
- [x] Regrouper les vidéos de toutes les chaînes dans un écran unique
- [x] Rechercher une vidéo par URL ou par chaîne
- [x] Filtrer par catégorie vidéo
- [x] Modifier le classement depuis la bibliothèque globale
- [x] Ouvrir une vidéo directement dans LeafPlayer

## 0.7.0 — Playlists communes
- [x] Créer des playlists partagées entre plusieurs chaînes
- [x] Ajouter ou retirer une vidéo d’une playlist depuis sa carte
- [x] Filtrer la bibliothèque par playlist
- [x] Inclure les playlists dans l’export JSON
- [x] Restaurer et fusionner les playlists sans dupliquer les URL

## 0.5.0 — Fiabilité et personnalisation
- [ ] Import/export JSON et sauvegarde ZIP
- [ ] Room, migrations, sauvegardes et restauration
- [ ] Favoris, collections, historique et recherche globale
- [ ] Paramètres des modules et stockage local configurable

## Contraintes
- Pas de téléchargement ni de mise à disposition hors ligne des vidéos YouTube.
- Conserver le lecteur officiel, ses commandes et ses mentions.
- Les modules d'analyse ne doivent pas masquer ni modifier les commandes du lecteur.


## 0.7.1 — Fiabilité
- [x] Exporter les playlists communes comme objet JSON structuré
- [x] Accepter les anciens exports de playlists au format texte JSON


## 0.8.0 — Connexions et sauvegardes
- [x] Renommer et supprimer une playlist sans supprimer les vidéos sources
- [x] Reconnaître les URL YouTube youtu.be, watch, embed, Shorts et live
- [x] Exporter les notes, chapitres et liens associés à chaque vidéo
- [x] Restaurer les données vidéo sans écraser les données locales existantes


## 0.8.1 — Correction
- [x] Corriger l'itération des playlists lors de leur suppression


## 0.9.0 — Navigation contextuelle
- [x] Ouvrir Notes, Chapitres et Liens directement depuis une vidéo d'une fiche chaîne
- [x] Revenir à la fiche chaîne d'origine depuis la bibliothèque globale
- [x] Signaler les notes, chapitres et liens déjà renseignés sur les cartes vidéo
- [x] Uniformiser l'extraction des identifiants YouTube dans les fiches et la bibliothèque
- [ ] Synchroniser les modifications des outils avec les indicateurs de la bibliothèque au retour du lecteur
- [ ] Ajouter une fiche vidéo détaillée avec historique, favoris et statut de visionnage


## 0.9.1 — Suivi des vidéos
- [x] Favoris indépendants des catégories de rangement
- [x] Historique des vidéos ouvertes automatiquement depuis LeafPlayer
- [x] Filtres Favoris et Historique dans la bibliothèque globale
- [x] Actualisation des états après lecture
- [x] Export/import des favoris et de l'historique sans écraser les données locales
- [ ] Historique trié par date avec reprise de lecture
- [ ] Statut de visionnage manuel (à voir / en cours / terminé)


## 0.9.2 — Suivi de visionnage
- [x] Statut individuel par vidéo : à regarder, en cours, terminé
- [x] Filtrer la bibliothèque selon le statut de visionnage
- [x] Combiner les statuts avec les favoris, l'historique, les catégories et les playlists
- [x] Exporter et fusionner les statuts dans les sauvegardes JSON
- [ ] Historique ordonné par date et reprise de lecture
- [ ] Fiche vidéo détaillée avec métadonnées et statistiques


## 0.9.3 — Fiches vidéo et historique intelligent
- [x] Enregistrer la date/heure de dernière ouverture de chaque vidéo
- [x] Trier l'historique par ouverture la plus récente
- [x] Fiche vidéo centralisant notes, chapitres, liens, favoris et statut
- [x] Compteurs des vidéos en cours et terminées
- [x] Sauvegarder et fusionner les dates d'historique
- [ ] Reprise de lecture à la position précédente (si disponible via l'API du lecteur)
- [ ] Actualisation automatique de la bibliothèque au retour du lecteur
- [ ] Récupération des titres et descriptions via YouTube Data API


## 0.9.4
- [x] Activer les éditeurs locaux par vidéo pour transcription, traduction, résumé, sous-titres et vocabulaire.
- [x] Relier les outils aux cartes de la bibliothèque et aux fiches de chaînes.
- [x] Inclure les nouveaux contenus dans la sauvegarde JSON.
- [ ] Connecter de vrais moteurs de transcription/traduction après choix d’un fournisseur et gestion sécurisée des clés.


## 0.9.5
- [x] Routage des outils vidéo depuis la barre Leaf vers la bibliothèque puis le lecteur.
- [x] LeafStats basé sur les données locales.
- [x] LeafGraph et LeafSimilar basés sur les chaînes et catégories existantes.
- [x] LeafDiscover et LeafSearch utilisables.
- [x] LeafPlaylist relié au gestionnaire global.
- [ ] Connexion inter-applications des Leaf autonomes après confirmation des package IDs et contrats d'échange.


## 0.9.6
- [x] Validation et normalisation des liens de chaînes YouTube.
- [x] Prévention des doublons d'URL à l'ajout.
- [x] Recherche dans le nom, l'URL, la description et les notes.
- [ ] Résolution automatique des noms/descriptions depuis l'API YouTube (nécessite une stratégie API et une clé fournie par l'utilisateur).
- [ ] Import/export JSON par sélecteur de fichier Android avec aperçu et restauration contrôlée.
- [ ] Tests instrumentés sur Android réel, notamment lecture WebView, arrière-plan et reprise d'activité.
