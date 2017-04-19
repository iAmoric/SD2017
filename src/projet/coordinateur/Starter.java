package projet.coordinateur;

import projet.exceptions.*;
import projet.exceptions.tooManyGoalsException;
import projet.joueur.Joueur;
import projet.producteur.Producteur;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jpabegg on 29/03/17.
 * Script Coordinateur chargé d'initialiser les joueurs et les producteurs
 * à partir du fichier d'init et de lancer la partie
 */
public class Starter {

    private Map<String,Integer> ressources;//associé un ID à une ressource
    private Map<Integer,Integer> objectifs;
    private Joueur[] joueurs;
    private String[] connectionRMIJoueur;
    private Producteur[] producteurs;
    private String[] connectionRMIProducteur;
    private End coordinateurFin;
    private String connectionRMIFin;
    private boolean haveOptionSUM = false; //indique qu'il faut atteindre le nombre total X de ressource
    private boolean haveOptionALL = false; //indique que le même nombre de unité doit être atteint pour toutes les ressources
    private int sommeObjectif = -1;
    private int nbRessourcePrenable;//nb max de ressource prenable d'un coup sur un producteur (N dans init)
    private int regenRessource;//délai en ms pour produire des ressource (K dans init)
    private boolean canSteal;
    private boolean isEpuisable;//savoir si on peut épuiser une ressource


