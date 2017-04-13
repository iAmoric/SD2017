package projet.coordinateur;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    Random r;

    JSONObject jsonMain;
    JSONArray playerJsonMain;
    JSONArray playerJson;
    JSONArray producerJsonMain;
    JSONArray producerJson;


    public HTMLGenerator (int nbJoueurs, int nbProducteurs) {
        this.nbJoueurs = nbJoueurs;
        this.nbProducteurs = nbProducteurs;
        r = new Random();

        jsonMain = new JSONObject();
        createPlayerJsonArray();
        createProducerJsonArray();
        createHtmlFile();

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
                    }
                } else {
                    if (i == 0) {
                        playerJson.add("");
                    } else {
                        playerJson.add(j*10 + (r.nextInt(20)-10));
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
                        producerJson.add("");
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
                        "\t\t\t\t\t<div id=\"exTab2\" class=\"container\" style=\"padding-left:0; width:95%\">\n" +
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
                                "\t\t\t\t\t\t\t\t<h3>Evolution des ressources totales des joueurs</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"totalPlayerChart\" style=\"height: 300px;\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"player"+i+"\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Evolution des ressources du joueur " + i + "</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"playerChart"+i+"\" style=\"height: 300px;\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
            }

            w.println("<!-- PRODUCTEURS -->");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"producer\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Evolution des ressources totales des producteurs</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"totalProducerChart\" style=\"height: 300px;\"></div>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t</div>\n");
                }
                else {
                    w.println(  "\t\t\t\t\t\t\t<div class=\"tab-pane\" id=\"producer"+i+"\">\n" +
                                "\t\t\t\t\t\t\t\t<h3>Evolution des ressources du producteur " + i + "</h3>\n" +
                                "\t\t\t\t\t\t\t\t<br>\n" +
                                "\t\t\t\t\t\t\t\t<div id=\"producerChart"+i+"\" style=\"height: 300px;\"></div>\n" +
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
                        "\t\tgoogle.charts.setOnLoadCallback(drawTotalProducerChart);\n");

            w.println(  "\t\tfunction drawTotalPlayerChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + playerJsonMain + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\thAxis: {title: '',  titleTextStyle: {color: '#333'}},\n" +
                        "\t\t\t\tvAxis: {minValue: 0},\n" +
                        "\t\t\t\tisStacked : true,\n" +
                        "\t\t\t\tlegend: {position: 'bottom'}" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.AreaChart(document.getElementById('totalPlayerChart'));\n" +
                        "\t\t\tchart.draw(data, options);\n" +
                        "\t\t}\n");

            w.println(  "\t\tfunction drawTotalProducerChart() {\n" +
                        "\t\t\tvar data = google.visualization.arrayToDataTable(" + producerJsonMain + ");\n" +
                        "\t\t\tvar options = {\n" +
                        "\t\t\t\ttitle: '',\n" +
                        "\t\t\t\thAxis: {title: '',  titleTextStyle: {color: '#333'}},\n" +
                        "\t\t\t\tvAxis: {minValue: 0},\n" +
                        "\t\t\t\tisStacked : true,\n" +
                        "\t\t\t\tlegend: {position: 'bottom'}" +
                        "\t\t\t};\n" +
                        "\t\t\tvar chart = new google.visualization.AreaChart(document.getElementById('totalProducerChart'));\n" +
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
