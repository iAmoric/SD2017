package producteur;

import java.rmi.Remote;

/**
 * Created by jpabegg on 25/03/17.
 */
public interface Producteur extends Remote
{
   int getRessource(int id,int n);
}
