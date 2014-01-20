package beans;

import entities.Client;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.GestionnaireDeComptesBancaires;

/**
 * Managed Bean qui gère le login.
 * @source : JSFLoginPassword de M.Buffa
 * @author David Da Silva
 */
@Named(value = "loginMBean")
@SessionScoped
public class LoginMBean implements Serializable {

    @EJB
    private GestionnaireDeComptesBancaires g;
    
    private Client client;
    private long idClient;
    private String login;
    private String password;
    private boolean connected = false;
    
    /**
     * Creates a new instance of LoginMBean
     */
    public LoginMBean() {
    }

    public Client getClient() {
        if(connected)
            return client;
        else
            return null;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    
    public String connexion(){
        System.out.println("Connexion pour "+login);      
        Client c = g.login(login, password);

        if(c != null){
            System.out.println("password ok");
            setConnected(true);
            setClient(c);
            setIdClient(c.getId());
            return "showClient?id="+idClient+"&faces-redirect=true";
            
        } else {
            System.out.println("logins incorrects");
            setConnected(false);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logins incorrects!","Méchant pirate!"));
            return "index?wronglogin=true&faces-redirect=true";
        }
    }
    
    public String deconnexion(){
        connected = false;
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Vous êtes déconnecté","A bientôt"));
        return "index?logoff=true&faces-redirect=true";
    }

    
    

    
    
    
    
}
