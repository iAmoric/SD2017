package projet.coordinateur;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by lucas on 11/04/17.
 */
public class HTMLGenerator {


    JSONConverter jc;
    JSONObject jsonObject;

    int nbPlayers;
    int nbProducers;
    int nbResources;
    boolean tour;

    int totalTheftAttempt;
    int[] playerTheftAttempt;
    int[] playerTheftSuccess;
    int[] playerSteps;
    double[] playerTime;
    String[] playerComportement;

    public HTMLGenerator (JSONConverter jsonConverter) {
        jc = jsonConverter;

        jsonObject = jc.getJsonObjectMain();
        nbPlayers = jc.getNbPlayers();
        nbProducers = jc.getNbProducers();
        totalTheftAttempt = jc.getTotalPlayerTheftAttempt();
        playerTheftAttempt = jc.getPlayerTheftAttempt();
        playerTheftSuccess = jc.getPlayerTheftSuccess();
        playerTime = jc.getPlayerTime();
        playerComportement = jc.getPlayerComportement();
        playerSteps = jc.getPlayerSteps();
        tour = jc.isTour();

        createHtmlFile();
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

            for (int i = 0 ; i < nbPlayers + 1; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                            "\t\t\t\t\t\t\t\t\t\t<a href=\"#player\" data-toggle=\"tab\">Total</a>\n" +
                            "\t\t\t\t\t\t\t\t\t</li>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                            "\t\t\t\t\t\t\t\t\t\t<a href=\"#player"+i+"\" data-toggle=\"tab\"> Joueur " + i + "</a>\n" +
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

            for (int i = 0 ; i < nbProducers + 1 ; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                            "\t\t\t\t\t\t\t\t\t\t<a href=\"#producer\" data-toggle=\"tab\">Total</a>\n" +
                            "\t\t\t\t\t\t\t\t\t</li>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t\t\t<li>\n" +
                            "\t\t\t\t\t\t\t\t\t\t<a href=\"#producer"+i+"\" data-toggle=\"tab\"> Producteur" + i + "</a>\n" +
                            "\t\t\t\t\t\t\t\t\t</li>\n");
                }
            }

            w.println(  "\t\t\t\t\t\t\t\t</ul>\n" +
                    "\t\t\t\t\t\t\t</li>\n" +
                    "\t\t\t\t\t\t</ul>\n");

            w.println("<!-- CONTENT -->\n" +
                    "\t\t\t\t\t\t<div class=\"tab-content\">\n");

            w.println("<!-- JOUEURS -->");

