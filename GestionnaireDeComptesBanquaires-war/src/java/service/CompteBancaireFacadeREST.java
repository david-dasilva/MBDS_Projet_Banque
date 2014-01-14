package service;


import entities.Client;
import entities.CompteBancaire;
import entities.OperationBancaire;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.GestionnaireDeComptesBancaires;

/**
 *
 * @author David Da Silva
 */
@Path("compte")
public class CompteBancaireFacadeREST {

    @EJB
    private GestionnaireDeComptesBancaires g;
    
    public static final String FIELD_ID_CLIENT = "idClient";
    public static final String FIELD_ID_COMPTE = "idCompte";
    public static final String FIELD_NOM = "nom";
    public static final String FIELD_SOLDE = "solde";
    
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long create(@FormParam(FIELD_ID_CLIENT) long idClient, @FormParam(FIELD_NOM) String nom, @FormParam(FIELD_SOLDE) long solde) {
        
        Client client = g.getClient(idClient);
        if(client == null){
            return 0;
        }
        CompteBancaire cb = new CompteBancaire(nom, solde);
        client.addCompte(cb);
        g.update(client);
        return g.creerCompte(cb);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public CompteBancaire edit(@FormParam(FIELD_ID_COMPTE) long idCompte, @FormParam(FIELD_NOM) String nom, @FormParam(FIELD_SOLDE) long solde) {
        
        CompteBancaire cb = g.getCompte(idCompte);
        if(cb == null){
            return null;
        }
        cb.setNom(nom);
        cb.setSolde(solde);
        return g.update(cb);
    }

    @GET
    @Path("deposer/{id}/{montant}")
    @Produces({"application/xml", "application/json"})
    public CompteBancaire deposer(@PathParam("id") Long id, @PathParam("montant") Long montant){
        CompteBancaire cb = g.getCompte(id);
        if(cb == null){
            return null;
        }
        cb.deposer(montant);
        return g.update(cb);
    }
    
    
    @GET
    @Path("retirer/{id}/{montant}")
    @Produces({"application/xml", "application/json"})
    public CompteBancaire retirer(@PathParam("id") Long id, @PathParam("montant") Long montant){
        CompteBancaire cb = g.getCompte(id);
        if(cb == null){
            return null;
        }
        cb.retirer(montant);
        return g.update(cb);
    }
    
    
    @GET
    @Path("transferer/{id_from}/{id_to}/{montant}")
    public void transferer(@PathParam("id_from") Long id_from, 
                            @PathParam("id_to") Long id_to, 
                            @PathParam("montant") Long montant){
        g.transfert(id_from, id_to, montant);
        //TODO : renvoyer quelque chose
    }
    
    
    
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        g.delete(g.getCompte(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public CompteBancaire find(@PathParam("id") Long id) {
        return g.getCompte(id);
    }
    
    @GET
    @Path("operations/{id}")
    @Produces({"application/xml", "application/json"})
    public Collection<OperationBancaire> getOperations(@PathParam("id") Long id) {
        return g.getCompte(id).getOperations();
    }
    

    @GET
    @Produces({"application/xml", "application/json"})
    public List<CompteBancaire> findAll() {
        return g.getAllComptes();
    }

    @GET
    @Path("{from}/{limit}")
    @Produces({"application/xml", "application/json"})
    public List<CompteBancaire> findRange(@PathParam("from") Integer from, @PathParam("limit") Integer limit) {
        return g.getLazyComptes(from, limit);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public int countREST() {
        return g.getNBComptes();
    }
    
}
