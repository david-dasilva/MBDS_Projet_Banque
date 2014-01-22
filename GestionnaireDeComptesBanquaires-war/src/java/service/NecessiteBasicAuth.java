package service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.ws.rs.NameBinding;

/**
 *
 * @author David Da Silva
 */

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface NecessiteBasicAuth {
    
}
