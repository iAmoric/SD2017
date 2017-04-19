package projet.coordinateur;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    //private Map<Integer, String> players;
    //private Map<Integer, String> producers;
    private Map<String, Integer> resources;

    private File[] logPlayers;
    private File[] logProducers;

    JSONObject jsonObjectMain;

    public JSONConverter(int nbPlayers,
                         int nbProducers,
                         Map<String, Integer> resources,
                         File[] logPlayers,
                         File[] logProducers)
    {
        this.resources = resources;

        this.nbPlayers = nbPlayers;
        this.nbProducers = nbProducers;
        this.nbResources = this.resources.size();

        this.logPlayers = logPlayers;
        this.logProducers = logProducers;

        random = new Random();

        jsonObjectMain = new JSONObject();
    }


    //Constructeur pour les tests
    public JSONConverter()
    {


        //init map resources
        resources = new HashMap<>();
        resources.put("or", 0);
        resources.put("petrol", 1);
        resources.put("fer", 2);
        resources.put("bois", 3);
        resources.put("pierre", 4);

        nbPlayers = 5;
        nbProducers = 3;
        //nbResources = resources.size();

        //init arrays (logs and thefts)
        logPlayers = new File[nbPlayers];
        playerTheftAttempt = new int[nbPlayers];
        playerTheftSuccess = new int[nbPlayers];

        for (int i = 0; i < nbPlayers; i++){
            logPlayers[i] = new File("playerLog_"+i+".txt");
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
            BufferedReader reader = new BufferedReader(new FileReader("playerLog_"+i+".txt"));

            String line;

            int cpt = 0;

            JSONArray jsonMain = new JSONArray();
            JSONArray json = new JSONArray();

            //Ajout des titres temps + ressources
            json.add("Time");
            for (int j = 0; j < resources.size(); j++){
                for (Object o : resources.keySet()) {
                    if (resources.get(o).equals(j)) {
                        json.add(o);
                    }
                }

            }
            jsonMain.add(json);

            line = reader.readLine();
            if (!line.equals("Joueur " + i)){
                System.err.println("Log ne connrespond pas au bon joueur : " + line + " != " + "Joueur " + i);
            }

            while ((line = reader.readLine()) != null) {
                json = new JSONArray();
                String[] words;

                cpt++; //TODO change with timestamp
                words = line.split(" ");

                //Si un vol
                if (words[1].equals("steal")) {
                    playerTheftAttempt[i]++;
                    //TODO enregistrer la ressources volée
                    if (Integer.parseInt(words[5]) != -1){
                        playerTheftSuccess[i]++;
                    }
                }

                //Ajout des ressources dans le json
                json.add(cpt);
                for (int j = 6; j < 6 + resources.size() ; j++){
                    json.add(Integer.parseInt(words[j]));
                }
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
            if (!line.equals("Joueur " + i)){
                System.err.println("Log ne connrespond pas au bon joueur : " + line + " != " + "Joueur " + i);
            }
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

        //Vol
        for (int i = 0; i < nbPlayers; i++){
            totalPlayerTheftAttempt += playerTheftAttempt[i];
            totalPlayerTheftSuccess += playerTheftSuccess[i];
        }
        jsonMain = new JSONArray();

        json = new JSONArray();
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

        for (int i = 0; i < readers.length; i++){
            readers[i].close();
        }
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

    public Map<String, Integer> getResources() {
        return resources;
    }

    public JSONObject getJsonObjectMain() {
        return jsonObjectMain;
    }
}