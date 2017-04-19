package projet.producteur;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Producteur de ressource dans le système
 * Created by jpabegg on 25/03/17.
 */
public class ProducteurImpl extends UnicastRemoteObject implements Producteur{
    private Map<Integer,Integer> ressourceDispo;// <idRessource,exemplaire disponible>
    private boolean isReady = false;
    private boolean isRessourceEpuisable;
    private int k;
    private ThreadRessource thread;
    private int id;
    private File log;
    private BufferedWriter writer;
    private BufferedReader logReader;
    private boolean tourParTour;

    public ProducteurImpl() throws RemoteException{
        super();
    }

    public ProducteurImpl(Map<Integer,Integer> ressourceDispo, boolean isRessourceEpuisable, int k)
            throws RemoteException {
        super();
        this.ressourceDispo = ressourceDispo;
        this.isRessourceEpuisable = isRessourceEpuisable;
        this.k = k;
        checkArguments();
    }

    /**
     * Vérifie que toutes les valeurs initiales ne soit pas négatives
     */
    private void checkArguments(){
        Set<Integer> keyset;
        keyset = ressourceDispo.keySet();
        for (int i: keyset){
            if(ressourceDispo.get(i) < 0){
                ressourceDispo.put(i,0);
            }
        }
    }

    @Override
    public void setId(int id) throws IOException {
        this.id = id;
        if(log == null){
            log = new File("logProducteur"+id);
            writer = new BufferedWriter(new FileWriter(log));
        }
    }

    /**
     * Permet de recuperer des exemplaires de la ressource id
     * Si il n'y a pas n exemplaires disponibles alors le nombre
     * restant de ressources est donné
     *
     * -Exclusion mutuelle sur les ressources disponible
     * @param id: le numéro de la ressource
     * @param n: le nombre d'exemplaire demandé
     * @return le nombre de ressource obtenue ou -1 si erreur
     */
    public synchronized int getRessource(int id,int n)throws RemoteException{
        if(n<0)return -1;//un ne peux pas demander un nombre negatif d'unité de ressource
        if(!ressourceDispo.keySet().contains(id))return -1;//On ne peut pas demander une ressource qu'il ne produit pas
        int result = 0;

        int dispo = ressourceDispo.get(id);
        if(dispo - n >= 0){
            result = n;
        }else{
            result = dispo;
        }
        ressourceDispo.put(id,dispo-result);
        return result;
    }

    /**
     * Fonction appelé toute les k milisecondes et qui permet d'augmenter le nombre de ressource
     * Nécessite une exclusion mutuelle sur le Map<Integer,Integer>
     */
    public synchronized void addRessource() {
        int n;
        try {
            writer.write("coucou");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i:ressourceDispo.keySet()){
            if(isRessourceEpuisable){
                n = (ressourceDispo.get(i)/2) + 1;
            }else{
                n = k;
            }
            try {
                writer.write(" "+i+" "+ressourceDispo.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ressourceDispo.put(i,n);
        }
        try {
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] whatDoYouProduce() {
        int[] ids = new int[ressourceDispo.keySet().size()];
        int j = 0;
        for(int i:ressourceDispo.keySet()){
            ids[j] = i;
            j++;
        }
        return ids;
    }

    public void setProductions(Map<Integer,Integer> ressourceDispo){
        this.ressourceDispo = new HashMap<Integer, Integer>(ressourceDispo);
        isReady = true;
    }

    public void setRules(boolean isEpuisable, int k,boolean tourParTour) throws RemoteException {
        isRessourceEpuisable = isEpuisable;
        this.tourParTour = tourParTour;
        this.k = k;
    }

    public void startProduction() throws RemoteException{
        if(thread == null){
            thread = new ThreadRessource(this,k,tourParTour);
            thread.start();
        }
    }

    /**
     * Arrete le thread de production du producteur et arrete le processus
     * @throws RemoteException
     */
    public void stopProduction()throws RemoteException{
        thread.stopWorking();
        //System.exit(0);
    }


    /**
     * Permet de connaitre les ressources que possède le Producteur
     * @return les ressource du Producteur
     */
    public synchronized Map<Integer,Integer> observe()  throws RemoteException{
        return new HashMap<Integer, Integer>(ressourceDispo);
    }

    @Override
    public boolean playTurn() throws RemoteException {
        thread.notify();
        return true;
    }

    public String readLog() throws IOException {
        if(logReader == null){
            try {
                logReader = new BufferedReader(new FileReader(log));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return logReader.readLine();
    }


    public void info(PrintStream os){
        os.println("Production:");
        for (int i:ressourceDispo.keySet()){
            os.println(" id/quantitée "+i+"/"+ressourceDispo.get(i));
        }
    }
}
