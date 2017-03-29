package coordinateur;

import joueur.Joueur;
import joueur.JoueurImpl;
import producteur.Producteur;
import producteur.ProducteurImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jpabegg on 29/03/17.
 * Coordinateur chargé d'initialiser les joueurs et les producteurs
 * à partir du fichier d'init
 */
public class Starter {

    private Map<String,Integer> ressources;//associé un ID à une ressource
    private Map<Integer,Integer> objectifs;
    private Joueur[] joueurs;
    private Producteur[] producteurs;



    public Starter(File file) throws IOException {
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

    public Starter(String s) throws IOException {
        this(new File(s));
    }

    /**
     *
     * @param reader
     * @throws IOException
     */
    private void parseRessource(BufferedReader reader) throws IOException {
        String ligne;
        int i = 0;
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Objectif")){
                break;
            }else{
                ressources.put(ligne,i);
                i++;
            }
        }
        if(i == 0){
            //TODO erreur il y a aucune ressource dans le fichier init
        }
        parseObjectifs(reader);

    }

    /**
     * Initialise l'objectifs de la partie. Pour le moment il faut autant de ligne que de ressource
     * TODO faire une option ALL et SUM
     * @param reader
     */
    private void parseObjectifs(BufferedReader reader) throws IOException {
        String ligne;
        int i;
        int objectif;
        i = 0;
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Joueurs")){
                break;
            }else{
                objectif = Integer.parseInt(ligne);
                objectifs.put(i,objectif);
                i++;
            }
        }
        if(ressources.size() != objectifs.size()){
            //TODO erreur sauf si il y a des options
        }
        parseJoueur(reader);
    }

    /**
     *
     * @param reader
     * @throws IOException
     */
    private void parseJoueur(BufferedReader reader) throws IOException {
        String ligne;
        List<Joueur> listJoueur = new ArrayList<Joueur>();
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Producteurs")){
                break;
            }else{
                //TODO se connecter en RMI pour récupérer le joueur
                listJoueur.add(new JoueurImpl());
            }
        }
        if(listJoueur.size() != 0){
            joueurs = new Joueur[listJoueur.size()];
            for(int i = 0;i < joueurs.length;i++){
                joueurs[i] = listJoueur.get(i);
            }
        }
        parseProducteur(reader);
    }

    /**
     *
     * @param reader
     */
    private void parseProducteur(BufferedReader reader) throws IOException {
        String ligne;
        List<Producteur> listProducteur = new ArrayList<Producteur>();
        while ( (ligne = reader.readLine()) != null){
            //TODO se connecter en RMI pour récupérer les Producteurs
           listProducteur.add(null);
        }
        if(listProducteur.size() != 0){
            producteurs = new Producteur[listProducteur.size()];
            for(int i = 0;i < producteurs.length;i++){
                producteurs[i] = listProducteur.get(i);
            }
        }

    }

    /**
     * Écrit les informations du Starter dans un flux de sortie
     * @param os: le flux de sortie
     */
    public void info(PrintStream os){
        os.println("Ressource:");
        for(String s: ressources.keySet()){
            os.println(s);
        }
        os.println("\nJoueurs");
        for(Joueur j: joueurs){
            os.println(j);
        }
        os.println("\nProducteurs");
    }

    public static void main(String[] args){
        /*if(args.length != 1){
            System.err.println("Usage: Starter fichier");
            System.exit(1);
        }
        File f = new File(args[0]);*/
        try {
            Starter s = new Starter("ressource/init");
            s.info(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
