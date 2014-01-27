package service;


import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.xml.wss.impl.misc.Base64;
import entities.Client;
import entities.CompteBancaire;
import entities.OperationBancaire;
import java.nio.charset.Charset;
import java.util.Collection;
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
 * Façade REST aux opérations sur les CompteBancaire.
 * La plupart des méthodes ont un accès restreint :
 * On ne peut modifier que ses propres données, sauf si on est admin.
 * @author David Da Silva
 */
@Path("compte")
public class CompteBancaireFacadeREST {

    @EJB
    private GestionnaireDeComptesBancaires g;
    
    public static final String  FIELD_ID_CLIENT = "idClient";
    public static final String  FIELD_ID_COMPTE = "idCompte";
    public static final String  FIELD_NOM = "nom";
    public static final String  FIELD_SOLDE = "solde";
    public static final long    ID_ADMIN = 1;
    public static final long    ACCESS_ALL = 0;
    
    
    /**
     * Créer un compte bancaire pour un client.
     * Accessible par tout le monde.
     * @param idClient
     * @param nom
     * @param solde
     * @return l'id du compte bancaire crée
     */
    @POST
    @NecessiteBasicAuth
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public long create(@FormParam(FIELD_ID_CLIENT) long idClient,
                    @FormParam(FIELD_NOM) String nom, 
                    @FormParam(FIELD_SOLDE) long solde) {
        
        Client client = g.getClient(idClient);
        if(client == null){
            return 0;
        }
        CompteBancaire cb = new CompteBancaire(nom, solde);
        client.addCompte(cb);
        g.update(client);
        return g.creerCompte(cb);
    }

    
    /**
     * Modification d'un compte bancaire d'un client.
     * Accès restreint.
     * @param req
     * @param idCompte
     * @param nom
     * @param solde
     * @return le compte bancaire mis à jour.
     */
    @PUT
    @NecessiteBasicAuth
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED,"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public CompteBancaire edit(@Context HttpServletRequest req, 
                            @FormParam(FIELD_ID_COMPTE) long idCompte, 
                            @FormParam(FIELD_NOM) String nom, 
                            @FormParam(FIELD_SOLDE) long solde) {
        
        Client loggedUser = getLoggedUser(req);
        
        if(canAccess(loggedUser, idCompte)){

            CompteBancaire cb = g.getCompte(idCompte);
            if(cb == null){
                return null;
            }
            cb.setNom(nom);
            cb.setSolde(solde);
            return g.update(cb);
        } else {
            error403(loggedUser, idCompte);
            return null;
        }
    }
    
    /**
     * Supprimer un compte bancaire.
     * Accès restreint.
     * @param req
     * @param id 
     */
    @DELETE
    @NecessiteBasicAuth
    @Path("{id}")
    public void remove(@Context HttpServletRequest req, @PathParam("id") Long id) {
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, id)){
            g.delete(loggedUser.getId(),g.getCompte(id));
        } else {
            error403(loggedUser, id);
        }
    }

    /**
     * Déposer de l'argent sur un compte bancaire.
     * Il suffit de dire "je veux déposer 500€" pour que 500€ apparaissent
     * sur le compte. Magie! :-P
     * Accès restreint.
     * @param req
     * @param id
     * @param montant
     * @return le compte bancaire mis à jour
     */
    @GET
    @NecessiteBasicAuth
    @Path("deposer/{id}/{montant}")
    @Produces({"application/xml", "application/json"})
    public CompteBancaire deposer(@Context HttpServletRequest req, @PathParam("id") Long id, @PathParam("montant") Long montant){
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, id)){
            CompteBancaire cb = g.getCompte(id);
            if(cb == null){
                return null;
            }
            cb.deposer(montant);
            return g.update(cb);
        } else {
            error403(loggedUser, id);
            return null;
        }
    }
    
    /**
     * Retirer de l'argent sur un compte bancaire.
     * Attention, si vous dites "je veux retirer 500€", l'argent disparait de
     * votre compte mais vous n'obtenez pas de billets. Disparition! :-P
     * Accès restreint.
     * @param req
     * @param id
     * @param montant
     * @return le compte bancaire mis à jour
     */
    @GET
    @NecessiteBasicAuth
    @Path("retirer/{id}/{montant}")
    @Produces({"application/xml", "application/json"})
    public CompteBancaire retirer(@Context HttpServletRequest req, @PathParam("id") Long id, @PathParam("montant") Long montant){
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, id)){
            CompteBancaire cb = g.getCompte(id);
            if(cb == null){
                return null;
            }
            cb.retirer(montant);
            return g.update(cb);
        } else {
            error403(loggedUser, id);
            return null;
        }
    }
    
    /**
     * Transferer une somme d'argent d'un compte à un autre.
     * Pas de contrainte sur le destinataire, idéalement il devrait être dans 
     * la liste des bénéficiaires.
     * Accès restreint.
     * @param req
     * @param id_from
     * @param id_to
     * @param montant
     * @return le solde du compte emeteur après transfert ou 0 si erreur.
     */
    @GET
    @NecessiteBasicAuth
    @Path("transferer/{id_from}/{id_to}/{montant}")
    public double transferer(@Context HttpServletRequest req, 
                            @PathParam("id_from") Long id_from, 
                            @PathParam("id_to") Long id_to, 
                            @PathParam("montant") Long montant){
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, id_from)){
            if (g.transfert(id_from, id_to, montant))
                return g.getCompte(id_from).getSolde();
            return 0;
        } else {
            error403(loggedUser, id_from);
            return 0;
        }
    }
    
    
    
    
    /**
     * Trouver un compte bancaire par son id.
     * Accès restreint.
     * @param req
     * @param id
     * @return le compte bancaire trouvé, null sinon.
     */
    @GET
    @NecessiteBasicAuth
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public CompteBancaire find(@Context HttpServletRequest req, 
                                @PathParam("id") Long id) {
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, id)){
            return g.getCompte(id);
        } else {
            error403(loggedUser, id);
            return null;
        }
    }
    
    /**
     * Trouver tous les comptes bancaires.
     * Admin only
     * @param req
     * @return une liste d'objets CompteBancaire
     */
    @GET
    @NecessiteBasicAuth
    @Produces({"application/xml", "application/json"})
    public List<CompteBancaire> findAll(@Context HttpServletRequest req) {
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, ACCESS_ALL)){
            return g.getAllComptes();
        } else {
            error403(loggedUser, ACCESS_ALL);
            return null;
        }
    }

    /**
     * Obtenir les comptes bancaires de {from} à {limit}.
     * Admin Only
     * @param req
     * @param from
     * @param limit
     * @return une liste d'objets CompteBancaire
     */
    @GET
    @NecessiteBasicAuth
    @Path("{from}/{limit}")
    @Produces({"application/xml", "application/json"})
    public List<CompteBancaire> findRange(@Context HttpServletRequest req, @PathParam("from") Integer from, @PathParam("limit") Integer limit) {
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, ACCESS_ALL)){
            return g.getLazyComptes(from, limit);
        } else {
            error403(loggedUser, ACCESS_ALL);
            return null;
        }
    }
    
    /**
     * Obtenir la liste des opérations bancaires sur un compte.
     * Afin de ne pas alourdir les réponses JSON, les opérations ne sont pas fournies
     * par defaut, il faut les demander.
     * Accès restreint.
     * @param req
     * @param id
     * @return une collection d'objets OperationBancaire.
     */
    @GET
    @NecessiteBasicAuth
    @Path("operations/{id}")
    @Produces({"application/xml", "application/json"})
    public Collection<OperationBancaire> getOperations(@Context HttpServletRequest req, 
                                                        @PathParam("id") Long id) {
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, id)){
            return g.getCompte(id).operationsBancaires();
        } else {
            error403(loggedUser, id);
            return null;
        }
    }

    /**
     * Compte le nombre de comptes bancaires.
     * Admin only
     * @param req
     * @return
     */
    @GET
    @NecessiteBasicAuth
    @Path("count")
    @Produces("text/plain")
    public int countREST(@Context HttpServletRequest req) {
        
        Client loggedUser = getLoggedUser(req);
        
        if (canAccess(loggedUser, ACCESS_ALL)){
            return g.getNBComptes();
        } else {
            error403(loggedUser, ACCESS_ALL);
            return 0;
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
            
            Client c = g.loginAlreadyHashed(values[0], values[1]);
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
            
            if(g.peutVoirCompte(idUser, idAsked))
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
