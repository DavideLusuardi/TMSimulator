/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.tags;

/**
 * Rappresenta un tag di tipo Float avente come operazione di concatenazione la
 * somma e come ordine parziale (totale) il classico ordinamento tra numeri.
 * 
 * @author davide
 */
public final class FloatTag extends Tag {
    /**
     * Tag epsilon. Corrisponde al valore -infinito.
     */
    public static final FloatTag EPSILON = new FloatTag(Float.NEGATIVE_INFINITY);
    
    /**
     * Tag identità. Corrisponde al valore 0.
     */
    public static final FloatTag IDENTITY = new FloatTag((float)0);
    
    /**
     * Soglia di errore nel confronto tra float.
     */
    public static float errValue = (float)0.0001;
    
    /**
     * Valore del tag.
     */
    private Float tag;
    
    /**
     * Costruisce un tag.
     * @param t il valore del tag.
     */
    public FloatTag(Float t){
        this.tag = t;
    }

    /**
     * Costruisce un tag con valore non definito.
     */
    public FloatTag() {
    }

    /**
     * Restituisce il valore del tag.
     * @return il valore del tag.
     */
    public Float getTag() {
        return tag;
    }
    
    /**
     * {@inheritDoc}
     * Corrisponde all'operazione di somma.
     * Richiede un tag di tipo {@link it.unitn.disi.tmsimulator.tags.FloatTag}.
     */
    @Override
    public Tag concatenate(Tag other) throws Exception {
        if(other == null)
            throw new Exception("other = null"); // TODO
        if(!(other instanceof FloatTag)){
            throw new Exception("la concatenazione richiede due tag dello stesso tipo");
        }

        FloatTag t = new FloatTag(this.tag + ((FloatTag) other).tag);
        if(this.isEpsilon() || other.isEpsilon())
            t = EPSILON;
        
        return t;
    }

    /**
     * Esegue l'operazione &gt; tra tag in base al classico ordinamento dei numeri.
     */
    @Override
    public boolean gt(Tag other) throws Exception {
        if(!(other instanceof FloatTag)){
            throw new Exception("la comparazione richiede due tag dello stesso tipo");
        }
        
        return this.tag > ((FloatTag) other).tag;
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
        if(other == null || !(other instanceof FloatTag))
            return false;
        
        if(this.isEpsilon()){
            if(other.isEpsilon())
                return true;
            else
                return false;
        } else {
            if(other.isEpsilon())
                return false;
            else
                return Math.abs(this.tag - ((FloatTag)other).getTag()) < FloatTag.errValue;
        }
        
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

}
