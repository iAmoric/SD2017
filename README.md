## Instruction pour lancer le système:

    java projet.producteur.LancerProducteur
    java projet.joueur.LancerJoueur
    java projet.coordinateur.Starter


## Info sur les fichiers de log:

### Format:

    SIGNATURE
    DATE ACTION(un mot clef) ARGUMENTS
    ...
    ...
    fin

### Différentes actions et arguments:

    prendre Producteur IdRessource QuantiteDemandee QuantiteOptenu QuantiteTotal
	  voler IdJoueur IdRessource QuantiteDemandee QuantiteOptenu QuantiteTotal
	  observerJoueur IdJoueur QuantiteRe0 QuantiteRe1 .... QuantiteReN
	  observerProducteur IdProd QuantiteRe0 QuantiteRe1 .... QuantiteReN
	  antivole
	  finantivole
	  fin
	
## TODO LIST:

* [ ] script Java qui lance l'intégralité du système
* [ ] faire l'observation du système
* [ ] améliorer les informations dans les log
* [ ] faire la récupération des fichiers log par le coordinateur End


## Nouveau format fichier log :

    nomDuJoueur
    date typeAction
    ...
    date typeAction

### typeAction :

    get idPro idRes QteDemande QteRecu nbRes1 ... nbResN
    steal idJoueur idRes QteVolee QteObtenue nbRes1 ... nbResN
