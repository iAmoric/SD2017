package projet.exceptions;

/**
 * Exception émise quand le coordinateur de Tour par tour
 * demande à un joueur qui a terminé le jeu de jouer.
 * Elle permet au coordinateur de tour par tour de détecter la fin de la partie
 * Created by jpabegg on 19/04/17.
 */
public class FinDePartieException extends Exception {

    public FinDePartieException(){
        super("Le joueur a terminé la partie");
    }
}