    public Starter(File file) throws IOException, PException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String ligne;
        //Parsing du fichier d'init
        ressources = new HashMap<String, Integer>();
        objectifs = new HashMap<Integer, Integer>();
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Ressource")){
                parseRessource(reader);
            }
        }
    }

    public Starter(String s) throws IOException, PException {
        this(new File(s));
    }

    /**
     *
     * @param reader
     * @throws IOException
     */
    private void parseRessource(BufferedReader reader) throws IOException, PException {
        String ligne;
        int i = 0;
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Objectif")){
                parseObjectifs(reader);
            }else{
                ressources.put(ligne,i);
                i++;
            }
        }
        if(i == 0){
            throw new noResourceException();
        }


    }

    /**
     * Initialise l'objectifs de la partie. Pour le moment il faut autant de ligne que de ressource
     *
     * Set le type de l'objectif :
     * All : même nombre de unité doit être atteint pour toutes les ressources
     * Sum : atteindre le nombre total X de ressources
     * @param reader
     */
    private void parseObjectifs(BufferedReader reader) throws IOException, PException {
        String ligne;
        int i;
        int objectif;
        i = 0;

        while ((ligne = reader.readLine()) != null){
            if(ligne.equals("Joueurs")){
                if (i == 0){
                    throw new noGoalException();
                }
                else {
                    parseJoueur(reader);
                }
            }else{
                if (ligne.equals("All")) {
                    haveOptionALL = true;
                    System.out.println("All : " + haveOptionALL);
                }
                else if (ligne.equals("Sum")) {
                    haveOptionSUM = true;
                    System.out.println("Sum : " + haveOptionSUM);
                }
                else {
                    objectif = Integer.parseInt(ligne);
                    if (haveOptionSUM){
                        sommeObjectif = objectif;
                    }
                    if (haveOptionALL) {
                        for (int k = 0; k < ressources.size(); k++)
                            objectifs.put(k, objectif);
                    }
                    objectifs.put(i,objectif);
                    i++;
                }
                if (i > 1 && (haveOptionSUM || haveOptionALL)) {
                    //Si une des options est set a true, il ne doit y avoir qu'un objectif
                    throw new tooManyGoalsException();
                }
            }
        }


        //On a lu le fichier en entier à ce moment
        if((ressources.size() != objectifs.size()) && (!haveOptionALL && !haveOptionSUM)) {
            //Si il n'y a pas d'objectif, nb ressource = nb objectif
            throw new resourcesGoalsException(ressources.size(), objectifs.size());
        }




    }

    /**
     *
     * @param reader
     * @throws IOException
     */
    private void parseJoueur(BufferedReader reader) throws IOException, RMIExecption {
        String ligne;
        List<Joueur> listJoueur = new ArrayList<Joueur>();
        List<String> listString = new ArrayList<String>();
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Producteurs")){
                parseProducteur(reader);
            }else{
                try {
                    listJoueur.add((Joueur)Naming.lookup(ligne));
                } catch (NotBoundException e) {
                    throw new RMIExecption(ligne);
                }
                listString.add(ligne);
            }
        }
        //On a lu le fichier en entier à ce moment
        if(listJoueur.size() != 0){
            joueurs = new Joueur[listJoueur.size()];
            connectionRMIJoueur = new String[listString.size()];
            for(int i = 0;i < joueurs.length;i++){
                joueurs[i] = listJoueur.get(i);
                connectionRMIJoueur[i] = listString.get(i);
            }
        }

    }

    /**
     *
     * @param reader
     */
    private void parseProducteur(BufferedReader reader) throws IOException, RMIExecption {
        int idRessource;
        int valInitiale;
        String ligne;
        String[] elements;
        Producteur producteur;
        Map<Integer,Integer> mapProducteur;//Les ressources de base du producteur
        List<Producteur> listProducteur = new ArrayList<Producteur>();
        List<String> listString = new ArrayList<String>();
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Regle")){
                parseRegle(reader);
                break;
            }else {
                elements = ligne.split(" ");
                try {
                    //connection au producteur
                    producteur = (Producteur) Naming.lookup(elements[0]);
                    //ajout du producteur dans la liste de producteur
                    listProducteur.add(producteur);
                    //ajout de l'adresse du producteur dans la liste d'adresse
                    listString.add(elements[0]);
                    mapProducteur = new HashMap<Integer, Integer>();
                    for (int i = 1; i < elements.length; i = i + 2) {
                        idRessource = findIdRessource(elements[i]);
                        valInitiale = Integer.parseInt(elements[i + 1]);
                        //On a pas le droit de commencer avec des ressources négatives
                        if(valInitiale<0){
                            valInitiale = 0;
                        }
                        mapProducteur.put(idRessource,valInitiale);
                    }
                    //On indique directement au producteur ce qu'il produit
                    producteur.setProductions(mapProducteur);
                } catch (NotBoundException e) {
                    throw new RMIExecption(elements[0]);
                }
            }
        }
        //On a lu le fichier en entier à ce moment
        if(listProducteur.size() != 0){
            producteurs = new Producteur[listProducteur.size()];
            connectionRMIProducteur = new String[listString.size()];
            for(int i = 0;i < producteurs.length;i++){
                producteurs[i] = listProducteur.get(i);
                connectionRMIProducteur[i] = listString.get(i);
                producteurs[i].setId(i);
            }
        }

    }

    private void parseRegle(BufferedReader reader) throws IOException {
        String ligne;
        String[] elements;
        while ( (ligne = reader.readLine()) != null){
            elements = ligne.split(" ");
            if (elements.length == 2) {
                switch (elements[0]) {
                    case "K" :
                        regenRessource = Integer.parseInt(elements[1]);
                        break;
                    case "N" :
                        nbRessourcePrenable = Integer.parseInt(elements[1]);
                        break;
                    case "STEAL" :
                        if (elements[1].equals("ON"))
                            canSteal = true;
                        else if (elements[1].equals("OFF"))
                            canSteal = false;
                        break;
                    case "EPUISABLE" :
                        if (elements[1].equals("ON"))
                            isEpuisable = true;
                        else if (elements[1].equals("OFF"))
                            isEpuisable = false;
                        break;
                    case "FIN":
                        try {
                            coordinateurFin = (End)Naming.lookup(elements[1]);
                            connectionRMIFin = elements[1];
                        } catch (NotBoundException e) {
                            //TODO faire une erreur
                        }
                        break;
                    default :
                        System.err.println("Problème regle inconnue : " + ligne);
                        //TODO exception problème lecture
                        break;
                }
            }
            else {
                System.err.println("Problème manque argument : " + ligne);
                //TODO exception manque argument
            }
        }
    }

    public void initFin() throws RemoteException{
        coordinateurFin.setJoueurs(connectionRMIJoueur);
        coordinateurFin.setProducteurs(connectionRMIProducteur);
    }
    /**
     * Initialise les infos disponible chez les joueurs
     * -son id
     * -les autres joueurs
     * -l'objectif
     * -les producteurs
     * -le projet.coordinateur de fin de partie
     * -les règles
     */
    public void initJoueurs() throws RemoteException {
        int i;
        //Donner l'id du joueur à chaque joueur
        for(i = 0;i<joueurs.length;i++){
            try {
                joueurs[i].setId(i); //Id du joueurs
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Donner l'adresse des autres joueurs au joueur ( il n'a pas besoin de connaitre l'id des autre)
        for (i =0;i<joueurs.length;i++) {
            if (joueurs[i].ajouteJoueurs(connectionRMIJoueur)) {
                if(joueurs[i].ajouteProducteurs(connectionRMIProducteur)){
                    if(joueurs[i].ajouteFin(connectionRMIFin)){
                        System.err.println("Joueur " + i + ": OK");
                    }else{
                        System.err.println("Joueur " + i + ": erreur pour se connecter au coordinateur de fin");
                    }
                }else{
                    System.err.println("Joueur " + i + ": erreur pour se connecter aux producteurs");
                }
            } else {
                System.err.println("Joueur " + i + ": erreur pour se connecter aux autres joueurs");
            }
            joueurs[i].setObjectifs(objectifs,haveOptionSUM,sommeObjectif);
        }
    }

    /**
     * Indique les règle de la partie chez les Joueur et les Producteur
     */
    public void initRegle() throws RemoteException {
        int i;
        //Producteur
        //Indique au producteur si les ressources sont épuisable et le délai pour génerer des ressources
        for(i = 0;i<producteurs.length;i++){
            producteurs[i].setRules(isEpuisable,regenRessource);
        }
        //Joueur
        for(i = 0;i<joueurs.length;i++){
            joueurs[i].setRules(nbRessourcePrenable,canSteal,isEpuisable);
        }
    }

    public void startGame() throws RemoteException {
        int i;
        for(i = 0;i<producteurs.length;i++){
            producteurs[i].startProduction();
        }
        for(i = 0;i<joueurs.length;i++){
            joueurs[i].start();
        }
    }

    /**
     * Écrit les informations du Starter dans un flux de sortie
     * @param os: le flux de sortie
     */
    public void info(PrintStream os){
        os.println("Ressource:");
        Integer k = 0;
        for(String s: ressources.keySet()){
            os.println(s + ":" + objectifs.get(k));
            k++;
        }
        os.println("\nJoueurs");
        for(Joueur j: joueurs){
            os.println(j);
        }
        os.println("\nProducteurs");
        for(Producteur p:producteurs){
            os.print(p);
        }
    }

    /**
     * Trouve le numéro associé au nom d'un resource
     * @param ressource: le nom d'une ressource
     * @return l'id de cette ressource dans le système
     */
    private int findIdRessource(String ressource){
        for (String s:ressources.keySet()){
            if (s.equals(ressource)){
                return ressources.get(s);
            }
        }

        return -1;
    }

    public static void main(String[] args){
        /*if(args.length != 1){
            System.err.println("Usage: Starter fichier");
            System.exit(1);
        }
        File f = new File(args[0]);*/
        try {
            Starter s = new Starter("ressource/init");
            s.initJoueurs();
            s.initFin();
            s.initRegle();
            s.startGame();
            //s.info(System.err);
        } catch (IOException | PException e) {
            e.printStackTrace();
        }

        JSONConverter jsonConverter = new JSONConverter();

    }
}
