package service;


import entities.Client;
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
import javax.ws.rs.core.MultivaluedMap;
import session.GestionnaireDeComptesBancaires;

/**
 *
 * @author David Da Silva
 */
@Path("client")
public class ClientFacadeREST {
    @EJB
    private GestionnaireDeComptesBancaires g;
    
    public static final String FIELD_NOM = "nom";
    public static final String FIELD_PASSWORD = "password";
    

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long create(@FormParam(FIELD_NOM) String nom, @FormParam(FIELD_PASSWORD) String password) {
        System.out.println("Je suis dans le create du Web Service client");
        System.out.println("nom :"+nom+" pass :"+password);
        
        Client client = new Client(nom, password);
        return g.creerClient(client);
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public Client edit(Client entity) {
        return g.update(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        g.delete(g.getClient(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Client find(@PathParam("id") Long id) {
        return g.getClient(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Client> findAll() {
        return g.getAllClients();
    }

    @GET
    @Path("{from}/{limit}")
    @Produces({"application/xml", "application/json"})
    public List<Client> findRange(@PathParam("from") Integer from, @PathParam("limit") Integer limit) {
        return g.getLazyClients(from, limit);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public int countREST() {
        return g.getNBClients();
    }


}