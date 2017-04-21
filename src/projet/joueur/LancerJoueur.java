package projet.joueur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Launcher pour lancer deux joueurs
 * Created by jpabegg on 31/03/17.
 */
public class LancerJoueur {
    public static void main(String[] args){
        int portRMI = 5555;
        try
        {
            //TOUR rmi://localhost:5555/TourImpl
            //TODO faire des arguments: hostStarter portStarter
            JoueurImpl impl = new JoueurImpl("logA",Comportement.MALIN);
            Joueur objLocal = (Joueur) impl;
            JoueurImpl implB = new JoueurImpl("logB",Comportement.AGGRESIF);
            Naming.rebind( "rmi://localhost:"+portRMI+"/JoueurA" ,objLocal) ;
            Joueur objLocalB = (Joueur)implB;
            Naming.rebind("rmi://localhost:"+portRMI+"/JoueurB",objLocalB);
            System.out.println("Joueur  A et B pret") ;
            //s'enregistrer chez Starter
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
