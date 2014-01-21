/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.xml.wss.impl.misc.Base64;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

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
                if(!values[0].equals("anonymous") || !values[1].equals("p@s$w0rd")) {
                    System.out.println("Mauvais identifiants");
                    throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
                                            .entity("Mauvais identifiants").build());
                }else {
                    System.out.println("OK pour "+values[0]+ " "+values[1]);
                }
            } catch (Base64DecodingException ex) {
                Logger.getLogger(SecurityFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
         
        } else {
            System.out.println("Pas de BasicAuth fourni, pas d'accès!");
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
        }
 
    }
 
}
