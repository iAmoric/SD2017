package projet.coordinateur;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by lucas on 19/04/17.
 */
public class JSONConverter {

    private Random random;

    private int nbPlayers;
    private int nbProducers;
    private int nbResources = 5;
    private int totalPlayerTheftAttempt = 0;
    private int totalPlayerTheftSuccess = 0;

    private int[] playerTheftAttempt;
    private int[] playerTheftSuccess;
    private double[] playerTime;
    private int[] playerSteps;
    private double[] producerTime;
    private String[] playerComportement;
    private int[] theftResources;

    private boolean tour = false;

    private Map<Integer, String> resources;

    private File[] logPlayers;
    private File[] logProducers;

    JSONObject jsonObjectMain;

    public JSONConverter(Map<Integer, String> resources,
                         File[] logPlayers,
                         File[] logProducers)
    {
        this.resources = resources;
        this.logPlayers = logPlayers;
        this.logProducers = logProducers;

        this.nbPlayers = this.logPlayers.length;
        this.nbProducers = this.logProducers.length;
        this.nbResources = this.resources.size();


        playerTheftAttempt = new int[this.nbPlayers];
        playerTheftSuccess = new int[this.nbPlayers];
        playerTime = new double[nbPlayers];
        playerSteps = new int[nbPlayers];
        playerComportement = new String[nbPlayers];
        theftResources = new int[nbResources];
        for (int i = 0 ; i < theftResources.length ; i++)
            theftResources[i] = 0;

        producerTime = new double[nbProducers];

        random = new Random();

        jsonObjectMain = new JSONObject();

        try {
            parsePlayer();
            parseAllPlayers();
            parseProducer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObjectMain);

        HTMLGenerator htmlGenerator = new HTMLGenerator(this);
    }

