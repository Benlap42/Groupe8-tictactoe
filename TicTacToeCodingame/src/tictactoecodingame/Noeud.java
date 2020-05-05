package tictactoecodingame;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author home
 */
public class Noeud {
    int victoir; 
    int simulation;
    ArrayList<Noeud> fils;
    ArrayList<Coup> coupF;
    Noeud pere;
    Coup coupP;
    
    /**
     * <div> Le constructeur de la Classe. </div>
     * @param np le Noeud père
     * @param c le Coup père 
     */
    public Noeud(Noeud np,Coup c){
        victoir =0;
        simulation =0;
        fils = new ArrayList();
        coupF = new ArrayList();
        pere =np;
        coupP=c;
    }
    
    /**
     * <div> Ajoute 1 à l'atribut simulation qui comptabilise le nombre de partie simulé en passant par ce noeud. </div>
     */
    public void jouer(){
        simulation++;
    }
    
    /**
     * <div> Ajoute 1 à l'atribut victoir qui comptabilise le nombre de partie gagné en passant par ce noeud. </div>
     */
    public void gagne(){
        victoir++;
    }
     
    /**
     * <div> Donne le noeud fil à partir d'un noeud père et d'un coup père. De plus si celui n'existait pas il est créé. </div>
     * @param np noeud père
     * @param c coup père
     * @return 
     */
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
    
    /**
     * <div> SELECTION / Donne le meilleur Neoud à selctionner dans l'arbre en donnant une grand par de la selection à l'exploration. </div>
     * @param n noeud père 
     * @param C niveau d'exploration 
     * @param coups liste des coups possibles
     * @return 
     */
    public Noeud MeilleurNoeud_exploration (Noeud n, double C, ArrayList<Coup> coups){
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
                    int min_exploration = 110;
                    if(s<min_exploration){
                        return n.fils.get(i);
                    }
                    selection=Math.log10(S)/s;
                    selection = v/s + C * Math.pow(selection,1/2);
                       
                    if (selection>max){
                        max = selection;
                        j=i;
                    }
                }
            }
            return n.fils.get(j);
        }
    }
    
    /**
     * <div> SELECTION / Donne le meilleur Neoud à jouer dans l'arbre selon les atributs victoir et simulation. </div>
     * @param n noeud père 
     * @param coups liste de coups possibles
     * @return 
     */
    public Noeud MeilleurNoeud_jouer(Noeud n, ArrayList<Coup> coups){
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
                    selection = v/s;
                    if (selection>max){
                        max = selection;
                        j=i;
                    }
                }
            }
            return n.fils.get(j);
        }
    }
    
    /**
     * <div> EXPANSION/ Donne ou crée un noeud fil au hasard.</div>
     * @param _plateau
     * @param _joueur
     * @param n
     * @return 
     */
    public Noeud ExtensionHazard (Plateau _plateau,Joueur _joueur, Noeud n){
        ExtensionForce(_plateau,_joueur,n);
        ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
        Random rnd = new Random();
        Coup c = coups.get(rnd.nextInt(abs(coups.size())));
        
        return n.fils(n, c);
    }
    
    /**
     * <div> EXPANSION/ Crée des noeud fil à partir d'un noeud père si ces derniers n'existaient pas.</div>
     * @param _plateau
     * @param _joueur
     * @param n 
     */
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
    
    /**
     * <div> Permet de booster une branche de façon artificielle en vu d'améliorer notre IA. <div/>
     * @param n
     * @param c 
     */
    public void booster(Noeud n, Coup c){
        n.fils.removeAll(n.fils);
        n.coupF.removeAll(n.coupF);
        Noeud nf = n.fils(n, c);
        nf.simulation = 100;
        nf.victoir=nf.simulation; 
    }
    
    /**
     * <div> Cette fonction permet de réinitialiser les données victoir et simulation des noeuds fils d'un noeud père. Ceci permettant de réinitiliser l'entrainement en vu des avancées du jeu. </div>
     * @param n 
     */
    public void réinitialisation (Noeud n){
        for (int i =0; i<n.fils.size();i++){
            n.fils.get(i).simulation=n.fils.get(i).victoir=0;
        }
    }
}
