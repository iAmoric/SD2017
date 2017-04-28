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
        if(args.length != 3){
            System.err.println("usage LancerJoueur portRMI nomService comportement");
        }
        int portRMI = Integer.parseInt(args[0]);
        String nomService = args[1];
        String comportementString = args[2];
        Comportement comportement = null;
        for (Comportement c : Comportement.values()){
            if(c.getNom().equals(comportementString)){
                comportement = c;
            }
        }
        try
        {
            //TOUR rmi://localhost:5555/TourImpl
            //TODO faire des arguments: hostStarter portStarter
            JoueurImpl impl = new JoueurImpl(comportement);
            Joueur objLocal = (Joueur) impl;
            Naming.rebind( "rmi://localhost:"+portRMI+"/"+nomService ,objLocal) ;
            System.err.println("Le joueur est enregistré");
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
