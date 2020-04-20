/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class AlgoMinMax9x9 extends AlgoRecherche{
    int profondeur;     // profondeur de recherche dans l'arbre des coups possibles
    ArbreCoups ac;
    
    Joueur adversaire;
    Joueur joueur;
    Plateau pl;
    
    int[][] diag_gd = {{0, 0}, {1, 1}, {2, 2}};
    int[][] diag_dg = {{2, 0}, {1, 1}, {0, 2}};
    

    public AlgoMinMax9x9(Plateau pl, Joueur j1, Joueur j2, int profondeur){
        
        this.profondeur = profondeur;
        Coup node = pl.getDernierCoup();
        ac = new ArbreCoups(pl,j1,j2, (CoupTicTacToe) node,profondeur);
        
        adversaire = j2;
        joueur = j1;
        this.pl = pl;
    }
    
    @Override
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {
        
        /*
        
        Récupère le meilleur coup à partir de l'algorithme MinMax
        
        */
        
        ArrayList<Coup> listeCoups = _plateau.getListeCoups(_joueur);
        
        // attribue la note à chaque coup possible (à l'aide de minmax)
        for(Coup c : listeCoups){
            c.setNote(minMax((CoupTicTacToe) c, profondeur, true));
        }
        
        // Récupère le meilleur coup
        Coup meilleurCoup = listeCoups.get(0);
        int scoreMax = meilleurCoup.getNote();
        
        for(Coup c : listeCoups){
            int note = c.getNote();
            if(note > scoreMax){
                meilleurCoup = c;
                scoreMax = note;
            }
        }
        
        return meilleurCoup;
        
    }
     private int minMax(CoupTicTacToe node, int profondeur, boolean max){
        // If end of the tree then return score from parameters
        if(profondeur == 0 || ac.hauteur() == 0){
            return determinationScore(node, joueur, pl);
        }
        // Recursive use of the minmax algorithm to set the scores of next plays
        ArrayList<Coup> listeCoupsSuiv = ac.getListeCoups(node);
        for(Coup c : listeCoupsSuiv){
            c.setNote(minMax((CoupTicTacToe) c, profondeur-1, !max));
        }
        
        // 
        if(max){
            return max(listeCoupsSuiv);
        }
        else{ // min
            return min(listeCoupsSuiv);
        }
    }
    
    private int max(ArrayList<Coup> lc){
        int max = lc.get(0).getNote();
        int note;
        
        for(Coup c : lc){
            note = c.getNote();
            if(max < note){
                max = note;
            }
        }
        return max;
    }
    private int min(ArrayList<Coup> lc){
        int min = lc.get(0).getNote();
        int note;
        
        for(Coup c : lc){
            note = c.getNote();
            if(min > note){
                min = note;
            }
        }
        return min;
    }
    
   

    private int determinationScore(CoupTicTacToe c, Joueur joueur, Plateau plateau) {
        /*
        
         pour chaque rangée :
         si rangée contient 2 pièces
         alors score +2
         si rangee contient 1 pièce et pas de pièce de l'adversaire
         alors score +1
            
        
         */
        int score = 0;

        int colonne = c.getColonne();
        int ligne = c.getLigne();

        int[][] hztl = {{0, ligne}, {1, ligne}, {2, ligne}};
        int[][] vert = {{colonne, 0}, {colonne, 1}, {colonne, 2}};

        ArrayList<int[][]> listeRangees = new ArrayList();
        listeRangees.add(hztl);
        listeRangees.add(vert);
        listeRangees.add(diag_gd);
        listeRangees.add(diag_dg);

        score += getScore(hztl, joueur, plateau);
        score += getScore(vert, joueur, plateau);

        if (colonne == ligne) {
            score += getScore(diag_gd, joueur, plateau);
        }
        if (colonne == ligne - 2 || ligne == colonne - 2 || colonne == 1 && ligne == 1) {
            score += getScore(diag_dg, joueur, plateau);
        }

        return score;
    }

    private int getScore(int[][] rangee, Joueur joueur, Plateau pl) {
        int score = 0;

        if (rangee(rangee, joueur, pl) == 2) {
            score += 3;
        } else if (rangee(rangee, joueur, pl) == 1) {
            score += 1;
        }
        if (rangee(rangee, adversaire, pl) == 2) {
            score += 3;
        } else if (rangee(rangee, adversaire, pl) == 1) {
            score -= 1;
        }

        return score;
    }

    public int rangee(int[][] rg, Joueur j, Plateau pl) {
        int ct = 0;

        // On incrémente de 1 le compteur pour chaque jeton de la rangéé appartenant au joueur
        for (int[] cs : rg) {
            Jeton jt = (Jeton) pl.getPiece(new Case(cs[0], cs[1]));
            if (jt != null && jt.getJoueur() == j) {
                ct++;
            }
        }

        return ct;
    }
    
    private int determinationScore2(){
        /*
        Version avec interactions entre grilles 3x3
        
        Attribue un score à chaque grille 3x3, déterminé par les scores de chaque case de ces grilles 3x3 (algo determinationScore)
        
        Ce score est répercuté sur les coups où l'IA peut jouer.
        + évite de jouer de sorte à faire remplir une grille 3x3 à l'adversaire
        + force l'adversaire à jouer de manière à permettre à l'IA de remplir une grille
        + règles du 3x3 appliquées à la grande grille pour gagner
        
        */
    }
}
