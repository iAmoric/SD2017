package joueur;

import producteur.Producteur;

import java.util.Map;

/**
 * Created by jpabegg on 25/03/17.
 */
public class JoueurImpl implements Joueur {
    private Producteur[] producteurs;//liste des producteurs
    private Joueur[] joueurs;//liste des joueurs
    private int id;//indice du joueur dans le tableau
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

    public void setId(int id) {
        this.id = id;
    }

    public void setJoueurs(Joueur[] joueurs) {
        this.joueurs = joueurs;
    }

    public void setProducteurs(Producteur[] producteurs) {
        this.producteurs = producteurs;
    }

    public void setRessources(Map<Integer, Integer> ressources) {
        this.ressources = ressources;
    }

    public void setObjectifs(Map<Integer, Integer> objectifs) {
        this.objectifs = objectifs;
    }
}
