package projet.coordinateur;

import projet.Agent;
import projet.joueur.Joueur;
import projet.producteur.Producteur;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Implémentation du coordinateur de fin de partie
 * Created by jpabegg on 08/04/17.
 */
public class EndImpl extends UnicastRemoteObject implements End {
    private Producteur[] producteurs;
    private Joueur[] joueurs;//on utilisera les joueurs pour récupérer les log de la partie
    private boolean[] joueursEnPartie;
    private File[] logsJoueur;
    private File[] logsProducteur;
    private Map<Integer,String> clefNumeroRessource;
    protected EndImpl() throws RemoteException {

    }

    /**
     * @param rmi: un tabelau permettant de se connecter en RMI au joueurs
     */
    public void setJoueurs(String[] rmi) throws RemoteException {
        joueurs = new Joueur[rmi.length];
        joueursEnPartie = new boolean[rmi.length];
        logsJoueur = new File[rmi.length];
        for(int i = 0;i<rmi.length;i++){
            try {
                joueurs[i] = (Joueur) Naming.lookup(rmi[i]);
                joueursEnPartie[i] = true;
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param rmi: un tableau permettant de se connecter en RMI au producteurs
     */
    public void setProducteurs(String[] rmi) throws RemoteException {
        producteurs = new Producteur[rmi.length];
        logsProducteur = new File[rmi.length];
        for(int i = 0;i<rmi.length;i++){
            try {
                producteurs[i] = (Producteur) Naming.lookup(rmi[i]);
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Permet à un joueur d'indiquer qu'il a atteint l'objectif fixé en début de partie
     * @param id: id d'un joueur de la partie
     */
    public synchronized void haveFinished(int id) throws RemoteException {
        boolean partieTermine = true;
        joueursEnPartie[id] = false;
        int i;
        System.err.println("Le joueur "+id+" a terminé");
        //On récup_re directement le log du joueur qui a terminé
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logsJoueur[id] = new File("logFinalJoueur"+id);
                    getFile(logsJoueur[id],joueurs[id]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        //On vérifie si tout les joueurs ont terminé
        for(i = 0;i<joueursEnPartie.length;i++){
            if(partieTermine){
                if(joueursEnPartie[i]){
                    partieTermine = false;
                }
            }
        }
        //Si oui alors on donne l'ordre au producteur de s'arreter
        if(partieTermine){
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        finDePartie();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    @Override
    public void clefRessource(Map<String, Integer> clefRessource) throws RemoteException {
        if(clefNumeroRessource == null)clefNumeroRessource = new HashMap<Integer,String>();
        for (String s:clefRessource.keySet()){
            clefNumeroRessource.put(clefRessource.get(s),s);
        }
    }


    public void finDePartie() throws IOException {
        int i;
        System.err.println("La partie est terminé");
        System.err.println("Récupération log des producteurs");
        for(i = 0;i<producteurs.length;i++){
            producteurs[i].stopProduction();
        }
        for (i = 0;i<producteurs.length;i++){
            logsProducteur[i] = new File("logFinalProducteur"+i);
            getFile(logsProducteur[i],producteurs[i]);
        }
        System.err.println("FIN LOGS PRODUCTEUR");
    }

    /**
     * Télécharge le fichier de log d'un agent dans le fichier f
     * @param f : le fichier de sortie
     * @param a : un agent du système
     * @return le fichier
     */
    private File getFile(File f, Agent a) throws IOException {
        String ligne;
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        do{
            ligne = a.readLog();
            if(ligne!=null){
                // System.err.println(ligne);
                writer.write(ligne+'\n');
            }
        }while (ligne != null );
        writer.flush();
        return f;
    }
}
