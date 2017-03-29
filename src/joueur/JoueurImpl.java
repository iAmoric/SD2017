package joueur;

import producteur.Producteur;

import java.util.Map;

/**
 * Created by jpabegg on 25/03/17.
 */
public class JoueurImpl implements Joueur {
    private Producteur[] producteurs;//liste des producteurs
    private Map<Integer,Integer> ressources;
    private Map<Integer,Integer> objectifs;
    private boolean working;

    public JoueurImpl(){

    }





    public int getRessources(int idRessource) {
        return 0;
    }


    public boolean observe(Joueur j) {
        return false;
    }
}
