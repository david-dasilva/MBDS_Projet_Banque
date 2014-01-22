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
 * Facade REST aux opérations sur les clients.
 * La plupart des méthodes ont un accès restreint :
 * On ne peut modifier que ses propres données, sauf si on est admin.
 * @author David Da Silva
 */
@Path("client")
public class ClientFacadeREST {
    @EJB
    private GestionnaireDeComptesBancaires g;
    
    public static final String  FIELD_ID = "idClient";
    public static final String  FIELD_LOGIN = "login";
    public static final String  FIELD_NOM = "nom";
    public static final String  FIELD_PASSWORD = "password";
    public static final String  FIELD_BENEF_ID = "idBeneficiaire";
    public static final String  FIELD_BENEF_LABEL = "labelBeneficiaire";
    public static final long    ID_ADMIN = 1;
    public static final long    ACCESS_ALL = 0;

    
    /**
     * Créer un utilisateur.
     * Accessible par tout le monde
     * @param nom
     * @param login
     * @param password
     * @return l'id du client crée
     */
    @POST
    @NecessiteBasicAuth
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long create(@FormParam(FIELD_NOM) String nom, 
                    @FormParam(FIELD_LOGIN) String login, 
                    @FormParam(FIELD_PASSWORD) String password) {
         
        Client client = new Client(nom, login,password);
        return g.creerClient(client);
    }

