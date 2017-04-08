package projet.exceptions;


/**
 * Exception généré si un Joueur essaie de voler un autre joueur et est repéré
 * TODO: L'exception un coordinateur
 * Created by jpabegg on 08/04/17.
 */
public class StealException extends Exception {
    public StealException(){
        super("Le vol a échoué");
    }
}
