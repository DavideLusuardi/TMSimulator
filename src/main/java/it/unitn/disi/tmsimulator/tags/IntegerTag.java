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
public final class IntegerTag extends Tag {
    /**
     * Tag epsilon. Corrisponde al valore Integer.MIN_VALUE.
     */
    public static final IntegerTag EPSILON = new IntegerTag(Integer.MIN_VALUE);
    
    /**
     * Tag identità. Corrisponde al valore 0.
     */
    public static final IntegerTag IDENTITY = new IntegerTag(0);
    
    /**
     * Valore del tag.
     */
    private Integer tag;
    
    /**
     * Costruisce un tag.
     * @param t il valore del tag.
     */
    public IntegerTag(Integer t){
        this.tag = t;
    }

    /**
     * Costruisce un tag con valore non definito.
     */
    public IntegerTag() {
    }

    /**
     * Restituisce il valore del tag.
     * @return il valore del tag.
     */
    public Integer getTag() {
        return tag;
    }
    
    /**
     * {@inheritDoc}
     * Corrisponde all'operazione di somma.
     * Richiede un tag di tipo {@link it.unitn.disi.tmsimulator.tags.IntegerTag}.
     */
    @Override
    public Tag concatenate(Tag other) throws Exception {
        if(other == null)
            throw new Exception("other = null"); // TODO
        if(!(other instanceof IntegerTag)){
            throw new Exception("la concatenazione richiede due tag dello stesso tipo");
        }

        IntegerTag t = new IntegerTag(this.tag + ((IntegerTag) other).tag);
        if(this.isEpsilon() || other.isEpsilon())
            t = EPSILON;
        
        return t;
    }

    /**
     * Esegue l'operazione &gt; tra tag in base al classico ordinamento dei numeri.
     */
    @Override
    public boolean gt(Tag other) throws Exception {
        if(!(other instanceof IntegerTag)){
            throw new Exception("la comparazione richiede due tag dello stesso tipo");
        }
        
        return this.tag > ((IntegerTag) other).tag;
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
        if(other == null || !(other instanceof IntegerTag))
            return false;
        
        return this.tag.equals(((IntegerTag)other).getTag());
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

}
