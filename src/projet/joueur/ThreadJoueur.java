package projet.joueur;

import projet.producteur.Producteur;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Controller du JoueurImpl
 * Il est normal que cette classe connaisse la class JoueurImpl
 * car est elle lié à l'implémentation du comportement du Joueur
 * TODO créer de nouvelle personnalité (ENUM)
 * Created by jpabegg on 08/04/17.
 */
public class ThreadJoueur extends Thread {
    private JoueurImpl j;
    private Map<Integer,Integer> objectifs;
    private Map<Integer,List<Producteur>> clefRessourceProducteurs;
    private Random loto = new Random();
    private int k;

    public ThreadJoueur(JoueurImpl j){
        this.j = j;
        k = j.getNbRessourcePrenable();
        objectifs = j.getObjectifs();
        clefRessourceProducteurs = j.getListProducteur();
    }
    
    public void run(){
        tour();
        System.err.println("Terminé Joueur "+j.getId());
    }

    /**
     * IA de base pour le joueur:
     * -choisit une ressource au hasard
     * -prend des unité de cette ressource jusqu'a atteindre l'objectif
     * -jusqu'a avoir atteint l'objectif sur toutes les ressources
     * */
    private void tour(){
        List<Integer> ressourceNonTermine = new ArrayList<Integer>();
        int i;
        boolean haveAObjectif = false;
        int objectif;
        int retour;
        Producteur p;
        ressourceNonTermine(ressourceNonTermine,j.getRessources());
        i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
        objectif = objectifs.get(i);
        while(!j.haveFinished()){
            if(!haveAObjectif){
                i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
                objectif = objectifs.get(i);
                haveAObjectif = true;
            }
            p = clefRessourceProducteurs.get(i).get(loto.nextInt(clefRessourceProducteurs.get(i).size()));
            retour = j.getRessource(p,i,k);
            if(retour>=objectif){
                haveAObjectif = false;
                ressourceNonTermine = ressourceNonTermine(ressourceNonTermine,j.getRessources());
            }
        }
    }
    /**
     *
     * @return une liste contenant les id des ressources dont on a pas atteint l'objectif
     */
    private synchronized List<Integer> ressourceNonTermine(List<Integer> list,Map<Integer,Integer> ressources){
        list.clear();
        //Pour toute les ressources du jeu
        for(int i:ressources.keySet()){
            //on regarde si on a atteint l'objectif
            if(ressources.get(i) < objectifs.get(i)){
                list.add(i);
            }
        }
        if(list.size() == 0) {
            try {
                j.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