    private void parseProducer() throws IOException {
        for (int i = 0; i < nbProducers; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(logProducers[i]));
            String line;
            String[] words;
            boolean res = false;
            int cpt = 0;

            JSONArray jsonMain = new JSONArray();
            JSONArray json = new JSONArray();

            while ((line = reader.readLine()) != null) {
                cpt++;
                words = line.split(" ");
                int nbRes = (words.length - 2) / 2;
                if (!res) {
                    //ajout des resources
                    json.add("Time");
                    for (int j = 2; j < words.length; j+=2){

                        json.add(resources.get(Integer.parseInt(words[j])));
                    }
                    jsonMain.add(json);
                    res = true;
                }

                json = new JSONArray();
                json.add(cpt);
                for (int j = 3; j < words.length; j+=2){
                    json.add(Integer.parseInt(words[j]));
                }
                jsonMain.add(json);
            }

            jsonObjectMain.put("Producer" + i, jsonMain);

            reader.close();
        }
    }


    //Constructeur pour les tests
    public JSONConverter()
    {
        //init map resources
        resources = new HashMap<>();
        resources.put(0, "or");
        resources.put(1, "petrol");
        //resources.put("fer", 2);
        //resources.put("bois", 3);
        //resources.put("pierre", 4);

        nbPlayers = 2;
        nbProducers = 1;
        //nbResources = resources.size();

        //init arrays (logs and thefts)
        logPlayers = new File[nbPlayers];
        playerTheftAttempt = new int[nbPlayers];
        playerTheftSuccess = new int[nbPlayers];
        playerTime = new double[nbPlayers];
        playerComportement = new String[nbPlayers];

        for (int i = 0; i < nbPlayers; i++){
            logPlayers[i] = new File("log"+i);
            playerTheftAttempt[i] = 0;
            playerTheftSuccess[i] = 0;
        }

        random = new Random();

        jsonObjectMain = new JSONObject();

        try {
            parsePlayer();
            parseAllPlayers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObjectMain);

        HTMLGenerator htmlGenerator = new HTMLGenerator(this);
    }

    public void parsePlayer() throws IOException {
        for (int i = 0; i < nbPlayers; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(logPlayers[i]));
            if (reader == null)
                System.err.println("Reader " + i + " null");

            String line;
            String[] words;
            int cpt = 0;

            JSONArray jsonMain = new JSONArray();
            JSONArray json = new JSONArray();

            //Ajout des titres temps + ressources
            json.add("Time");
            for (int j = 0; j < resources.size(); j++){
                json.add(resources.get(j));
            }
            jsonMain.add(json);

            line = reader.readLine();
            playerComportement[i] = line;

            //TODO tour par tour
            line = reader.readLine();
            if (line.equals("ON")){
                tour = true;
            }

            while ((line = reader.readLine()) != null) {
                json = new JSONArray();

                words = line.split(" ");
                if (tour) {
                    cpt++; //TODO change with timestamp
                    playerSteps[i] = cpt;
                }
                else {
                    double t = (Integer.parseInt(words[0]))/1000.;
                    long factor = (long) Math.pow(10, 1);
                    t = t * factor;
                    long tmp = Math.round(t);
                    playerTime[i] = (double) tmp / factor;
                }


                //Si un vol
                if (words[1].equals("steal")) {
                    playerTheftAttempt[i]++;
                    //TODO enregistrer la ressources volée
                    theftResources[Integer.parseInt(words[2])]++;
                    if (Integer.parseInt(words[5]) != -1){
                        playerTheftSuccess[i]++;
                    }
                }

                //Ajout des ressources dans le json
                if (tour)
                    json.add(playerSteps[i]);
                else
                    json.add(playerTime[i]);

                for (int j = 6; j < 6 + resources.size() ; j++)
                    json.add(Integer.parseInt(words[j]));
                jsonMain.add(json);
            }

            jsonObjectMain.put("Joueur" + i, jsonMain);

            //Vol
            jsonMain = new JSONArray();

            json = new JSONArray();
            json.add("Type de vol");
            json.add("Nombre");
            jsonMain.add(json);

            json = new JSONArray();
            json.add("Vols réussis");
            json.add(playerTheftSuccess[i]);
            jsonMain.add(json);

            json = new JSONArray();
            json.add("Vols manqués");
            json.add(playerTheftAttempt[i] - playerTheftSuccess[i]);
            jsonMain.add(json);

            jsonObjectMain.put("Joueur" + i + "Theft", jsonMain);

            reader.close();
        }
    }



    public void parseAllPlayers() throws IOException {

        BufferedReader[] readers = new BufferedReader[nbPlayers];
        for (int i = 0; i < nbPlayers; i++){
            readers[i] = new BufferedReader(new FileReader(logPlayers[i]));
        }

        JSONArray jsonMain = new JSONArray();
        JSONArray json = new JSONArray();

        boolean[] hasLine = new boolean[nbPlayers];
        for (int i = 0; i < nbPlayers; i++){
            hasLine[i] = true;
        }
        boolean noMoreLine = false;

        String line;
        int[] lastSum = new int[nbPlayers];
        int cpt = 0;


        json.add("Time");
        for (int i = 0; i < readers.length; i++){
            line = readers[i].readLine();
            /*if (!line.equals("Joueur " + i)){
                System.err.println("Log ne connrespond pas au bon joueur : " + line + " != " + "Joueur " + i);
            }*/
            json.add("Joueur " + i);
        }
        jsonMain.add(json);


        while (!noMoreLine){
            json = new JSONArray();

            cpt++;
            json.add(cpt);

            boolean tmpNoMoreLine = false;
            for (int i = 0; i < readers.length; i++){
                line = readers[i].readLine();
                if (line == null){
                    hasLine[i] = false;

                    tmpNoMoreLine = true;
                    for (int j = 0; j < hasLine.length; j++){
                        if (hasLine[j]){
                            tmpNoMoreLine = false;
                        }
                    }

                    if (!tmpNoMoreLine){
                        json.add((lastSum[i]));
                    }
                }
                else {
                    String[] words = line.split(" ");
                    int sum = 0;
                    for (int j = 6; j < words.length; j++){
                        sum += Integer.parseInt(words[j]);
                    }
                    lastSum[i] = sum;
                    json.add(sum);
                }
            }
            noMoreLine = tmpNoMoreLine;
            jsonMain.add(json);
        }

        jsonMain.remove(jsonMain.size()-1);
        jsonObjectMain.put("TotalPlayer", jsonMain);

        //vol
        addTheftToJson();

        //Temps
        addTimeToJson();

        //vol ressources
        addResourcesTheft();

        for (int i = 0; i < readers.length; i++){
            readers[i].close();
        }
    }

    private void addResourcesTheft() {
        JSONArray jsonMain = new JSONArray();
        JSONArray json = new JSONArray();
        json.add("Ressources");
        json.add("Nombre de tentatives de vol");
        jsonMain.add(json);

        for (int j = 0; j < nbResources; j++){
            json = new JSONArray();
            json.add(resources.get(j));
            json.add(theftResources[j]);
            jsonMain.add(json);
        }

        jsonObjectMain.put("TheftResources", jsonMain);
    }

    private void addTimeToJson() {
        JSONArray jsonMain = new JSONArray();
        JSONArray json = new JSONArray();
        json.add("Joueur");
        json.add("Temps (sec.)");
        jsonMain.add(json);

        for (int j = 0; j < nbPlayers; j++){
            json = new JSONArray();
            json.add("Joueur " + j + " (" + playerComportement[j] + ")");
            json.add(playerTime[j]);
            jsonMain.add(json);
        }

        jsonObjectMain.put("PlayerTime", jsonMain);
    }

    private void addTheftToJson(){
        //Vol
        for (int i = 0; i < nbPlayers; i++){
            totalPlayerTheftAttempt += playerTheftAttempt[i];
            totalPlayerTheftSuccess += playerTheftSuccess[i];
        }
        JSONArray jsonMain = new JSONArray();

        JSONArray json = new JSONArray();
        json.add("Type de vol");
        json.add("Nombre");
        jsonMain.add(json);

        json = new JSONArray();
        json.add("Vols réussis");
        json.add(totalPlayerTheftSuccess);
        jsonMain.add(json);

        json = new JSONArray();
        json.add("Vols manqués");
        json.add(totalPlayerTheftAttempt - totalPlayerTheftSuccess);
        jsonMain.add(json);

        jsonObjectMain.put("TotalTheft", jsonMain);
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public int getNbProducers() {
        return nbProducers;
    }

    public int getNbResources() {
        return nbResources;
    }

    public int getTotalPlayerTheftAttempt() {
        return totalPlayerTheftAttempt;
    }

    public int getTotalPlayerTheftSuccess() {
        return totalPlayerTheftSuccess;
    }

    public int[] getPlayerTheftAttempt() {
        return playerTheftAttempt;
    }

    public int[] getPlayerTheftSuccess() {
        return playerTheftSuccess;
    }

    public double[] getPlayerTime() {
        return playerTime;
    }

    public Map<Integer, String> getResources() {
        return resources;
    }

    public JSONObject getJsonObjectMain() {
        return jsonObjectMain;
    }

    public String[] getPlayerComportement() {
        return playerComportement;
    }

    public int[] getPlayerSteps() {
        return playerSteps;
    }

    public boolean isTour() {
        System.err.println(tour);
        return tour;
    }

    public double getMaxTime() {
        double max = 0.;
        for (double d : playerTime)
            if (d > max) max = d;

        return max;
    }

    public double getMaxSteps() {
        int max = 0;
        for (int i : playerSteps)
            if (i > max) max = i;

        return max;
    }
}