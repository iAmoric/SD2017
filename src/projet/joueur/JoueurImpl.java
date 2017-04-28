package projet.joueur;

//import org.omg.PortableInterceptor.INACTIVE;
import projet.coordinateur.End;
import projet.exceptions.FinDePartieException;
import projet.exceptions.StealException;
import projet.producteur.Producteur;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Modèle du joueur.
 * Cette classe contient toutes les informations
 * que le joueur doit connaitre sur lui-même, sur le système
 * et sur les règles de la partie
 * Created by jpabegg on 25/03/17.
 */
public class JoueurImpl extends UnicastRemoteObject implements Joueur {
    private Producteur[] producteurs;//liste des producteurs
    private Joueur[] joueurs;//liste des joueurs
    private End finDePartie;
    private int id;//indice du joueur dans le tableau
    private Map<Integer,List<Integer>> mapRessourceProducteurs;
    private Map<Integer,Integer> ressources;
    private Map<Integer,Integer> objectifs;
    private List<Integer> ressourceOrdonnee;//id des ressources dans l'ordre croissant
    private int nbRessourcePrenable;//nb de ressource récupérable en une fois chez un producteur
    private boolean doSum = false;
    private int sumObjectif;
    private boolean isEpuisable;
    private boolean canSteal;
    private BufferedWriter writer;
    private File log;
    private boolean haveFinished = false;
    private boolean isPlaying = false;//vrai si on a lancé un ThreadJoueur avec ce joueur
    private boolean modeAntiVoleActive = false;
    private BufferedReader logReader = null;
    private Comportement comportement;
    private ThreadJoueur threadJoueur = null;
    private boolean tourParTour;
    private Set<Integer> observateur;//numéro des joueurs qui nous observes
    private long numeroTour = 0;
    private long timestamp;
    private Object lock = new Object();

    public JoueurImpl(Comportement comportement) throws IOException {
        super();
        this.comportement = comportement;
        mapRessourceProducteurs = new HashMap<Integer, List<Integer>>();
        ressources = new HashMap<Integer,Integer>();
        observateur = new HashSet<Integer>();
    }


