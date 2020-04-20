/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author benoi
 */
public class Montecarlo {
    
     public Montecarlo() {
    }
    
    
    public Coup meilleurCoup( Plateau _plateau_mcts , Joueur _joueur , boolean _ponder ){
        
        ArrayList<Coup> coups_test = _plateau_mcts.getListeCoups(_joueur);        //donne coup posssible après
        
        int[][] learn=new int[coups_test.size()][2];
        Random rand = new Random();
        
        for (int j =0;j<50;j++){         // tester chance de gagner en fonction du prochain coup joué
            
            Plateau plateau_test =_plateau_mcts;
            
            int i = rand.nextInt(coups_test.size());     //prend valeur au pif à tester
            learn[i][1]++;
            
            plateau_test.joueCoup(coups_test.get(i));                                       // joue coup dont veut tester chance de réussite
            
            JoueurOrdi Ordi = new JoueurOrdi("Ordi_vs",0);
            JoueurOrdi IA = new JoueurOrdi("IA_train",1);
            AlgoRechercheAleatoire alea_test  = new AlgoRechercheAleatoire( );   // L'ordinateur joue au hasard
            Ordi.setAlgoRecherche(alea_test);
            IA.setAlgoRecherche(alea_test);
            Arbitre a_test = new Arbitre(plateau_test, Ordi , IA );          //met partie en jeu AVEC IA JOUE EN DEUXIEME CAR COMME SI VENAIT DE JOUER
            
            Joueur vainqueur;
            vainqueur = a_test.startNewTest(false,plateau_test);
            
            if (vainqueur!=null){
                if (IA==vainqueur){
                    learn[i][0]++;
                }
            }else{
                learn[i][1]--;
            }
        }
        
        int[] chance=new int[coups_test.size()];
        for (int h=1;h<chance.length;h++){
            if (learn[h][1]!=0){
                chance[h]=(learn[h][0]*100)/learn[h][1];
            }else{
                chance[h]=0;
            }
        }
        
        int max = 0;                                //recherche du coup qui donne le plus de chance de gagner
        for (int k=1;k<coups_test.size();k++){
            if (Math.max(chance[max],chance[k])!= chance[max]){
                max=k;
            }
        }
        
        System.out.println(max);
        return coups_test.get(max);
    }
    
    
}

}
