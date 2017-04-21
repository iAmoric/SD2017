package projet.joueur;

import projet.exceptions.FinDePartieException;
import projet.exceptions.StealException;
import projet.producteur.Producteur;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.ObjDoubleConsumer;

/**
 * Controller du JoueurImpl
 * Il est normal que cette classe connaisse la class JoueurImpl
 * car est elle lié à l'implémentation du comportement du Joueur
 * Created by jpabegg on 08/04/17.
 */
public class ThreadJoueur extends Thread {
    private JoueurImpl j;
    private Map<Integer,Integer> objectifs;
    private Map<Integer,List<Integer>> clefRessourceProducteurs;
    private Random loto = new Random();
    private Producteur[] producteurs;
    private boolean sum;
    private boolean tourParTour;
    private boolean observeSysteme = false;
    private int objectifSum;
    private int k;
    private Comportement comportement;
    private Object lock;
    private Scanner scanner = new Scanner(System.in);

    public ThreadJoueur(JoueurImpl j){
        this.j = j;
        k = j.getNbRessourcePrenable();
        this.comportement = j.getComportement();
        objectifs = j.getObjectifs();
        sum = j.doSum();
        tourParTour = j.isTourParTour();
        producteurs = j.getProducteurs();
        objectifSum = j.getSumObjectif();
        clefRessourceProducteurs = j.getListProducteur();
        lock = j.getLock();
    }
    
    public void run(){
        switch (comportement){
            case PASSIF:
                tourPassif();
                System.err.println("PARTIE TERMINEE");
                break;
            case AGGRESIF:
                if(j.canSteal()){
                    tourAggresifAvecVole();
                }else{
                    tourAggresifSansVole();
                }
                System.err.println("PARTIE TERMINEE");
                break;
            case MALIN:
                tourMalin();
                System.err.println("PARTIE TERMINEE");
                break;
            case JOUEUR:
                break;
        }

    }

    /**
     * Comportement de IA malin
     * Similaire à l'IA passive seulement quelle repère le vole
     */
    private void tourMalin() {
        List<Integer> ressourceNonTermine = new ArrayList<Integer>();
        Map<Integer,Integer> ressourceT = new HashMap<Integer,Integer>(j.getRessources());
        int i;
        boolean haveAObjectif = false;
        int objectif;
        int retour;
        int index;
        ressourceNonTermine(ressourceNonTermine,j.getRessources());
        i = meilleurObjectif(j.getRessources(),objectifs);
        objectif = objectifs.get(i);
        while(!j.haveFinished()){
            if(tourParTour){
                tourParTour();
            }
            if(!tourParTour)
                if(detectionVole(ressourceT,j.getRessources())){
                    modeAntiVole(100);
                }
            //Le joueur choisie une ressource qu'il va compléter
            if(!haveAObjectif){
                if(sum){
                    objectif = objectifSum;
                }else{
                    i = meilleurObjectif(j.getRessources(),objectifs);
                    objectif = objectifs.get(i);
                }
                haveAObjectif = true;
            }
            if(sum){
                i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
            }
            //Il sélectionne le producteur qui possède le plus de ressource
            try {
                index = calculProducteur(i);
            } catch (RemoteException e) {
                index = 0;
            }
            retour = j.getRessource(index,i,k);
            if(sum){
                retour = j.getTotalRessource();
            }
            if(retour>=objectif){
                haveAObjectif = false;
                ressourceNonTermine = ressourceNonTermine(ressourceNonTermine,j.getRessources());
            }
            ressourceT = new HashMap<Integer,Integer>(j.getRessources());

        }
    }

    /**
     * Active le mode antivole pour i milliseconde
     * Pendant le mode antivole il est impossible de faire une action
     * @param i temps en milliseconde
     */
    private void modeAntiVole(int i) {
        j.setModeAntiVole(true);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        j.setModeAntiVole(false);
    }

    /**
     * Permet de détecter si on a été volé
     * @param ressourceT ressources possédées enregistré à un instant T
     * @param ressources ressources possédées actuellement
     * @return vrai si une valeur de ressources est inférieur à la valeur enregistrer dans ressource T
     */
    private boolean detectionVole(Map<Integer, Integer> ressourceT, Map<Integer, Integer> ressources) {
        for(int i:ressourceT.keySet()){
            if(ressources.get(i) < ressourceT.get(i)){
                return true;
            }
        }
        return false;
    }