    public void setId(int id) throws RemoteException{
        log = new File("logJoueur"+id);
        try {
            writer = new BufferedWriter(new FileWriter(log));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.id = id;
        try {
            //writer.write("Joueur " + id + "\n");
            switch (comportement){
                case PASSIF:
                    writer.write("Passif\n");
                    break;
                case AGGRESIF:
                    writer.write("Aggressif\n");
                    break;
                case JOUEUR:
                    writer.write("Joueur\n");
                    break;
                case MALIN:
                    writer.write("Malin\n");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setObjectifs(Map<Integer, Integer> objectif, boolean doSum, int sum) throws RemoteException {
        objectifs = new HashMap<Integer, Integer>(objectif);
        for(int i:objectif.keySet()){
            ressources.put(i,0);
        }
        ressourceOrdonnee = new ArrayList<Integer>(ressources.keySet());
        Collections.sort(ressourceOrdonnee);
        this.doSum = doSum;
        sumObjectif = sum;
    }

    /**
     * Se connecte au autre instance de Joueur RMI
     * et initialise le tableau Joueur[] joueurs
     * @param rmi: les infos RMI
     * @return si les connections sont réussi
     */
    public boolean ajouteJoueurs(String[] rmi) {
        joueurs = new Joueur[rmi.length-1];
        int j = 0;
        for (int i =0;i<rmi.length;i++){
            if(i != id){
                try {
                    joueurs[j] = (Joueur) Naming.lookup(rmi[i]) ;
                }
                catch (Exception re) {System.out.println(re) ; return false;}
                j++;
            }

        }
        return true;
    }

    /**
     * Méthode pour se connecter aux producteurs
     * @param rmi: les chaines RMI pour se connecter
     * @return si les connections ont été réussies
     * @throws RemoteException
     */
    public boolean ajouteProducteurs(String[] rmi) throws RemoteException {
        producteurs = new Producteur[rmi.length];
        for (int i = 0;i<rmi.length;i++){
            try {
                producteurs[i] = (Producteur)Naming.lookup(rmi[i]);
                whatDoHeProduce(i);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Setter du coordinateur de fin de partie
     * @param rmi l'adresse de connection RMI
     * @return true si la connection est OK / false erreur
     */
    public boolean ajouteFin(String rmi) throws RemoteException {
        try {
            finDePartie = (End)Naming.lookup(rmi);
            return true;
        } catch (MalformedURLException| NotBoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Peremt de définir un certain nombre d'informations sur les règles de la partie
     * @param n le nombre de ressource prenable en une fois chez un producteur
     * @param canSteal si on peut voler les autres joueurs
     * @param isEpuisable si la production de ressource est épuisable
     */
    public void setRules(int n, boolean canSteal, boolean isEpuisable,boolean tourParTour) {
        nbRessourcePrenable = n;
        this.canSteal = canSteal;
        this.tourParTour = tourParTour;
        this.isEpuisable = isEpuisable;
        try {
            writer.write(tourParTour ? "ON\n" : "OFF\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de connaitre les ressources que produit le producteur
     * et de l'ajouter dans la liste des producteurs qui produisent ces ressources
     * (il est possible qu'un producteur produise plusieurs ressources )s
     * @param index: l'index du producetur dans le tableau
     */
    public void whatDoHeProduce(int index) throws RemoteException {
        int[] ressourcesProduites;
        ressourcesProduites = producteurs[index].whatDoYouProduce();
        List<Integer> list;
        for (int i =0;i<ressourcesProduites.length;i++){
            if((list = mapRessourceProducteurs.get(i)) == null){
                list = new ArrayList<Integer>();
            }
            list.add(index);
            mapRessourceProducteurs.put(i,list);
        }
    }

    /**
     * Méthode de DEBUG
     * @param os un flux de sortie
     */
    public void info(PrintStream os){
        os.println("Joueur "+id);
        os.println("Ressources:");
        for (int i :mapRessourceProducteurs.keySet()){
            os.println("id/possedé (nbProducteur) "+i+"/"+ressources.get(i)+"("+mapRessourceProducteurs.get(i).size()+")");
        }
        os.println("Objectifs");
        if(doSum){
            os.println(sumObjectif+" ressources au total");
        }
        for(int i:objectifs.keySet()){
            os.println("id/nombre "+i+"/"+objectifs.get(i));
        }
    }

    /**
     *
     * @param index l'index du producteur dans le tableau
     * @param idRessource l'id d'une ressource
     * @param quantite le nombre d'unité a recupéré chez le producteur
     * @return le nombre total d'unité de la ressource id que le joueur possède ou -1 si erreur
     */
    public synchronized int getRessource(int index,int idRessource,int quantite){
        int total;
        String message;
        long estampille = genereEstampille();
        try {
            int obtenue = producteurs[index].getRessource(idRessource,quantite);
            if(obtenue != -1){
                total = obtenue + ressources.get(idRessource);
                ressources.put(idRessource,total);
                message = (estampille+" get "+index+" "+idRessource+" "+quantite+" "+obtenue);
                for(int i:ressourceOrdonnee){
                   message = message+" "+ressources.get(i);
                }
                writer.write(message+"\n");
                writer.flush();
                for (int i:observateur){
                    joueurs[i].echo(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!ressources.keySet().contains(idRessource))return -1;
        return ressources.get(idRessource);
    }

    private long genereEstampille() {
        long estampille;
        if(tourParTour){
            estampille = numeroTour;
            numeroTour++;
        }else{
            estampille = System.currentTimeMillis() - timestamp;
        }
        return estampille;
    }

    /**
     * Arrete le joueur et préviens le coordinateur final de la fin de partie
     * @throws RemoteException
     */
    public synchronized void stop() throws RemoteException {
        haveFinished = true;
        finDePartie.haveFinished(id);
        synchronized (lock){
            lock.notifyAll();
        }

    }

    /**
     * Lance le thread de jeu du Joueur. Il n'y a qu'un seul thread par instance de Joueur
     * @throws RemoteException
     */
    public void start() throws RemoteException{
        if(!isPlaying){
            isPlaying = true;
            threadJoueur = new ThreadJoueur(this);
            timestamp = System.currentTimeMillis();
            threadJoueur.start();
        }
    }

    /**
     * @param id: id de la ressource
     * @param quantite: quantité de ressource à voler
     * @return la quantité réellement volée
     * @throws StealException: vol annulé car on a été repéré
     */
    public synchronized int voler(int id, int quantite) throws RemoteException, StealException {
        int result = 0;
        if(!haveFinished){
            if(modeAntiVoleActive)throw new StealException();
            //On ne peut pas voler un joueur qui a terminé la partie
            if(!ressources.keySet().contains(id))return -1;
            int dispo = ressources.get(id);
            if(dispo - quantite >= 0){
                result = quantite;
            }else{
                result = dispo;
            }
            ressources.put(id,dispo-result);
        }
        return result;
    }

    /**
     * Ajoute un joueur à la liste des joueur qui observe les actions cette instance
     * @param id, numéro d'un joueur
     * @return true
     * @throws RemoteException
     */
    public synchronized boolean ajouteObservation(int id) throws RemoteException {
        if(id>this.id){
            id--;
        }
        observateur.add(id);
        return true;
    }

    /**
     * Retire un joueur de la listre des joueurs qui observe les actions de cette instance
     * @param id, numéro d'un joueur
     * @return true
     * @throws RemoteException
     */
    public synchronized boolean retireObservation(int id) throws RemoteException {
        if(id>this.id){
            id--;
        }
        observateur.remove(id);
        return true;
    }

    /**
     * Permet de recevoir l'action d'un  joueur qu'on observe
     * @param message: mesage d'un joueur qu'on observe
     * @throws RemoteException
     */
    public void echo(String message) throws RemoteException {
        threadJoueur.echo(message);
    }

    /**
     *
     * @param indexJoueur index du joueur dans Joueur[]
     * @param idRessource id de la ressource à volé
     * @param quantite quantité a voler
     * @return nombre de ressource total
     * @throws RemoteException
     * @throws StealException
     */
    public int voleJoueur(int indexJoueur,int idRessource, int quantite) throws RemoteException, StealException {
        int result = 0;
        long estampille = genereEstampille();
        String message;
        try{
            result =  joueurs[indexJoueur].voler(idRessource,quantite);
            message = estampille+" steal "+indexJoueur+" "+idRessource+" "+quantite+" "+result;
            for(int i:ressourceOrdonnee){
                message = message+" "+ressources.get(i);
            }
            writer.write(message+"\n");
            writer.flush();
            for (int i:observateur){
                joueurs[i].echo(message);
            }
        }catch (StealException e){
            try {
                message = estampille+" steal "+indexJoueur+" "+idRessource+" "+quantite+" -1";
                for(int i:ressourceOrdonnee){
                    message = message+" "+ressources.get(i);
                }
                writer.write(message+"\n");
                writer.flush();
                for (int i:observateur){
                    joueurs[i].echo(message);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            throw new StealException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Permet de connaitre les ressources que possède le Joueur
     * @return les ressources du Joueur
     */
    public synchronized Map<Integer,Integer> observe() throws RemoteException{
        return new HashMap<Integer, Integer>(ressources);
    }

    /**
     * @return une ligne du fichier de log
     * @throws IOException
     */
    @Override
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

    public boolean playTurn() throws RemoteException,FinDePartieException{
        synchronized (lock){
            if(haveFinished) throw new FinDePartieException();
            //System.err.println("Joueur"+id+ ":notify");
            if(comportement == Comportement.JOUEUR){
                return threadJoueur.tourJoueur();
            }else{
                lock.notifyAll();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public Map<Integer,Integer> observeAutreJoueur(int id) throws RemoteException {
        if(id >= joueurs.length)return null;
        return joueurs[id].observe();
    }


    public void observeSysteme(){
        for (int i = 0;i<joueurs.length;i++){
            try {
                joueurs[i].ajouteObservation(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void observeSystemeFin(){
        for (int i = 0;i<joueurs.length;i++){
            try {
                joueurs[i].retireObservation(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    //GETTERS
    public int getNbRessourcePrenable(){
        return nbRessourcePrenable;
    }

    public boolean haveFinished(){
        return haveFinished;
    }

    public Map<Integer,Integer> getObjectifs(){
        return objectifs;
    }

    public Map<Integer,Integer> getRessources(){
        return ressources;
    }



    public Map<Integer,List<Integer>> getListProducteur(){
        return mapRessourceProducteurs;
    }

    public int getId() {return id;}

    public Comportement getComportement(){
        return comportement;
    }

    public boolean canSteal(){
        return canSteal;
    }

    public boolean isEpuisable(){
        return isEpuisable;
    }

    public boolean doSum(){
        return doSum;
    }

    public int getSumObjectif(){
        return sumObjectif;
    }

    public synchronized int getTotalRessource(){
        int result = 0;
        for( int i:ressources.keySet()){
            result+=ressources.get(i);
        }
        return result;
    }

    public int autreJoueurs(){
        return joueurs.length;
    }

    public Producteur[] getProducteurs() {
        return producteurs;
    }

    public boolean isTourParTour() {
        return tourParTour;
    }

    public Object getLock(){
        return lock;
    }

    //SETTERS
    public void setModeAntiVole(boolean modeAntiVoleActive){
        this.modeAntiVoleActive = modeAntiVoleActive;
    }


    public boolean numeroRessourceValide(int numeroRessource) {
        return numeroRessource>0 && numeroRessource <= objectifs.size();
    }

    public boolean joueurValide(int numeroJoueur) {
        return numeroJoueur>= 0 && numeroJoueur<joueurs.length;
    }
}