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

    JSONArray playerJsonMain;
    JSONArray playerJson;
    JSONArray producerJsonMain;
    JSONArray producerJson;


    public HTMLGenerator (int nbJoueurs, int nbProducteurs) {
        this.nbJoueurs = nbJoueurs;
        this.nbProducteurs = nbProducteurs;
        r = new Random();

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
    }

    public void createHtmlFile() {

        try {
            PrintWriter w = new PrintWriter("projetSD2017.html", "UTF-8");
            w.println(  "<!DOCTYPE html>\n" +
                        "<html lang=\"fr\">\n" +
                        "<head>\n" +
                        "<meta charset=\"UTF-8\">\n" +
                        "<title>Projet SD 2017</title>\n" +
                        "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n");
            w.println(  "<script src=\"https://code.jquery.com/jquery-3.2.1.min.js\" integrity=\"sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=\" crossorigin=\"anonymous\"></script>\n" +
                        "<script src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                        "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>\n" +
                        "</head>\n" +
                        "<body>\n" );

            w.println(  "<div class=\"container\">\n" +
                        "<div class=\"row\">\n" +
                        "<div class=\"page-header\">\n" +
                        "<h1>Projet SD 2017 <small>Résumé de la partie</small></h1>\n" +
                        "</div>\n" +
                        "<div class=\"panel panel-default\">\n" +
                        "<div class=\"panel-body\">\n" +
                        "<div id=\"exTab2\" class=\"container\" style=\"padding-left:0; width:95%\">\n" +
                        "<!-- NAVIGATION -->\n" +
                        "<ul class=\"nav nav-tabs\">\n");

            w.println(  "<!-- JOUEURS -->\n" +
                        "<li role=\"presentation\" class=\"dropdown\">\n" +
                        "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                        " Joueurs <span class=\"caret\"></span>\n" +
                        "</a>\n");

            w.println("<ul class=\"dropdown-menu\">\n");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<li>\n" +
                                "<a href=\"#player\" data-toggle=\"tab\">Total</a>\n" +
                                "</li>\n");
                }
                else {
                    w.println(  "<li>\n" +
                                "<a href=\"#player"+i+"\" data-toggle=\"tab\">Joueur " + i + "</a>\n" +
                                "</li>\n");
                }
            }

            w.println("</ul>\n</li>\n");

            w.println(  "<!-- PRODUCTEURS -->\n" +
                        "<li role=\"presentation\" class=\"dropdown\">\n" +
                        "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">\n" +
                        " Producteurs <span class=\"caret\"></span>\n" +
                        "</a>\n" +
                        "<ul class=\"dropdown-menu\">\n");

            for (int i = 0 ; i < nbProducteurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<li>\n" +
                                "<a href=\"#producer\" data-toggle=\"tab\">Total</a>\n" +
                                "</li>\n");
                }
                else {
                    w.println(  "<li>\n" +
                                "<a href=\"#producer"+i+"\" data-toggle=\"tab\">Producteur " + i + "</a>\n" +
                                "</li>\n");
                }
            }

            w.println("</ul>\n</li>\n</ul>\n");

            w.println("<!-- CONTENT -->\n" +
                    "<div class=\"tab-content\">\n");

            w.println("<!-- JOUEURS -->\n");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<div class=\"tab-pane active\" id=\"player\">\n" +
                                "<h3>Evolution des ressources totales des joueurs</h3>\n" +
                                "<br>\n" +
                                "<div id=\"totalPlayerChart\" style=\"height: 300px;\"></div>\n" +
                                "<br>\n" +
                                "</div>\n");
                }
                else {
                    w.println(  "<div class=\"tab-pane\" id=\"player"+i+"\">\n" +
                                "<h3>Evolution des ressources du joueur " + i + "</h3>\n" +
                                "<br>\n" +
                                "<div id=\"playerChart"+i+"\" style=\"height: 300px;\"></div>\n" +
                                "<br>\n" +
                                "</div>\n");
                }
            }

            w.println("<!-- PRODUCTEURS -->\n");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<div class=\"tab-pane\" id=\"producer\">\n" +
                            "<h3>Evolution des ressources totales des producteurs</h3>\n" +
                            "<br>\n" +
                            "<div id=\"totalProducerChart\" style=\"height: 300px;\"></div>\n" +
                            "<br>\n" +
                            "</div>\n");
                }
                else {
                    w.println(  "<div class=\"tab-pane\" id=\"producer"+i+"\">\n" +
                            "<h3>Evolution des ressources du producteur " + i + "</h3>\n" +
                            "<br>\n" +
                            "<div id=\"producerChart"+i+"\" style=\"height: 300px;\"></div>\n" +
                            "<br>\n" +
                            "</div>\n");
                }
            }

            w.println("</div>\n</div>\n</div>\n</div>\n</div>\n</div>\n");

            w.print("<script type=\"text/javascript\">\n" +
                    "\n" +
                    "      // Load Charts and the corechart package.\n" +
                    "      google.charts.load('current', {'packages':['corechart']});\n" +
                    "\n" +
                    "      // Draw charts when Charts are loaded.\n");
            w.println("google.charts.setOnLoadCallback(drawTotalPlayerChart);\n");
            w.println("google.charts.setOnLoadCallback(drawTotalProducerChart);\n");

            w.println(  "function drawTotalPlayerChart() {\n" +
                        " var data = google.visualization.arrayToDataTable(" + playerJsonMain + ");\n" +
                        " var options = {\n" +
                        "   title: '',\n" +
                        "   hAxis: {title: '',  titleTextStyle: {color: '#333'}},\n" +
                        "   vAxis: {minValue: 0},\n" +
                        "   isStacked : true,\n" +
                        "   legend: {position: 'bottom'}" +
                        "};\n" +
                        "var chart = new google.visualization.AreaChart(document.getElementById('totalPlayerChart'));\n" +
                        "chart.draw(data, options);\n" +
                        "}\n");

            w.println(  "function drawTotalProducerChart() {\n" +
                    " var data = google.visualization.arrayToDataTable(" + producerJsonMain + ");\n" +
                    " var options = {\n" +
                    "   title: '',\n" +
                    "   hAxis: {title: '',  titleTextStyle: {color: '#333'}},\n" +
                    "   vAxis: {minValue: 0},\n" +
                    "   isStacked : true,\n" +
                    "   legend: {position: 'bottom'}" +
                    "};\n" +
                    "var chart = new google.visualization.AreaChart(document.getElementById('totalProducerChart'));\n" +
                    "chart.draw(data, options);\n" +
                    "}\n");

            w.println("</script>");
            w.println(  "</body>" +
                        "</html>");

            w.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }
}
