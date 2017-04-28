import os


fichier = open("init","r")
contenu = fichier.read()
lignes = contenu.split("\n")
ligneSplit = ""

executeJoueur = 0
executeProducteur = 0
readingRegle = 0


portRMI = 0
ipRMI = 0
nomServiceRMI = 0

for ligne in lignes:
	if ligne == "Joueurs" :
		executeJoueur = 1
		executeProducteur = 0
	elif ligne == "Producteurs" :
		executeProducteur = 1
		executeJoueur = 0
	elif ligne == "Regle" :
		executeProducteur = 0
		executeJoueur = 0
		readingRegle = 1
	elif executeJoueur == 1 :
		ligneSplit = ligne.split("/")
		#print(" ".join(ligneSplit))
		ipRMI = " ".join(ligneSplit).split(":")[1]
		arguments = " ".join(ligneSplit).split(":")[2]
		string = 'gnome-terminal --command="java projet.joueur.LancerJoueur '+arguments+'"'
		print string
		os.system(string)
	elif executeProducteur == 1:
		ligneSplit = ligne.split("/")
		#print(" ".join(ligneSplit))
		ipRMI = " ".join(ligneSplit).split(":")[1]
		arguments = " ".join(ligneSplit).split(":")[2].split(" ")
		while len(arguments) > 2:
			arguments.pop()
		string = 'gnome-terminal --command="java projet.producteur.LancerProducteur '+" "+" ".join(arguments)+'"'
		print string
		os.system(string)
		#os.system('gnome-terminal --command="./LancerProducteur '+portRMI+' '+nomService+'"');
	elif readingRegle == 1:
		ligneSplit = ligne.split(" ")
		if ligneSplit[0] == "TOUR" :
			ligneRMI = ligneSplit[1]
			ipRMI = ligneRMI.split(":")[1].split("/")[2]
			arguments = " ".join(ligneRMI.split(":")[2].split("/"))
			string = 'gnome-terminal --command="java projet.coordinateur.LancerTour '+arguments+'"'
			print string
			os.system(string)
		if ligneSplit[0] == "FIN":
			ligneRMI = ligneSplit[1]
			ipRMI = ligneRMI.split(":")[1].split("/")[2]
			arguments = " ".join(ligneRMI.split(":")[2].split("/"))
			string = 'gnome-terminal --command="java projet.coordinateur.LancerEnd '+arguments+'"'
			print string
			os.system(string)


string = 'gnome-terminal --command="java projet.coordinateur.Starter init"'





		



		
		

		
		
