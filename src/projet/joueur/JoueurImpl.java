package projet.joueur;

import org.omg.PortableInterceptor.INACTIVE;
import projet.coordinateur.End;
import projet.producteur.Producteur;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by jpabegg on 25/03/17.
 */
public class JoueurImpl extends UnicastRemoteObject implements Joueur {
    private Producteur[] producteurs;//liste des producteurs
    private Joueur[] joueurs;//liste des joueurs
    private End finDePartie;
    private int id;//indice du joueur dans le tableau
    private Map<Integer,List<Producteur>> mapRessourceProducteurs;
    private Map<Integer,Integer> ressources;
    private Map<Integer,Integer> objectifs;
    private int nbRessourcePrenable;//nb de ressource récupérable en une fois chez un producteur
    private boolean isReady =false;
    private boolean doSum = false;
    private int sumObjectif;
    private boolean isEpuisable;
    private boolean canSteal;
    private boolean haveFinished = false;
    private boolean isPlaying = false;//vrai si on a lancé un ThreadJoueur avec ce joueur

    public JoueurImpl() throws RemoteException {
        super();
        mapRessourceProducteurs = new HashMap<Integer, List<Producteur>>();
        ressources = new HashMap<Integer,Integer>();
    }

    public boolean isReady() throws RemoteException {
        return isReady;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setObjectifs(Map<Integer, Integer> objectif, boolean doSum, int sum) throws RemoteException {
        objectifs = new HashMap<Integer, Integer>(objectif);
        for(int i:objectif.keySet()){
            ressources.put(i,0);
        }
        this.doSum = doSum;
        sumObjectif = sum;
    }

    /**
     * Se connecte au autre instance de Joueur RMI
     * et initialise le tableau Joueur[] joueurs
     * @param rmi: les infos RMI
     * @return si les connections sont réussi
     */
    public boolean ajouteJoueurs(String[] rmi) {
        joueurs = new Joueur[rmi.length-1];
        int j = 0;
        for (int i =0;i<rmi.length;i++){
            if(i != id){
                try {
                    joueurs[j] = (Joueur) Naming.lookup(rmi[i]) ;
                }
                catch (Exception re) {System.out.println(re) ; return false;}
                j++;
            }

        }
        return true;
    }


    public boolean ajouteProducteurs(String[] rmi) throws RemoteException {
        producteurs = new Producteur[rmi.length];
        for (int i = 0;i<rmi.length;i++){
            try {
                producteurs[i] = (Producteur)Naming.lookup(rmi[i]);
                whatDoHeProduce(producteurs[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        isReady = true;
        return true;
    }


    public boolean ajouteFin(String rmi) throws RemoteException {
        try {
            finDePartie = (End)Naming.lookup(rmi);
            return true;
        } catch (MalformedURLException| NotBoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void setRules(int n, boolean canSteal, boolean isEpuisable) {
        nbRessourcePrenable = n;
        this.canSteal = canSteal;
        this.isEpuisable = isEpuisable;
    }

    public void whatDoHeProduce(Producteur p) throws RemoteException {
        int[] ressourcesProduites;
        ressourcesProduites = p.whatDoYouProduce();
        List<Producteur> list;
        for (int i =0;i<ressourcesProduites.length;i++){
            if((list = mapRessourceProducteurs.get(i)) == null){
                list = new ArrayList<Producteur>();
            }
            list.add(p);
            mapRessourceProducteurs.put(i,list);
        }
    }

    public void info(PrintStream os){
        os.println("Joueur "+id);
        os.println("Ressources:");
        for (int i :mapRessourceProducteurs.keySet()){
            os.println("id/possedé (nbProducteur) "+i+"/"+ressources.get(i)+"("+mapRessourceProducteurs.get(i).size()+")");
        }

        os.println("Objectifs");
        if(doSum){
            os.println(sumObjectif+" ressources au total");
        }
        for(int i:objectifs.keySet()){
            os.println("id/nombre "+i+"/"+objectifs.get(i));
        }
    }

    /**
     *
     * @param p un producteur
     * @param idRessource l'id d'une ressource
     * @param quantite le nombre d'unité a recupéré chez le producteur
     * @return le nombre total d'unité de la ressource id que le joueur possède ou -1 si erreur
     */
    public synchronized int getRessource(Producteur p,int idRessource,int quantite){
        try {
            int obtenue = p.getRessource(idRessource,quantite);
            if(obtenue != -1){
                obtenue = obtenue + ressources.get(idRessource);
                ressources.put(idRessource,obtenue);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ressources.get(idRessource);
    }

    public int getNbRessourcePrenable(){
        return nbRessourcePrenable;
    }


    public synchronized boolean haveFinished(){
        return haveFinished;
    }
    public Map<Integer,Integer> getObjectifs(){
        return objectifs;
    }

    public Map<Integer,Integer> getRessources(){
        return ressources;
    }

    public synchronized void stop() throws RemoteException {
        haveFinished = true;
        finDePartie.haveFinished(id);
    }

    public Map<Integer,List<Producteur>> getListProducteur(){
        return mapRessourceProducteurs;
    }

    public void start() throws RemoteException{
        if(!isPlaying){
            isPlaying = true;
            Thread t = new ThreadJoueur(this);
            t.start();
        }
    }

}
