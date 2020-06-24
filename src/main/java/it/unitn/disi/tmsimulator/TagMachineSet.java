/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.tags.Tag;
import it.unitn.disi.tmsimulator.variables.Var;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
                    if(TagPiece.isUnifiable(e1.getTagPiece(), e2.getTagPiece(), sharedVars)){

                        // System.out.println(String.format("isUnifiable: e1 %d -> %d; e2 %d -> %d", e1.getFromState(), e1.getToState(), e2.getFromState(), e2.getToState()));

                        TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                varMap, tm1.getTagInstance().getEpsilon()); // TODO

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
    
    
    
    // TODO: controllare che le TMs abbiano dimensione > 0
    public void simulate(ArrayList<Morphism> tagMorphismList, int steps, boolean random) throws Exception {
        FileWriter xFile = new FileWriter("plots/x_without_control.txt");
        FileWriter awFile = new FileWriter("plots/aw_without_control.txt");
        
        
        boolean eterComposition = (tagMorphismList != null);
        
        if(this.size() < 2) // TODO: simulazione va bene anche con una sola TM
            throw new Exception("la composizione richiede almeno due TagMachine");
        else if(eterComposition && tagMorphismList.size() != this.size())
            throw new Exception("la lunghezza della lista dei tagMorphism deve corrispondere a quella di TagMachineSet");                
        
        
        ArrayList<HashMap<String, Integer>> varMap = new ArrayList<>(this.size());//= new HashMap<>(this.get(0).getVarMap());
        ArrayList<ArrayList<String>> sharedVars = new ArrayList<>(this.size());
        varMap.add(new HashMap<>(this.get(0).getVarMap()));
        int pos = varMap.get(0).size();
        for(int i=1; i<this.size(); i++){
//            sharedVars.add(TagMachine.getSharedVars(varNames, this.get(i)));
            sharedVars.add(new ArrayList<>());
            for(String var : this.get(i).getVarMap().keySet()){
                Integer val = varMap.get(i-1).putIfAbsent(var, pos);
                if(val == null) // chiave non presente
                    pos++;
                else
                    sharedVars.get(i-1).add(var);
            }
            if(i+1<this.size())
                varMap.add(new HashMap<>(varMap.get(i-1)));
            
        }
        
        ArrayList<Integer> currentState = new ArrayList<>(this.size());        
        ArrayList<ArrayList<Tag>> tagVector = new ArrayList<>(this.size());
        ArrayList<HashMap<String, Var>> varValues = new ArrayList<>(this.size());
        
        for(int i=0; i<this.size(); i++){
            TagMachine tm = this.get(i);
            currentState.add(tm.getInitialState());
            
            ArrayList<Tag> tv = new ArrayList<>(tm.getVarMap().size());
            for(int j=0; j<tm.getVarMap().size(); j++){
                if(tagMorphismList.get(i) != null)
                    tv.add(tagMorphismList.get(i).getTagInstance().getIdentity());
                else
                    tv.add(tm.getTagInstance().getIdentity());
            }
            tagVector.add(tv);
            
            HashMap<String, Var> vv = new HashMap<>(tm.getInitialVarValues().length);
            for(Map.Entry<String, Integer> entry : tm.getVarMap().entrySet()){
                vv.put(entry.getKey(), tm.getInitialVarValues()[entry.getValue()]);
            }
            varValues.add(vv);
        }
                
        
        for(int step=0; step<steps; step++){
            boolean exhausted = false;
            
            ArrayList<Integer> currentEdge = new ArrayList<>(this.size());
            for(int i=0; i<this.size(); i++){
                if(this.get(i).getEdges().get(currentState.get(i)).isEmpty()){
                    exhausted = true; // nessuna transizione possibile
                    break;
                }
                currentEdge.add(0);
            }
            
            ArrayList<ArrayList<Integer>> validEdges = new ArrayList<>();
            ArrayList<ArrayList<HashMap<String, Var>>> varValuesPrime = new ArrayList<>();
            while(!exhausted){
                
                // controlliamo che i tag piece siano unificabili
                boolean unifiable = true;      
                TagPiece tpComp = this.get(0).getEdges().get(currentState.get(0)).get(currentEdge.get(0)).getTagPiece();
                if(eterComposition && !tpComp.morphismApplied){
                    tpComp.applyMorphism(tagMorphismList.get(0));
                }                
                for(int i=1; i<this.size(); i++){
                    TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();
                    
                    // applichiamo i morphismi al tag piece se non già applicato
                    if(eterComposition && !tp.morphismApplied){
                        tp.applyMorphism(tagMorphismList.get(i)); // TOOD: morphismApplied si può togliere
                    }
                    
                    if(!TagPiece.isUnifiable(tpComp, tp, sharedVars.get(i-1))){
                        unifiable = false;
                        break;
                    } else {
                        Tag epsilon = this.get(i).getTagInstance().getEpsilon();
                        if(tagMorphismList.get(i)!=null)
                            epsilon = tagMorphismList.get(i).getTagInstance().getEpsilon();
                        
                        tpComp = TagPiece.union(tpComp, tp, varMap.get(i-1), epsilon);
                    }
                }
                
                // controlliamo che le labeling function siano unificabili
                if(unifiable){
//                    for(String varName : varMap.get(varMap.size()-1).keySet()){
//                        
//                        for(int i=0; i<this.size(); i++){
//                            if(this.get(i).getVarMap().get(varName)==null) // variabile non presente nella TM
//                                continue;
//                            
//                            TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();
//                            Var v = tp.applyLabelingFunction(varValues.get(i), varName);
//                            
//                        }
//                    }

                    ArrayList<HashMap<String, Var>> varValuesPrimeEdge = new ArrayList<>(this.size());
                    HashMap<String, Var> varValuesComp = new HashMap<>(varMap.get(varMap.size()-1).size());
                    for(int i=0; i<this.size() && unifiable; i++){
                        TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();
                        HashMap<String, Var> varValuesTm = tp.applyLabelingFunction(varValues.get(i));
                        varValuesPrimeEdge.add(varValuesTm);
                        
                        for(Map.Entry<String, Var> var : varValuesTm.entrySet()){
                            if(varValuesComp.get(var.getKey()) == null)
                                varValuesComp.put(var.getKey(), var.getValue());
                            else if(!var.getValue().equals(varValuesComp.get(var.getKey()))){
                                unifiable = false;
                                break;
                            }                                
                        }
                    }

                    if(unifiable){
                        validEdges.add(currentEdge);
                        varValuesPrime.add(varValuesPrimeEdge);
                    }                        
                }
                
                // aggiorniamo currentEdge
                exhausted = true;
                for(int i=0; i<this.size(); i++){
                    int size = this.get(i).getEdges().get(currentState.get(i)).size();
                    if(currentEdge.get(i) < size-1){
                        currentEdge.set(i, currentEdge.get(i)+1);
                        exhausted = false;
                        break;
                    } else {
                        currentEdge.set(i, 0);
                    }
                }
                
            }
            
            if(validEdges.isEmpty()){
                System.out.println("nessuna transizione possibile");
                break;
            }
            
            int choice = -1;
            if(random || validEdges.size() == 1) {
                choice = ThreadLocalRandom.current().nextInt(validEdges.size());
            } else {
                choice = 1; // override choice to select always the same transition
                while(choice < 0 || choice >= validEdges.size()){
//                    System.out.print(String.format("State %d, choice the next state [0-%d]: ", state, nextStateIndexes.size()-1));
//                    choice = scan.nextInt();
                }
            }
            
            // aggiornamento currentEdge, varVector e varValues
            for(int i=0; i<this.size(); i++){
                Edge e = this.get(i).getEdges().get(currentState.get(i)).get(validEdges.get(choice).get(i));
                currentState.set(i, e.getToState());
                
                
                tagVector.set(i, e.getTagPiece().apply(tagVector.get(i))); // si può applicare anche solo tpComp
//                varValues.set(i, e.getTagPiece().applyLabelingFunction(varValues.get(i)));
            }
            varValues = varValuesPrime.get(choice);
            
            xFile.write(String.format("%s %s\n", varValues.get(0).get("x11").toString(), varValues.get(0).get("x21").toString()));
            awFile.write(String.format("%s %s\n", tagVector.get(0).get(0).toString(), varValues.get(0).get("aw").toString()));
        }

        xFile.close();
        awFile.close();
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
    
    
    
    public void simulate(int steps, boolean random) throws Exception {
        simulate(null, steps, random);
    }
    
}
