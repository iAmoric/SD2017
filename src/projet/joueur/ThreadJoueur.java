package projet.joueur;

import projet.exceptions.StealException;
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
    private Map<Integer,List<Integer>> clefRessourceProducteurs;
    private Random loto = new Random();
    private int k;
    private Comportement comportement;

    public ThreadJoueur(JoueurImpl j){
        this.j = j;
        k = j.getNbRessourcePrenable();
        this.comportement = j.getComportement();
        objectifs = j.getObjectifs();
        clefRessourceProducteurs = j.getListProducteur();
    }
    
    public void run(){
        switch (comportement){
            case PASSIF:
                tourPassif();
                break;
            case AGGRESIF:
                if(j.canSteal()){
                    tourAggresifAvecVole();
                }else{
                    tourAggresifSansVole();
                }

                break;
            case JOUEUR:
                tourJoueur();
                break;
        }
        System.err.println("Terminé Joueur "+j.getId());
    }

    private void tourJoueur() {
        //TODO il faut faire un coordinateur de tour par tour
    }

    /**
     * Comportement de IA Aggresive avec le vole
     * -l'IA cherche à accomplir l'objectif d'une ressource
     *    elle alterne entre épuisser les ressources d'un producteur et épuisser les ressources d'un joueur
     *
     */
    private void tourAggresifAvecVole() {
        List<Integer> ressourceNonTermine = new ArrayList<Integer>();
        int i;
        boolean haveAObjectif = false;
        int nbAutreJouers = j.autreJoueurs();
        int objectif;
        int retour;
        int index;
        int indexProducteur = 0;//L'index du producteur dans Producteur[]
        boolean haveAProducteur = false;//Le producteur que le jp
        boolean vole = false;
        ressourceNonTermine(ressourceNonTermine,j.getRessources());
        i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
        objectif = objectifs.get(i);
        int precedent = 0;//Nombre d'unité de la ressource en cours à l'itération précedente
        while(!j.haveFinished()){
            //Le joueur choisie une ressource qu'il va compléter
            if(!haveAObjectif){
                i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
                objectif = objectifs.get(i);
                haveAObjectif = true;
                vole = false;
                haveAProducteur = false;
                precedent = 0;
            }
            //L'IA selectionne un producteur
            if(!haveAProducteur && !vole){
                index = loto.nextInt(clefRessourceProducteurs.get(i).size());
                indexProducteur = clefRessourceProducteurs.get(i).get(index);
                haveAProducteur = true;
            }
            if(haveAProducteur){
                retour = j.getRessource(indexProducteur,i,k);
                if(retour < precedent + k){
                    //On a récupéré moins de ressource que demandé
                    haveAProducteur = false;
                    vole = true;
                }
            }else{
                try {
                    retour = j.voleJoueur(loto.nextInt(nbAutreJouers),i,k);
                    if(retour < precedent + k){
                        vole = false;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (StealException e) {
                    e.printStackTrace();
                } finally {
                    retour = 0;
                }
            }
            //System.err.println("AGGRESSIF "+retour);
            precedent = retour;
            //On a terminé l'objectif de cette ressource
            if(retour>=objectif){
                haveAObjectif = false;
                ressourceNonTermine = ressourceNonTermine(ressourceNonTermine,j.getRessources());
            }
        }
    }

    private void tourAggresifSansVole(){

    }

    /**
     * IA de base pour le joueur:
     * -choisit une ressource au hasard
     * -prend des unité de cette ressource jusqu'a atteindre l'objectif
     * -jusqu'a avoir atteint l'objectif sur toutes les ressources
     * */
    private void tourPassif(){
        List<Integer> ressourceNonTermine = new ArrayList<Integer>();
        int i;
        boolean haveAObjectif = false;
        int objectif;
        int retour;
        int index;
        ressourceNonTermine(ressourceNonTermine,j.getRessources());
        i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
        objectif = objectifs.get(i);
        int precedent = 0;
        while(!j.haveFinished()){
            //Le joueur choisie une ressource qu'il va compléter
            if(!haveAObjectif){
                i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
                objectif = objectifs.get(i);
                haveAObjectif = true;
                precedent = 0;
            }
            //Il sélectionne un producteur au hasard de cette ressource
            index = loto.nextInt(clefRessourceProducteurs.get(i).size());
            retour = j.getRessource(clefRessourceProducteurs.get(i).get(index),i,k);
            /*if(retour < precedent + 50){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            precedent = retour;*/
            //System.err.println("PASSIF "+retour);
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
