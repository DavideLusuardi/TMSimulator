/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.variables.Var;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe TagMachineSet rappresenta un insieme di TagMachine su cui è 
 * possibile eseguire la composizione. Tale composizione può essere omogenea o
 * etherogenea. In quest'ultimo caso vengono sono necessari i TagMophism da 
 * applicare ad ogni TagMachine.
 * 
 * 
 * @author davide
 */
public class TagMachineSet extends ArrayList<TagMachine> {

    /**
     * Esegue la composizione omogenea di due TagMachine.
     * 
     * @param tm1
     * @param tm2
     * @return
     * @throws Exception 
     */
    public TagMachine compose(TagMachine tm1, TagMachine tm2) throws Exception {
        
        // TODO: controllare che le TMs abbiano dimensione > 0
        
        // ricaviamo le variabili condivise e la mappatura var-index per la composizione
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
        int[][] compStateIndexes = new int[tm1.getEdges().size()][tm2.getEdges().size()]; // TODO: si può implementare attraverso mappa hash
        for(int i=0; i<compStateIndexes.length; i++)
            for(int j=0; j<compStateIndexes[0].length; j++)
                compStateIndexes[i][j] = -1;
        
        // TODO: usare Queue invece di ArrayList
        ArrayList<Integer> stateQueue1 = new ArrayList<>(); // coda degli stati aggiunti per TM1
        ArrayList<Integer> stateQueue2 = new ArrayList<>(); // coda degli stati aggiunti per TM2
        
        edges.add(new ArrayList<>());
        compStateIndexes[tm1.getInitialState()][tm2.getInitialState()] = 0;
        stateQueue1.add(tm1.getInitialState());
        stateQueue2.add(tm2.getInitialState());
        
        int sComp=0;
        while(edges.size() > sComp){
            int s1 = stateQueue1.get(sComp);
            int s2 = stateQueue2.get(sComp);
            for(Edge e1 : tm1.getEdges().get(s1)){
                for(Edge e2 : tm2.getEdges().get(s2)){
                    if(TagPiece.isUnifiable(e1.getTagPiece(), e2.getTagPiece(), 
                            tm1.getVarMap(), tm2.getVarMap(), sharedVars)){

                        // System.out.println(String.format("isUnifiable: e1 %d -> %d; e2 %d -> %d", e1.getFromState(), e1.getToState(), e2.getFromState(), e2.getToState()));

                        TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                tm1.getVarMap(), tm2.getVarMap(), varMap, tm1.getTagInstance().getEpsilon()); // TODO

                        if(compStateIndexes[e1.getToState()][e2.getToState()] == -1){ // lo stato (e1.getToState(), e2.getToState()) non esiste ancora                            
                            compStateIndexes[e1.getToState()][e2.getToState()] = edges.size(); // il nuovo stato assume indice pari a edges.size()
                            stateQueue1.add(e1.getToState());
                            stateQueue2.add(e2.getToState());
                            edges.add(new ArrayList<>()); // aggiungo il nuovo stato (inizialmente senza transizioni)
                            
                            // System.out.println(String.format("state assignment %d = (%d,%d)", edges.size()-1, e1.getToState(), e2.getToState()));
                        }
                        Edge e = new Edge(sComp, compStateIndexes[e1.getToState()][e2.getToState()], tp);
                        edges.get(sComp).add(e);
                    }
                }
            }
            sComp++;
        }
        
        int initialState = compStateIndexes[tm1.getInitialState()][tm2.getInitialState()];
        ArrayList<Integer> acceptingStates = new ArrayList<>();
        for(int i : tm1.getAcceptingStates()){
            for(int j : tm2.getAcceptingStates()){
                if(compStateIndexes[i][j] != -1)
                    acceptingStates.add(compStateIndexes[i][j]);
            }
        }
        
        // from ArrayList to array
        int[] accStates = new int[acceptingStates.size()];
        for(int i=0; i<acceptingStates.size(); i++)
            accStates[i] = acceptingStates.get(i); // TODO
        
        // assegnazione valore variabili iniziali
        Var[] initialVarValues = new Var[varMap.size()];
        for (Map.Entry<String, Integer> entry : tm1.getVarMap().entrySet()) {
            initialVarValues[varMap.get(entry.getKey())] = tm1.getInitialVarValues()[entry.getValue()];
        }
        for (Map.Entry<String, Integer> entry : tm2.getVarMap().entrySet()) {
            // TODO: controllare che sharedVars abbiano stesso valore iniziale
            initialVarValues[varMap.get(entry.getKey())] = tm2.getInitialVarValues()[entry.getValue()];
        }
        
        TagMachine tmComp = new TagMachine(varMap, edges, initialState, accStates, initialVarValues, tm1.getTagInstance());
        return tmComp;
    }
    
    // TODO: fermarsi quando tmComp ha un solo stato oppure nessun acceptingState
    /**
     * Esegue la composizione omogenea di tutte le TagMachine presenti. Partendo 
     * dalla prima, viene eseguita la composizione con la seconda ottenendo la 
     * TagMachine che rappresenta la composione. A sua volta quest'ultima viene
     * composta con la terza e così via.
     * 
     * @return
     * @throws Exception 
     */
    public TagMachine compose() throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        
        TagMachine tmComp = this.get(0);
        for(int i=1; i<this.size(); i++){
            tmComp = compose(tmComp, this.get(i));
        }
        return tmComp;
    }
    
    /**
     * Esegue la composizione eterogenea di tutte le TagMachine presenti. Prima 
     * di eseguirne la composizione viene applicato il relativo Morphism alla
     * TagMachine. Nel caso in cui il morfismo è null, la TagMachine rimane
     * invariata.
     * Partendo dalla prima TagMachine, viene eseguita la composizione con la 
     * seconda ottenendo la TagMachine che rappresenta la composione. A sua volta 
     * quest'ultima viene composta con la terza e così via.
     * 
     * @return
     * @throws Exception 
     */
    public TagMachine compose(ArrayList<Morphism> tagMorphismList) throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        else if(tagMorphismList.size() != this.size())
            throw new Exception("la lunghezza della lista dei tagMorphism deve corrispondere a quella di TagMachineSet");
        
        this.get(0).applyMorphism(tagMorphismList.get(0));
        TagMachine tmComp = this.get(0);
        for(int i=1; i<this.size(); i++){
            this.get(i).applyMorphism(tagMorphismList.get(i));
            tmComp = compose(tmComp, this.get(i));
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println(tmComp);
            
            
            /*
            for(ArrayList<Edge> edgeList : tmComp.getEdges()){
                for(Edge e : edgeList){
                    Tag[][] m = e.getTagPiece().getMatrix();
                    System.out.print("-----|");
                    for (Map.Entry<String, Integer> vc : tmComp.getVarMap().entrySet()) {
                        System.out.print(vc.getKey() + "     |");
                    }
                    System.out.println("");
                    for (Map.Entry<String, Integer> vr : tmComp.getVarMap().entrySet()) {
                        System.out.print(vr.getKey()+" : ");
                        for (Map.Entry<String, Integer> vc : tmComp.getVarMap().entrySet()) {
                            System.out.print(m[vr.getValue()][vc.getValue()]+"|");
                        }
                        System.out.println("");
                    }
                }
            }
            */
        }
        return tmComp;
    }
    
}
