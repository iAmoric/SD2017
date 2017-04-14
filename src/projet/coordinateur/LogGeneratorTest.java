package projet.coordinateur;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * Created by Lucas on 14/04/2017.
 */
public class LogGeneratorTest {

    public static void main(String[] args){

        Random r = new Random();
        int total1 = 0;
        int total2 = 0;
        int total3 = 0;
        int total4 = 0;
        int total5 = 0;
        int target1 = 250;
        int target2 = 200;
        int target3 = 300;
        int target4 = 250;
        int target5 = 200;

        int cpt;

        for (int i = 0; i<5; i++){
            cpt = 0;
            total1 = 0;
            total2 = 0;
            total3 = 0;
            total4 = 0;
            total5 = 0;
            try{
                PrintWriter w = new PrintWriter("log_"+i+".txt", "UTF-8");
                w.println("JOUEUR");
                w.println("Joueur " + i);
                w.println("ACTIONS");

                while (total1 < target1 || total2 < target2 || total3 < target3 || total4 < target4 || total5 < target5) {
                    cpt++;
                    int k = r.nextInt(5);
                    int demande, recu;
                    switch (k){
                        case 0:
                            if (total1 < target1){
                                if (target1-total1 < 50){
                                    demande = target1-total1;
                                    recu = demande;
                                }
                                else {
                                    demande = 50;
                                    recu = r.nextInt(demande);
                                }
                                total1 += recu;
                                w.println(cpt + " get " + (k+1) + " " + demande + " " + recu + " " + 5 +
                                        " " + total1 + " " + total2 + " " + total3 + " " + total4 + " " + total5);
                            }
                            else{
                                cpt--;
                            }
                            break;
                        case 1:
                            if (total2 < target2){
                                if (target2-total2 < 50){
                                    demande = target2-total2;
                                    recu = demande;
                                }
                                else {
                                    demande = 50;
                                    recu = r.nextInt(demande)+1;
                                }
                                total2 += recu;
                                w.println(cpt + " get " + (k+1) + " " + demande + " " + recu + " " + 5 +
                                        " " + total1 + " " + total2 + " " + total3 + " " + total4 + " " + total5);
                            }
                            else{
                                cpt--;
                            }
                            break;
                        case 2:
                            if (total3 < target3){
                                if (target3-total3 < 50){
                                    demande = target3-total3;
                                    recu = demande;
                                }
                                else {
                                    demande = 50;
                                    recu = r.nextInt(demande)+1;
                                }
                                total3 += recu;
                                w.println(cpt + " get " + (k+1) + " " + demande + " " + recu + " " + 5 +
                                        " " + total1 + " " + total2 + " " + total3 + " " + total4 + " " + total5);
                            }
                            else{
                                cpt--;
                            }
                            break;
                        case 3:
                            if (total4 < target4){
                                if (target4-total4 < 50){
                                    demande = target4-total4;
                                    recu = demande;
                                }
                                else {
                                    demande = 50;
                                    recu = r.nextInt(demande)+1;
                                }
                                total4 += recu;
                                w.println(cpt + " get " + (k+1) + " " + demande + " " + recu + " " + 5 +
                                        " " + total1 + " " + total2 + " " + total3 + " " + total4 + " " + total5);
                            }
                            else{
                                cpt--;
                            }
                            break;
                        case 4:
                            if (total5 < target5){
                                if (target5-total5 < 50){
                                    demande = target5-total5;
                                    recu = demande;
                                }
                                else {
                                    demande = 50;
                                    recu = r.nextInt(50)+1;
                                }
                                total5 += recu;
                                w.println(cpt + " get " + (k+1) + " " + demande + " " + recu + " " + 5 +
                                        " " + total1 + " " + total2 + " " + total3 + " " + total4 + " " + total5);
                            }
                            else{
                                cpt--;
                            }
                            break;
                    }
                }

                w.close();
            } catch (IOException e) {
                // do something
            }
        }



    }
}
