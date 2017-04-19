package projet.coordinateur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 19/04/17.
 */
public interface Coordinateur extends Remote{
    void setJoueurs(String[] rmi) throws RemoteException;
    void setProducteurs(String[] rmi)throws RemoteException;
}
