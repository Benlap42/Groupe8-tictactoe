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
    
    public AlgoMCTS() {
    }
    
    
    public Coup meilleurCoup( Plateau _plateau , Joueur _joueur , boolean _ponder ){
        
        ArrayList<Coup> coups_test = _plateau.getListeCoups(_joueur);        //donne coup posssible après
        
        int[][] learn=new int[coups_test.size()][2];
        Random rand = new Random();
        
        Plateau plateau_algo =_plateau;         // engendre nouveau jeu
        
        JoueurOrdi IA = new JoueurOrdi("IA_train",_joueur.getIdJoueur());   //nouveau joueur avec les même symbole (croix ou rond) que précédemmant
        int id;
        if (_joueur.getIdJoueur()==1){
            id=0;
        }else{
            id=1;
        }
        JoueurOrdi Ordi = new JoueurOrdi("Ordi_train",_joueur.getIdJoueur());
            
        AlgoRechercheAleatoire alea_test  = new AlgoRechercheAleatoire( );   // L'ordinateur et l'IA jouent au hasard
        Ordi.setAlgoRecherche(alea_test);
        IA.setAlgoRecherche(alea_test);
            
        for (int k=0; k<plateau_algo.getNbColonnes();k++){              // attribut les coups aux nouveaux joueurs 
            for (int l=0; l<plateau_algo.getNbLignes();l++){
                
                Case c = new Case(k,l);
                
                if ( plateau_algo.getPiece(c) != null){
                    Piece p = plateau_algo.getPiece(c);
               
                    Joueur joueur_test = p.getJoueur(); 
                
                    if (joueur_test == _joueur){
                        p.setJoueur(IA);
                    }else{
                        p.setJoueur(Ordi);
                    }
                
                }
            }
                    
        }
        

        for (int j =0;j<50;j++){         // tester chance de gagner en fonction du prochain coup joué
            
            Plateau plateau_test = plateau_algo;
            
            int i = rand.nextInt(coups_test.size());     //prend valeur au pif à tester
            learn[i][1]++;
            
            plateau_test.joueCoup(coups_test.get(i));                                       // joue coup dont veut tester chance de réussite
            
            Arbitre a_test = new Arbitre(plateau_test, Ordi , IA );          //met partie en jeu AVEC IA JOUE EN DEUXIEME CAR COMME SI VENAIT DE JOUER
            
            Joueur vainqueur;
            vainqueur = a_test.startGame(false);
            
            if (vainqueur!=null){
                if (IA==vainqueur){
                    learn[i][0]++;
                }
            }else{
                learn[i][1]--;
            }
        }
        
        int[] chance=new int[coups_test.size()];        // calcule de la chance de réussite de chaque coup
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
