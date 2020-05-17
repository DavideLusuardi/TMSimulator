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
public class TagMachineSet extends ArrayList<TagMachine> {
    
    public TagMachine compose(TagMachine tm1, TagMachine tm2) throws Exception {
        
        // TODO: controllare che le TMs abbiano dimensione > 0
        
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
        
        ArrayList<Integer> stateMap1 = new ArrayList<>();
        ArrayList<Integer> stateMap2 = new ArrayList<>();
        
        edges.add(new ArrayList<>());
        compositionMatrix[tm1.getInitialState()][tm2.getInitialState()] = 0;
        stateMap1.add(tm1.getInitialState());
        stateMap2.add(tm2.getInitialState());
        
        int sComp=0;
        while(edges.size() > sComp){
            int s1 = stateMap1.get(sComp);
            int s2 = stateMap2.get(sComp);
            for(Edge e1 : tm1.getEdges().get(s1)){
                for(Edge e2 : tm2.getEdges().get(s2)){
                    if(TagPiece.unifiable(e1.getTagPiece(), e2.getTagPiece(), 
                            tm1.getVarMap(), tm2.getVarMap(), sharedVars)){

                        // System.out.println(String.format("unifiable: e1 %d -> %d; e2 %d -> %d", e1.getFromState(), e1.getToState(), e2.getFromState(), e2.getToState()));

                        TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                tm1.getVarMap(), tm2.getVarMap(), varMap, tm1.getTagInstance().getEpsilon()); // TODO

                        if(compositionMatrix[e1.getToState()][e2.getToState()] == -1){
                            compositionMatrix[e1.getToState()][e2.getToState()] = edges.size();
                            stateMap1.add(e1.getToState());
                            stateMap2.add(e2.getToState());
                            edges.add(new ArrayList<>());
                        }
                        Edge e = new Edge(sComp, compositionMatrix[e1.getToState()][e2.getToState()], tp);
                        edges.get(sComp).add(e);
                    }
                }
            }
            sComp++;
        }
        
        /* 
        for(int i=0; i<tm1.getEdges().size(); i++){
            for(Edge e1 : tm1.getEdges().get(i)){
                for(int j=0; j<tm2.getEdges().size(); j++){
                    for(Edge e2 : tm2.getEdges().get(j)){
                        if(TagPiece.unifiable(e1.getTagPiece(), e2.getTagPiece(), 
                                tm1.getVarMap(), tm2.getVarMap(), sharedVars)){
                            
                            System.out.println(String.format("unifiable: e1 %d -> %d; e2 %d -> %d", e1.getFromState(), e1.getToState(), e2.getFromState(), e2.getToState()));
                            
                            TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                    tm1.getVarMap(), tm2.getVarMap(), varMap, tm1.getTagInstance().getEpsilon()); // TODO
                            
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
        */
        
        int initialState = compositionMatrix[tm1.getInitialState()][tm2.getInitialState()];
        ArrayList<Integer> acceptingStates = new ArrayList<>();
        for(int i : tm1.getAcceptingStates()){
            for(int j : tm2.getAcceptingStates()){
                if(compositionMatrix[i][j] != -1)
                    acceptingStates.add(compositionMatrix[i][j]);
            }
        }
        
        int[] accStates = new int[acceptingStates.size()];
        for(int i=0; i<acceptingStates.size(); i++)
            accStates[i] = acceptingStates.get(i); // TODO
        
        Var[] initialVarValues = new Var[varMap.size()];
        for(int i=0; i<initialVarValues.length; i++){
            initialVarValues[i] = tm1.getInitialVarValues()[0]; // TODO
        }
        
