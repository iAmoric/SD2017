package projet.joueur;

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
            JoueurImpl implB = new JoueurImpl();
            Naming.rebind( "rmi://localhost:"+portRMI+"/JoueurA" ,objLocal) ;
            Joueur objLocalB = (Joueur)implB;
            Naming.rebind("rmi://localhost:"+portRMI+"/JoueurB",objLocalB);
            System.out.println("Joueur  A et B pret") ;
            //s'enregistrer chez Starter

            //Tant que le Starter ne l'a pas initialisé
            while(!impl.isReady() || !implB.isReady()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }

    }
}
