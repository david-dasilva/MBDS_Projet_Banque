package session;

import entities.CompteBancaire;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
     */
    public void creerCompte(CompteBancaire cb){
        em.persist(cb);
    }
    
    
    /**
     * Création de comptes de tests
     */
    public void creerComptesTest(){
        //TODO : Augmenter significativement le nombre de comptes
        // via GenerateData.com
        
        System.out.println("### Génération des comptes de test...");
        creerCompte(new CompteBancaire("Conan Peck",37996));
        creerCompte(new CompteBancaire("Winter Pacheco",3511));
        creerCompte(new CompteBancaire("Kalia Lynch",91610));
        creerCompte(new CompteBancaire("Samson Abbott",28538));
        creerCompte(new CompteBancaire("Ruth Dickerson",19793));
        creerCompte(new CompteBancaire("Aiko Noel",27856));
        creerCompte(new CompteBancaire("Damian Merritt",84805));
        creerCompte(new CompteBancaire("Christopher Nunez",88941));
        creerCompte(new CompteBancaire("Nero Morris",50137));
        creerCompte(new CompteBancaire("Susan Berry",36967));
        creerCompte(new CompteBancaire("Denton Horn",75318));
        creerCompte(new CompteBancaire("Cooper Fletcher",44787));
        creerCompte(new CompteBancaire("Kellie Cantu",5313));
        creerCompte(new CompteBancaire("Laura Ford",56589));
        creerCompte(new CompteBancaire("Sloane Harper",79109));
        creerCompte(new CompteBancaire("Mollie Bonner",50722));
        creerCompte(new CompteBancaire("Beverly Morales",5985));
        creerCompte(new CompteBancaire("Rama Mejia",76454));
        creerCompte(new CompteBancaire("Chastity Mcmillan",94982));
        creerCompte(new CompteBancaire("Lyle Allen",22949));
        creerCompte(new CompteBancaire("Shana Miranda",96343));
        creerCompte(new CompteBancaire("Morgan Bond",20033));
        creerCompte(new CompteBancaire("Dean Strong",15578));
        creerCompte(new CompteBancaire("Portia Watts",47738));
        creerCompte(new CompteBancaire("Keaton Moore",32371));
        creerCompte(new CompteBancaire("Akeem Rodriquez",72360));
        creerCompte(new CompteBancaire("Ava Dillard",34952));
        creerCompte(new CompteBancaire("Austin Melendez",73132));
        creerCompte(new CompteBancaire("Hannah Murray",16050));
        creerCompte(new CompteBancaire("Tucker Donaldson",19887));
        creerCompte(new CompteBancaire("Amethyst Mcgee",80642));
        creerCompte(new CompteBancaire("Frances Carter",70577));
        creerCompte(new CompteBancaire("Dalton Browning",98954));
        creerCompte(new CompteBancaire("Quamar Higgins",87499));
        creerCompte(new CompteBancaire("Norman Hall",43581));
        creerCompte(new CompteBancaire("Indigo Marshall",40667));
        creerCompte(new CompteBancaire("Adam Buckner",79809));
        creerCompte(new CompteBancaire("Marshall Dunn",17341));
        creerCompte(new CompteBancaire("Zia Heath",71978));
        creerCompte(new CompteBancaire("Clinton Lawrence",23194));
        creerCompte(new CompteBancaire("Amaya Dawson",94899));
        creerCompte(new CompteBancaire("Eve Burton",82102));
        creerCompte(new CompteBancaire("Jasmine Harper",26827));
        creerCompte(new CompteBancaire("Palmer Sims",25023));
        creerCompte(new CompteBancaire("Amethyst Mullen",28825));
        creerCompte(new CompteBancaire("Beatrice Bradshaw",30075));
        creerCompte(new CompteBancaire("Davis Higgins",71776));
        creerCompte(new CompteBancaire("Darryl Jones",42867));
        creerCompte(new CompteBancaire("Keelie Alford",18760));
        creerCompte(new CompteBancaire("Teegan Donovan",57178));
        creerCompte(new CompteBancaire("Haviva George",78251));
        creerCompte(new CompteBancaire("Robert Hartman",61601));
        creerCompte(new CompteBancaire("Ivy Huffman",43724));
        creerCompte(new CompteBancaire("Matthew House",90795));
        creerCompte(new CompteBancaire("Simon Soto",45906));
        creerCompte(new CompteBancaire("Xaviera Torres",61553));
        creerCompte(new CompteBancaire("Barrett Farrell",38101));
        creerCompte(new CompteBancaire("Alexandra Maddox",76748));
        creerCompte(new CompteBancaire("Merritt Richard",90057));
        creerCompte(new CompteBancaire("Hillary Pennington",52636));
        creerCompte(new CompteBancaire("Idona Kim",51054));
        creerCompte(new CompteBancaire("Robin Nicholson",2255));
        creerCompte(new CompteBancaire("Uma Beck",71684));
        creerCompte(new CompteBancaire("Calista Howell",30679));
        creerCompte(new CompteBancaire("Armand Baxter",14323));
        creerCompte(new CompteBancaire("Neil Huff",43406));
        creerCompte(new CompteBancaire("Hasad Kline",54325));
        creerCompte(new CompteBancaire("Sharon Sharpe",5508));
        creerCompte(new CompteBancaire("Wynne Kirby",5753));
        creerCompte(new CompteBancaire("Eaton Schmidt",76467));
        creerCompte(new CompteBancaire("Gray Doyle",93439));
        creerCompte(new CompteBancaire("Oliver Hudson",84990));
        creerCompte(new CompteBancaire("Keiko Ratliff",42879));
        creerCompte(new CompteBancaire("Adele Cooke",18833));
        creerCompte(new CompteBancaire("Kaseem Herrera",29682));
        creerCompte(new CompteBancaire("Tyler Walls",57986));
        creerCompte(new CompteBancaire("Trevor Pierce",87564));
        creerCompte(new CompteBancaire("Lisandra Sharp",79391));
        creerCompte(new CompteBancaire("Raphael Ray",7730));
        creerCompte(new CompteBancaire("Edan Singleton",38916));
        creerCompte(new CompteBancaire("Rigel Wise",86377));
        creerCompte(new CompteBancaire("Erich Colon",17292));
        creerCompte(new CompteBancaire("Joelle Case",89819));
        creerCompte(new CompteBancaire("Alexander Howell",80601));
        creerCompte(new CompteBancaire("May Franklin",5037));
        creerCompte(new CompteBancaire("Dorian Arnold",18265));
        creerCompte(new CompteBancaire("Kylan Zamora",91979));
        creerCompte(new CompteBancaire("Jemima Boyer",17994));
        creerCompte(new CompteBancaire("Peter Joseph",39300));
        creerCompte(new CompteBancaire("Elizabeth England",73770));
        creerCompte(new CompteBancaire("Tashya Cox",23615));
        creerCompte(new CompteBancaire("Clayton Sampson",66037));
        creerCompte(new CompteBancaire("Vera Sampson",64969));
        creerCompte(new CompteBancaire("Marshall Perez",4266));
        creerCompte(new CompteBancaire("Kyra Vincent",8999));
        creerCompte(new CompteBancaire("Lael Guerra",35388));
        creerCompte(new CompteBancaire("Timothy Stein",15405));
        creerCompte(new CompteBancaire("Kuame Hatfield",76385));
        creerCompte(new CompteBancaire("Alexa Mcconnell",61284));
        creerCompte(new CompteBancaire("Shana Oconnor",83527));

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
        Query q = em.createQuery("select c from CompteBancaire c order by c.nom");
        return q.getResultList();
    }
    
    
    /**
     * Recherche de comptes bancaires sur le nom
     * @param search
     * @return liste de compte bancaire
     */
    public List<CompteBancaire> findComptes(String search) {
        Query q = em.createQuery("select c from CompteBancaire c where lower(c.nom) like :search");
        q.setParameter("search", "%" + search + "%");
        
        return q.getResultList();
    }
    
    /**
     * Retourne une partie des comptes bancaires pour la pagination
     * @param start
     * @param limit
     * @return liste de comptes bancaires
     */
    public List<CompteBancaire> getComptes(int start, int limit) { 
        Query q = em.createNamedQuery("SELECT * FROM CompteBancaire");
        q.setFirstResult(start);
        q.setMaxResults(limit);
        
        return q.getResultList();
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
    
    /**
     * Enregistre un objet
     * @param object 
     */
    public void persist(Object object) {
        em.persist(object);
    }
    
    /**
     * Supprime un compte bancaire
     * @param c 
     */
    public void delete(CompteBancaire c) {
        em.createQuery("delete from CompteBancaire c where c.id=" + c.getId()).executeUpdate();
    }
}
