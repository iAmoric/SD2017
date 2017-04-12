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
    Random r;

    JSONArray jsonMain;
    JSONArray json;


    public HTMLGenerator (int nbJoueurs, int nbProducteurs) {
        this.nbJoueurs = nbJoueurs;
        this.nbProducteurs = nbProducteurs;
        r = new Random();

        createJsonArray();
        createHtmlFile();
    }

    public void createJsonArray() {
        jsonMain = new JSONArray();
        for (int j = 0; j < 5 ; j++) {
            json = new JSONArray();
            for (int i = 0; i < nbJoueurs + 1; i++) {
                if (j == 0) {
                    if (i == 0) {
                        json.add("Time");
                    } else {
                        json.add("joueur" + i);
                    }
                } else {
                    if (i == 0) {
                        json.add("");
                    } else {
                        json.add(j*10 + (r.nextInt(20)-10));
                    }
                }
            }
            jsonMain.add(json);
        }
    }

    public void createHtmlFile() {

        try {
            PrintWriter w = new PrintWriter("projetSD2017.html", "UTF-8");
            w.println(  "<!DOCTYPE html>" +
                        "<html lang=\"fr\">" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <title>Projet SD 2017</title>" +
                        "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">");
            w.println(  "<script src=\"https://code.jquery.com/jquery-3.2.1.min.js\" integrity=\"sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=\" crossorigin=\"anonymous\"></script>" +
                        "<script src=\"https://www.gstatic.com/charts/loader.js\"></script>" +
                        "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>" +
                        "</head>" +
                        "<body>" );

            w.println(  "<div class=\"container\">" +
                        "   <div class=\"row\">" +
                        "       <div class=\"page-header\">" +
                        "           <h1>Projet SD 2017 <small>Résumé de la partie</small></h1>" +
                        "        </div>" +
                        "        <div class=\"panel panel-default\">" +
                        "            <div class=\"panel-body\">" +
                        "                <div id=\"exTab2\" class=\"container\" style=\"padding-left:0; width:95%\">" +
                        "                    <!-- NAVIGATION -->\n" +
                        "                    <ul class=\"nav nav-tabs\">\n");

            w.println(  "<!-- JOUEURS -->\n" +
                        "<li role=\"presentation\" class=\"dropdown\">" +
                        "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">" +
                        " Joueurs <span class=\"caret\"></span>" +
                        "</a>");

            w.println("<ul class=\"dropdown-menu\">");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<li>" +
                                "<a href=\"#player\" data-toggle=\"tab\">Total</a>" +
                                "</li>");
                }
                else {
                    w.println(  "<li>" +
                                "<a href=\"#player"+i+"\" data-toggle=\"tab\">Joueur " + i + "</a>" +
                                "</li>");
                }
            }

            w.println("</ul></li>");

            w.println(  "<!-- PRODUCTEURS -->\n" +
                        "<li role=\"presentation\" class=\"dropdown\">" +
                        "<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">" +
                        " Producteurs <span class=\"caret\"></span>" +
                        "</a>" +
                        "<ul class=\"dropdown-menu\">");

            for (int i = 0 ; i < nbProducteurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<li>" +
                                "<a href=\"#producteur\" data-toggle=\"tab\">Total</a>" +
                                "</li>");
                }
                else {
                    w.println(  "<li>" +
                                "<a href=\"#producteur"+i+"\" data-toggle=\"tab\">Producteur " + i + "</a>" +
                                "</li>");
                }
            }

            w.println("</ul></li></ul>\n");

            w.println("<!-- CONTENT -->\n" +
                    "<div class=\"tab-content\">\n");

            w.println("<!-- JOUEURS -->\n");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<div class=\"tab-pane active\" id=\"player\">" +
                                "<h3>Evolution des ressources totales des joueurs</h3>" +
                                "<br>" +
                                "<div id=\"totalPlayerChart\" style=\"height: 300px;\">Graphique bliblablou</div>" +
                                "<br>" +
                                "</div>");
                }
                else {
                    w.println(  "<div class=\"tab-pane\" id=\"player"+i+"\">" +
                                "<h3>Evolution des ressources du joueur " + i + "</h3>" +
                                "<br>" +
                                "<div id=\"playerChart"+i+"\" style=\"height: 300px;\"></div>" +
                                "<br>" +
                                "</div>");
                }
            }

            w.println("<!-- PRODUCTEURS -->\n");

            for (int i = 0 ; i < nbJoueurs+1 ; i++){
                if (i == 0) {
                    w.println(  "<div class=\"tab-pane\" id=\"producter\">" +
                            "<h3>Evolution des ressources totales des producteurs</h3>" +
                            "<br>" +
                            "<div id=\"totalProducterChart\" style=\"height: 300px;\">Graphique bliblablou</div>" +
                            "<br>" +
                            "</div>");
                }
                else {
                    w.println(  "<div class=\"tab-pane\" id=\"producter"+i+"\">" +
                            "<h3>Evolution des ressources du producteur " + i + "</h3>" +
                            "<br>" +
                            "<div id=\"producterChart"+i+"\" style=\"height: 300px;\"></div>" +
                            "<br>" +
                            "</div>");
                }
            }

            w.println(  "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>");


            w.print("<script type=\"text/javascript\">\n" +
                    "\n" +
                    "      // Load Charts and the corechart package.\n" +
                    "      google.charts.load('current', {'packages':['corechart']});\n" +
                    "\n" +
                    "      // Draw charts when Charts are loaded.\n" +
                    "      google.charts.setOnLoadCallback(drawTotalPlayerChart);\n" +
                    "\n" +
                    "      function drawTotalPlayerChart() {\n" +
                    "\n" +
                    "        var data = google.visualization.arrayToDataTable(" + jsonMain + ");");

            w.println(  "   var options = {\n" +
                        "   title: '',\n" +
                        "   hAxis: {title: '',  titleTextStyle: {color: '#333'}},\n" +
                        "   vAxis: {minValue: 0},\n" +
                        " isStacked : true,\n" +
                        "   legend: {position: 'bottom'}" +
                        "   };\n" +
                    "\n" +
                    "        var chart = new google.visualization.AreaChart(document.getElementById('totalPlayerChart'));\n" +
                    "        chart.draw(data, options);\n" +
                    "      }\n" +
                    "\n" +
                    "    </script>");
            w.println(  "</body>" +
                        "</html>");

            w.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }
}
