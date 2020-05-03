package tictactoecodingame;

/**
 *
 * @author franck
 */

    /*--------------------------------------------------------*/
    /*                 Version jeu en local                   */
    /*--------------------------------------------------------*/


public class Player {

    public static void main(String args[]) {
        
        JoueurOrdi humain = new JoueurOrdi("Humain1");
        JoueurOrdi ordi = new JoueurOrdi("Ordi1");
        
        GrilleTicTacToe9x9 grille = new GrilleTicTacToe9x9();

        Arbitre a = new Arbitre(grille,  humain, ordi);
        
        // Attention, la mise en place de l'algorithme de recherche du meilleur coup doit être faite après création de l'arbitre
        // Une profondeur de 3 est le plus optimal pour l'algorithme
        AlgoMinMax9x9 a1 = new AlgoMinMax9x9(grille, ordi, 3);
        ordi.setAlgoRecherche(a1);
        AlgoMinMax9x9 a2 = new AlgoMinMax9x9(grille, humain, 3);
        humain.setAlgoRecherche(a2);
       
        a.startNewGame(true);    // Demarre une partie en affichant la grille du jeu
        
       // Pour lancer un tournoi de 100 parties en affichant la grille du jeu
        //a.startTournament(1000 , false);
        
    }
}

    /*--------------------------------------------------------*/
    /*                 Version Codin game                     */
    /*--------------------------------------------------------*/

    /*
    import java.util.Scanner;



    class Player {

       public static void main(String args[]) {

            Scanner in = new Scanner(System.in);

            CoupTicTacToe coup;
            JoueurHumain adversaire = new JoueurHumain("Adversaire");
            JoueurOrdi joueurOrdi = new JoueurOrdi("Ordi");

            AlgoRechercheAleatoire alea  = new AlgoRechercheAleatoire( );   // L'ordinateur joue au hasard
            joueurOrdi.setAlgoRecherche(alea);

            GrilleTicTacToe3x3 grille = new GrilleTicTacToe3x3();
            grille.init();


            while (true) {
                int opponentRow = in.nextInt();
                int opponentCol = in.nextInt();
                int validActionCount = in.nextInt();
                for (int i = 0; i < validActionCount; i++) {
                    int row = in.nextInt();
                    int col = in.nextInt();
                }
                if ( opponentCol != -1  ) {
                    coup = new CoupTicTacToe(opponentCol, opponentRow, new Jeton(adversaire));
                    grille.joueCoup(coup);
                }

                coup = (CoupTicTacToe) joueurOrdi.joue(grille);
                grille.joueCoup(coup);
                System.out.println(coup.getLigne() + " " + coup.getColonne() ); 
                System.out.flush();
            }
       }
    
}*/
