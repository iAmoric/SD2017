package projet.joueur;

/**
 * Comportement du joueur
 * Created by jpabegg on 16/04/17.
 */
public enum Comportement {
    PASSIF("passif"),
    AGGRESIF("aggresif"),
    JOUEUR("joueur"),
    MALIN("malin");
    private String nom;
    Comportement(String nom){
        this.nom = nom;
    }

    public String getNom(){
        return nom;
    }
}
