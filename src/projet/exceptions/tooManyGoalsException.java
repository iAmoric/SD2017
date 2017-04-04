package projet.exceptions;

/**
 * Created by lucas on 04/04/17.
 */
public class tooManyGoalsException extends Exception {

    public tooManyGoalsException(){
        super("Trop d'objectifs pour le nombre de ressource");
    }

}
