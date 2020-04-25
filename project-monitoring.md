# Project monitoring

L'objectif de ce document est de faire un suivi de la réalisation du projet. Une bonne gestion de projet c'est avant tout être capable de : dire ce que je fais et de faire ce que je dis.

Le projet doit se décomposer en briques logicielles unitaires à développer. Une brique représente un incrément fonctionnel au projet. Elle est unitaire, c'est-à-dire qu'elle peut être développée et testée de façons indépendantes.

- *Testé*: Il existe un protocole permettant de valider si l'élément est fonctionnel.
- *Indépendante*: La réalisation de la brique ne requière pas qu'une autre brique soit réalisée en même temps (toutes les dépendances sont contenues dans des briques terminées).

La notion de brique est fondamentale dans la conduite de projet Agile, elle permet de se placer dans un cadre de développement itératif :
Je développe une brique ; je la teste ; je sauvegarde ma nouvelle version de mon programme avec une brique fonctionnelle en plus ; puis je passe à la brique suivante...

Dans la terminologie Scrum, la notion de brique est nommée: Use-Story

Il vous est demandé ici de lister les briques du projet avec un minimum d'information. 
On listera les briques terminées, en cours de réalisation ainsi que celle qui devrait venir après.


## Intitulé de la brique fonctionnel

Description de la brique, de la fonctionnalité qu'elle apporte. 

- Responsable (c.-à-d. développeur principal sur la brique) 
- Séances: x, y ... (les séances pendant lesquels et entre lesquels cette brique à été développé)
- Commit:  (le numéro de commit associé à la livraison de cette brique cf. git log)

Éléments relatifs à la réalisation de cette brique.

## (Exemple) Mise en place Git

Mise en place de git comme solution de versionnage du projet et de partage du répertoire de travail. 

- XXXX
- Séance: 1
- Commit:  97f634a1a185fad9b6d84c351673c73f27478aff

Ouverture d'un projet sur internet (e.g. le gitlab de l'école) et clonage du répertoire pour chacun des développeurs.
Édition d'un premier README.md.

URL sur gitlab: [https://gvipers.imt-lille-douai.fr/...](gvipers.imt-lille-douai.fr/...)


## Hello world

Mise en place de l'environnement de travail et exécution d'un premier programme basique

- XXXX
- Séance: 1
- Commit: fac2a9f9fb4a341e9b4c8b980f62c8089353c79b

Téléchargement des archives fournies pour le projet et des librairies XXXX.
Configuration de mon IDE préféré YYY et ZZZ.
Exécusion d'un premier programme basic "Hello-world"  (l'ouverture d'une fenêtre pour le projet DÉTECTION)

cf. le fichier: myPackage.MyFirstClass.java


## (Exemple) Next step...

...

# Projet monitoring 

## Première approche du programme donné

Télécharger le dossier, l'ouvrir, le lire et le faire tourner dans le but de comprendre ces fonctionnalitées.

-Responsable : Tout le monde 
-Séance : 1
-Commit


## Mise en place du Git

Mise en place d'un Git comme outil de partage permettant le visionnage du projet

-Responsable : Benoit 
-Séance : 1 et 2 
-Commit

Ouverture d'un projet sur internet (initialement le gitlab de l'école puis avec Github)
Faire le lien avec GitKraken
Création des branches du projet
Appropriation du fonctionnement

URL sur Github : " https://github.com/Benlap42/Groupe8-tictactoe "

## Manipulation des données du programme 

Utiliser les bonnes fonctions du programme déjà existant afin de rajouter des fonctionnalités à celui ci 

-Responsable : Tout le monde
-Séances : Toutes

Naviguer entre les différentes class 
Tester leur fonctionalité à part 


## Compréhension des algorithmes 

Faire la traduction entre la théorie des algorithme et le code java

-Responsable : Tout le monde
-Séances : 1, 2 et 3


## Création de la classe ArbreCoups

Element de l'arborescence utilisé pour l'algorithme MinMax. La classe sert à modéliser un arbre de coups possibles.

-Responsables: Yoann et Abdourahmane
-Séance: 3

## Création et amélioration de la classe AlgoMinMax9x9
Classe héritée de la classe AlgoRecherche. Elle contient les méthodes nécessaires pour évaluer et attribuer des notes aux coups, ainsi que choisir le meilleur coup.
-Responsables: Yoann et Abdourahmane
-Séances: 3 et 4

## Création de le class Noeud 

Elément de l'arbolescance utilisé pour l'algorithme Monté Carlo

-Responsable : Benoit et Inès
-Séance : 3 et 4
-Commit :


## Implémentation des différentes phases de l'algorithme Monté Carlo

Création de fonctions

-Responsable : Benoit et Inès 
-Séances : 3 et 4
-Commit :

## Rendre l'algorithme fonctionnel 

Faire le lien entre le class Noeud et Montecarlo

-Responsable : Benoit et Inès 
-Séances : 4 et 5
-Commit :

Rajouter une phase d'initialisation, implémenter la fonction MeilleurCoup





