/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
    Var[] initialVarValues;
    Tag tagInstance;

    public TagMachine(String[] variables, ArrayList<ArrayList<Edge>> edges, int initialState, 
            int[] acceptingStates, /*Tag[] initialTagValues,*/ Var[] initialVarValues, Tag tagInstance) 
            throws Exception {
        
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
        this.tagInstance = tagInstance;
    }

    public String[] getVariables() {
        return variables;
    }

    public ArrayList<ArrayList<Edge>> getEdges() {
        return edges;
    }

    public int getInitialState() {
        return initialState;
    }

    public int[] getAcceptingStates() {
        return acceptingStates;
    }

    public Var[] getInitialVarValues() {
        return initialVarValues;
    }

    public Tag getTagInstance() {
        return tagInstance;
    }
    
    public void simulate(int steps, boolean random, boolean debug) throws Exception {
        Tag[] tagVector = new Tag[variables.length];
        for(int i=0; i<tagVector.length; i++){
            tagVector[i] = tagInstance.getIdentity();
        }
        
        ArrayList<ArrayList<Tag>> behaviorsTag = new ArrayList<>(variables.length);
        ArrayList<ArrayList<Var>> behaviorsVarValue = new ArrayList<>(variables.length);
        for(int i=0; i < variables.length; i++){
            behaviorsTag.add(new ArrayList<>());
            behaviorsVarValue.add(new ArrayList<>());
        }
        
        int state=initialState;
        for(int i=0; i<steps; i++){
            int nextStateIndex = 0;
            if(random)
                nextStateIndex = ThreadLocalRandom.current().nextInt(edges.get(state).size());
            
            TagPiece tagPiece = edges.get(state).get(nextStateIndex).tagPiece;
            tagVector = tagPiece.apply(tagVector);
            
            if(debug)
                for(int j=0; j<tagVector.length; j++){
                    System.out.print(variables[j]+": ");
                    System.out.println(((TagIntPlus)tagVector[j]).getTag());                
                }
            
            Integer[] updatedVarIndexes = tagPiece.domLabelingFunction();
            for(int varIndex : updatedVarIndexes){
                behaviorsTag.get(varIndex).add(tagVector[varIndex]);
                behaviorsVarValue.get(varIndex).add(tagPiece.labelingFunction(varIndex));
            }
            
            state = edges.get(state).get(nextStateIndex).toState;
            
        }
        
    }
    
}
