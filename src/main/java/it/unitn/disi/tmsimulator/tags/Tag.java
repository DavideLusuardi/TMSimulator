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
public abstract class Tag {
    
    public abstract Tag concatenate(Tag other) throws Exception;
    public abstract boolean gt(Tag other) throws Exception;
    public abstract boolean isEpsilon();
    public abstract Tag getIdentity();
    public abstract Tag getEpsilon();

    public abstract boolean equals(Tag other);
    
    @Override
    public abstract String toString();
}
