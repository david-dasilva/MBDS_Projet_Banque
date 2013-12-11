/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package initialisation;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import session.GestionnaireDeComptesBancaires;

/**
 *
 * @author shepard
 */
@Singleton
@Startup // instancie une seule fois des le déploiement
@LocalBean
public class InitBD {
    @EJB
    private GestionnaireDeComptesBancaires compteBancaireFacade;

    public InitBD(){
    }
    
    @PostConstruct
    public void init(){
        System.out.println("### Initialisation de la BDD avec les comptes tests");
        compteBancaireFacade.creerComptesTest();
    }
    

}
