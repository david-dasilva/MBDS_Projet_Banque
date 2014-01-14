/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author shepard
 */
@Entity
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nom;
    private String login;
    private String password;
    
    /**
     * Liste de comptes benéficiaires.
     * Ici, j'ai choisi de ne garder qu'un id des CompteBancaire afin de ne pas rajouter une relation ManyToMany.
     * Le compte est retrouvé via un simple g.getCompte(id).
     * Le String est le label du bénéficiaire, choisi par le client. Ex : "Propriétaire", "Assurance auto" etc
     * 
     */
    private HashMap<Long,String> beneficiaires;
    
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<CompteBancaire> comptes;
    
    
    public Client() {}
    
    public Client(String nom, String login, String password){
        this.nom = nom;
        this.login = login;
        this.setPassword(password);
        this.comptes = new ArrayList();
        this.beneficiaires = new HashMap<>();
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //TODO : convertir en SHA1 ou MD5
        this.password = password;
    }

    public List<CompteBancaire> getComptes() {
        return comptes;
    }

    public void setComptes(List<CompteBancaire> comptes) {
        this.comptes = comptes;
    }

    public void addCompte(CompteBancaire c){
        this.comptes.add(c);
    }
    
    public void removeCompte(CompteBancaire c){
        this.comptes.remove(c);
    }

    public HashMap<Long, String> getBeneficiaires() {
        return beneficiaires;
    }

    public void setBeneficiaires(HashMap<Long, String> beneficiaires) {
        this.beneficiaires = beneficiaires;
    }
    
    public void addBeneficiaire(Long idCompte, String label){
        this.beneficiaires.put(idCompte, label);
    }
    
    /**
     * Supprime le beneficiaire.
     * @param idCompte
     * @return le label du beneficiaire supprimé si trouvé, null sinon.
     */
    public String removeBeneficiaire(Long idCompte){
        return this.beneficiaires.remove(idCompte);
    }
    
    /**
     * Modifie le nom d'un bénéficiaire.
     * @param idCompte
     * @param nouveaulabel
     * @return ancien label si Ok, null sinon.
     */
    public String editBeneficiaire(Long idCompte, String nouveaulabel){
        return this.beneficiaires.put(idCompte, nouveaulabel);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Client[ id=" + id + " ]";
    }
    
}
