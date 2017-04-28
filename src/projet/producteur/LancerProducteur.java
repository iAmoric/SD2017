package projet.producteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 29/03/17.
 */
public class LancerProducteur {

    /**
     * Rappel pour faire du RMI:
     * rmic NomImpl <-- génère les stubs/skeleton
     *
     * A propos de rmic
     *      Il faut lancer depuis le dossier src
     *      et faire rmic [packages].nomDeLaClass
     *
     * rmiregistry <No port> & <-- dans le dossier avec les stubs ( .class)
     */
    public static void main(String[] args){
        if(args.length != 2){
            System.err.println("usage LancerProducteur portRMI nomService");
            System.exit(1);
        }
        int portRMI = Integer.parseInt(args[0]);
        String nomService = args[1];
        try
        {
            ProducteurImpl impl = new ProducteurImpl();
            Producteur objLocal = (Producteur)impl;
            Naming.rebind( "rmi://localhost:"+portRMI+"/"+nomService ,objLocal) ;
            System.out.println("Producteur pret") ;

        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }

    }
}
