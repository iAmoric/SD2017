package projet.coordinateur;

import projet.joueur.Joueur;
import projet.joueur.JoueurImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by jpabegg on 08/04/17.
 */
public class LancerEnd {

    public static void main(String[] args){
        int portRMI = 5555;
        try
        {
            EndImpl impl = new EndImpl();
            End objLocal = (End)impl;
            Naming.rebind( "rmi://localhost:"+portRMI+"/EndImpl" ,objLocal) ;
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }
    }
}
