package projet.joueur;

import projet.producteur.Producteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

/**
 * Created by jpabegg on 25/03/17.
 */
public class JoueurImpl extends UnicastRemoteObject implements Joueur {
    private Producteur[] producteurs;//liste des producteurs
    private Joueur[] joueurs;//liste des joueurs
    private int id;//indice du projet.joueur dans le tableau
    private Map<Integer,Integer> ressources;
    private Map<Integer,Integer> objectifs;
    private boolean working;

    public JoueurImpl() throws RemoteException {
        super();
    }





    public int getRessources(int idRessource) {
        return 0;
    }


    public boolean observe(Joueur j) {
        return false;
    }


    public void setId(int id) {
        this.id = id;
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
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void setProducteurs(Producteur[] producteurs) {
        this.producteurs = producteurs;
    }

    public void setRessources(Map<Integer, Integer> ressources) {
        this.ressources = ressources;
    }

    public void setObjectifs(Map<Integer, Integer> objectifs) {
        this.objectifs = objectifs;
    }
}
