package projet.coordinateur;

import projet.joueur.Joueur;
import projet.producteur.Producteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Implémentation du coordinateur de fin de partie
 * Created by jpabegg on 08/04/17.
 */
public class EndImpl extends UnicastRemoteObject implements End {
    private Producteur[] producteurs;
    private Joueur[] joueurs;
    private boolean[] joueursEnPartie;
    protected EndImpl() throws RemoteException {
    }


    public void setJoueurs(String[] rmi) throws RemoteException {
        joueurs = new Joueur[rmi.length];
        joueursEnPartie = new boolean[rmi.length];
        for(int i = 0;i<rmi.length;i++){
            try {
                joueurs[i] = (Joueur) Naming.lookup(rmi[i]);
                joueursEnPartie[i] = true;
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setProducteurs(String[] rmi) throws RemoteException {
        producteurs = new Producteur[rmi.length];
        for(int i = 0;i<rmi.length;i++){
            try {
                producteurs[i] = (Producteur) Naming.lookup(rmi[i]);
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void haveFinished(int id) throws RemoteException {
        boolean partieTermine = true;
        int i;
        joueursEnPartie[id] = false;
        System.err.println("Le joueur "+id+" a terminé");
        for(i = 0;i<joueursEnPartie.length;i++){
            if(partieTermine){
                if(joueursEnPartie[i]){
                    partieTermine = false;
                }
            }
        }
        if(partieTermine){
            System.err.println("La partie est terminé");
            for (i = 0;i<producteurs.length;i++){
                producteurs[i].stopProduction();
            }
        }
    }
}
