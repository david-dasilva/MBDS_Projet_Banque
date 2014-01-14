/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity bean Compte Bancaire
 * Le code metier d'un compte bancaire
 * @author David Da Silva
 */
@Entity
public class CompteBancaire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double solde;
    private String nom;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private transient Collection<OperationBancaire> operations = new ArrayList<>();

    public CompteBancaire() {
    }

    public CompteBancaire(String nom, double solde) {
        this.solde = solde;
        this.nom = nom;
        this.operations.add(new OperationBancaire("Création du compte", solde));
    }

    
    
    /*
     * Getters & Setters
     */

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }
    
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    

    public Collection<OperationBancaire> getOperations(){
        return this.operations;
    }
    
    
    
    /*
     * Code metier
     */
    
    /**
     * Deposer de l'argent
     * @param montant
     * @return le nouveau solde du compte
     */
    public double deposer(double montant){
        solde += montant;
        this.operations.add(new OperationBancaire("Crédit", montant));
        return solde;
    }
    
    /**
     * Retirer de l'argent
     * @param montant
     */
    public double retirer(double montant) {
        
        solde -= montant;
        this.operations.add(new OperationBancaire("Débit", montant));
        return solde;

    }
    
    
    
    
    
    
    
    
    /*
     * Overrides
     */
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompteBancaire)) {
            return false;
        }
        CompteBancaire other = (CompteBancaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CompteBancaire[ id=" + id + " ]";
    }
    
}
