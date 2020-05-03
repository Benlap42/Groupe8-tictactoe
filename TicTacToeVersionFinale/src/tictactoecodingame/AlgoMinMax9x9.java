package tictactoecodingame;

import java.util.ArrayList;

/**
 *
 * @author Yoann Bordin
 */
public class AlgoMinMax9x9 extends AlgoRecherche {

    private int profondeur;     // Profondeur de recherche dans l'arbre des coups possibles
    private ArbreCoups ac;      // Arbre des coups possibles à partir d'un coup (noeud principal)

    private Joueur joueur;
    private Plateau pl;
    
    // Coordonnées des diagonales de la grille pour le calcul du score d'une grille 3x3
    final private int[][] diag_gd = {{0, 0}, {1, 1}, {2, 2}};
    final private int[][] diag_dg = {{2, 0}, {1, 1}, {0, 2}};
    
    private int index;          // Utilisé pour sauvegarder le plateau

    public AlgoMinMax9x9(Plateau pl, Joueur j, int profondeur) {

        this.profondeur = profondeur;
        Coup node = pl.getDernierCoup();

        ac = new ArbreCoups(pl, j, j.getAdversaire(), (CoupTicTacToe) node, this.profondeur);

        joueur = j;
        this.pl = pl;

        index = 0;
    }

    @Override
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {

        /*
        
         Récupère le meilleur coup à partir de l'algorithme MinMax
        
         */
        
        ArrayList<Coup> listeCoups = _plateau.getListeCoups(_joueur);   // Liste des coups possibles

        // Attribue la note à chaque coup possible à l'aide de minmax et en récupère le meilleur
        Coup meilleurCoup = listeCoups.get(0);
        int noteMax = -1000;
            
        for (Coup c : listeCoups) {
            index = 0;
            int noteCoup = minMax(pl, listeCoups, (CoupTicTacToe) c, profondeur, _joueur, true); // Pour chaque coup possible, en récupère le score avec l'algorithme minmax
            c.setNote(noteCoup);    // Attribue la note au coup
            
            if (noteMax < noteCoup) {
                meilleurCoup = c;
                noteMax = noteCoup;
            }
        }

        return meilleurCoup;
    }

    

    private int minMax(Plateau pl, ArrayList<Coup> listeCoupsOpti, CoupTicTacToe node, int profondeur, Joueur joueur, boolean isMaxPlayer) {

        /*
        
         Algorithme minmax qui détermine récursivement le score d'un coup en fonction de la profondeur.
        
        
         */
        
        // Si on arrive au bout de l'arbre on retourne le score du tableau
        if (isTerminalState()) {
            return determinationScore9x9(node, joueur, pl);
        }
        
        if (isMaxPlayer) { // Joueur
            int valMax = -1000;

            for (Coup c : listeCoupsOpti) { // Pour chaque coup, en calcule le score récursivement
                if (index < 99) { // On arrête de chercher s'il y a 100 positions enregistrées
                    // Joue le coup et l'annule
                    pl.sauvegardePosition(index);
                    pl.joueCoup(c);
                    pl.restaurePosition(index);
                    
                    // Mise à jour de l'arbre et de la liste des coups
                    ac = new ArbreCoups(pl, joueur, joueur.getAdversaire(), (CoupTicTacToe) c, profondeur - 1);
                    ArrayList<Coup> newListeCoupsOpti = getListeCoupsOpti(pl, joueur, isMaxPlayer);

                    // Calcul récursif de la valeur du coup
                    int val = minMax(pl, newListeCoupsOpti, (CoupTicTacToe) c, profondeur - 1, joueur.getAdversaire(), false);

                    c.setNote(val);

                    // Calcule le score max
                    if (valMax < val) {
                        valMax = val;
                    }
                    index++;
                }
            }
            return valMax;

        } else {         // Adversaire
            
            // Idem mais avec le min au lieu du max
            
            int valMin = 1000;

            for (Coup c : listeCoupsOpti) {
                if (index < 99) {
                    pl.sauvegardePosition(index);
                    pl.joueCoup(c);
                    pl.restaurePosition(index);

                    ac = new ArbreCoups(pl, joueur, joueur.getAdversaire(), (CoupTicTacToe) c, profondeur - 1);
                    ArrayList<Coup> newListeCoupsOpti = getListeCoupsOpti(pl, joueur, isMaxPlayer);

                    int val = minMax(pl, newListeCoupsOpti, (CoupTicTacToe) c, profondeur - 1, joueur.getAdversaire(), false);

                    c.setNote(val);

                    if (valMin > val) {
                        valMin = val;
                    }

                    index++;
                }
            }
            return valMin;
        }
    }

    private boolean isTerminalState() {
        return (profondeur == 0 || ac.hauteur() == 0);
    }

    private ArrayList<Coup> getListeCoupsOpti(Plateau pl, Joueur joueur, boolean isMax) {
        
        /*
        
        Réduit la liste des coups possibles aux coups intéressants immédiatements pour l'IA, afin de réduire la taille de l'arbre
        
        */
        
        ArrayList<Coup> listeCoups = ac.getListeCoups(pl, joueur);
        ArrayList<Coup> listeCoupsOpti = new ArrayList();
        
        final int refScore = 50; // Score de référence, à partir duquel un coup est intéressant

        for (Coup _c : listeCoups) {
            CoupTicTacToe c = (CoupTicTacToe) _c;
            int note = determinationScore9x9(c, joueur, pl);

            if (isMax) {        // Si le joueur est l'IA
                if (note > refScore) {
                    listeCoupsOpti.add(_c);
                }
            } else {            // Si le joueur est l'advrsaire
                if (note < -refScore) {
                    listeCoupsOpti.add(_c);
                }
            }
        }

        if (listeCoupsOpti.isEmpty()) {     // Si aucun score n'est au dessus du score référence, on prend la liste des coups originelle
            return listeCoups;
        }

        return listeCoupsOpti;
    }

