package projet.coordinateur;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by lucas on 11/04/17.
 */
public class HTMLGenerator {

    int nbJoueurs;
    int nbProducteurs;
    int nbResources = 7;
    int target = 55;
    Random r;

    JSONObject jsonMain;
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


        jsonMain = new JSONObject();
        createPlayerJsonArray();
        //createProducerJsonArray();
        playerName = new String[nbJoueurs];
        File[] logs = new File[5];
        for (int i = 0; i < 5; i++){
            logs[i] = new File("log_"+i+".txt");
        }

        try {
            parseLogFiles(logs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        createHtmlFile();



    }

    public void parseLogFiles(File[] logs) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(logs[0]));
        String line, last = "";

        //Recupérer le nombre d'étapes
        while ((line = reader.readLine()) != null) {
            last = line;
        }
        int nbSteps = Integer.parseInt(last.split(" ")[0]);
        reader.close();

        //read file
        reader = new BufferedReader(new FileReader(logs[0]));

        line = reader.readLine();

        if (line.equals("JOUEUR")){
            playerName[0] = reader.readLine();
        }

        JSONArray[] jsonArrays = new JSONArray[nbSteps+1];
        for (int i = 0; i <= nbSteps; i++){
            jsonArrays[i] = new JSONArray();
        }

        jsonArrays[0].add("Time");
        for (int i = 1; i <= 5; i++) { //nbRessources
            jsonArrays[0].add("Ressource " + i);
        }

        line = reader.readLine();
        String[] words;
        JSONArray jsonArrayMain = new JSONArray();
        if (line.equals("ACTIONS")){
            while ((line = reader.readLine()) != null){
                words = line.split(" ");
                System.out.println(line);
                switch (words[1]){
                    case "get":
                        //System.out.println(words.length);
                        jsonArrays[Integer.parseInt(words[0])].add(Integer.parseInt(words[0]));
                        for (int i = 6; i < words.length; i++){
                            jsonArrays[Integer.parseInt(words[0])].add(Integer.parseInt(words[i]));
                        }
                        break;
                    default:
                        break;
                }
            }
            for (int i = 0; i < jsonArrays.length; i++){
                jsonArrayMain.add(jsonArrays[i]);
            }
        }
        jsonMain.put(playerName[0], jsonArrayMain);
    }


    public void createPlayerJsonArray() {
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
            jsonMain.put("totalPlayer", playerJsonMain);
        }
    }

    public void createProducerJsonArray() {
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
        jsonMain.put("totalProducer", producerJsonMain);
    }

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

            /*w.println(  "\t<script tupe=\"text/javascript\">\n" +
                        "\t\tfunction removeHidden() {\n" +
                        "\t\t\t$(\"#producer\").css({'visibility':'visible'});\n\t\t}\n" +
                        "\t</script>\n");*/

            w.println(  "\t<script type=\"text/javascript\">\n" +
                        "\t\tgoogle.charts.load('current', {'packages':['corechart']});\n" +
                        "\t\tgoogle.charts.setOnLoadCallback(drawTotalPlayerChart);\n" +
                        "\t\tgoogle.charts.setOnLoadCallback(drawTotalProducerChart);\n");

            w.println(  "\t\tfunction drawTotalPlayerChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonMain.get(playerName[0]) + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        //"\t\t\t\tcurveType: 'function',\n" +
                        "\t\t\t\tanimation:{ duration: 750, easing: 'out', startup: true},\n" +
                        "\t\t\t\thAxis: {title: '',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n" +
                        "\t\t\t\tvAxis: {minValue: 0},\n" +
                        "\t\t\t\tseries:{"+nbJoueurs+": {lineWidth:4, color: '#e2431e'}}\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('totalPlayerChart'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");

            w.println(  "\t\tfunction drawTotalProducerChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonMain.get("totalProducer") + ");\n" +
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
                        "\t\t}\n");

            w.println(  "\t</script>");
            w.println(  "</body>\n" +
                        "</html>");

            w.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }
}
