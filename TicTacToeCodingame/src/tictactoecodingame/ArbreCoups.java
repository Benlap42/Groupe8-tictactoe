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
    private int node;
    private ArrayList<ArbreCoups> coupsPossibles;
    
    public ArbreCoups(int n){
        node = n;
    }
    
    
    
    public void SaveAsTxt(){
        // Not coded yet
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public ArrayList<ArbreCoups> getCoupsPossibles() {
        return coupsPossibles;
    }

    public void setCoupsPossibles(ArrayList<ArbreCoups> coupsPossibles) {
        this.coupsPossibles = coupsPossibles;
    }
}
