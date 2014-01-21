package session;

import entities.Client;
import javax.ejb.Remote;

/**
 * Interface pour GestionnaireDeComptesBancaires.
 * Necessaire pour pouvoir acceder aux méthodes du gestionnaire depuis le SecurityFilter
 * @author David Da Silva
 */
@Remote
public interface RemoteGestionnaire {
    /**
     * Methode appellant login de GestionnaireDeComptesBancaires
     * mais qui ne renvoie que True ou False car le SecurityFilter n'a pas de
     * visibilité sur entities.Client.
     * @param login
     * @param password
     * @return
     */
    public boolean auth(String login, String password);
}
