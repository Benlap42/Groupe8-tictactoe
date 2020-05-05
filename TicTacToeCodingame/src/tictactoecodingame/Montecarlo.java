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
 * @autor Benoit et Inès 
 */
public class Montecarlo extends AlgoRecherche{
    Noeud origine;
    Noeud joue; 
    Noeud adv;
    double C; // niveau exploration
    int reflexion;
    
    /**
     * <div> Constructeur de la Classe. </div>
     * @param c le niveau d'exploration
     * @param r le niveau de réflexion (le nombre d'entrainement qui est réaliser avant chaque tour)
     */
    public Montecarlo(double c,int r) {
       origine = new Noeud (null,null);
       joue = origine; 
       adv=origine;
       C=c;
       reflexion=r; 
    }
    
    
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {
        
        _plateau.sauvegardePosition(0);
        // regarder état du plateau et si 2 jetons alignés le forcer à le positionner là + booster la branche 
        ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
        
        Coup dernier = _plateau.getDernierCoup();
        if (dernier!=null){
            adv=joue.fils(joue,dernier);
            
             _plateau.sauvegardePosition(0);
            
            if (_plateau.getNbColonnes()==3){
                for (int i=0; i<coups.size();i++){
                    _plateau.joueCoup(coups.get(i));
                    _plateau.sauvegardePosition(1);
                    if (_plateau.partieTerminee()){
                        if(_plateau.partieGagnee()){
                            _plateau.restaurePosition(0);
                            joue=adv.fils(adv, coups.get(i));
                            joue.booster(adv, coups.get(i));
                            return joue.coupP;
                        }
                    }else {
                        Joueur A = dernier.getJeton().getJoueur();
                        ArrayList<Coup> coups2 = _plateau.getListeCoups(A);
                        for (int j=0; j<coups2.size();j++){
                            _plateau.joueCoup(coups2.get(j));
                            if (_plateau.partieTerminee()){
                                if (_plateau.partieGagnee()){
                                    Coup c = coups2.get(j);
                                    c.getJeton().setJoueur(_joueur);
                                    _plateau.restaurePosition(0);
                                    joue=adv.fils(adv, c);
                                    joue.booster(adv, c);
                                    return joue.coupP;
                                }
                            }
                            _plateau.restaurePosition(1);
                        }
                    }
                    _plateau.restaurePosition(0);
                }
            }else if (_plateau.getNbColonnes()==9){
                for (int i=0; i<coups.size();i++){
                    _plateau.joueCoup(coups.get(i));
                    _plateau.sauvegardePosition(1);
                    if (((GrilleTicTacToe9x9)_plateau).grilleGagnee){
                        _plateau.restaurePosition(0);
                        joue=adv.fils(adv, coups.get(i));
                        joue.booster(adv, coups.get(i));
                        return joue.coupP;
                    }else {
                        Joueur A = dernier.getJeton().getJoueur();
                        ArrayList<Coup> coups2 = _plateau.getListeCoups(A);
                        for (int j=0; j<coups2.size();j++){
                            _plateau.joueCoup(coups2.get(j));
                            if (_plateau.partieTerminee()){
                                if (_plateau.partieGagnee()){
                                    Coup c = coups2.get(j);
                                    c.getJeton().setJoueur(_joueur);
                                    _plateau.restaurePosition(0);
                                    joue=adv.fils(adv, c);
                                    joue.booster(adv, c);
                                    return joue.coupP;
                                }
                            }
                            _plateau.restaurePosition(1);
                        }
                    }
                    _plateau.restaurePosition(0);
                }
            }
            
            adv.réinitialisation(adv);
         
            for (int i=0; i<reflexion; i++){
                entrainement(_plateau,_joueur,adv);
            }
            
            visualisation(adv);
        }
        
        
        
        joue=adv.MeilleurNoeud_jouer(adv,coups);
        if (joue!=null){
            _plateau.restaurePosition(0);
            return joue.coupP;
        }else{
            joue=adv.ExtensionHazard(_plateau, _joueur, adv);
            _plateau.restaurePosition(0);
            return joue.coupP;
        }
         
    }
    
    /**
     * <div> Initialise le noeud n en créant des noeuds fils </div>
     * @param p
     * @param j
     * @param n 
     */
    public void initialisation (Plateau p, Joueur j, Noeud n){
        
        if (n==null){
            n=origine;
        }
        n.ExtensionForce(p, j, n);
    }
    
    /**
     * <div> L'algorithme s'entraine en agrandissant sous arbre selon la méthode de la Sélection, de l'expansion, de la simulation et du back propagation. </div>
     * @param _plateau
     * @param _joueur
     * @param n 
     */
    public void entrainement (Plateau _plateau, Joueur _joueur,Noeud n){
        
        _plateau.sauvegardePosition(5);
        joue=n;
        int j=0;    // compteur : 0 =1er joueur et 1 =2ème joueur
        
        while (joue.fils.size()!=0){                                    // SELECTION
            ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
            joue=joue.MeilleurNoeud_exploration(joue, C,coups);
            _plateau.joueCoup(joue.coupP);
            j=(j++)%2;
        }
        
        int k =j; //garde en mémoir on évalue quelle couleur de noeud
        if (_plateau.partieTerminee()==false){                              //EXPANSION
            joue=joue.ExtensionHazard(_plateau, _joueur, joue);
            _plateau.joueCoup(joue.coupP);
        }else{
            k=(j++)%2;
        }
            
        while (_plateau.partieTerminee()==false){                           //SIMULATION
            j=(j++)%2;
            ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
            Random rnd = new Random();
            Coup c = coups.get(rnd.nextInt( coups.size()));
            _plateau.joueCoup(c);
        }
       
        if (_plateau.partieGagnee()){                           // BACK PROPAGATION
            while (joue.pere!= null){
                if (j==k){
                    joue.gagne();
                }
                joue.jouer();
                joue=joue.pere;
                k=(k++)%2;
            }
        }
        _plateau.restaurePosition(5);
    }
    
    /**
     * <div> Permet la visualisation des noeuds fils du noeud n avec leur nombre de partie gagnée sur leur nombre de partie simulée. </div>
     * @param n le Noeud père 
     */
    public void visualisation (Noeud n){
        joue=n;
        for (int i =0;i<joue.coupF.size();i++){
            System.out.println(joue.coupF.get(i).toString()+' '+joue.fils.get(i).victoir+'/'+joue.fils.get(i).simulation);
            
        } 
    }
    
}
