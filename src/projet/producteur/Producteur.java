package projet.producteur;

import projet.Agent;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by jpabegg on 25/03/17.
 */
public interface Producteur extends Agent
{
   int getRessource(int id,int n) throws RemoteException;
   int[] whatDoYouProduce() throws RemoteException;
   void setProductions(Map<Integer,Integer> ressourceDispo) throws RemoteException;
   void setRules(boolean isEpuisable,int k,boolean tourParTour) throws RemoteException;
   void startProduction() throws RemoteException;
   void stopProduction() throws RemoteException;


}
