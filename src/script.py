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
		print(" ".join(ligneSplit))
		nomServiceRMI = ligneSplit[3]
		ipRMI = " ".join(ligneSplit).split(":")[1]
		portRMI = " ".join(ligneSplit).split(":")[2]
		comportement = ligne.split(" ")[1];
		string = 'gnome-terminal --command="java projet.joueur.LancerJoueur '+portRMI+' '+nomServiceRMI+' '+comportement+'"'
		print string
		os.system(string)
	elif executeProducteur == 1:
		ligneSplit = ligne.split("/")
		print(" ".join(ligneSplit))
		nomServiceRMI = ligneSplit[3]
		ipRMI = " ".join(ligneSplit).split(":")[1]
		portRMI = " ".join(ligneSplit).split(":")[2]
		string = 'gnome-terminal --command="java projet.producteur.LancerProducteur '+portRMI+' '+nomServiceRMI+'"'
		print string
		os.system(string)
		#os.system('gnome-terminal --command="./LancerProducteur '+portRMI+' '+nomService+'"');
	elif readingRegle == 1:
		ligneSplit = ligne.split(" ")
		if ligneSplit[0] == "TOUR" :
			os.system('gnome-terminal')
		if ligneSplit[0] == "FIN":
			os.system('gnome-terminal')





		



		
		

		
		
