/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author benoi
 */
public class Noeud {
    int victoir; 
    int simulation;
    ArrayList<Noeud> fils;
    ArrayList<Coup> coupF;
    Noeud pere;
    Coup coupP;
    
    public Noeud(Noeud np,Coup c){
        victoir =0;
        simulation =0;
        fils = new ArrayList();
        coupF = new ArrayList();
        pere =np;
        coupP=c;
    }
    
    public void jouer(){
        simulation++;
    }
    
    public void gagne(){
        victoir++;
    }
     
    
    public Noeud fils(Noeud np,Coup c){
        int i = coupF.indexOf(c); // donne index de l'objet et -1 si trouve pas 
        if (i!=-1){
            return np.fils.get(i);
        }else{
            np.coupF.add(c);
            Noeud nf = new Noeud(np,c);
            np.fils.add(nf);
            return nf;
        }
    } 
    
    public Noeud MeilleurNoeud (Noeud n, double C, ArrayList<Coup> coups){
        double selection;
        double v;
        double s;
        double S = n.fils.size();
        double max=0;
        int j=0; // indice 
        
        if (S==0){
            return null;
        }else{
            for (int i=0;i<S;i++){
                if (coups.indexOf(n.coupF.get(i))!=-1){
                    v=n.fils.get(i).victoir;
                    s=n.fils.get(i).simulation;
                    if (s!=0){
                        selection=Math.log10(S)/s;
                        selection = v/s + C * Math.pow(selection,1/2);
                    }else{
                        selection = 2;
                    }
                    if (selection>max){
                        max = selection;
                        j=i;
                    }
                }
            }
            return n.fils.get(j);
        }
    }
    
    public Noeud ExtensionHazard (Plateau _plateau,Joueur _joueur, Noeud n){
        ExtensionForce(_plateau,_joueur,n);
        ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
        Random rnd = new Random();
        Coup c = coups.get(rnd.nextInt(abs(coups.size())));
        
        return n.fils(n, c);
    }
    
    public void ExtensionForce(Plateau _plateau,Joueur _joueur, Noeud n){
        ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
        for (int i=0;i<coups.size();i++){
            int j = n.coupF.indexOf(coups.get(i)); // donne index de l'objet et -1 si trouve pas 
            if (j==-1){
                Noeud nf = new Noeud(n,coups.get(i));
                n.fils.add(nf);
                n.coupF.add(coups.get(i));
                nf.pere=n;
                nf.coupP=coups.get(i);
            }
        }
    }

    public void booster(Noeud n, Coup c){
        n.fils.removeAll(n.fils);
        n.coupF.removeAll(n.coupF);
        Noeud nf = n.fils(n, c);
        nf.simulation = 100;
        nf.victoir=nf.simulation; 
    }

    public void réinitialisation (Noeud n){
        for(int i = 0;i<n.fils.size();i++){
            n.fils.get(i).simulation = n.fils.get(i).victoir=0;
        }
    }
}
