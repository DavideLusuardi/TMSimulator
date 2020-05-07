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
public class TagIntPlus extends Tag {
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
    Tag concatenate(Tag other) throws Exception {
        if(!(other instanceof TagIntPlus)){
            throw new Exception("la concatenazione richiede due tag dello stesso tipo");
        }

        TagIntPlus t = new TagIntPlus(this.tag + ((TagIntPlus) other).tag);
        if(this.isEpsilon() || other.isEpsilon())
            t = EPSILON;
        
        return t;
    }

    @Override
    boolean gt(Tag other) throws Exception {
        if(!(other instanceof TagIntPlus)){
            throw new Exception("la comparazione richiede due tag dello stesso tipo");
        }
        
        return this.tag > ((TagIntPlus) other).tag;
    }

    @Override
    boolean isEpsilon() {
        return this.tag.equals(EPSILON.tag);
    }

    @Override
    Tag getIdentity() {
        return IDENTITY;
    }

    @Override
    Tag getEpsilon() {
        return EPSILON;
    }

}
