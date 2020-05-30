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
public final class MaxPlusFloat extends Tag {
    public static final MaxPlusFloat EPSILON = new MaxPlusFloat(Float.NEGATIVE_INFINITY);
    public static final MaxPlusFloat IDENTITY = new MaxPlusFloat((float)0);
    
    private Float tag;
    
    public MaxPlusFloat(Float t){
        this.tag = t;
    }

    public MaxPlusFloat() {
    }

    public Float getTag() {
        return tag;
    }
    
    @Override
    public Tag concatenate(Tag other) throws Exception {
        if(other == null)
            throw new Exception("other = null"); // TODO
        if(!(other instanceof MaxPlusFloat)){
            throw new Exception("la concatenazione richiede due tag dello stesso tipo");
        }

        MaxPlusFloat t = new MaxPlusFloat(this.tag + ((MaxPlusFloat) other).tag);
        if(this.isEpsilon() || other.isEpsilon())
            t = EPSILON;
        
        return t;
    }

    @Override
    public boolean gt(Tag other) throws Exception {
        if(!(other instanceof MaxPlusFloat)){
            throw new Exception("la comparazione richiede due tag dello stesso tipo");
        }
        
        return this.tag > ((MaxPlusFloat) other).tag;
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
        if(other == null || !(other instanceof MaxPlusFloat))
            return false;
        
        // return this.tag.equals(((MaxPlusFloat)other).getTag());
        return Math.abs(this.tag - ((MaxPlusFloat)other).getTag()) < 0.0001; // TODO: decidere margine di errore
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

}
