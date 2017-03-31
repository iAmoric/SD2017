package joueur;

import producteur.Producteur;
import producteur.ProducteurImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 31/03/17.
 */
public class LancerJoueur {
    public static void main(String[] args){
        int portRMI = 5555;
        try
        {
            //TODO faire des arguments: hostStarter portStarter
            JoueurImpl impl = new JoueurImpl();
            Joueur objLocal = (Joueur) impl;
            Naming.rebind( "rmi://localhost:"+portRMI+"/JoueurA" ,objLocal) ;
            System.out.println("Joueur  A pret") ;
            //s'enregistrer chez Starter

            //Tant que le Starter ne l'a pas initialisé
            while(true){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Lancer la partie

        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }

    }
}
