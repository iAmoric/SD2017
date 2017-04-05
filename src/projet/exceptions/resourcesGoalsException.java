package projet.exceptions;

/**
 * Created by Lucas on 05/04/2017.
 */
public class resourcesGoalsException extends PException {

    public resourcesGoalsException(int res, int obj){
        super("Nombre de ressources (" + res + ") différents du nombre d'objectifs (" + obj + ")");
    }
}
