package projet.exceptions;

/**
 * Created by Lucas on 05/04/2017.
 */
public class noResourceException extends PException {

    public noResourceException(){
        super("Aucune ressource trouvée");
    }
}