        TagMachine tmComp = new TagMachine(varMap, edges, initialState, accStates, initialVarValues, tm1.getTagInstance());
        return tmComp;
    }
    
    // TODO: fermarsi quando tmComp ha un solo stato oppure nessun acceptingState
    public TagMachine compose() throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        
        TagMachine tmComp = this.get(0);
        for(int i=1; i<this.size(); i++){
            tmComp = compose(tmComp, this.get(i));
        }
        return tmComp;
    }
    
    public TagMachine compose(ArrayList<Morphism> tagMorphismList) throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        else if(tagMorphismList.size() != this.size())
            throw new Exception("la lunghezza della lista dei tagMorphism deve corrispondere a quella di TagMachineSet");
        
        TagMachine tmComp = this.get(0);
        tmComp.applyMorphism(tagMorphismList.get(0));
        for(int i=1; i<this.size(); i++){
            this.get(i).applyMorphism(tagMorphismList.get(i));
            tmComp = compose(tmComp, this.get(i));
        }
        return tmComp;
    }
    
    /*
    public TagMachine compose2() throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        
        // TODO: controllare che le TMs abbiano dimensione > 0
        
        HashMap<String, Integer> varMap = new HashMap<>();
        ArrayList<Integer> countVars = new ArrayList<>();
        int pos = 0;
        for(TagMachine tm : this){
            for(String var : tm.getVarMap().keySet()){
                Integer val = varMap.putIfAbsent(var, pos);
                if(val == null){ // chiave non presente                    
                    pos++;
                    countVars.add(1);
                } else {
                    countVars.add(val, countVars.get(val) + 1);
                }
            }
        }
        
        ArrayList<String> sharedVars = new ArrayList<>();
        for(Map.Entry<String, Integer> v : varMap.entrySet()){
            if(countVars.get(v.getValue()) == this.size()){
                sharedVars.add(v.getKey());
            }
        }
        
        
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>(tm1.getEdges().size());
        int[][] compositionMatrix = new int[tm1.getEdges().size()][tm2.getEdges().size()]; // TODO: si può implementare attraverso mappa hash
        for(int i=0; i<compositionMatrix.length; i++)
            for(int j=0; j<compositionMatrix[0].length; j++)
                compositionMatrix[i][j] = -1;
        
        ArrayList<Integer> stateMap1 = new ArrayList<>();
        ArrayList<Integer> stateMap2 = new ArrayList<>();
        
        edges.add(new ArrayList<>());
        compositionMatrix[tm1.getInitialState()][tm2.getInitialState()] = 0;
        stateMap1.add(tm1.getInitialState());
        stateMap2.add(tm2.getInitialState());
        
        int sComp=0;
        while(edges.size() > sComp){
            int s1 = stateMap1.get(sComp);
            int s2 = stateMap2.get(sComp);
            for(Edge e1 : tm1.getEdges().get(s1)){
                for(Edge e2 : tm2.getEdges().get(s2)){
                    if(TagPiece.unifiable(e1.getTagPiece(), e2.getTagPiece(), 
                            tm1.getVarMap(), tm2.getVarMap(), sharedVars)){

                        System.out.println(String.format("unifiable: e1 %d -> %d; e2 %d -> %d", e1.getFromState(), e1.getToState(), e2.getFromState(), e2.getToState()));

                        TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                tm1.getVarMap(), tm2.getVarMap(), varMap, tm1.getTagInstance().getEpsilon()); // TODO

                        if(compositionMatrix[e1.getToState()][e2.getToState()] == -1){
                            compositionMatrix[e1.getToState()][e2.getToState()] = edges.size();
                            stateMap1.add(e1.getToState());
                            stateMap2.add(e2.getToState());
                            edges.add(new ArrayList<>());
                        }
                        Edge e = new Edge(sComp, compositionMatrix[e1.getToState()][e2.getToState()], tp);
                        edges.get(sComp).add(e);
                    }
                }
            }
            sComp++;
        }
        
        int initialState = compositionMatrix[tm1.getInitialState()][tm2.getInitialState()];
        ArrayList<Integer> acceptingStates = new ArrayList<>();
        for(int i : tm1.getAcceptingStates()){
            for(int j : tm2.getAcceptingStates()){
                if(compositionMatrix[i][j] != -1)
                    acceptingStates.add(compositionMatrix[i][j]);
            }
        }
        
        int[] accStates = new int[acceptingStates.size()];
        for(int i=0; i<acceptingStates.size(); i++)
            accStates[i] = acceptingStates.get(i); // TODO
        
        Var[] initialVarValues = new Var[varMap.size()];
        for(int i=0; i<initialVarValues.length; i++){
            initialVarValues[i] = tm1.getInitialVarValues()[0]; // TODO
        }
        
        TagMachine tmComp = new TagMachine(varMap, edges, initialState, accStates, initialVarValues, tm1.getTagInstance());
        return tmComp;
    }
    */
}
