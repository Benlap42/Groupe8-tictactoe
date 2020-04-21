
package tictactoecodingame;

import java.util.ArrayList;

/**
 *
 * @author Yoann Bordin
 */
public class AlgoMinMax9x9 extends AlgoRecherche{
    int profondeur;     // profondeur de recherche dans l'arbre des coups possibles
    ArbreCoups ac;
    
    Joueur joueur;
    Plateau pl;
    
    int[][] diag_gd = {{0, 0}, {1, 1}, {2, 2}};
    int[][] diag_dg = {{2, 0}, {1, 1}, {0, 2}};
    

    public AlgoMinMax9x9(Plateau pl, Joueur j, int profondeur){
        
        this.profondeur = profondeur;
        Coup node = pl.getDernierCoup();
        ac = new ArbreCoups(pl,j,j.getAdversaire(), (CoupTicTacToe) node,profondeur);
        
        joueur = j;
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
            return determinationScore9x9(node, joueur, pl);
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
    
   

    private int determinationScoreCases3x3(CoupTicTacToe c, Joueur joueur, Plateau plateau) {
        /*
        
        Version de base utilisée pour les grilles 3x3
        
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
        if (rangee(rangee, joueur.getAdversaire(), pl) == 2) {
            score += 3;
        } else if (rangee(rangee, joueur.getAdversaire(), pl) == 1) {
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
    
    private int determinationScore9x9(CoupTicTacToe cp, Joueur joueur, Plateau _plateau){
        /*
        Version avec interactions entre grilles 3x3
        
        Attribue un score à chaque grille 3x3, déterminé par les scores de chaque case de ces grilles 3x3 (algo determinationScore)
        
        Ce score est répercuté sur les coups où l'IA peut jouer.
        + évite de jouer de sorte à faire remplir une grille 3x3 à l'adversaire
        + force l'adversaire à jouer de manière à permettre à l'IA de remplir une grille
        + règles du 3x3 appliquées à la grande grille pour gagner
        
        */
        GrilleTicTacToe9x9 plateau = (GrilleTicTacToe9x9) _plateau;
        
        // Découpage en grilles 3x3
        GrilleTicTacToe3x3[][] grille3x3 = decoupageGrille3x3(plateau);
        
        // Détermination du score de chaque grille 3x3
        int[][] score3x3 = determinationScoreGrille3x3(plateau, grille3x3);
        
        // Calcul du score des coups possibles
        int ligne3x3 = cp.getLigne()%3;
        int colonne3x3 = cp.getColonne()%3;
        System.out.println(cp.getColonne() + " " + cp.getLigne());
        
        int score;
        score = score3x3[ligne3x3][colonne3x3];
        
        // Ajout des scores de la grille 3x3
        score += determinationScoreCases3x3(cp, joueur, plateau);
        
        System.out.println("Score : " + score);
        
        return score; 
    }

    private GrilleTicTacToe3x3[][] decoupageGrille3x3(GrilleTicTacToe9x9 plateau) {
        GrilleTicTacToe3x3[][] grille3x3 = new GrilleTicTacToe3x3[3][3];
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                
                GrilleTicTacToe3x3 g = new GrilleTicTacToe3x3();
                
                // Remplit les grilles 3x3 avec les jetons du plateau
                Jeton jList[][] = new Jeton[3][3];
                for(int l = 0; l<3; l++){
                    for(int c = 0; c < 3; c++){
                        jList[l][c] = (Jeton) plateau.getPiece(new Case(3*i+l,3*j+c));
                    }
                }
                g.grille = jList;
                // Remplit le tableau contenant les grilles 3x3
                grille3x3[i][j] = g;
            }
        }
        return grille3x3;
    }

    private int[][] determinationScoreGrille3x3(GrilleTicTacToe9x9 plateau, GrilleTicTacToe3x3[][] grille3x3) {
        int[][] score3x3 = new int[3][3];
        
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                
                // Détermination du score de chaque case (pour l'adversaire)
                GrilleTicTacToe3x3 g3x3 = grille3x3[i][j];
                ArrayList<Coup> lCoups3x3;
                
                lCoups3x3 = g3x3.getListeCoups(joueur.getAdversaire());
                System.out.println(lCoups3x3);
                
                for(Coup _c : lCoups3x3){
                    CoupTicTacToe c = (CoupTicTacToe) _c;
                    c.setNote(determinationScoreCases3x3(c, joueur.getAdversaire(), plateau));
                }
                
                // Calcul du score de la grille 3x3 pour l'adversaire et en prend le max
                int sc3x3Prov = lCoups3x3.get(0).getNote();
                for(Coup c : lCoups3x3){
                    int note = c.getNote();
                    
                    if(note > sc3x3Prov){
                        sc3x3Prov = note;
                    }
                }
                
                //Ajoute l'inverse du score à la liste des scores des grilles 3x3 (score les plus faibles moins intéressants pour l'IA)
                score3x3[i][j] = -sc3x3Prov;
            }
        }
        return score3x3;
    }
}
