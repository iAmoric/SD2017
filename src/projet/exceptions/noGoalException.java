package projet.exceptions;

/**
 * Created by lucas on 04/04/17.
 */
public class noGoalException extends PException {

    public noGoalException(){
        super("Aucun objectif trouvé !");
    }

}
