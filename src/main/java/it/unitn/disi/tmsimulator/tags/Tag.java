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
     * @param other l'altro tag da concatenare.
     * @return il risultato della concatenzaione.
     * @throws Exception se i due tag non sono della stessa classe.
     */
    public abstract Tag concatenate(Tag other) throws Exception;
    
    /**
     * Esegue l'operazione &gt; tra tag.
     * 
     * @param other l'altro tag su cui applicare l'operazione &gt;.
     * @return true se questo tag è &gt; di quello passato.
     * @throws Exception se i due tag non sono della stessa classe.
     */
    public abstract boolean gt(Tag other) throws Exception;
    
    /**
     * Verifica se corrisponde al tag epsilon.
     * 
     * @return true se corrisponde al tag epsilon, false altrimenti.
     */
    public abstract boolean isEpsilon();
    
    /**
     * Restituisce il tag identità associato alla classe del tag.
     * 
     * @return il tag identità associato alla classe del tag.
     */
    public abstract Tag getIdentity();
    
    /**
     * Restituisce il tag epsilon associato alla classe del tag.
     * 
     * @return il tag epsilon associato alla classe del tag.
     */
    public abstract Tag getEpsilon();

    /**
     * Verifica se i due tag sono uguali.
     * 
     * @param other l'altro tag da confrontare.
     * @return true se i due tag sono uguali, false altrimenti.
     */
    public abstract boolean equals(Tag other);
    
    /**
     * Rappresentazione come stringa del tag.
     * 
     * @return la stringa che rappresenta il tag.
     */
    @Override
    public abstract String toString();
}
