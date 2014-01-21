/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.sun.xml.wss.impl.misc.Base64;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import session.RemoteGestionnaire;

/**
 *
 * @author David Da Silva
 * @source Sebastien Cano
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {
   
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        System.out.println("--- SecurityFilter ---");
        
        String authorization = requestContext.getHeaderString("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            try {
                // Authorization: Basic base64credentials
                String base64Credentials = authorization.substring("Basic".length()).trim();
                String credentials = new String(Base64.decode(base64Credentials),
                        Charset.forName("UTF-8"));
                // credentials = username:password
                String[] values = credentials.split(":", 2);
        
                // Vérification à la con, le temps de trouver comment faire un looking sur EJB pour comparer les credentials
                // de la requete avec ceux de la base de données
                
                InitialContext ctx = new InitialContext();
                RemoteGestionnaire g = (RemoteGestionnaire) ctx.lookup("gestionnaireBancaire");

                if (g.auth(values[0], values[1]))
                    System.out.println("OK pour "+values[0]+ " "+values[1]);
                else {
                    System.out.println("Mauvais identifiants");
                    throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
                                            .entity("Mauvais identifiants").build());
                }
            } catch (Exception ex) {
                System.out.println("Exception dans le SecurityFilter");
                Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
         
        } else {
            System.out.println("Pas de BasicAuth fourni, pas d'accès!");
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
        }
 
    }
 
}
