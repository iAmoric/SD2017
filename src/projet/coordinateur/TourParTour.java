package projet.coordinateur;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface du coordinateur du mode Tour par tour
 * Created by jpabegg on 19/04/17.
 */
public interface TourParTour extends Coordinateur{

    void start();
}
