package projet.joueur;

import projet.producteur.Producteur;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jpabegg on 25/03/17.
 */
public class JoueurImpl extends UnicastRemoteObject implements Joueur {
    private Producteur[] producteurs;//liste des producteurs
    private Joueur[] joueurs;//liste des joueurs
    private int id;//indice du projet.joueur dans le tableau
    private Map<Integer,Set<Producteur>> mapRessourceProducteurs;
    private Map<Integer,Integer> ressources;
    private Map<Integer,Integer> objectifs;
    private int nbRessourcePrenable;//nb de ressource récupérable en une fois chez un producteur
    private boolean isReady;
    private boolean doSum = false;
    private int sumObjectif;
    private boolean isEpuisable;
    private boolean canSteal;

    public JoueurImpl() throws RemoteException {
        super();
        mapRessourceProducteurs = new HashMap<Integer, Set<Producteur>>();
        ressources = new HashMap<Integer,Integer>();
        objectifs = new HashMap<Integer, Integer>();
    }





    public int getRessources(int idRessource) {
        return 0;
    }


    public boolean observe(Joueur j) {
        return false;
    }

    public boolean isReady() throws RemoteException {
        return isReady;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setObjectifs(Map<Integer, Integer> objectif, boolean doSum, int sum) throws RemoteException {
        objectifs = new HashMap<Integer, Integer>(objectif);
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

    @Override
    public void setRules(int n, boolean canSteal, boolean isEpuisable) {
        nbRessourcePrenable = n;
        this.canSteal = canSteal;
        this.isEpuisable = isEpuisable;
    }

    public void whatDoHeProduce(Producteur p) throws RemoteException {
        int[] ressourcesProduites;
        ressourcesProduites = p.whatDoYouProduce();
        Set<Producteur> set;
        for (int i =0;i<ressourcesProduites.length;i++){
            if((set = mapRessourceProducteurs.get(i)) == null){
                set = new HashSet<Producteur>();
            }
            set.add(p);
            mapRessourceProducteurs.put(i,set);
        }
    }
    public void setProducteurs(Producteur[] producteurs) {
        this.producteurs = producteurs;
    }

    public void info(PrintStream os){
        os.println("Joueur "+id);
        os.println("Ressources:");
        for (int i :mapRessourceProducteurs.keySet()){
            os.println("id/nbProducteur "+i+"/"+mapRessourceProducteurs.get(i).size());
        }

        os.println("Objectifs");
        if(doSum){
            os.println(sumObjectif+" ressources au total");
        }
        for(int i:objectifs.keySet()){
            os.println("id/nombre "+i+"/"+objectifs.get(i));
        }
    }

    public void setRessources(Map<Integer, Integer> ressources) {
        this.ressources = ressources;
    }

    public void setObjectifs(Map<Integer, Integer> objectifs) {
        this.objectifs = objectifs;
    }
}
