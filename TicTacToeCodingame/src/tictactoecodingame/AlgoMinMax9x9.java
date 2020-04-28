
package tictactoecodingame;

import java.util.ArrayList;

/**
 *
 * @author Yoann Bordin
 */
public class AlgoMinMax9x9 extends AlgoRecherche{
    private int profondeur;     // profondeur de recherche dans l'arbre des coups possibles
    private ArbreCoups ac;
    
    private Joueur joueur;
    private Plateau pl;
    
    final private int[][] diag_gd = {{0, 0}, {1, 1}, {2, 2}};
    final private int[][] diag_dg = {{2, 0}, {1, 1}, {0, 2}};
    
    private int index;
    

    public AlgoMinMax9x9(Plateau pl, Joueur j, int profondeur){
        
        this.profondeur = profondeur;
        Coup node = pl.getDernierCoup();
        
        ac = new ArbreCoups(pl,j,j.getAdversaire(), (CoupTicTacToe) node,this.profondeur);
        
        joueur = j;
        this.pl = pl;
        
        index = 0;
    }
    
    @Override
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {
        
        /*
        
        Récupère le meilleur coup à partir de l'algorithme MinMax
        
        */
        
        ArrayList<Coup> listeCoups = _plateau.getListeCoups(_joueur);
        
        // attribue la note à chaque coup possible à l'aide de minmax et en récupère le meilleur
        
        
        Coup meilleurCoup = listeCoups.get(0);
        int noteMax = -1000;
        
        for(Coup c : listeCoups){
            index = 0;
            int noteCoup = minMax((CoupTicTacToe) c, profondeur, true);
            c.setNote(noteCoup);
            
            System.out.println(c + " " + noteCoup + " " + index);
            
            if(noteMax < noteCoup){
                meilleurCoup = c;
                noteMax = noteCoup;
            }
        }
        
        
        return meilleurCoup;
        
    }
     private int minMax(CoupTicTacToe node, int profondeur, boolean isMaxPlayer){
         ac = new ArbreCoups(pl, joueur, joueur.getAdversaire(), node, profondeur);
         ArrayList<Coup> listeCoups = ac.getListeCoups();

         // Si on arrive au bout de l'arbre on retourne le score du tableau
         if (isTerminalState()) {
             return determinationScore9x9(node, joueur, pl);
         }

        // Recursive use of the minmax algorithm to set the scores of next plays
         if (isMaxPlayer) {
             int valMax = -1000;

             for (Coup c : listeCoups) { // Pour chaque coup, en calcule le score récursivement
                 
                 // Joue le coup
                 index ++;
                 pl.sauvegardePosition(index);
                 pl.joueCoup(c);
                 
                 int val = minMax((CoupTicTacToe) c, profondeur - 1, false);
                 c.setNote(val);
                 
                 // Calcule le score max
                 if (valMax < val) {
                     valMax = val;
                 }
                 
                 // Annule le coup
                 pl.restaurePosition(index);
             }
             return valMax;
             
         } else { // isMinPlayer
             int valMin = 1000;

             for (Coup c : listeCoups) {
                 pl.joueCoup(c);
                 
                 int val = minMax((CoupTicTacToe) c, profondeur - 1, true);
                 c.setNote(val);

                 if (valMin > val) {
                     valMin = val;
                 }
                 
                 if(pl.getDernierCoup() != null){
                    pl.annuleDernierCoup(); 
                 }
                 
             }
             return valMin;
         }
        
    }
     
    private boolean isTerminalState(){
        return (profondeur == 0 || ac.hauteur() == 0);
    }
    
    /*
    private int max(ArrayList<Coup> listeCoups){
        int max = listeCoups.get(0).getNote();
        int note;
        
        for(Coup c : listeCoups){
            note = c.getNote();
            if(max < note){
                max = note;
            }
        }
        return max;
    }
    private int min(ArrayList<Coup> listeCoups){
        int min = listeCoups.get(0).getNote();
        int note;
        
        for(Coup c : listeCoups){
            note = c.getNote();
            if(min > note){
                min = note;
            }
        }
        return min;
    }
    
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
    }*/

    private int determinationScoreCases3x3(CoupTicTacToe c, Joueur joueur, Plateau plateau) {
        /*
        
        Version de base utilisée pour les grilles 3x3
        
         pour chaque rangée :
         si rangée contient 2 pièces
         alors score +4
         si rangee contient 1 pièce et pas de pièce de l'adversaire
         alors score +1
         si rangée contient 2 pièces de l'adversaire
         alors score +4
         si rangee contient 1 pièce de l'adversaire 
         alors score -1
            
        
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
            score += 4;
        } else if (rangee(rangee, joueur, pl) == 1) {
            score += 1;
        }
        if (rangee(rangee, joueur.getAdversaire(), pl) == 2) {
            score += 4;
        } else if (rangee(rangee, joueur.getAdversaire(), pl) == 1) {
            score -= 1;
        }
        return score;
    }

    public int rangee(int[][] rangee, Joueur j, Plateau pl) {
        int compteur = 0;

        // On incrémente de 1 le compteur pour chaque jeton de la rangéé appartenant au joueur
        for (int[] _case : rangee) {
            Jeton jeton = (Jeton) pl.getPiece(new Case(_case[0], _case[1]));
            if (jeton != null && jeton.getJoueur() == j) {
                compteur++;
            }
        }
        return compteur;
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
        
        int score;
        score = score3x3[ligne3x3][colonne3x3];
        
        // Ajout du score de la grille 3x3
        score += determinationScoreCases3x3(cp, joueur, plateau);
        
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

                if (!lCoups3x3.isEmpty()) {
                    for (Coup _c : lCoups3x3) {
                        CoupTicTacToe c = (CoupTicTacToe) _c;
                        c.setNote(determinationScoreCases3x3(c, joueur.getAdversaire(), plateau));
                    }

                    // Calcul du score de la grille 3x3 pour l'adversaire et en prend le max
                    int sc3x3Prov = lCoups3x3.get(0).getNote();
                    for (Coup c : lCoups3x3) {
                        int note = c.getNote();

                        if (note > sc3x3Prov) {
                            sc3x3Prov = note;
                        }
                    }
                    //Ajoute 10 moins l'inverse du score à la liste des scores des grilles 3x3 (score les plus faibles moins intéressants pour l'IA)
                    score3x3[i][j] = -sc3x3Prov;
                }
                
                else{
                    // Score nul si la grille 3x3 est pleine
                    score3x3[i][j] = 0;
                }
            }
        }
        return score3x3;
    }
}
