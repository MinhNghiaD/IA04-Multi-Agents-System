# IA04 - TD4 : Réseau d'accointance

Le TD a pour l'objectif de réaliser un un système d’agents formant un “arbre binaire d’agents”,un agent intermédiaire permet de manipuler l’arbre, par exemple : l’ajout d’un noeud, la vérification de la présence d’une valeur ou l’affichage l’arbre. Pour ce TD, le nombre d’agents n’est plus prédéfini, il va augmenter progressivement en fonction de l’utilisation et nous traiterons également le contenu des différents messages.

## Getting Started

Ces instructions vous permettront de lancer le projet sur votre ordinateur local à des fins de développement et de test. 

### Prerequisites

* Eclipse 4.14.0
* [JavaSE-11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* Jade 4.5.0

### Importer le projet

- Créer un nouveau projet Java en Eclispe
- Cliquer droit sur le nom du projet et choisir Import
- Dans la fenêtre de Import, cliquer General et sélectionner Archive File. Cliquer sur Next.
- Sélectionner le fichier JAR que vous souhaitez importer et cliquez sur Finir.

### Configurer 

**Import Jade**
- Dans le répertoire lib, cliquer droit sur jade.jar et sélectionner Build Path 

**Import Java11**
- Cliquer droit sur le nom de projet, sélectionner Properties
- Dans la fenêtre ouvrant, choisir Java Build Path/ Libraries / Modulespath et puis Add Library
- Sélectionner Installed JREs... -> Add -> Standard VM
- Saisir le chemin du répertoire dk-11.0.6.jdk/Contents/Home dans le champs JRE home et Finir

### Execution
- Cliquer droit sur MainBoot.java et choisir Run As Java Application
- Des messages valable :

```
{"action":”inserer", "value":50}	//Insérer un agent
{"action":"presence", "value":120}	//Vérifer la présence d'un agent
{"action":"affichage"}	//Afficher l'arbre binaire
```

## Auteurs

* Tran Khanh Linh NGUYEN
* Minh-Nghia DUONG


