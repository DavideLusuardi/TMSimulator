/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author davide
 */
public class TagMachineHomoComposition extends ArrayList<TagMachine> {
    
    public TagMachine compose() throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        
        // TODO: controllare che le TMs abbiano dimensione > 0
        TagMachine tm1 = this.get(0);
        TagMachine tm2 = this.get(1);
        
        HashMap<String, Integer> varMap = new HashMap<>(tm1.getVarMap());
        ArrayList<String> sharedVars = new ArrayList<>(tm2.getVarMap().size());
        int pos = varMap.size();
        for(String var : tm2.getVarMap().keySet()){
            Integer val = varMap.putIfAbsent(var, pos);
            if(val == null){ // chiave non presente
                pos++;
            } else {
                sharedVars.add(var);
            }
        }
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>(tm1.getEdges().size());
        int[][] compositionMatrix = new int[tm1.getEdges().size()][tm2.getEdges().size()]; // TODO: si può implementare attraverso mappa hash
        for(int i=0; i<compositionMatrix.length; i++)
            for(int j=0; j<compositionMatrix[0].length; j++)
                compositionMatrix[i][j] = -1;
        
        for(int i=0; i<tm1.getEdges().size(); i++){
            for(Edge e1 : tm1.getEdges().get(i)){
                for(int j=0; i<tm2.getEdges().size(); j++){
                    for(Edge e2 : tm2.getEdges().get(j)){
                        if(TagPiece.unifiable(e1.getTagPiece(), e2.getTagPiece(), 
                                tm1.getVarMap(), tm2.getVarMap(), sharedVars)){
                            
                            TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                    tm1.getVarMap(), tm2.getVarMap(), varMap); // TODO
                            
                            if(compositionMatrix[i][j] == -1){
                                compositionMatrix[i][j] = edges.size();
                                edges.add(new ArrayList<>());
                            }
                            if(compositionMatrix[e1.getToState()][e2.getToState()] == -1){
                                compositionMatrix[e1.getToState()][e2.getToState()] = edges.size();
                                edges.add(new ArrayList<>());
                            }
                            Edge e = new Edge(compositionMatrix[i][j], compositionMatrix[e1.getToState()][e2.getToState()], tp);
                            edges.get(compositionMatrix[i][j]).add(e);
                        }
                    }
                }
            }
        }
        
        int initialState = compositionMatrix[tm1.getInitialState()][tm2.getInitialState()];
        ArrayList<Integer> acceptingStates = new ArrayList<>();
        for(int i : tm1.getAcceptingStates()){
            for(int j : tm2.getAcceptingStates()){
                if(compositionMatrix[i][j] != -1)
                    acceptingStates.add(compositionMatrix[i][j]);
            }
        }
        
        tmComp = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, tagInstance);
    }
}
