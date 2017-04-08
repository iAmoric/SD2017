package projet.producteur;

/**
 * Thread chargé de la production de ressource
 * Created by jpabegg on 25/03/17.
 */
public class ThreadRessource extends Thread {

    private int k;
    private boolean working = true;
    private ProducteurImpl producteur;


    public ThreadRessource(ProducteurImpl p, int k) {
        this.k = k;
        producteur = p;

    }

    @Override
    public void run() {
        super.run();
        while (working){
            try {
                Thread.sleep(k);
                producteur.addRessource();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Permet d'arreter le thread une fois qu'il est lancé
     */
    public synchronized void stopWorking(){
        working = false;
    }
}
