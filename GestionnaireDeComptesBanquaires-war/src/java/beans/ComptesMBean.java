package beans;

import entities.Client;
import entities.CompteBancaire;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
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
    private LoginMBean login;
    //private Collection<CompteBancaire> tousLesComptes;
    private CompteBancaire compte;
    private long idCompte;
    private double montant;
    private double montantTransfert;
    private CompteBancaire destinationTransfert;
    //private LazyDataModel<CompteBancaire> modele;
    private LazyDataModel<Client> modele;
    
    // Pages HTML
    private static final String PAGE_DETAIL_COMPTE  = "showCompte";
    private static final String PAGE_LISTE_COMPTES  = "liste_comptes";
    

    /**
     * Creates a new instance of ComptesMBean
     */
    public ComptesMBean() {

        modele = new LazyDataModel<Client>(){

                          @Override
                          public List load(int i, int i1, String string, SortOrder so, Map map) {
                              List<Client> clients = new ArrayList<Client>();
                              clients = g.getLazyClients(i, i1);
                              return clients;
                          }            
                          
                           @Override     
                           public int getRowCount() {       
                               return g.getNBComptes();      
                           }
                      }; 
        
        /*
        modele = new LazyDataModel<CompteBancaire>(){

                          @Override
                          public List load(int i, int i1, String string, SortOrder so, Map map) {
                              List<CompteBancaire> comptes = new ArrayList<CompteBancaire>();
                              comptes = g.getLazyComptes(i, i1);
                              return comptes;
                          }            
                          
                           @Override     
                           public int getRowCount() {       
                               return g.getNBComptes();      
                           }
                      }; */
    }
    
    /**
     * Appel a la méthode getAllComptes du SessionBean
     * @return 
     */
    @Deprecated
    public List<CompteBancaire> getAllComptes(){
        return g.getAllComptes();
    }
    
    public LazyDataModel getModele(){
        return this.modele;
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
        System.out.println("TRANSFERT "+montantTransfert+ " vers compte #"+destinationTransfert.getId());
        g.transfert(compte.getId(), destinationTransfert.getId(), montantTransfert);
        return redirectTo(PAGE_DETAIL_COMPTE, true, "id="+compte.getId());
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
        System.out.println("loadCompte");
        
        // Morceau de code qui permet de récuperer l'instance de LoginMBean.
        // Ne pas poser de question, ne pas modifier, magie voudou inside
        Application app = FacesContext.getCurrentInstance().getApplication();
        ValueBinding vb = app.createValueBinding("#{loginMBean}");
        login = (LoginMBean) vb.getValue(FacesContext.getCurrentInstance());
        
        if (login.isConnected()){
            System.out.println("Compte. connected. idCompte = "+idCompte);
            if(idCompte!=0)
                this.compte = g.getCompte(idCompte);
        }
    }
    
    
    public String getNom(){
        return compte.getNom();
    }
    
    public void setNom(String nom){
        compte.setNom(nom);
        compte = g.update(compte);
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
        //this.tousLesComptes = getAllComptes();// refresh
        return redirectTo(PAGE_LISTE_COMPTES, true, null);
    }
    
    public String showDetails(CompteBancaire compte) {
        this.compte = compte;  
        return redirectTo(PAGE_DETAIL_COMPTE, true, null);  
    }
    
    public List<CompteBancaire> complete(String query) {
        System.out.println("Recherche pour : "+query);
        List<CompteBancaire> l = g.findComptes(query);
        System.out.println("trouvé : "+l.size());
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
