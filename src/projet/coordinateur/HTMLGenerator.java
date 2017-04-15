package projet.coordinateur;

import java.io.*;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by lucas on 11/04/17.
 */
public class HTMLGenerator {

    int nbJoueurs;
    int nbProducteurs;
    int nbResources = 5;
    int target = 55;
    Random r;

    JSONObject jsonObjectMain;
    JSONArray jsonArrayMain;



    JSONArray playerJsonMain;
    JSONArray playerJson;
    JSONArray producerJsonMain;
    JSONArray producerJson;

    String[] playerName;
    String[] producerName;


    public HTMLGenerator (int nbJoueurs, int nbProducteurs) {
        this.nbJoueurs = nbJoueurs;
        this.nbProducteurs = nbProducteurs;

        r = new Random();

        jsonObjectMain = new JSONObject();

        try {
            parseTotalLogFiles();
            parsePlayerLogFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*createPlayerJsonArray();
        createProducerJsonArray();*/
        createHtmlFile();



    }

    /**
     * Nécessaire de connaitre le nombre de ressources ainsi que leur nombre
     *  + Nombre de joueurs
     *  + Nombre de producteurs (si on fait un fichier de log pour les producteurs)
     */
    public void parsePlayerLogFiles() throws IOException {

        String ressources[] =  {"pétrole","or", "gaz naturel", "bois", "pierre"};
        for (int i = nbJoueurs-1; i >= 0; i--){
            File file = new File("log_"+i+".txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String playerName = "";

            JSONArray jsonPlayerMain = new JSONArray();
            JSONArray jsonPlayer = new JSONArray();

            line = reader.readLine();
            if (line.equals("JOUEUR")){
                playerName = reader.readLine();
            }

            line = reader.readLine();
            if (line.equals("ACTIONS")){

                jsonPlayer.add("Time");
                for (int j = 0; j < ressources.length; j++){
                    jsonPlayer.add(ressources[j]);
                }
                jsonPlayerMain.add(jsonPlayer);

                while ((line = reader.readLine()) != null){
                    String[] words;
                    words = line.split(" ");
                    if (words[1].equals("get")){
                        jsonPlayer = new JSONArray();

                        jsonPlayer.add(Integer.parseInt(words[0]));
                        for (int j = 6; j < 6 + ressources.length ; j++){
                            jsonPlayer.add(Integer.parseInt(words[j]));
                        }
                        jsonPlayerMain.add(jsonPlayer);
                    }
                }
            }

            jsonObjectMain.put(playerName, jsonPlayerMain);

            reader.close();
        }
    }

    public void parseTotalLogFiles() throws IOException {

        BufferedReader[] readers = new BufferedReader[nbJoueurs];
        String line;
        JSONArray jsonMain = new JSONArray();
        JSONArray json = new JSONArray();

        for (int i = 0; i < nbJoueurs; i++){
            File file = new File("log_"+i+".txt");
            readers[i] = new BufferedReader(new FileReader(file));
        }

        json.add("Time");
        for (int i = 0; i < readers.length; i++){
            line = readers[i].readLine();
            if (line.equals("JOUEUR")){
                line = readers[i].readLine();
                int n = Integer.parseInt(line.substring(line.length() - 1));
                String playerName = "Joueur " + (n+1);
                json.add(playerName);
            }
        }
        jsonMain.add(json);


        for(BufferedReader reader: readers){
            reader.readLine(); // ACTIONS
        }

        boolean noMoreLine = false;

        boolean[] hasLineArray= new boolean[nbJoueurs];
        for (int i = 0; i < hasLineArray.length; i++){
            hasLineArray[i] = true;
        }

        int[] lastSum = new int[nbJoueurs];

        int cpt = 0;
        while (!noMoreLine){
            cpt++;
            json = new JSONArray();
            json.add(cpt);
            boolean tmpNoMoreLine = false;
            for (int i = 0; i < readers.length; i++){
                line = readers[i].readLine();
                if (line == null){
                    hasLineArray[i] = false;

                    boolean continu = false;
                    for (int j = 0; j < hasLineArray.length; j++){
                        if (hasLineArray[j]){
                            continu = true;
                        }
                    }

                    if (continu)
                        tmpNoMoreLine = false;
                    else
                        tmpNoMoreLine = true;

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
                    //System.out.println("Sum " + i + " : " + lastSum[i]);
                    json.add(sum);
                }
            }
            noMoreLine = tmpNoMoreLine;
            jsonMain.add(json);
        }

        jsonMain.remove(jsonMain.size()-1);
        jsonObjectMain.put("TotalPlayer", jsonMain);

        for (int i = 0; i < readers.length; i++){
            readers[i].close();
        }
    }

    /*public void createPlayerJsonArray() {
        playerJsonMain = new JSONArray();
        for (int j = 0; j < nbResources ; j++) {
            playerJson = new JSONArray();
            for (int i = 0; i < nbJoueurs + 1; i++) {
                if (j == 0) {
                    if (i == 0) {
                        playerJson.add("Time");
                    } else {
                        playerJson.add("Joueur " + i);
                        if (i == nbJoueurs) {
                            playerJson.add("Objectif");
                        }
                    }
                } else {
                    if (i == 0) {
                        playerJson.add(j-1);
                    } else {
                        playerJson.add(j*10 + (r.nextInt(20)-10));
                        if (i == nbJoueurs) {
                            playerJson.add(target);
                        }
                    }
                }
            }

            playerJsonMain.add(playerJson);
            jsonObjectMain.put("totalPlayer", playerJsonMain);
        }
    }*/

    /*public void createProducerJsonArray() {
        producerJsonMain = new JSONArray();
        for (int j = 0; j < nbResources ; j++) {
            producerJson = new JSONArray();
            for (int i = 0; i < nbJoueurs + 1; i++) {
                if (j == 0) {
                    if (i == 0) {
                        producerJson.add("Time");
                    } else {
                        producerJson.add("Producteur " + i);
                    }
                } else {
                    if (i == 0) {
                        producerJson.add(j-1);
                    } else {
                        producerJson.add(j*10 + (r.nextInt(20)-10));
                    }
                }
            }
            producerJsonMain.add(producerJson);
        }
        jsonObjectMain.put("totalProducer", producerJsonMain);
    }*/

    public void createHtmlFile() {

        try {
            PrintWriter w = new PrintWriter("projetSD2017.html", "UTF-8");

            w.println(  "<!DOCTYPE html>\n" +
                        "<html lang=\"fr\">\n" +
                        "<head>\n" +
                        "\t<meta charset=\"UTF-8\">\n" +
                        "\t<title>Projet SD 2017</title>\n" +
                        "\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                        "\t<script src=\"https://code.jquery.com/jquery-3.2.1.min.js\" integrity=\"sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=\" crossorigin=\"anonymous\"></script>\n" +
                        "\t<script src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                        "\t<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>\n" +
                        "</head>\n");

            w.println(  "<body>\n" +
                        "\t<div class=\"container\">\n" +
                        "\t\t<div class=\"row\">\n" +
                        "\t\t\t<div class=\"page-header\">\n" +
                        "\t\t\t\t<h1>Projet SD 2017 <small>Résumé de la partie</small></h1>\n" +
                        "\t\t\t</div>\n" +
                        "\t\t\t<div class=\"panel panel-default\">\n" +
                        "\t\t\t\t<div class=\"panel-body\">\n" +
                        "\t\t\t\t\t<div id=\"exTab2\" class=\"container\">\n" +
                        "<!-- NAVIGATION -->\n" +
                        "\t\t\t\t\t\t<ul class=\"nav nav-tabs\">\n" +
                        "<!-- JOUEURS -->\n" +
                        "\t\t\t\t\t\t\t<li role=\"presentation\" class=\"dropdown\">\n" +
                        "\t\t\t\t\t\t\t\t<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                        "\t\t\t\t\t\t\t\t\tJoueurs <span class=\"caret\"></span>\n" +
                        "\t\t\t\t\t\t\t\t</a>\n" +
                        "\t\t\t\t\t\t\t\t<ul class=\"dropdown-menu\">\n");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                                "\t\t\t\t\t\t\t\t\t\t<a href=\"#player\" data-toggle=\"tab\">Total</a>\n" +
                                "\t\t\t\t\t\t\t\t\t</li>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                                "\t\t\t\t\t\t\t\t\t\t<a href=\"#player"+i+"\" data-toggle=\"tab\">Joueur " + i + "</a>\n" +
                                "\t\t\t\t\t\t\t\t\t</li>\n");
                }
            }

            w.println(  "\t\t\t\t\t\t\t\t</ul>\n" +
                        "\t\t\t\t\t\t\t</li>\n");

            w.println(  "<!-- PRODUCTEURS -->\n" +
                        "\t\t\t\t\t\t\t<li role=\"presentation\" class=\"dropdown\">\n" +
                        "\t\t\t\t\t\t\t\t<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                        "\t\t\t\t\t\t\t\t\tProducteurs <span class=\"caret\"></span>\n" +
                        "\t\t\t\t\t\t\t\t</a>\n" +
                        "\t\t\t\t\t\t\t\t<ul class=\"dropdown-menu\">\n");

            for (int i = 0 ; i < nbProducteurs+1 ; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                                "\t\t\t\t\t\t\t\t\t\t<a href=\"#producer\" data-toggle=\"tab\">Total</a>\n" +
                                "\t\t\t\t\t\t\t\t\t</li>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                                "\t\t\t\t\t\t\t\t\t\t<a href=\"#producer"+i+"\" data-toggle=\"tab\">Producteur " + i + "</a>\n" +
                                "\t\t\t\t\t\t\t\t\t</li>\n");
                }
            }

            w.println(  "\t\t\t\t\t\t\t\t</ul>\n" +
                        "\t\t\t\t\t\t\t</li>\n" +
                        "\t\t\t\t\t\t</ul>\n");

            w.println("<!-- CONTENT -->\n" +
                    "\t\t\t\t\t\t<div class=\"tab-content\">\n");

            w.println("<!-- JOUEURS -->");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane active\" id=\"player\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Évolution des ressources totales des joueurs</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"totalPlayerChart\" style=\"height:450px;width:1100px\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"player"+i+"\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Evolution des ressources du joueur " + i + "</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"playerChart"+i+"\" style=\"height:450px;width:1100px\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
            }

            w.println("<!-- PRODUCTEURS -->");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"producer\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Évolution des ressources totales des producteurs</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"totalProducerChart\" style=\"height:450px;width:1100px\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"producer"+i+"\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Evolution des ressources du producteur " + i + "</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"producerChart"+i+"\" style=\"height: 450px;width:1100px\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
            }

            w.println(  "\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t</div>\n" +
                        "\t\t\t\t</div>\n" +
                        "\t\t\t</div>\n" +
                        "\t\t</div>\n" +
                        "\t</div>\n");

            w.println(  "\t<script type=\"text/javascript\">\n" +
                        "\t\tgoogle.charts.load('current', {'packages':['corechart']});\n" +
                        "\t\tgoogle.charts.setOnLoadCallback(drawTotalPlayerChart);\n" /*+*/
                        /*"\t\tgoogle.charts.setOnLoadCallback(drawTotalProducerChart);\n"*/);

            for (int i = 0; i < nbJoueurs; i++){
                w.println("\t\tgoogle.charts.setOnLoadCallback(drawPlayer"+i+"Chart);");
            }

            w.println(  "\t\tfunction drawTotalPlayerChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObjectMain.get("TotalPlayer") + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        //"\t\t\t\tcurveType: 'function',\n" +
                        "\t\t\t\tanimation:{ duration: 750, easing: 'out', startup: true},\n" +
                        "\t\t\t\thAxis: {title: 'temps',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n" +
                        "\t\t\t\tvAxis: {minValue: 0, title: 'nombre de ressource'},\n" +
                        "\t\t\t\tseries:{"+nbJoueurs+": {lineWidth:4, color: '#e2431e'}}\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('totalPlayerChart'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");

            for (int i = 0; i < nbJoueurs; i++){
                w.println(  "\t\tfunction drawPlayer"+i+"Chart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObjectMain.get("Joueur "+i) + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        "\t\t\t\tanimation:{ duration: 750, easing: 'out', startup: true},\n" +
                        "\t\t\t\thAxis: {title: 'temps',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n" +
                        "\t\t\t\tvAxis: {minValue: 0, title: 'nombre de ressource'},\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('playerChart"+(i+1)+"'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");
            }

            /*w.println(  "\t\tfunction drawTotalProducerChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObjectMain.get("totalProducer") + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        "\t\t\t\tcurveType: 'function',\n" +
                        "\t\t\t\tanimation:{ duration: 750, easing: 'out', startup: true},\n" +
                        "\t\t\t\thAxis: {title: '',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n" +
                        "\t\t\t\tvAxis: {minValue: 0},\n" +
                        "\t\t\t\tseries:{"+nbJoueurs+": {lineWidth:4, color: '#e2431e'}}\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('totalProducerChart'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");*/

            w.println(  "\t</script>");
            w.println(  "</body>\n" +
                        "</html>");

            w.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }
}
