/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Client;
import entities.CompteBancaire;
import java.io.Serializable;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.GestionnaireDeComptesBancaires;

/**
 * BackingBean pour les clients
 * @author David Da Silva
 */
@Named(value = "clientsMBean")
@ViewScoped
public class ClientsMBean implements Serializable{
    
    
    @EJB
    private GestionnaireDeComptesBancaires g;
    private LoginMBean login;
    private Client client;
    private long idClient;
    private Collection<CompteBancaire> comptes;
    private String nomNouveauCompte;
    
    // Pages HTML
    private static final String PAGE_DETAIL_CLIENT  = "showClient";
    private static final String PAGE_LISTE_COMPTES  = "liste_comptes";
    
    

    /**
     * Creates a new instance of ClientsMBean
     */
    public ClientsMBean() {
        //login = new LoginMBean();
    }
    
    public void setId(long id){
        this.idClient = id;
    }
    
    public long getId(){
        return this.idClient;
    }
    
    /**
     * Cette méthode charge le client a partir de l'id déjà transmis.
     */
    public void loadClient(){
        System.out.println("loadClient");
        
        // Morceau de code qui permet de récuperer l'instance de LoginMBean.
        // Ne pas poser de question, ne pas modifier, magie voudou inside
        Application app = FacesContext.getCurrentInstance().getApplication();
        ValueBinding vb = app.createValueBinding("#{loginMBean}");
        login = (LoginMBean) vb.getValue(FacesContext.getCurrentInstance());
        
        
        
        if(login.isConnected()){
            System.out.println("Client #"+login.getIdClient()+" connecté. idClient de ClientMBean="+idClient);
            if(idClient == 0){
                idClient = login.getIdClient();
                System.out.println("idClient de ClientMBean = "+idClient+" maintenant");
            }
            this.client = g.getClient(idClient);
        } else {
            System.out.println("Pas connecté");
        }
    }
    
    public Client getClient(){
        return this.client;
    }
    
    public void setClient(Client c){
        this.client = c;
    }
    
    public String getNom(){
        return this.client.getNom();
    }
    
    public void setNom(String nom){
        client.setNom(nom);
        client = g.update(client);
    }
    
    public String getPassword(){
        return client.getPassword();
    }
    
    public void setPassword(String password){
        client.setPassword(password);
        client= g.update(client);
    }

    public String getNomNouveauCompte() {
        return nomNouveauCompte;
    }

    public void setNomNouveauCompte(String nomNouveauCompte) {
        this.nomNouveauCompte = nomNouveauCompte;
    }
    
    public String creerCompteBancaire(){
        client.addCompte(new CompteBancaire(nomNouveauCompte,0.0));
        client = g.update(client);
        return redirectTo(PAGE_DETAIL_CLIENT, true, "id="+client.getId());
    }
    
    
    /**
     * Methode perso qui génère les url parce que ça me saoule de le copier 50 fois
     * @param page
     * @param facesRedirect
     * @param params
     * @return 
     */
    private String redirectTo(String page, boolean facesRedirect, String... params){
        
        String url = "";
        
        url += page;
        
        if(params != null || facesRedirect)
            url += "?";
        
        if(params != null){
            for(String param : params){
                url+=param+"&";
            }
        }
        if (facesRedirect)
            url+="faces-redirect=true";
        
        
        return url;
    }
}
