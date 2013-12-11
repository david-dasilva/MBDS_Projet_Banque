package beans;

import entities.CompteBancaire;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.GestionnaireDeComptesBancaires;


/**
 * BackingBean pour les ComptesBancaires
 * @author David Da Silva
 */
@Named(value = "comptesMBean")
@ViewScoped
public class ComptesMBean implements Serializable{
    @EJB
    private GestionnaireDeComptesBancaires g;
    private Collection<CompteBancaire> tousLesComptes;
    private CompteBancaire compte;
    private long idCompte;
    private double montant;
    private double montantTransfert;
    private CompteBancaire destinationTransfert;
    
    // Pages HTML
    private static final String PAGE_DETAIL_COMPTE  = "show";
    private static final String PAGE_LISTE_COMPTES  = "liste_comptes";
    

    /**
     * Creates a new instance of ComptesMBean
     */
    public ComptesMBean() {
    }
    
    /**
     * Appel a la méthode getAllComptes du SessionBean
     * @return 
     */
    public List<CompteBancaire> getAllComptes(){
        return g.getAllComptes();
    }
    
    /**
     * Appel à la méthode déposer du SessionBean + redirection
     * @return 
     */
    public String deposer(){
        System.out.println("DEPOSER "+montant);
        compte.deposer(this.montant);
        g.update(compte);
        return redirectTo(PAGE_DETAIL_COMPTE, true, "id="+this.idCompte);
    }
    
    /**
     * Appel à la méthode déposer du SessionBean + redirection
     * @return 
     */
    public String retirer(){
        System.out.println("RETIRER "+montant);
        compte.retirer(this.montant);
        g.update(compte);
        return redirectTo(PAGE_DETAIL_COMPTE, true, "id="+this.idCompte);
    }
    
    
    public String transferer(){
        System.out.println("TRANSFERT "+montantTransfert+ " vers compte #"+destinationTransfert);
        g.transfert(compte.getId(), destinationTransfert.getId(), montantTransfert);
        return redirectTo(PAGE_DETAIL_COMPTE, true, null);
    }
    
    /**
     * Renvoie vers la page qui liste tous les comptes.
     */
    public String list() {  
        System.out.println("###LIST###");  
        return redirectTo(PAGE_LISTE_COMPTES, true, null);
    }
    
    /**
     * Cette méthode charge un object compte a partir de l'id déjà récupéré.
     */
    public void loadCompte(){
        this.compte = g.getCompte(idCompte);
    }
    
    public String update(){
        System.out.println("### UPDATE du compte"+idCompte);  
        compte = g.update(compte);  
        return redirectTo(PAGE_LISTE_COMPTES, true, null);
    }
    
    public String delete() {  
        System.out.println("### DELETE du compte "+idCompte);
        g.delete(compte);
        // MAJ
        this.tousLesComptes = getAllComptes();// refresh
        return redirectTo(PAGE_LISTE_COMPTES, true, null);
    }
    
    public String showDetails(CompteBancaire compte) {
        this.compte = compte;  
        return redirectTo(PAGE_DETAIL_COMPTE, true, null);  
    }
    
    public List<CompteBancaire> complete(String query) {
        List<CompteBancaire> l = g.findComptes(query);
        // Je retire l'utilisateur courant de la liste
        l.remove(compte);
        return l;
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
    
    
    // Autocomplete
    // TODO : ça ça marche pooooooo
    private Converter converter = new Converter() {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return g.getCompte(Long.parseLong(value));
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            CompteBancaire c = (CompteBancaire) value;
            return c.getId().toString();
        }
        
    };
    
    
    
    /*
     * Getters & Setters
     */
    
    /**
     * Cette méthode est appellée automatiquement quand un id est défini dans l'url
     * @param id 
     */
    public void setId(long id){
        this.idCompte = id;
    }
    
    public long getId(){
        return idCompte;
    }
    
    public void setMontant(double montant){
        System.out.println("SET MONTANT "+montant);
        this.montant = montant;
    }
    
    public double getMontant(){
        return this.montant;
    }
    
    public CompteBancaire getCompte() {
        return compte;
    }
    
    public void setMontantTransfert(double montant){
        this.montantTransfert = montant;
    }
    
    public double getMontantTransfert(){
        return this.montantTransfert;
    }

    public CompteBancaire getDestinationTransfert() {
        return destinationTransfert;
    }

    public void setDestinationTransfert(CompteBancaire destinationTransfert) {
        this.destinationTransfert = destinationTransfert;
    }
    
    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
    
}
