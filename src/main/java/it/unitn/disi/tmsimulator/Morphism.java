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
public abstract class Morphism {
    public abstract Tag map(Tag tag) throws Exception;
    public abstract Tag getTagInstance();
}
