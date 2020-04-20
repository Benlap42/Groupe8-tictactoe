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
public class ArbreCoups {
    
    private CoupTicTacToe coupNode;
    private ArrayList<ArbreCoups> coupsPossibles;
    private int profondeur;
    
    private Joueur j1, j2;
    private Plateau pl;

    public ArbreCoups(CoupTicTacToe node) {
        this.coupNode = node;
        coupsPossibles = new ArrayList();
    }

    public ArbreCoups(Plateau pl, Joueur j1, Joueur j2, CoupTicTacToe node, int profondeur) {
        
        // Peut-on supposer que j1 est le joueur auquel appartient le noeud de l'arbre ?
        
        this.coupNode = node;
        this.j1 = j1;
        this.j2 = j2;
        coupsPossibles = new ArrayList();
        this.profondeur = profondeur;
        
        ArrayList<Coup> listeCoups;
        
        if(profondeur == 0){
            coupNode = node;
        
        }
        else{
            if (node.getJeton().getJoueur() == j1){
                listeCoups = pl.getListeCoups(j2);
                for(Coup c : listeCoups){
                    coupsPossibles.add(new ArbreCoups(pl, j2, j1, (CoupTicTacToe) c, profondeur-1));
                    
                }
            }
            else{
                listeCoups = pl.getListeCoups(j1);
                for(Coup c : listeCoups){
                    coupsPossibles.add(new ArbreCoups(pl, j1, j2, (CoupTicTacToe) c, profondeur-1));
                }
            }
        }
    }
    
    public CoupTicTacToe getNode() {
        return this.coupNode;
    }

    public void setNode(Coup node) {
        this.coupNode = (CoupTicTacToe) node;
    }

    public ArrayList<ArbreCoups> getCoupsPossibles() {
        return coupsPossibles;
    }

    public void setCoupsPossibles(ArrayList<ArbreCoups> coupsPossibles) {
        this.coupsPossibles = coupsPossibles;
    }
    
    public int getNote(){
        return coupNode.getNote();
    }
    
    public Joueur getJ1(){
        return j1;
    }
    public Joueur getJ2(){
        return j2;
    }
    public ArrayList<Coup> getListeCoups(Joueur j){
        return pl.getListeCoups(j);
    }
    
    ArrayList<Coup> getListeCoups(CoupTicTacToe node) {
        ArrayList<Coup> listeCoups = new ArrayList();
        
        for(int i = 0; i < coupsPossibles.size(); i++){
            listeCoups.add(coupsPossibles.get(i).coupNode);
        }
        return listeCoups;
    }
    
    public void SaveAsTxt(){
        /*
        
        Saves tree data in a treedata.txt file
        
        */
    }
    
    public static ArbreCoups getTreeFromTxt(){
        /*
        
        Extracts tree data form treedata.txt file and return the correspnding ArbreCoup
        
        */
        return null;
    }

    public int hauteur() {
        
        if(coupsPossibles.isEmpty()){
            return 0;
        }
        
        int hMax = 0;
        for(ArbreCoups ac : coupsPossibles){
            int ach = ac.hauteur();
            if(hMax < ach){
                hMax = ach;
            }
        }
        return hMax;
    }

    @Override
    public String toString() {
        String t = "(" + coupNode;
        for(ArbreCoups ac : coupsPossibles){
            t += " " + ac;
        }
        return t;
    }

    
    
    
    
}
