/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoecodingame;

<<<<<<< HEAD
/**
 *
 * @author benoi
 */
public class Montecarlo {
    
}
=======
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Inès
 * @author benoit
 */
public class Montecarlo extends AlgoRecherche  {
    Noeud origine;
    Noeud joue; 
    Noeud adv;
    double C; // niveau exploration
    int reflexion;
    
    public Montecarlo(double c, int r) {
       origine = new Noeud (null,null);
       joue = origine; 
       C=c;
       adv = origine;
       reflexion=r; 
    }
    
    public Coup meilleurCoup(Plateau _plateau, Joueur _joueur, boolean _ponder) {
        
        _plateau.sauvegardePosition(0);
        
        /// regarder état du plateau et si 2 jetons alignés le forcer à le positionner là + booster la branche 
        ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);

        Coup dernier = _plateau.getDernierCoup();
        if (dernier!=null){
            adv=joue.fils(joue,dernier);
        
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
            
            adv.réinitialisation(adv);
            
            for (int i=0; i<reflexion; i++){
                entrainement(_plateau,_joueur,adv);
            }
        }
        
        joue=adv.MeilleurNoeud(adv, C,coups);
        if (joue!=null){
            _plateau.restaurePosition(0);
            return joue.coupP;
        }else{
            _plateau.restaurePosition(0);
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
        
         _plateau.sauvegardePosition(5);
        joue=n;
        int j=0;    // compteur : 0 =1er joueur et 1 =2ème joueur
        
        while (joue.fils.size()!=0){                                        // SELECTION
            ArrayList<Coup> coups = _plateau.getListeCoups(_joueur);
            joue=joue.MeilleurNoeud(joue, C,coups);
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
       
        if (_plateau.partieGagnee()){                                       // BACK PROPAGATION
            while (joue.pere!= null){
                joue.jouer();
                if (j==k){
                    joue.gagne();
                } 
                joue=joue.pere;
                k=(k++)%2;
            }
        }
        _plateau.restaurePosition(5);
        
    }
    
    
    public void visualisation (Noeud n){
        joue=n;
        for (int i =0;i<joue.coupF.size();i++){
            System.out.println(joue.coupF.get(i).toString()+' '+joue.fils.get(i).victoir+'/'+joue.fils.get(i).simulation);
        } 
    }
    
}

>>>>>>> Montecarlo
