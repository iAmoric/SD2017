package projet.coordinateur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Coordinateur de fin de partie
 * Created by jpabegg on 08/04/17.
 */
public interface End extends Remote {
    void setJoueurs(String[] rmi) throws RemoteException;
    void setProducteurs(String[] rmi)throws RemoteException;
    void haveFinished(int id)throws RemoteException;

}