    public boolean tourJoueur() throws FinDePartieException {
        String[] splitCommande;
        String commande;
        boolean commandeValide = false;
        j.setModeAntiVole(false);
        Map<Integer,Integer> ressource = j.getRessources();
        //Tant que le joueur n'a pas de commande valide

        if(observeSysteme){
            j.observeSystemeFin();
            observeSysteme = false;
        }
        System.out.println("Début de votre tour");
        while (!commandeValide){
            System.out.println("Veuillez entrer votre commande");
            //On demande au joueur d'entrer une commande
            commande = scanner.nextLine();
            splitCommande = commande.split(" ");
            if(splitCommande.length > 0){
                //On vérifie la commande
                switch (splitCommande[0]){
                    case "PASS":
                        commandeValide = true;
                        break;
                    case "STEAL":
                        commandeValide = voleJoueur(splitCommande);
                        break;
                    case "GET":
                        commandeValide = getJoueur(splitCommande);
                        break;
                    case "WAIT":
                        commandeValide = waitJoueur(splitCommande);
                        break;
                    case "OBSERVE":
                        commandeValide = observeJoueur(splitCommande);
                        break;
                    case "INFO":
                        System.out.print("Vos ressources (id quantite ): ");
                        for (int i: ressource.keySet()){
                            System.out.print(i+" "+ressource.get(i)+" ");
                        }
                        System.out.print("\n");
                        break;
                    default:
                        System.err.println("La commande "+splitCommande[0]+" n'existe pas");
                        break;
                }
            }
        }
        System.out.print("Vos ressources (id quantite ): ");
        for (int i: ressource.keySet()){
            System.out.print(i+" "+ressource.get(i)+" ");
        }
        System.out.print("\n");
        System.out.println("Fin du tour");

        //Détection de fin de partie
        List<Integer> l = new ArrayList<Integer>();
        l = ressourceNonTermine(l,j.getRessources());
        if(l.size() == 0)throw new FinDePartieException();
        return true;
    }

    private boolean observeJoueur(String[] splitCommande) {
        int id;
        if(splitCommande.length < 3){
            if(splitCommande.length != 2){
                return false;
            }
            if(splitCommande[1].equals("ALL")){
                j.observeSysteme();
                observeSysteme = true;
                return true;
            }
        }
        try{
            id = Integer.parseInt(splitCommande[2]);
            Map<Integer,Integer> ressources = null;
            switch (splitCommande[1]){
                case "PROD":
                    ressources = j.observeAutreJoueur(id);
                    break;
                case "PLAYER":
                    ressources = producteurs[id].observe();
                    break;

                default:
                    //USAGE
                    break;
            }
            if(ressources != null){
                System.out.print("Ressources observées: (id quantite)");
                for (int i:ressources.keySet()){
                    System.out.print(i+" "+ressources.get(i));
                }
                System.out.print("\n");
            }
        }catch (ArithmeticException e){
            return false;
        } catch (RemoteException e) {
           return false;
        }

        return true;
    }

    private boolean waitJoueur(String[] splitCommande) {
        j.setModeAntiVole(true);
        return true;
    }

    private boolean getJoueur(String[] splitCommande) {
        if(splitCommande.length != 4){
            //USAGE
            return false;
        }
        int numeroProducteur;
        int numeroRessource;
        int quantite;
        try{
            numeroProducteur = Integer.parseInt(splitCommande[1]);
            numeroRessource = Integer.parseInt(splitCommande[2]);
            quantite = Integer.parseInt(splitCommande[3]);
            j.getRessource(numeroProducteur,numeroRessource,quantite);
            return true;
        }catch (ArithmeticException e){
            return false;
        }
    }


