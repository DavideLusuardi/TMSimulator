/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.morphism;

import it.unitn.disi.tmsimulator.morphism.Morphism;
import it.unitn.disi.tmsimulator.tags.Tag;
import it.unitn.disi.tmsimulator.tags.IntegerTag;
import it.unitn.disi.tmsimulator.tags.FloatTag;

/**
 * Morfismo da un tag {@link it.unitn.disi.tmsimulator.tags.IntegerTag} a un
 * tag {@link it.unitn.disi.tmsimulator.tags.FloatTag}.
 * Applica il mapping tag' = alpha*tag.
 * 
 * @author davide
 */
public class Int2FloatMorphism extends Morphism {

    /**
     * Coefficiente alpha.
     */
    private Float alpha;

    /**
     * Costruisce il morfismo.
     * 
     * @param alpha coefficiente lineare alpha.
     */
    public Int2FloatMorphism(Float alpha) {
        this.alpha = alpha;
    }

    /**
     * Costruisce il morfismo con alpha pari a 1.
     */
    public Int2FloatMorphism() {
        this((float)1);
    }

    /**
     * Restituisce un'istanza della stessa classe del tag generato.
     * 
     * @return un'istanza della stessa classe del tag generato.
     */
    @Override
    public Tag getTagInstance() {
        return new FloatTag();
    }
    
    /**
     * Applica il mapping tag' = alpha*tag.
     * 
     * @param tag tag di partenza.
     * @return tag calcolato.
     * @throws Exception se il tag passato non è di classe {@link it.unitn.disi.tmsimulator.tags.IntegerTag}.
     */
    @Override
    public Tag map(Tag tag) throws Exception {
        if(!(tag instanceof IntegerTag))
            throw new Exception("il tag deve essere di tipo IntegerTag");
        
        if(tag.isEpsilon())
            return getTagInstance().getEpsilon();
        else        
            return new FloatTag(alpha*((IntegerTag)tag).getTag());
    }
    
    
}
