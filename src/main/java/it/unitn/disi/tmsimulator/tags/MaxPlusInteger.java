/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.tags;

/**
 * Rappresenta un tag di tipo Integer avente come operazione di concatenazione la
 * somma e come ordine parziale (totale) il classico ordinamento tra numeri.
 * 
 * @author davide
 */
public final class MaxPlusInteger extends Tag {
    public static final MaxPlusInteger EPSILON = new MaxPlusInteger(Integer.MIN_VALUE);
    public static final MaxPlusInteger IDENTITY = new MaxPlusInteger(0);
    
    private Integer tag;
    
    public MaxPlusInteger(Integer t){
        this.tag = t;
    }

    public MaxPlusInteger() {
    }

    public Integer getTag() {
        return tag;
    }
    
    /**
     * {@inheritDoc}.
     * Corrisponde all'operazione di somma.
     * Richiede un tag di tipo {@link it.unitn.disi.tmsimulator.tags.MaxPlusInteger}.
     * 
     * @param other
     * @return
     * @throws Exception 
     */
    @Override
    public Tag concatenate(Tag other) throws Exception {
        if(other == null)
            throw new Exception("other = null"); // TODO
        if(!(other instanceof MaxPlusInteger)){
            throw new Exception("la concatenazione richiede due tag dello stesso tipo");
        }

        MaxPlusInteger t = new MaxPlusInteger(this.tag + ((MaxPlusInteger) other).tag);
        if(this.isEpsilon() || other.isEpsilon())
            t = EPSILON;
        
        return t;
    }

    /**
     * Esegue l'operazione &gt; tra tag in base al classico ordinamento dei numeri.
     * 
     * @param other
     * @return
     * @throws Exception 
     */
    @Override
    public boolean gt(Tag other) throws Exception {
        if(!(other instanceof MaxPlusInteger)){
            throw new Exception("la comparazione richiede due tag dello stesso tipo");
        }
        
        return this.tag > ((MaxPlusInteger) other).tag;
    }

    @Override
    public boolean isEpsilon() {
        return this.tag.equals(EPSILON.tag);
    }

    @Override
    public Tag getIdentity() {
        return IDENTITY;
    }

    @Override
    public Tag getEpsilon() {
        return EPSILON;
    }

    @Override
    public boolean equals(Tag other) {
        if(other == null || !(other instanceof MaxPlusInteger))
            return false;
        
        return this.tag.equals(((MaxPlusInteger)other).getTag());
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

}
