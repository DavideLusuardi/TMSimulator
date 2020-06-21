/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.tags.Tag;

/**
 * Rappresenta un morfismo di una tag machine.
 *
 * @author davide
 */
public abstract class Morphism {
    /**
     * Applica il morfismo ad un tag generando il tag corrispondente.
     * 
     * @param tag
     * @return
     * @throws Exception 
     */
    public abstract Tag map(Tag tag) throws Exception;
    
    /**
     * Restituisce un'istanza di tag che rappresenta il codominio del morfismo.
     * 
     * @return 
     */
    public abstract Tag getTagInstance();
}
