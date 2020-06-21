/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.tags.Tag;
import it.unitn.disi.tmsimulator.tags.MaxPlusInteger;
import it.unitn.disi.tmsimulator.tags.MaxPlusFloat;

/**
 *
 * @author davide
 */
public class Int2FloatMorphism extends Morphism {

    // applica il mapping tag' = alpha*tag
    private Float alpha;

    public Int2FloatMorphism(Float alpha) {
        this.alpha = alpha;
    }

    public Int2FloatMorphism() {
        this(new Float(1));
    }

    @Override
    public Tag getTagInstance() {
        return new MaxPlusFloat();
    }
    
    @Override
    public Tag map(Tag tag) throws Exception {
        if(!(tag instanceof MaxPlusInteger)) // TODO: servirebbe classe più generica
            throw new Exception("il tag deve essere di tipo MaxPlusInteger");
        
        if(tag.isEpsilon())
            return getTagInstance().getEpsilon();
        else        
            return new MaxPlusFloat(alpha*((MaxPlusInteger)tag).getTag());
    }
    
    
}
