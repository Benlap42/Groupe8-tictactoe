
package tictactoecodingame;

import java.util.ArrayList;

/**
 *
 * @author Yoann Bordin
 */
public class AlgoRechercheBasique2 extends AlgoRecherche {

    int[][] diag_gd = {{0, 0}, {1, 1}, {2, 2}};
    int[][] diag_dg = {{2, 0}, {1, 1}, {0, 2}};
    Joueur adversaire;
    
    public AlgoRechercheBasique2(Joueur joueur2) {
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
            
            //debug
            System.out.println(c + " " + c.getNote());
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

}
