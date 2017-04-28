package projet.coordinateur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 19/04/17.
 */
public class LancerTour {
    public static void main(String[] args){
        if(args.length != 2){
            System.err.println("usage LancerTour portRMI nomService");
            System.exit(1);
        }
        int portRMI = Integer.parseInt(args[0]);
        String nomService = args[1];
        try
        {
            String rmi =  "rmi://localhost:"+portRMI+"/"+nomService;
           TourParTourImpl impl = new TourParTourImpl();
            TourParTour objLocal = (TourParTour)impl;
            Naming.rebind( rmi,objLocal) ;
            System.err.println("Coordinateur de tour par tour enregistré: "+rmi);
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }
    }
}
