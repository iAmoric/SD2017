package projet.producteur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 25/03/17.
 */
public interface Producteur extends Remote
{
   int getRessource(int id,int n) throws RemoteException;
   boolean isReady() throws RemoteException;

}
