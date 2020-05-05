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
public class Edge {
    int fromState;
    int toState;
    TagPiece tagPiece;

    public Edge(int fromState, int toState, TagPiece tagPiece) {
        this.fromState = fromState;
        this.toState = toState;
        this.tagPiece = tagPiece;
    }    
    
}
