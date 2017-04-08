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
        int portRMI = 5555;
        try
        {
            ProducteurImpl impl = new ProducteurImpl();
            Producteur objLocal = (Producteur)impl;
            Naming.rebind( "rmi://localhost:"+portRMI+"/ProducteurA" ,objLocal) ;
            System.out.println("Producteur pret") ;
            //impl.startProduction();

        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }

    }
}
