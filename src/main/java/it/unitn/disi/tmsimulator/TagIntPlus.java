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
public final class TagIntPlus extends Tag {
    public static final TagIntPlus EPSILON = new TagIntPlus(Integer.MIN_VALUE);
    public static final TagIntPlus IDENTITY = new TagIntPlus(0);
    
    private Integer tag;
    
    public TagIntPlus(Integer t){
        this.tag = t;
    }

    public TagIntPlus() {
    }

    public Integer getTag() {
        return tag;
    }
    
    @Override
    public Tag concatenate(Tag other) throws Exception {
        if(!(other instanceof TagIntPlus)){
            throw new Exception("la concatenazione richiede due tag dello stesso tipo");
        }

        TagIntPlus t = new TagIntPlus(this.tag + ((TagIntPlus) other).tag);
        if(this.isEpsilon() || other.isEpsilon())
            t = EPSILON;
        
        return t;
    }

    @Override
    public boolean gt(Tag other) throws Exception {
        if(!(other instanceof TagIntPlus)){
            throw new Exception("la comparazione richiede due tag dello stesso tipo");
        }
        
        return this.tag > ((TagIntPlus) other).tag;
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
        if(other == null || !(other instanceof TagIntPlus))
            return false;
        
        return this.tag.equals(((TagIntPlus)other).getTag());
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

}
