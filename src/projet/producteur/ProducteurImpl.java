package projet.producteur;

import java.io.PrintStream;
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
        for (int i:ressourceDispo.keySet()){
            if(isRessourceEpuisable){
                n = (ressourceDispo.get(i)/2) + 1;
            }else{
                n = k;
            }
            ressourceDispo.put(i,n);
        }
    }

    /**
     * @return faux tant que le projet.producteur n'est pas pret
     */
    public boolean isReady() throws RemoteException{
        return isReady;
    }

    @Override
    public Integer[] whatDoYouProduce() {
        return (Integer[])ressourceDispo.keySet().toArray();
    }

    public void setProductions(Map<Integer,Integer> ressourceDispo){
        this.ressourceDispo = new HashMap<Integer, Integer>(ressourceDispo);
        isReady = true;
    }

    public void startProduction(){
        if(thread == null){
            thread = new ThreadRessource(this,k);
            thread.start();
        }
    }

    public void stopProduction(){
        thread.stopWorking();
    }


    public void info(PrintStream os){
        os.println("Production:");
        for (int i:ressourceDispo.keySet()){
            os.println(" id/quantitée "+i+"/"+ressourceDispo.get(i));
        }
    }
}