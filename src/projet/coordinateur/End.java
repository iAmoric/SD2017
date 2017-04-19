package projet.coordinateur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Coordinateur de fin de partie
 * Created by jpabegg on 08/04/17.
 */
public interface End extends Coordinateur {
    void haveFinished(int id)throws RemoteException;
}
