package projet.joueur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 25/03/17.
 */
public interface  Joueur extends Remote{
    int getRessources(int idRessource) throws RemoteException;
    boolean observe(Joueur j) throws RemoteException;
    boolean isReady() throws RemoteException;
    void setId(int id) throws RemoteException;
    boolean ajouteJoueurs(String[] rmi) throws RemoteException;
    boolean ajouteProducteurs(String[] rmi)throws RemoteException;
}
