package projet.coordinateur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 19/04/17.
 */
public class LancerTour {
    public static void main(String[] args){
        int portRMI = 5555;
        try
        {
           TourParTourImpl impl = new TourParTourImpl();
            TourParTour objLocal = (TourParTour)impl;
            Naming.rebind( "rmi://localhost:"+portRMI+"/TourImpl" ,objLocal) ;
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }
    }
}
