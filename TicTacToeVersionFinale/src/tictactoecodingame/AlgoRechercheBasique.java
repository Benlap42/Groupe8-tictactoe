/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

import java.util.ArrayList;

/**
 *
 * @author Yoann Bordin
 */
public class AlgoRechercheBasique extends AlgoRecherche {
    
    int[][] diag_gd = { {0,0},{1,1},{2,2} };
    int[][] diag_dg = { {2,0},{1,1},{0,2} };
    Joueur adversaire;
    
    public AlgoRechercheBasique(Joueur joueur2) {
        adversaire = joueur2;
    }

    @Override
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {

        /*
        
         listeCoups;
        
         attribuerScore(coup);
         meilleurCoup = getMaxScore();
         return meilleurCoup;
        
         */
        ArrayList<Coup> listeCoups = _plateau.getListeCoups(_joueur);
        Coup meilleurCoup = listeCoups.get(0);
        int max = meilleurCoup.getNote();

        // Attribution du score de chaque coup
        for (Coup c : listeCoups) {
            int score;
            score = determinationScore((CoupTicTacToe) c, _joueur, _plateau);
            c.setNote(score);
        }

        // Recherche de la note max
        for (Coup c : listeCoups) {
            if (c.getNote() > max) {
                max = c.getNote();
                meilleurCoup = c;
            }
        }

        return meilleurCoup;
    }

    private int determinationScore(CoupTicTacToe c, Joueur joueur, Plateau pl) {

        /*
        
         int score;
        
         si 2 jetons en ligne/colonne/diag et 3e case libre
         alors score = 10
        
         si 1 jeton en ligne/colonne/diag et 2 cases libres
         alors score = 5
        
         */
        
        int score = 0;
        
        int colonne = c.getColonne();
        int ligne = c.getLigne();
        
        if(rangee(colonne, ligne, joueur, pl) == 2){
            score = 3;
        }
        else if(rangee(colonne, ligne, adversaire, pl) == 2){
            score = 2;
        }
        else if(rangee(colonne, ligne, joueur, pl) == 1 && rangee(colonne, ligne, adversaire, pl) == 0){
            score = 1;
        }
        else{
            score = 0;
        }

        return score;

    }
    
    // Renvoie le nombre de cases appartenant au joueur j sur la rangee qui en contient le plus
    private int rangee(int colonne, int ligne, Joueur joueur, Plateau pl) {
        int[][] hztl = { {0,ligne},{1,ligne},{2,ligne} };
        int[][] vert = { {colonne,0},{colonne,1},{colonne,2} };
        
        int ct_vert = 0, ct_hztl = 0;
        int ct_diag_gd = 0, ct_diag_dg = 0;
        
        for(int i = 0; i < vert.length; i++){
            //Rangées horizontales/vertivales
            Jeton jt_vert = (Jeton) pl.getPiece( new Case(vert[i][0],vert[i][1]) );
            Jeton jt_hztl = (Jeton) pl.getPiece( new Case(hztl[i][0],hztl[i][1]) );
            
            if(jt_vert != null && jt_vert.getJoueur() == joueur){
                ct_vert ++;
            }
            if(jt_hztl != null && jt_hztl.getJoueur() == joueur){
                ct_hztl ++;
            }
            
            //Rangées diagonales
            if(colonne == 0 && ligne == 0 || colonne == 1 && ligne == 1 || colonne == 2 && ligne == 2){
                Jeton jt_diag_gd = (Jeton) pl.getPiece( new Case(diag_gd[i][0],diag_gd[i][1]) );
                
                if(jt_diag_gd != null && jt_diag_gd.getJoueur() == joueur){
                    ct_diag_gd ++;
                }
            }
            if(colonne == 0 && ligne == 2 || colonne == 1 && ligne == 1 || colonne == 2 && ligne == 0){
                Jeton jt_diag_dg = (Jeton) pl.getPiece( new Case(diag_dg[i][0],diag_dg[i][1]) );
                
                if(jt_diag_dg != null && jt_diag_dg.getJoueur() == joueur){
                    ct_diag_dg ++;
                }
            }
        }
        
        // Vérification
        int[] liste_ct = {ct_vert, ct_hztl, ct_diag_gd, ct_diag_dg};
        int max = 0;
        for (int ct : liste_ct) {
            if(ct > max){
                max = ct;
            }
        }
        
        return max;
    }
    
}
