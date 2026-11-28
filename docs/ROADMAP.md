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
