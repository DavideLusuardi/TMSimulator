/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author davide
 */
public class TagMachine {
    private HashMap<String, Integer> varMap;
    private ArrayList<ArrayList<Edge>> edges;
    private int initialState;
    private int[] acceptingStates;
    
    private Var[] initialVarValues;
    private Tag tagInstance;

    // TODO: gestire TM con un solo stato e senza accepting states o non raggiungibili
    public TagMachine(HashMap<String, Integer> varMap, ArrayList<ArrayList<Edge>> edges, 
            int initialState, int[] acceptingStates, Var[] initialVarValues, 
            Tag tagInstance) throws Exception {
        
        // alcuni controlli
        // TODO: fare controlli esaustivi
        if(initialState < 0 || initialState >= edges.size())
            throw new Exception("stato iniziale non valido");
        
        for(int s : acceptingStates)
            if(s < 0 || s >= edges.size())
                throw new Exception("stati finali non validi");
        
        //if(initialTagValues.length != variables.length || initialVarValues.length != variables.length)
        //    throw new Exception("dimensioni errate"); // TODO
                
        this.varMap = varMap;
        this.edges = edges;
        this.initialState = initialState;
        this.acceptingStates = acceptingStates; // TODO: ordinare ed eliminare doppioni
        this.initialVarValues = initialVarValues;
        this.tagInstance = tagInstance;        
    }

    public HashMap<String, Integer> getVarMap() {
        return varMap;
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
    
    
    // TODO: si può generare una nuova tag machine
    public void applyMorphism(TagMorphism tagMorphism) throws Exception {
        if(tagMorphism == null)
            return;
        
        this.tagInstance = tagMorphism.getTagInstance();
        
        for(ArrayList<Edge> edgeList : this.edges){
            for(Edge e : edgeList){
                e.getTagPiece().applyMorphism(tagMorphism);
            }
        }
    }
    
    public void simulate(int steps, boolean random, boolean debug) throws Exception {
        Tag[] tagVector = new Tag[varMap.size()];
        for(int i=0; i<tagVector.length; i++){
            tagVector[i] = tagInstance.getIdentity();
        }
        
        ArrayList<ArrayList<Tag>> behaviorsTag = new ArrayList<>(varMap.size());
        ArrayList<ArrayList<Var>> behaviorsVarValue = new ArrayList<>(varMap.size());
        for(int i=0; i < varMap.size(); i++){
            behaviorsTag.add(new ArrayList<>());
            behaviorsVarValue.add(new ArrayList<>());
        }
        
        int state=initialState;
        for(int i=0; i<steps; i++){
            int nextStateIndex = 0;
            if(random)
                nextStateIndex = ThreadLocalRandom.current().nextInt(edges.get(state).size()); // TODO: gestire quando c'è un solo stato
            
            TagPiece tagPiece = edges.get(state).get(nextStateIndex).getTagPiece();
            tagVector = tagPiece.apply(tagVector);
            
            if(debug){
                System.out.println(String.format("%d -> %d", edges.get(state).get(nextStateIndex).getFromState(), 
                        edges.get(state).get(nextStateIndex).getToState()));
                for(Map.Entry<String, Integer> entry : varMap.entrySet()){
                    String var = entry.getKey();
                    Integer j = entry.getValue();
                    System.out.print(var+": ");
                    System.out.println(tagVector[j].toString());
                }
            }
            
            Integer[] updatedVarIndexes = tagPiece.domLabelingFunction();
            for(int varIndex : updatedVarIndexes){
                behaviorsTag.get(varIndex).add(tagVector[varIndex]);
                behaviorsVarValue.get(varIndex).add(tagPiece.labelingFunction(varIndex));
            }
            
            state = edges.get(state).get(nextStateIndex).getToState();
            
        }
        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("initial state: %d\n", initialState));
        
        sb.append("accepting states: ");
        for(Integer s : acceptingStates){
            sb.append(String.format("%d, ", s));
        }
        
        sb.append("\n\nedges\n");
        
        int i=0;
        for(ArrayList<Edge> edgeList : edges){
            sb.append(String.format("state %d: ", i++));
            for(Edge e : edgeList){
                sb.append(String.format("%d, ", e.getToState()));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
}
