/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

/**
 *
 * @author user
 */
public class testEval {
    
    // Renvoie le score attribué à une grille 3x3 du plateau
    public int evalScore3x3(GrilleTicTacToe3x3 g3x3, Joueur joueur) {
        
        if(g3x3.partieGagnee()){
            if(g3x3.vainqueur == joueur){ // Victoire du joueur
                return 10;
            }
            else if(g3x3.vainqueur != null){ // Victoire de l'adversaire
                return -10;
            }
            else{
                System.out.println("Erreur");
                return 0;
            }
        }
        // Else if none of them have won then return 0 
        return 0;
    }
}
