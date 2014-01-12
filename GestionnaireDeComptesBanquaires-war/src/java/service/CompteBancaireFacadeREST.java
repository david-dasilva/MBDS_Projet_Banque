package service;


import entities.CompteBancaire;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import session.GestionnaireDeComptesBancaires;

/**
 *
 * @author David Da Silva
 */
@Path("compte")
public class CompteBancaireFacadeREST {

    @EJB
    private GestionnaireDeComptesBancaires g;

    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(CompteBancaire entity) {
        g.creerCompte(entity);
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public CompteBancaire edit(CompteBancaire entity) {
        return g.update(entity);
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
