import os


fichier = open("init","r");
contenu = fichier.read();
lignes = contenu.split("\n");
ligneSplit = "";

executeJoueur = 0;
executeProducteur = 0;


portRMI = 0;
ipRMI = 0;
nomServiceRMI = 0;

for ligne in lignes:
	if(ligne == "Joueurs"):
		executeJoueur = 1;
		executeProducteur = 0;
	elif(ligne == "Producteurs"):
		executeProducteur = 1;
		executeJoueur = 0;
	elif(ligne == "Regle"):
		break;
	elif(executeJoueur == 1):
		ligneSplit = ligne.split("/");
		print(" ".join(ligneSplit));
		nomServiceRMI = ligneSplit[3];
		ipRMI = " ".join(ligneSplit).split(":")[0];
		portRMI = " ".join(ligneSplit).split(":")[1];
		#os.system('gnome-terminal --command="./LancerJoueur '+portRMI+' '+nomService+'"');
		os.system('gnome-terminal');
	elif(executeProducteur == 1):
		ligneSplit = ligne.split("/");
		print(" ".join(ligneSplit));
		os.system('gnome-terminal');
		#os.system('gnome-terminal --command="./LancerProducteur '+portRMI+' '+nomService+'"');



		



		
		

		
		
