package projet;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by jpabegg on 18/04/17.
 */
public interface Agent extends Remote {
    void setId(int id) throws IOException;
    String readLog() throws IOException;
    Map<Integer,Integer> observe() throws RemoteException;
    boolean playTurn() throws RemoteException;
}
