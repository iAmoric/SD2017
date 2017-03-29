package joueur;

import java.rmi.Remote;

/**
 * Created by jpabegg on 25/03/17.
 */
public interface  Joueur extends Remote{
    int getRessources(int idRessource);
    boolean observe(Joueur j);

}
