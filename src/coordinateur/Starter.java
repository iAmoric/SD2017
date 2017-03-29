package coordinateur;

import java.io.*;
import java.util.Map;

/**
 * Created by jpabegg on 29/03/17.
 * Coordinateur chargé d'initialiser les joueurs et les producteurs
 * à partir du fichier d'init
 */
public class Starter {

    private Map<String,Integer> ressources;//associé un ID à une ressource
    private Map<Integer,Integer> objectifs;



    public Starter(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String ligne;
        //Parsing du fichier d'init
        while ( (ligne = reader.readLine()) != null){
            if(ligne.equals("Ressource")){
                initRessource(reader);
            }
        }
    }

    private void initRessource(BufferedReader reader) throws IOException {
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
        initObjectifs(reader);

    }

    /**
     * Initialise l'objectifs de la partie. Pour le moment il faut autant de ligne que de ressource
     * TODO faire une option ALL et SUM
     * @param reader
     */
    private void initObjectifs(BufferedReader reader) throws IOException {
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
    }



    public static void main(String[] args){
        /*if(args.length != 1){
            System.err.println("Usage: Starter fichier");
            System.exit(1);
        }
        File f = new File(args[0]);*/

    }
}
