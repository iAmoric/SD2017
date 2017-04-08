package projet.joueur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by jpabegg on 25/03/17.
 */
public interface  Joueur extends Remote{
    boolean isReady() throws RemoteException;
    void setId(int id) throws RemoteException;
    void setObjectifs(Map<Integer,Integer> objectif,boolean doSum,int sum) throws RemoteException;
    boolean ajouteJoueurs(String[] rmi) throws RemoteException;
    boolean ajouteProducteurs(String[] rmi)throws RemoteException;
    boolean ajouteFin(String rmi) throws RemoteException;
    void setRules(int n,boolean canSteal,boolean isEpuisable)throws RemoteException;
    void start() throws RemoteException;
}
