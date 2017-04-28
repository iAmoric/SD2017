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
        if(args.length != 2){
            System.err.println("usage LancerEnd portRMI nomService");
            System.exit(1);
        }
        int portRMI = Integer.parseInt(args[0]);
        String nomService = args[1];
        try
        {
            EndImpl impl = new EndImpl();
            End objLocal = (End)impl;
            String rmi =  "rmi://localhost:"+portRMI+"/"+nomService;
            Naming.rebind(rmi ,objLocal) ;
            System.err.println("Coordinateur de fin enregistré: "+rmi);
        }
        catch (RemoteException re) { System.out.println(re) ; }
        catch (MalformedURLException e) { System.out.println(e) ; }
    }
}
