package projet.coordinateur;

import projet.exceptions.FinDePartieException;
import projet.joueur.Joueur;
import projet.producteur.Producteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Coordinateur de tour par tour
 * Created by jpabegg on 19/04/17.
 */
public class TourParTourImpl extends UnicastRemoteObject implements TourParTour {
    private Joueur[] joueurs;
    private Producteur[] producteurs;
    private boolean isWorking = false;

    protected TourParTourImpl() throws RemoteException {
    }


    public synchronized void start() throws RemoteException {
        if(!isWorking){
            isWorking = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    coordinationTourParTour();
                }
            });
            t.start();
        }
    }

    private void coordinationTourParTour() {
        boolean continuer = true;
        int i = 0;
        int j;
        int k;
        Joueur[] temp;
        while (continuer){
            for(j=0;j<producteurs.length;j++){
                try {
                    producteurs[j].playTurn();
                } catch (RemoteException e) {
                    System.err.println("Tour par tour: erreur avec le producteur "+j);
                } catch (FinDePartieException e) {
                    e.printStackTrace();
                }
            }
            try {
                if(i>=joueurs.length)i=0;
                //System.err.println("Tour "+i);
                joueurs[i].playTurn();
                i = (i+1)%joueurs.length;
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (FinDePartieException e) {
                //On doit supprimer un joueur
                System.err.println("SUPPRESSION D UN JOUEUR");
                //Si le dernier joueur a généré l'exception alors la partie est terminée
                if (joueurs.length == 1) continuer = false;
                else {
                    //sinon on supprime le joueur du tableau
                    temp = new Joueur[joueurs.length - 1];
                    k = 0;
                    for (j=0;j<joueurs.length;j++){
                        if(j!=i){
                            temp[k] = joueurs[j];
                            k++;
                        }
                    }

                    joueurs = temp;
                }
            }
        }
        System.err.println("La partie est terminé");
    }

    /**
     * @param rmi: un tabelau permettant de se connecter en RMI au joueurs
     */
    public void setJoueurs(String[] rmi) throws RemoteException {
        joueurs = new Joueur[rmi.length];
        for(int i = 0;i<rmi.length;i++){
            try {
                joueurs[i] = (Joueur) Naming.lookup(rmi[i]);
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param rmi: un tableau permettant de se connecter en RMI au producteurs
     */
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

}
