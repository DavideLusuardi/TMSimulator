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

    public Edge(int fromState, int toState, TagPiece tagPiece) {
        this.fromState = fromState;
        this.toState = toState;
        this.tagPiece = tagPiece;
    }

    public int getFromState() {
        return fromState;
    }

    public int getToState() {
        return toState;
    }

    public TagPiece getTagPiece() {
        return tagPiece;
    }
    
}
