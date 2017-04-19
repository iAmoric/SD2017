package projet.coordinateur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Coordinateur de tour par tour
 * Created by jpabegg on 19/04/17.
 */
public class TourParTourImpl extends UnicastRemoteObject implements TourParTour {
    protected TourParTourImpl() throws RemoteException {
    }
}
