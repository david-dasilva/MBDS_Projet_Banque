package session;

import entities.Client;
import entities.CompteBancaire;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.ListeDonneesDeTest;

/**
 * Facade CRUD sur Comptes Bancaires
 * @author David Da Silva
 */
@Stateless
public class GestionnaireDeComptesBancaires {
    @PersistenceContext()
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public GestionnaireDeComptesBancaires() {
    }
    
    /**
     * Création d'un compte bancaire
     * @param cb 
     * @return l'id du compte bancaire crée
     */
    public long creerCompte(CompteBancaire cb){
        em.persist(cb);
        return cb.getId();
    }
    
    /**
     * Création d'un compte client (sans comptes bancaires)
     * @param c
     * @return l'id du compte client crée
     */
    public long creerClient(Client c){
        em.persist(c);
        return c.getId();
    }
    
    
    /**
     * Création de comptes de tests
     */
    public void creerComptesTest(){
        //TODO : Augmenter significativement le nombre de comptes
        // via GenerateData.com
        
        System.out.println("### Génération des comptes de test...");
        

        
        LinkedHashMap<String, Integer> liste = new ListeDonneesDeTest().getListe();
        
        // quelques comptes pour le fun
        Client c1 = new Client("* Anonymous United! *", "p@s$w0rd");
        c1.addCompte(new CompteBancaire(c1,"Anonymous's looting",77777));
        c1.addCompte(new CompteBancaire(c1,"Anonymous's savings",1337000));
        creerClient(c1);
        
        Client c2 = new Client("Hadopi", "gné?");
        c2.addCompte(new CompteBancaire(c2,"Hadopi - compte courant",-200000));
        creerClient(c2);
        
        Client c3 = new Client("Wikileaks", "helloFBI");
        c3.addCompte(new CompteBancaire(c3,"Wikileaks - Nouveau compte pas encore bloqué!",500));
        creerClient(c3);
        
        Client c4 = new Client("MPAA", "money");
        c4.addCompte(new CompteBancaire(c4,"MPAA - Lobbying funds",5000000));
        creerClient(c4);
        
        
        Client c5 = new Client("Scientology church", "hacked");
        c5.addCompte(new CompteBancaire(c5,"Scientology - Thx 4 the $$$ - Anonymous",-5000000));
        creerClient(c5);
  
        // reste des comptes randoms
        for(String nom : liste.keySet()){
            Integer solde = liste.get(nom);
            Client c = new Client(nom, "password");
            c.addCompte(new CompteBancaire(c,nom+" - compte courant", solde));
            creerClient(c);
        }
        

        System.out.println("### ... Génération terminée");
    }

    
    /**
     * Effectue un transfert.
     * Tout se passe dans la même transaction
     * @param id1
     * @param id2
     * @param montant 
     */
    public void transfert(long id1, long id2, double montant) {
        CompteBancaire c1 = em.find(CompteBancaire.class, id1);
        CompteBancaire c2 = em.find(CompteBancaire.class, id2);
        c1.retirer(montant);
        c2.deposer(montant);
    }
    
    /**
     * retourne tous les comptes bancaires
     * @return liste de comptes bancaires
     */
    public List<CompteBancaire> getAllComptes() {
        Query q = em.createQuery("select c from CompteBancaire c order by c.id");
        return q.getResultList();
    }
    
    public List<Client> getAllClients(){
        Query q = em.createQuery("select c from Client c order by c.id");
        return q.getResultList();
    }
   
    /**
     * Recherche de comptes bancaires sur le nom
     * @param search
     * @return liste de compte bancaire
     */
    public List<CompteBancaire> findComptes(String search) {
        Query q = em.createQuery("select c from CompteBancaire c where lower(c.nom) like lower(:search)");
        q.setParameter("search", "%" + search + "%");
        
        return q.getResultList();
    }
    
    public List<Client> findClients(String search) {
        Query q = em.createQuery("select c from Client c where lower(c.nom) like lower(:search)");
        q.setParameter("search", "%" + search + "%");
        
        return q.getResultList();
    }
    
    public List<Client> getLazyClients(int start, int limit){
        Query q = em.createQuery("select c from Client c order by c.id");
        q.setFirstResult(start);
        q.setMaxResults(limit);
        return q.getResultList();
    }
    
    public int getNBClients(){
        Query q = em.createQuery("Select count(c) from Client c");
        return ((Long) q.getSingleResult()).intValue();
    }
    public Client getClient(long id){
        return em.find(Client.class, id);
    }
    
    /**
     * Retourne une partie des comptes bancaires pour la pagination
     * @param start
     * @param limit
     * @return liste de comptes bancaires
     */
    public List<CompteBancaire> getLazyComptes(int start, int limit) { 
        Query q = em.createQuery("select c from CompteBancaire c order by c.id");
        q.setFirstResult(start);
        q.setMaxResults(limit);
        System.out.println("LazyLoading comptes de "+ start+" a "+limit);
        return q.getResultList();
    }
    
    public int getNBComptes(){
        Query query = em.createQuery("SELECT COUNT(c) FROM CompteBancaire c");
        return ((Long) query.getSingleResult()).intValue();
    }
    
    
    /**
     * Retourne un compte bancaire par son ID
     * @param id
     * @return le compte bancaire, connecté
     */
    public CompteBancaire getCompte(long id){
        return em.find(CompteBancaire.class, id);
    }
    
    /**
     * Met a jour un compte bancaire
     * @param c
     * @return le compte bancaire modifié, connecté
     */
    public CompteBancaire update(CompteBancaire c) {
        return em.merge(c);
    }
    
    public Client update(Client c){
        return em.merge(c);
    }
    
    /**
     * Enregistre un objet
     * @param object 
     */
    public void persist(Object object) {
        em.persist(object);
    }
    
    public void delete(Client c){
        em.createQuery("delete from client c where c.id="+c.getId()).executeUpdate();
    }
    
    /**
     * Supprime un compte bancaire
     * @param c 
     */
    public void delete(CompteBancaire c) {
        em.createQuery("delete from CompteBancaire c where c.id=" + c.getId()).executeUpdate();
    }
}
