package projet.joueur;

import projet.Agent;
import projet.exceptions.StealException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Interface RMI
 * Created by jpabegg on 25/03/17.
 */
public interface  Joueur extends Agent{
    //Pour initialiser le système
    void setObjectifs(Map<Integer,Integer> objectif,boolean doSum,int sum) throws RemoteException;
    boolean ajouteJoueurs(String[] rmi) throws RemoteException;
    boolean ajouteProducteurs(String[] rmi)throws RemoteException;
    boolean ajouteFin(String rmi) throws RemoteException;
    void setRules(int n,boolean canSteal,boolean isEpuisable,boolean tourParTour)throws RemoteException;
    void start() throws RemoteException;
    //Action
    int voler(int id,int quantite) throws RemoteException,StealException;

    //Pour observer toutes les actions dans le système
    boolean ajouteObservation(int id)throws RemoteException;
    boolean retireObservation(int id)throws RemoteException;
    void echo(String message)throws RemoteException;


}
