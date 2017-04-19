package projet.coordinateur;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by Lucas on 14/04/2017.
 */
public class LogGeneratorTest {

    public static void main(String[] args){

        Random r = new Random();

        int nbJoueurs = 5;
        int nbResources = 5;

        int[] total = new int[nbResources];
        int[] target = new int[nbResources];

        for (int i = 0; i < target.length; i++){
            target[i] = r.nextInt(150)+200;
        }

        int cpt;

        for (int i = 0; i < nbJoueurs; i++){

            cpt = 0;

            for (int j = 0; j < total.length; j++){
                total[j] = 0;
            }

            try{
                PrintWriter w = new PrintWriter("playerLog_"+i+".txt", "UTF-8");
                w.println("Joueur " + i);

                boolean resMissing = true;
                boolean[] b = new boolean[nbResources];
                for (int j = 0; j < b.length; j++){
                    b[j] = true;
                }

                while(resMissing) {
                    cpt++;
                    int k = r.nextInt(5);
                    int demande, recu;
                    if (total[k] < target[k]){
                        if (target[k] - total[k] < 50){
                            demande = target[k] - total[k];
                            recu = demande;
                        }
                        else {
                            demande = 50;
                            recu = r.nextInt(demande);
                        }
                        total[k] += recu;
                        int random = r.nextInt(100)+1;
                        if (random > 80){
                            random = r.nextInt(10)+1;
                            if (random > 7){
                                w.print(cpt + " steal " + (r.nextInt(3) + 1) + " " + (k + 1) + " " + demande + " " + (-1));
                            }
                            else {
                                w.print(cpt + " steal " + (r.nextInt(3) + 1) + " " + (k + 1) + " " + demande + " " + recu);
                            }
                        }
                        else {
                            w.print(cpt + " get " + (r.nextInt(3) + 1) + " " + (k + 1) + " " + demande + " " + recu);
                        }
                        for (int j = 0; j < nbResources; j++){
                            w.print(" " + total[j]);
                        }
                        w.println();

                        if (total[k] >= target[k])
                            b[k] = false;

                        resMissing = false;
                        for (int j = 0; j < b.length; j++){
                            if (b[j]){
                                resMissing = true;
                            }
                        }

                    }
                    else{
                        cpt--;
                    }
                }

                w.close();
            } catch (IOException e) {
                // do something
            }
        }



    }
}
