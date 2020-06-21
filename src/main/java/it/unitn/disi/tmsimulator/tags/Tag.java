/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.tags;

/**
 * Rappresenta un tag.
 * Le operazioni devono essere applicate a tag dello stesso tipo.
 * 
 * @author davide
 */
public abstract class Tag {
    
    /**
     * Esegue l'operazione di concatenazione tra tag.
     * 
     * @param other
     * @return
     * @throws Exception 
     */
    public abstract Tag concatenate(Tag other) throws Exception;
    
    /**
     * Esegue l'operazione &gt; tra tag.
     * 
     * @param other
     * @return
     * @throws Exception 
     */
    public abstract boolean gt(Tag other) throws Exception;
    
    /**
     * Verifica se è il tag epsilon.
     * 
     * @return 
     */
    public abstract boolean isEpsilon();
    
    /**
     * Restituisce il tag identità per quel tipo di tag.
     * 
     * @return 
     */
    public abstract Tag getIdentity();
    
    /**
     * Restituisce il tag epsilon per quel tipo di tag.
     * 
     * @return 
     */
    public abstract Tag getEpsilon();

    /**
     * Verifica se due tag sono equivalenti.
     * 
     * @param other
     * @return 
     */
    public abstract boolean equals(Tag other);
    
    /**
     * Rappresentazione come stringa del tag.
     * 
     * @return 
     */
    @Override
    public abstract String toString();
}