            for (int i = 0 ; i < nbPlayers + 1 ; i++){
                if (i == 0) {  // Total
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane active\" id=\"player\">\n" +
                            "\t\t\t\t\t\t\t\t<h3>Évolution des ressources totales des joueurs</h3>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n");
                    if (tour){
                        w.print("\t\t\t\t\t\t\t\t<h4>Mode tour par tour. Nombre de tours : " + jc.getMaxSteps() + ".</h4>\n");
                    }
                    else {
                        w.print("\t\t\t\t\t\t\t\t<h4>Durée totale de la partie : " + jc.getMaxTime() + " secondes.</h4>\n");
                    }

                    w.println(  "\t\t\t\t\t\t\t\t<div id=\"totalPlayerChart\" style=\"height:450px;width:1100px\"></div>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<hr>\n");
                    if (!tour){
                        w.println("\t\t\t\t\t\t\t\t<h3>Temps des parties</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"playerTimeChart\" style=\"height:450px;width:1100px\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<hr>\n");
                    }

                    w.println("\t\t\t\t\t\t\t\t<h3>Statistiques sur les vols</h3>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<h4>Il y a eu au total " + totalTheftAttempt + " tentatives de vols, dont :</h4>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<div id=\"totalTheftChart\" style=\"height:450px;width:1100px\"></div>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<h4>Nombre de vols par resources</h4>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<div id=\"theftsPerResource\" style=\"height:450px;width:1100px\"></div>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t</div>\n");
                }
                else { //chaque joueur
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"player"+i+"\">\n" +
                            "\t\t\t\t\t\t\t\t<h3>Evolution des ressources du joueur " + i + "</h3>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n");
                    if (tour){
                        w.print("\t\t\t\t\t\t\t\t<h4>Mode tour par tour. Nombre de tour : " + playerSteps[i-1] + ".</h4>\n");
                    }
                    else {
                        w.print("\t\t\t\t\t\t\t\t<h4>Durée de la partie : " + playerTime[i-1] + " secondes.</h4>\n");
                    }
                    w.println( "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<h4>Comportement du joueur : " + playerComportement[i-1] + "</h4>\n" +
                            "\t\t\t\t\t\t\t\t<div id=\"playerChart"+i+"\" style=\"height:450px;width:1100px\"></div>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<hr>\n" +
                            "\t\t\t\t\t\t\t\t<h3>Statistiques sur les vols</h3>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<h4>Le joueur " + i + " a tenté " + playerTheftAttempt[(i-1)] + " vols, dont :</h4>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t\t<div id=\"player"+i+"TheftChart\" style=\"height:450px;width:1100px\"></div>\n" +
                            "\t\t\t\t\t\t\t\t<br>\n" +
                            "\t\t\t\t\t\t\t</div>\n");
                }
            }

            w.println("<!-- PRODUCTEURS -->");

            for (int i = 0 ; i <nbProducers+1 ; i++){
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
                            "\t\tgoogle.charts.setOnLoadCallback(drawTotalPlayerChart);\n" +
                            "\t\tgoogle.charts.setOnLoadCallback(drawTotalTheftChart);\n" +
                            "\t\tgoogle.charts.setOnLoadCallback(drawTheftsResourceChart);\n"
                        /*"\t\tgoogle.charts.setOnLoadCallback(drawTotalProducerChart);\n"*/);
            if (!tour){
                w.println("\t\tgoogle.charts.setOnLoadCallback(drawPlayerTimeChart);\n");
            }
            for (int i = 0; i < nbPlayers; i++){
                w.println("\t\tgoogle.charts.setOnLoadCallback(drawPlayer"+i+"Chart);");
                w.println("\t\tgoogle.charts.setOnLoadCallback(drawPlayerTheft"+i+"Chart);");
            }

            for (int i = 0; i < nbProducers; i++){
                w.println("\t\tgoogle.charts.setOnLoadCallback(drawProducer"+i+"Chart);");
            }

            w.println(  "\t\tfunction drawTotalPlayerChart() {\n" +
                    "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("TotalPlayer") + ");\n" +
                    "\t\t\tvar options = {\n" +
                    "\t\t\t\ttitle: '',\n" +
                    "\t\t\t\theight:450,\n" +
                    "\t\t\t\twidth:1100,\n" +
                    "\t\t\t\tcurveType: 'function',\n" +
                    "\t\t\t\thAxis: {title: 'tours',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n" +
                    "\t\t\t\tvAxis: {minValue: 0, title: 'nombre de ressources'},\n" +
                    "\t\t\t};\n" +
                    "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('totalPlayerChart'));\n" +
                    "\t\t\tchart.draw(data, options);\n" +
                    "\t\t}\n");

            w.println(  "\t\tfunction drawTotalTheftChart() {\n" +
                    "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("TotalTheft") + ");\n" +
                    "\t\t\tvar options = {\n" +
                    "\t\t\t\ttitle: '',\n" +
                    "\t\t\t\theight:450,\n" +
                    "\t\t\t\twidth:1100,\n" +
                    "\t\t\t};\n" +
                    "\t\t\tvar chart = new google.visualization.PieChart(document.getElementById('totalTheftChart'));\n" +
                    "\t\t\tchart.draw(data, options);\n" +
                    "\t\t}\n");

            if (!tour){
                w.println(  "\t\tfunction drawPlayerTimeChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("PlayerTime") + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        "\t\t\t\tvAxis: {minValue: 0},\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.ColumnChart(document.getElementById('playerTimeChart'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");
            }

            w.println(  "\t\tfunction drawTheftsResourceChart() {\n" +
                    "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("TheftResources") + ");\n" +
                    "\t\t\tvar options = {\n" +
                    "\t\t\t\ttitle: '',\n" +
                    "\t\t\t\theight:450,\n" +
                    "\t\t\t\twidth:1100,\n" +
                    "\t\t\t\tvAxis: {minValue: 0},\n" +
                    "\t\t\t};\n" +
                    "\t\t\tvar chart = new google.visualization.ColumnChart(document.getElementById('theftsPerResource'));\n" +
                    "\t\t\tchart.draw(data, options);\n" +
                    "\t\t}\n");

            for (int i = 0; i < nbPlayers; i++){
                w.println(  "\t\tfunction drawPlayer"+i+"Chart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("Joueur"+i) + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        "\t\t\t\tcurveType: 'function',\n");
                if (tour)
                    w.println("\t\t\t\thAxis: {title: 'tours',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n");
                else
                    w.println("\t\t\t\thAxis: {title: 'temps (sec.)',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n");

                w.println("\t\t\t\tvAxis: {minValue: 0, title: 'nombre de ressource'},\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('playerChart"+(i+1)+"'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");

                w.println(  "\t\tfunction drawPlayerTheft"+i+"Chart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("Joueur"+i+"Theft") + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.PieChart(document.getElementById('player"+(i+1)+"TheftChart'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");
            }

            for (int i = 0; i < nbProducers; i++){
                w.println(  "\t\tfunction drawProducer"+i+"Chart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + jsonObject.get("Producer"+i) + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\theight:450,\n" +
                        "\t\t\t\twidth:1100,\n" +
                        "\t\t\t\tcurveType: 'function',\n");
                if (tour)
                    w.println("\t\t\t\thAxis: {title: 'tours',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n");
                else
                    w.println("\t\t\t\thAxis: {title: 'temps (sec.)',  titleTextStyle: {color: '#333'}, minValue: 1, gridlines: {color: 'transparent'}},\n");

                w.println("\t\t\t\tvAxis: {minValue: 0, title: 'nombre de ressource'},\n" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.LineChart(document.getElementById('producerChart"+(i+1)+"'));\n" +
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
