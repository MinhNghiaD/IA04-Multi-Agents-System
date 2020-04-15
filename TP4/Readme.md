# IA04 - TD4 : Simulation et Environnement - Le Sudoku

Le TD a pour l’objectif de réaliser un système simulation multi-agents. Concrètement, il s’agit un système qui permet de résoudre le Sudoku (9x9) en utilisant des différents agents, des performatifs, ainsi que des behaviours.

## Getting Started

Ces instructions vous permettront de lancer le projet sur votre ordinateur local à des fins de développement et de test. 

### Prerequisites

* Eclipse 4.14.0
* JavaSE-11
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
- Les grilles de Sudoku se trouvent dans les fichiers `.res` de le répertoire `data`. Envoyer au Simulateur un message de type **request** avec le contenu `data/<nom_de_fichier_sudoku>.res` pour commencer la résolution

![Imgur](https://i.imgur.com/wTROL5b.png)

## Auteur

* Tran Khanh Linh NGUYEN
* Minh-Nghia DUONG


