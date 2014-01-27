/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Ce filtre sert a remplacer l'ancienne configuration de CORS.
 * Sans lui, les requetes PUT et DELETE ne sont pas acceptées.
 * @author David Da Silva
 */
@WebFilter(urlPatterns = {"/*"}, description = "Un filtre qui ne fait rien de particulier.")
public class FiltrePermissif implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
        // Ne rien faire
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletResponse res = (HttpServletResponse) response;
        //Dire qu'on accepte des requetes de n'importe quel domaine sur les ressources gérés par la servlet
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, UPDATE, OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Ne rien faire
    }
    
}
