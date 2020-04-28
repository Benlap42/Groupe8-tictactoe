/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author benoi
 */
public class Montecarlo extends AlgoRecherche  {
    Noeud origine;
    Noeud joue; 
    Noeud adv;
    double C; // niveau exploration
    
    public Montecarlo(double c) {
       origine = new Noeud (null,null);
       joue = origine; 
       C=c;
       adv = origine;
       
    }
    
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {
        
        ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);

        Coup dernier = _plateau.getDernierCoup();
        if (dernier!=null){
            adv=joue.fils(joue,dernier);

             _plateau.sauvegardePosition(0);
        
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

        }
        
        joue=adv.MeilleurNoeud(adv, C,coups);
        if (joue!=null){
            return joue.coupP;
        }else{
            joue=adv.ExtensionHazard(_plateau, _joueur, adv);
            return joue.coupP;
        }
    }
    
    public void initialisation (Plateau p, Joueur j, Noeud n){
        
        if (n==null){
            n=origine;
        }
        n.ExtensionForce(p, j, n);
    }
    
    public void entrainement (Plateau _plateau, Joueur _joueur){
        joue=origine;
        _plateau.init();
        int j=0;    // compteur : 0 =1er joueur et 1 =2ème joueur
        
        while (joue.fils.size()!=0){
            ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
            joue=joue.MeilleurNoeud(joue, C,coups);
            joue.jouer();
            _plateau.joueCoup(joue.coupP);
            j++;
            j=j%2;
        }
        
        joue=joue.ExtensionHazard(_plateau, _joueur, joue);
        joue.jouer();
        _plateau.joueCoup(joue.coupP);
        int k =j; //garde en mémoir on évalue quelle couleur de noeud
            
        while (_plateau.partieTerminee()==false){
            j++;
            j=j%2;
            ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
            Random rnd = new Random();
            Coup c = coups.get(rnd.nextInt( coups.size()));
            _plateau.joueCoup(c);
        }
       
        if (_plateau.partieGagnee()){
            while (joue.pere!= null){
                if (j==k){
                    joue.gagne();
                }
                joue=joue.pere;
                k++;
                k=k%2;
            }
        }
        
        
    }
    
    public void visualisation (){
        joue=origine;
        for (int i =0;i<joue.coupF.size();i++){
            Noeud n = joue.fils.get(i);
            System.out.println(joue.coupF.get(i).toString()+' '+n.victoir+'/'+n.simulation);
            for (int j =0;j<n.coupF.size();j++){
                System.out.println("___"+n.coupF.get(j).toString()+' '+n.fils.get(j).victoir+'/'+n.fils.get(j).simulation);
            }
        }
        
    }
    
}

