package projet.producteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Lance un producteur. 2 arguments: portRMI et le nom du service a enregistrer
 * Created by jpabegg on 29/03/17.
 */
public class LancerProducteur {
    public static void main(String[] args){
        if(args.length != 2){
            System.err.println("usage LancerProducteur portRMI nomService");
            System.exit(1);
        }
        int portRMI = Integer.parseInt(args[0]);
        String nomService = args[1];
        try
        {
            String rmi =  "rmi://localhost:"+portRMI+"/"+nomService;
            ProducteurImpl impl = new ProducteurImpl();
            Producteur objLocal = (Producteur)impl;
            Naming.rebind(rmi ,objLocal) ;
            System.out.println("Producteur pret: "+rmi) ;

        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }

    }
}
