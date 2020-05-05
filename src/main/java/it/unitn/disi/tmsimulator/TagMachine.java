/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;

/**
 *
 * @author davide
 */
public class TagMachine {
    String[] variables;
    ArrayList<ArrayList<Edge>> edges;
    int initialState;
    int[] acceptingStates;
    
    // Tag[] initialTagValues;
    VarValue[] initialVarValues;

    public TagMachine(String[] variables, ArrayList<ArrayList<Edge>> edges, int initialState, 
            int[] acceptingStates, /*Tag[] initialTagValues,*/ VarValue[] initialVarValues) throws Exception {
        
        // alcuni controlli
        // TODO: fare controlli esaustivi
        if(initialState < 0 || initialState >= edges.size())
            throw new Exception("stato iniziale non valido");
        
        for(int s : acceptingStates)
            if(s < 0 || s >= edges.size())
                throw new Exception("stati finali non validi");
        
        //if(initialTagValues.length != variables.length || initialVarValues.length != variables.length)
        //    throw new Exception("dimensioni errate"); // TODO
        
        this.variables = variables;
        this.edges = edges;
        this.initialState = initialState;
        this.acceptingStates = acceptingStates; // TODO: ordinare ed eliminare doppioni
    }
    
    public void simulate(int steps) throws Exception {
        Tag[] tagVector = new Tag[variables.length];
        
        // TODO: inizializzazione con identity element
        for(int i=0; i<tagVector.length; i++){
            tagVector[i] = new TagIntPlus(0);
        }
        
        ArrayList<ArrayList<Tag>> behaviorsTag = new ArrayList<>(variables.length);
        ArrayList<ArrayList<VarValue>> behaviorsVarValue = new ArrayList<>(variables.length);
        for(int i=0; i < variables.length; i++){
            behaviorsTag.add(new ArrayList<>());
            behaviorsVarValue.add(new ArrayList<>());
        }
        
        int state=initialState;
        for(int i=0; i<steps; i++){
            int nextStateIndex = 0; // TODO
            
            TagPiece tagPiece = edges.get(state).get(nextStateIndex).tagPiece;
            tagVector = tagPiece.apply(tagVector);
            
            for(int j=0; j<tagVector.length; j++){
                System.out.print(variables[j]+": ");
                System.out.println(((TagIntPlus)tagVector[j]).tag);                
            }
            
            ArrayList<Integer> updatedVarIndexes = tagPiece.domLabelingFunction();
            for(int varIndex : updatedVarIndexes){
                behaviorsTag.get(varIndex).add(tagVector[varIndex]);
                behaviorsVarValue.get(varIndex).add(tagPiece.labelingFunction[varIndex]);
            }
            
            state = edges.get(state).get(nextStateIndex).toState;
            
        }
        
    }
    
}
