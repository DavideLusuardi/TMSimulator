/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

/**
 * Rappresenta una transizione tra due stati.
 *
 * @author davide
 */
public class Edge {
    /**
     * Stato di partenza.
     */
    private int fromState;
    
    /**
     * Stato di arrivo.
     */
    private int toState;
    
    /**
     * Labeled tag piece.
     */
    private TagPiece tagPiece;

    /**
     * Costruisce un Edge con gli opportuni attributi.
     * 
     * @param fromState stato di partenza.
     * @param toState stato di arrivo.
     * @param tagPiece labeled tag piece associato alla transizione.
     */
    public Edge(int fromState, int toState, TagPiece tagPiece) {
        this.fromState = fromState;
        this.toState = toState;
        this.tagPiece = tagPiece;
    }

    /**
     * Restituisce lo stato di partenza.
     * 
     * @return stato di partenza.
     */
    public int getFromState() {
        return fromState;
    }

    /**
     * Restituisce lo stato di arrivo.
     * 
     * @return stato di arrivo.
     */
    public int getToState() {
        return toState;
    }

    /**
     * Restituisce il labeled tag piece associato alla transizione.
     * 
     * @return labeled tag piece associato alla transizione.
     */
    public TagPiece getTagPiece() {
        return tagPiece;
    }
    
}
