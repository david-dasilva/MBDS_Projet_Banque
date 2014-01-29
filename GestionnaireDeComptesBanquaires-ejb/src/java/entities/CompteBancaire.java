/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    private ArrayList<OperationBancaire> operations = new ArrayList<>();

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
        System.out.println("Je ne renvoie pas d'opérationsBancaires");
        return Collections.EMPTY_LIST;
    }
    
    public ArrayList<OperationBancaire> getAllOperations(){
        return operationsBancaires();
    }
    
    public ArrayList<OperationBancaire> operationsBancaires(){
        System.out.println("Je renvoie les opérationsBancaires, il y en a "+this.operations.size());
        return this.operations;
    }
    
    public void setOperations(ArrayList<OperationBancaire> operations){
        this.operations = operations;
    }
    
    
    
    /*
     * Code metier
     */
    
    /**
     * Deposer de l'argent
     * @param montant
     * @param description
     * @return le nouveau solde du compte
     */
    public double deposer(String description, double montant){
        if(description.isEmpty()) description = "Crédit";
        solde += montant;
        this.operations.add(new OperationBancaire(description, montant));
        return solde;
    }
    
    public double deposer(double montant){
        return deposer("Crédit",montant);
    }
    
    
    
    /**
     * Retirer de l'argent
     * @param montant
     * @param description
     */
    public double retirer(String description, double montant){
        if(description.isEmpty()) description = "Débit";
        solde -= montant;
        this.operations.add(new OperationBancaire(description, -montant));
        return solde;
    }
    
    
    public double retirer(double montant) {
        return retirer("Débit", montant);
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