    private int determinationScoreCases3x3(CoupTicTacToe c, Joueur joueur, Plateau plateau) {
        /*
        
        Détermine le score de la case d'une grille 3x3 en fonction de paramètres
        
        */
        int score = 0;

        int colonne = c.getColonne();
        int ligne = c.getLigne();
        
        // Coordonnées des rangées horizontales et verticales
        int[][] hztl = {{0, ligne}, {1, ligne}, {2, ligne}};
        int[][] vert = {{colonne, 0}, {colonne, 1}, {colonne, 2}};
        
        // Chaque case appartient à une rangée horizontale et verticale
        score += getScore(hztl, joueur, plateau);
        score += getScore(vert, joueur, plateau);
        
        // Certaines cases appartiennent à 1 ou 2 diagonales
        if (colonne == ligne) {
            score += getScore(diag_gd, joueur, plateau);
        }
        if (colonne == ligne - 2 || ligne == colonne - 2 || colonne == 1 && ligne == 1) {
            score += getScore(diag_dg, joueur, plateau);
        }
        
        return score;
    }

    private int getScore(int[][] rangee, Joueur joueur, Plateau pl) {
        /*
        
        Pour chaque rangée :
            - Si un jeton du joueur, ajoute 10
            - Si un jeton de l'adversaire, retire 10
            - Si 2 jetons du joueur, ajoute 40
            - Si 2 jetons de l'adversaire, ajoute 40
        
        */
        
        int score = 0;

        if (rangee(rangee, joueur, pl) == 2) {
            score += 40;
        } else if (rangee(rangee, joueur, pl) == 1) {
            score += 10;
        }
        if (rangee(rangee, joueur.getAdversaire(), pl) == 2) {
            score += 40;
        } else if (rangee(rangee, joueur.getAdversaire(), pl) == 1) {
            score -= 10;
        }
        return score;
    }

    public int rangee(int[][] rangee, Joueur j, Plateau pl) {
        
        /*
        
        Détermine le nombre de cases de la rangée occupées par des jetons du joueur j
        
        */
        
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

    private int determinationScore9x9(CoupTicTacToe cp, Joueur joueur, Plateau _plateau) {
        /*
        
        Détermine le score d'un coup sur la grille 9x9.
        Pour un coup donné :
            - Ajoute le score (pour l'adversaire) de la grille 3x3 associée
            - Ajoute le score de la grille 3x3 (pour l'IA)
        */
        
        GrilleTicTacToe9x9 plateau = (GrilleTicTacToe9x9) _plateau;

        // Découpage en grilles 3x3
        GrilleTicTacToe3x3[][] grille3x3 = decoupageGrille3x3(plateau);

        // Détermination du score de chaque grille 3x3
        int[][] score3x3 = determinationScoreGrille3x3(plateau, grille3x3);

        // Calcul du score des coups possibles
        int ligne3x3 = cp.getLigne() % 3;
        int colonne3x3 = cp.getColonne() % 3;

        int score;
        score = score3x3[ligne3x3][colonne3x3];

        // Ajout du score de la grille 3x3
        score += determinationScoreCases3x3(cp, joueur, plateau);

        return score;
    }

    private GrilleTicTacToe3x3[][] decoupageGrille3x3(GrilleTicTacToe9x9 plateau) {
        
        /*
        
        Découpe la grille 9x9 en grilles 3x3 pour le calcul des scores
        
        */
        
        GrilleTicTacToe3x3[][] grille3x3 = new GrilleTicTacToe3x3[3][3];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                GrilleTicTacToe3x3 g = new GrilleTicTacToe3x3();

                // Remplit les grilles 3x3 avec les jetons du plateau
                Jeton jList[][] = new Jeton[3][3];
                for (int l = 0; l < 3; l++) {
                    for (int c = 0; c < 3; c++) {
                        jList[l][c] = (Jeton) plateau.getPiece(new Case(3 * i + l, 3 * j + c));
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
        
        /*
        
        Détermine le score d'une grille 3x3 pour l'adversaire.
        Effectue la somme des scores de chaque case.
        
        */
        
        int[][] score3x3 = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                // Détermination du score de chaque case (pour l'adversaire)
                GrilleTicTacToe3x3 g3x3 = grille3x3[i][j];
                ArrayList<Coup> lCoups3x3;
                lCoups3x3 = g3x3.getListeCoups(joueur.getAdversaire());

                if (!lCoups3x3.isEmpty()) {
                    for (Coup _c : lCoups3x3) {
                        CoupTicTacToe c = (CoupTicTacToe) _c;
                        c.setNote(determinationScoreCases3x3(c, joueur.getAdversaire(), plateau));
                    }
                    
                    //Calcul de la somme
                    int sc3x3Prov = 0;
                    for (Coup c : lCoups3x3){
                        sc3x3Prov += c.getNote();
                    }
                    
                    score3x3[i][j] = sc3x3Prov;
                } else {
                    // Score nul si la grille 3x3 est pleine
                    score3x3[i][j] = 0;
                }
            }
        }
        return score3x3;
    }
}
