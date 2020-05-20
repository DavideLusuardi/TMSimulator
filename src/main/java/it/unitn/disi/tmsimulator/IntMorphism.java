/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

/**
 *
 * @author davide
 */
public class IntMorphism extends Morphism {

    // applica il mapping tag' = alpha*tag
    private Integer alpha;

    public IntMorphism(Integer alpha) {
        this.alpha = alpha;
    }

    public IntMorphism() {
        this(1);
    }

    @Override
    public Tag getTagInstance() {
        return new MaxPlusInteger();
    }
    
    @Override
    public Tag map(Tag tag) throws Exception {
        if(!(tag instanceof MaxPlusInteger)) // TODO: servirebbe classe più generica
            throw new Exception("il tag deve essere di tipo MaxPlusInteger");
        
        if(tag.isEpsilon())
            return getTagInstance().getEpsilon();
        else
            return new MaxPlusInteger(alpha*((MaxPlusInteger)tag).getTag());
    }
    
    
}
