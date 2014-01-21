package service;


import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.xml.wss.impl.misc.Base64;
import entities.Client;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.GestionnaireDeComptesBancaires;

/**
 *
 * @author David Da Silva
 */
@Path("client")
public class ClientFacadeREST {
    @EJB
    private GestionnaireDeComptesBancaires g;
    
    public static final String FIELD_ID = "idClient";
    public static final String FIELD_LOGIN = "login";
    public static final String FIELD_NOM = "nom";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_BENEF_ID = "idBeneficiaire";
    public static final String FIELD_BENEF_LABEL = "labelBeneficiaire";

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long create(@FormParam(FIELD_NOM) String nom, @FormParam(FIELD_LOGIN) String login, @FormParam(FIELD_PASSWORD) String password) {
         
        Client client = new Client(nom, login,password);
        return g.creerClient(client);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Client edit(@FormParam(FIELD_ID) long id,@FormParam(FIELD_NOM) String nom, @FormParam(FIELD_LOGIN) String login, @FormParam(FIELD_PASSWORD) String password) {
        Client client = g.getClient(id);
        if(client == null){
            return null;
        }
        client.setNom(nom);
        client.setLogin(login);
        client.setPassword(password);
        return g.update(client);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        g.delete(g.getClient(id));
    }

    
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long login(@FormParam(FIELD_LOGIN) String login, @FormParam(FIELD_PASSWORD) String password){
        System.out.println("WEBSERVICE : Login "+login+":"+password);
        if(login ==null || password ==null)
            return 0;
        Client c = g.loginAlreadyHashed(login, password);
        if(c!=null){
            System.out.println("Identifiants ok pour client "+c.toString());
          return c.getId();
        } else{
            System.out.println("Identifiants incorrects");
            return 0;
        }
    }
    
    
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Client find(@Context HttpServletRequest req, @PathParam("id") Long id) {
        
        if(canAccess(getLoggedUserId(req), id))
            return g.getClient(id);
        else{
            System.err.println("L'utilisateur ayant l'id "+ getLoggedUserId(req)+ 
                    " n'est pas autorisé a consulter des ressource de l'utilisateur "+id);
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
        }
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

    
    @POST
    @Path("benef")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Client addBeneficiaire(@FormParam(FIELD_ID) long id, @FormParam(FIELD_BENEF_ID) long idCompte, @FormParam(FIELD_BENEF_LABEL) String label){
        Client c = g.getClient(id);
        if (c!=null){
            c.addBeneficiaire(idCompte, label);
            return c;
        }
        return null;
    }
    
    @PUT
    @Path("benef")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public String editBeneficiaire(@FormParam(FIELD_ID) long id, @FormParam(FIELD_BENEF_ID) long idCompte, @FormParam(FIELD_BENEF_LABEL) String label){
        Client c = g.getClient(id);
        if (c!=null){
            return c.editBeneficiaire(idCompte, label);
        }
        return null;
    }
    
    @DELETE
    @Path("benef/{id}/{idCompte}")
    @Produces({"application/xml", "application/json"})
    public String removeBeneficiaire(@PathParam("id") long id, @PathParam("idCompte")long idCompte){
        Client c = g.getClient(id);
        if (c!=null){
            return c.removeBeneficiaire(idCompte);
        }
        return null;
    }
    
    
    /**
     * Cette methode extrait le login et password du header HTTP Authorization
     * et renvoie l'id de l'utilisateur
     * @param req
     * @return l'id de l'utilisateur si identifiants corrects, 0 sinon
     */
    private long getLoggedUserId(HttpServletRequest req){
        try {
            String AuthLine = req.getHeader("authorization");
            if(AuthLine == null)
                return 0;
            String base64Credentials = AuthLine.substring("Basic".length()).trim();
            String credentials = new String(Base64.decode(base64Credentials), Charset.forName("UTF-8"));
            // credentials = username:password
            String[] values = credentials.split(":", 2);
            
            Client c = g.login(values[0], values[1]);
            if(c != null)
                return c.getId();
            return 0;

        } catch (Base64DecodingException ex) {
            Logger.getLogger(ClientFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    
    private boolean canAccess(long idUser, long idAsked){
        
        if(idUser > 0){
            if(idUser == 1 || (idUser == idAsked))
                return true;
        }
        return false;
    }
    
    

}