    /**
     * Modification d'un client.
     * Accès restreint.
     * @param req
     * @param id
     * @param nom
     * @param login
     * @param password
     * @return le client mis à jour
     */
    @PUT
    @NecessiteBasicAuth
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Client edit(@Context HttpServletRequest req, 
                    @FormParam(FIELD_ID) long id,
                    @FormParam(FIELD_NOM) String nom,
                    @FormParam(FIELD_LOGIN) String login,
                    @FormParam(FIELD_PASSWORD) String password) {
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, id)){

            Client client = g.getClient(id);
            
            if(client == null){
                return null;
            }
            client.setNom(nom);
            client.setLogin(login);
            client.setPassword(password);
            return g.update(client);
        }
        else{
            error403(loggedClient, id);
            return null;
        }
    }

    /**
     * Suppression d'un client.
     * Accès restreint.
     * @param req
     * @param id 
     */
    @DELETE
    @Path("{id}")
    public void remove(@Context HttpServletRequest req,
                    @PathParam("id") Long id) {
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, id))
            g.delete(g.getClient(id));
        else{
            error403(loggedClient, id);
        }
    }

    /**
     * Login d'un client.
     * Cette méthode permet a l'application Client de vérifier les identifiants
     * avant de les utiliser pour chaques requêtes dans le Basic Auth.
     * Accessible par tout le monde.
     * @param login
     * @param password
     * @return l'id du client connecté. 0 en cas d'erreur.
     */
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long login(@FormParam(FIELD_LOGIN) String login, 
                    @FormParam(FIELD_PASSWORD) String password){
        
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
    
    
    /**
     * Trouver un client par son id.
     * Accès restreint.
     * @param req
     * @param id
     * @return le client trouvé, null sinon.
     */
    @GET
    @NecessiteBasicAuth
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Client find(@Context HttpServletRequest req,
                    @PathParam("id") Long id) {
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, id))
            return g.getClient(id);
        else{
            error403(loggedClient, id);
            return null;
        }
    }

    /**
     * Trouver tous les clients de la banque.
     * Admin only.
     * @param req
     * @return une liste d'objets Client
     */
    @GET
    @NecessiteBasicAuth
    @Produces({"application/xml", "application/json"})
    public List<Client> findAll(@Context HttpServletRequest req) {
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, ACCESS_ALL))
            return g.getAllClients();
        else{
            error403(loggedClient, ACCESS_ALL);
            return null;
        }
    }

    /**
     * Obtenir les clients de {from} à {limit}.
     * Admin only.
     * @param req
     * @param from
     * @param limit
     * @return une liste d'objets Client
     */
    @GET
    @NecessiteBasicAuth
    @Path("{from}/{limit}")
    @Produces({"application/xml", "application/json"})
    public List<Client> findRange(@Context HttpServletRequest req,
                                @PathParam("from") Integer from, 
                                @PathParam("limit") Integer limit) {
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, ACCESS_ALL))
            return g.getLazyClients(from, limit);
        else{
            error403(loggedClient, ACCESS_ALL);
            return null;
        }
        
        
    }

    /**
     * Obtenir le nombre de clients de la banque.
     * Admin only.
     * @param req
     * @return 
     */
    @GET
    @NecessiteBasicAuth
    @Path("count")
    @Produces("text/plain")
    public int countREST(@Context HttpServletRequest req) {
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, ACCESS_ALL))
            return g.getNBClients();
        else{
            error403(loggedClient, ACCESS_ALL);
            return 0;
        }
        
        
    }

    
    /**
     * Ajoute un compte bénéficiaire au client.
     * Accès restreint.
     * @param req
     * @param id
     * @param idCompte  compte a ajouter
     * @param label     label du bénéficiaire
     * @return          le client mis à jour
     */
    @POST
    @NecessiteBasicAuth
    @Path("benef")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Client addBeneficiaire(@Context HttpServletRequest req, 
                                @FormParam(FIELD_ID) long id, 
                                @FormParam(FIELD_BENEF_ID) long idCompte, 
                                @FormParam(FIELD_BENEF_LABEL) String label){
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, id)){
            Client c = g.getClient(id);
            if (c!=null){
                c.addBeneficiaire(idCompte, label);
                return c;
            }
            return null;
        }
        else{
            error403(loggedClient, id);
            return null;
        }     
    }
    
    /**
     * Modifier un bénéficiaire.
     * Accès restreint.
     * @param req
     * @param id
     * @param idCompte
     * @param label
     * @return l'ancien label si ok, null sinon
     */
    @PUT
    @NecessiteBasicAuth
    @Path("benef")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public String editBeneficiaire(@Context HttpServletRequest req, 
                                @FormParam(FIELD_ID) long id, 
                                @FormParam(FIELD_BENEF_ID) long idCompte, 
                                @FormParam(FIELD_BENEF_LABEL) String label){
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, id)){
            Client c = g.getClient(id);
            if (c!=null){
                return c.editBeneficiaire(idCompte, label);
            }
            return null;
        } else {
            error403(loggedClient, id);
            return null;
        }
        
        
    }
    
    /**
     * Supprimer un bénéficiaire.
     * Accès restreint.
     * @param req
     * @param id
     * @param idCompte
     * @return le label du bénéficiaire supprimé, null sinon
     */
    @DELETE
    @NecessiteBasicAuth
    @Path("benef/{id}/{idCompte}")
    @Produces({"application/xml", "application/json"})
    public String removeBeneficiaire(@Context HttpServletRequest req, 
                                    @PathParam("id") long id, 
                                    @PathParam("idCompte")long idCompte){
        
        Client loggedClient = getLoggedUser(req);
        
        if(canAccess(loggedClient, id)){
            Client c = g.getClient(id);
            if (c!=null){
                return c.removeBeneficiaire(idCompte);
            }
            return null;
        } else {
            error403(loggedClient, id);
            return null;
        }
    }
    
    
    /**
     * Obtenir un Client depuis les identifiants contenus dans le Basic Auth.
     * Cette méthode extrait le login et password de la ligne Authorization des
     * headers HTTP et tente un login. Si les identifiants sont bon, elle renvoie
     * le Client, sinon null.
     * 
     * @param req la requette HTTP
     * @return l'utilisateur, null sinon.
     */
    private Client getLoggedUser(HttpServletRequest req){
        try {
            String AuthLine = req.getHeader("authorization");
            if(AuthLine == null)
                return null;
            String base64Credentials = AuthLine.substring("Basic".length()).trim();
            String credentials = new String(Base64.decode(base64Credentials), Charset.forName("UTF-8"));
            // credentials = username:password
            String[] values = credentials.split(":", 2);
            
            Client c = g.login(values[0], values[1]);
            if(c != null)
                return c;
            return null;

        } catch (Base64DecodingException ex) {
            Logger.getLogger(ClientFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Vérifier si un utilisateur à le droit d'accèder à la ressource.
     * Un utilisateur ne peux acceder qu'a ses propres ressources, sauf s'il
     * est administrateur.
     * Dans le cas ou on demande d'accèder à tous les utilisateurs, utiliser
     * ACCESS_ALL pour idAsked.
     * @param user
     * @param idAsked
     * @return true si l'utilisateur a le droit d'acceder a la ressource.
     */
    private boolean canAccess(Client user, long idAsked){
        
        if (user != null){
            long idUser = user.getId();
            if(idUser == ID_ADMIN || (idUser == idAsked))
                return true;
        }
        return false;
    }
    
    /**
     * Renvoie une erreur 403.
     * @param client
     * @param idAsked 
     */
    private void error403(Client client, long idAsked){
        long idClient = 0;
        if (client != null) idClient = client.getId();
        
        System.err.println("L'utilisateur ayant l'id "+ idClient+ 
                    " n'est pas autorisé a consulter des ressource de l'utilisateur "+idAsked);
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
    }
    

}
