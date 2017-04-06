package projet.exceptions;

/**
 * Created by jpabegg on 06/04/17.
 */
public class RMIExecption extends PException {
    public RMIExecption(String s){
        super("Impossible de se connecter à "+s);
    }
}