    private boolean voleJoueur(String[] splitCommande) {
        int numeroJoueur;
        int numeroRessource;
        int quantite;
        if(splitCommande.length != 4){
            //USAGE
            return false;
        }
        try{
            numeroJoueur = Integer.parseInt(splitCommande[1]);
            numeroRessource = Integer.parseInt(splitCommande[2]);
            quantite = Integer.parseInt(splitCommande[3]);
            j.voleJoueur(numeroJoueur,numeroRessource,quantite);
            return true;
        }catch (ArithmeticException e){
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } catch (StealException e) {
            System.err.println("Votre vole a échoué");
            return true;
        }
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
        int retour = 0;
        int index;
        int indexProducteur = 0;//L'index du producteur dans Producteur[]
        boolean haveAProducteur = false;//Le producteur que le jp
        boolean vole = false;
        i = 0;
        objectif = 0;

        int precedent = 0;//Nombre d'unité de la ressource en cours à l'itération précedente
        while(!j.haveFinished()){
            if(tourParTour){
                tourParTour();
            }
            //Le joueur choisie une ressource qu'il va compléter
            if(!haveAObjectif){
                if(sum){
                    i = 0;
                    objectif = objectifSum;
                }else{
                    i = meilleurObjectif(j.getRessources(),objectifs);
                    objectif = objectifs.get(i);

                }
                precedent = 0;
                vole = false;
                haveAProducteur = false;
                haveAObjectif = true;

            }

            //L'IA selectionne un producteur
            if(!haveAProducteur && !vole){
                if(sum){
                    //On sélectionne une ressource au hasard si on doit faire la somme
                    i = loto.nextInt(clefRessourceProducteurs.size());
                }
                //ON sélectionne un producteur au hasard de cette ressource
                index = loto.nextInt(clefRessourceProducteurs.get(i).size());
                try {
                    indexProducteur = calculProducteur(i);
                } catch (RemoteException e) {
                    indexProducteur = clefRessourceProducteurs.get(i).get(index);
                }
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
                    punition();
                    vole = false;
                    System.err.println(e.getMessage());
                } finally {
                    retour = 0;
                }
            }
            //System.err.println("AGGRESSIF "+retour);
            precedent = retour;
            if(sum){
                retour = j.getTotalRessource();
            }
            //On a terminé l'objectif de cette ressource ou l'objectif total
            if(retour>=objectif){
                haveAObjectif = false;
                ressourceNonTermine = ressourceNonTermine(ressourceNonTermine,j.getRessources());
            }
        }
    }

    private void tourParTour(){
        synchronized (lock) {
            try {
                lock.notifyAll();
                System.err.println("Thread joueur"+j.getId()+":wait");
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Punition quand un vole a échoué
     */
    private void punition() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tourAggresifSansVole(){
        List<Integer> ressourceNonTermine = new ArrayList<Integer>();
        int i;
        boolean haveAObjectif = false;
        int objectif;
        int retour = 0;
        int index;
        int indexProducteur = 0;//L'index du producteur dans Producteur[]
        boolean haveAProducteur = false;//Le producteur que le jp
        i = 0;
        objectif = 0;
        int precedent = 0;//Nombre d'unité de la ressource en cours à l'itération précedente
        while(!j.haveFinished()){
            if(tourParTour){
                tourParTour();
            }
            //Le joueur choisie une ressource qu'il va compléter
            if(!haveAObjectif){
                if(sum){
                    i = 0;
                    objectif = objectifSum;
                }else{
                    i = meilleurObjectif(j.getRessources(),objectifs);
                    objectif = objectifs.get(i);

                }
                precedent = 0;
                haveAProducteur = false;
                haveAObjectif = true;
            }

            //L'IA selectionne un producteur
            if(!haveAProducteur){
                if(sum){
                    //On sélectionne une ressource au hasard si on doit faire la somme
                    i = loto.nextInt(clefRessourceProducteurs.size());
                }
                //ON sélectionne un producteur au hasard de cette ressource
                index = loto.nextInt(clefRessourceProducteurs.get(i).size());
                try {
                    indexProducteur = calculProducteur(i);
                } catch (RemoteException e) {
                    indexProducteur = clefRessourceProducteurs.get(i).get(index);
                }
                haveAProducteur = true;
            }
            retour = j.getRessource(indexProducteur,i,k);
            if(retour < precedent + k){
                //On a récupéré moins de ressource que demandé
                haveAProducteur = false;
            }
            //System.err.println("AGGRESSIF "+retour);
            precedent = retour;
            if(sum){
                retour = j.getTotalRessource();
            }
            //On a terminé l'objectif de cette ressource ou l'objectif total
            if(retour>=objectif){
                haveAObjectif = false;
                ressourceNonTermine = ressourceNonTermine(ressourceNonTermine,j.getRessources());
            }
        }
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
        i = meilleurObjectif(j.getRessources(),objectifs);
        objectif = objectifs.get(i);
        while(!j.haveFinished()){
            if(tourParTour){
                tourParTour();
            }
            //Le joueur choisie une ressource qu'il va compléter
            if(!haveAObjectif){
                if(sum){
                    objectif = objectifSum;
                }else{
                    i = meilleurObjectif(j.getRessources(),objectifs);
                    objectif = objectifs.get(i);
                }
                haveAObjectif = true;
            }
            if(sum){
                i = ressourceNonTermine.get(loto.nextInt(ressourceNonTermine.size()));
            }
            //Il sélectionne le producteur qui possède le plus de ressource
            try {
                index = calculProducteur(i);
            } catch (RemoteException e) {
                index = 0;
            }
            retour = j.getRessource(index,i,k);
            if(sum){
                retour = j.getTotalRessource();
            }
            if(retour>=objectif){
                haveAObjectif = false;
                ressourceNonTermine = ressourceNonTermine(ressourceNonTermine,j.getRessources());
            }
        }
    }

    /**
     * Permet au joueur passif de trouver le meilleur producteur de la ressource i
     * @return l'index du producteur dans Producteur[]
     */
    private int calculProducteur(int i) throws RemoteException {
        Map<Integer,Integer> ressourceProducteur;
        int index = 0;
        int valMAX = -1;
        for(int j = 0;j<producteurs.length;j++){
            ressourceProducteur = producteurs[j].observe();
            if(ressourceProducteur.get(i) > valMAX){
                index = j;
                valMAX = ressourceProducteur.get(i);
            }
        }
        return index;
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
        }else if(sum){
            if(j.getTotalRessource() == objectifSum){
                try {
                    j.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * Retourne l'index de la meilleur ressource à l'instant T. La ressource où
     * l'écart entre se qu'on possède et l'objectif est la plus grande
     * @param ressource les ressources du joueurs
     * @param objectif les objectifs par ressource
     * @return le numéro de la ressource
     */
    private synchronized int meilleurObjectif(Map<Integer,Integer> ressource,Map<Integer,Integer> objectif){
        int index = 0;
        int maxDiff = 0;
        int diffActuel = 0;
        for(int i: objectif.keySet()){
            diffActuel = objectif.get(i) - ressource.get(i);
            if(diffActuel >= maxDiff){
                index = i;
                maxDiff = diffActuel;
            }
        }
        return index;
    }


    public void echo(String message) {
        System.err.println("Action d'un joueur: "+message);
    }
}
