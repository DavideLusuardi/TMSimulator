/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.morphism.Morphism;
import it.unitn.disi.tmsimulator.tags.Tag;
import it.unitn.disi.tmsimulator.variables.Var;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * La classe TagMachineSet rappresenta un insieme di tag machine su cui è 
 * possibile eseguire la composizione. Tale composizione può essere omogenea o
 * eterogenea. In quest'ultimo caso sono necessari i morfismi da 
 * applicare ad ogni tag machine.
 * 
 * 
 * @author davide
 */
public class TagMachineSet extends ArrayList<TagMachine> {
    
    /**
     * Esegue la composizione omogenea di due tag machine (composizione binaria).
     * 
     * @param tm1 prima tag machine
     * @param tm2 seconda tag machine
     * @return tag machine risultato della composizione
     * @throws Exception 
     */
    public TagMachine compose(TagMachine tm1, TagMachine tm2) throws Exception {
        
        // TODO: controllare che le TMs abbiano dimensione > 0
        
        // ricaviamo le variabili condivise e la mappatura variabile-posizione per la composizione
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
        
        // mappatura stato-indice per la tag machine composizione
        HashMap<Integer, HashMap<Integer, Integer>> mapStateIndex = new HashMap<>(tm1.getEdges().size());
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>(tm1.getEdges().size());
        edges.add(new ArrayList<>());
        HashMap<Integer, Integer> secondMap = new HashMap<>();
        secondMap.put(tm2.getInitialState(), 0);
        mapStateIndex.put(tm1.getInitialState(), secondMap);
        
        LinkedList<Integer> stateQueue1 = new LinkedList<>(); // coda degli stati aggiunti per TM1
        LinkedList<Integer> stateQueue2 = new LinkedList<>(); // coda degli stati aggiunti per TM2
        stateQueue1.add(tm1.getInitialState());
        stateQueue2.add(tm2.getInitialState());
        
        while(!stateQueue1.isEmpty()){
            int s1 = stateQueue1.poll();
            int s2 = stateQueue2.poll();
            for(Edge e1 : tm1.getEdges().get(s1)){
                for(Edge e2 : tm2.getEdges().get(s2)){
                    
                    if(TagPiece.isUnifiable(e1.getTagPiece(), e2.getTagPiece(), sharedVars)){
                        TagPiece tp = TagPiece.union(e1.getTagPiece(), e2.getTagPiece(), 
                                varMap, tm1.getTagInstance().getEpsilon());

                        // controlliamo se lo stato (e1.getToState(), e2.getToState()) non esiste ancora
                        secondMap = null;
                        if(mapStateIndex.get(e1.getToState()) == null){
                            secondMap = new HashMap<>();
                            mapStateIndex.put(e1.getToState(), secondMap);
                        } else if(mapStateIndex.get(e1.getToState()).get(e2.getToState()) == null){
                            secondMap = mapStateIndex.get(e1.getToState());
                        }
                        
                        if(secondMap != null){
                            secondMap.put(e2.getToState(), edges.size()); // il nuovo stato assume indice pari a edges.size()
                            stateQueue1.add(e1.getToState());
                            stateQueue2.add(e2.getToState());
                            edges.add(new ArrayList<>()); // aggiungo il nuovo stato (inizialmente senza transizioni)
                        }
                        
                        int fromState = mapStateIndex.get(e1.getFromState()).get(e2.getFromState());
                        int toState = mapStateIndex.get(e1.getToState()).get(e2.getToState());
                        Edge e = new Edge(fromState, toState, tp);
                        edges.get(fromState).add(e);
                    }
                }
            }
        }
        
        int initialState = mapStateIndex.get(tm1.getInitialState()).get(tm2.getInitialState());
        ArrayList<Integer> acceptingStates = new ArrayList<>();
        for(int i : tm1.getAcceptingStates()){
            for(int j : tm2.getAcceptingStates()){
                if(mapStateIndex.get(i) != null && mapStateIndex.get(i).get(j) != null)
                    acceptingStates.add(mapStateIndex.get(i).get(j));
            }
        }
        
        // from ArrayList to array
        int[] accStates = new int[acceptingStates.size()];
        for(int i=0; i<acceptingStates.size(); i++)
            accStates[i] = acceptingStates.get(i);
        
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
     * Esegue la composizione omogenea di tutte le tag machine presenti. Partendo 
     * dalla prima, viene eseguita la composizione con la seconda ottenendo la 
     * tag machine che rappresenta la composione. A sua volta quest'ultima viene
     * composta con la terza e così via.
     * 
     * @return la tag machine che rappresenta la composizione
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
     * Esegue la composizione eterogenea di tutte le tag machine presenti. Prima 
     * di eseguirne la composizione viene applicato il relativo morfismo alla
     * tag machine. Nel caso in cui il morfismo è null, la tag machine rimane
     * invariata.
     * Partendo dalla prima tag machine, viene eseguita la composizione con la 
     * seconda ottenendo la tag machine che rappresenta la composione. A sua volta 
     * quest'ultima viene composta con la terza e così via.
     * 
     * @param tagMorphismList lista di morfismi da applicare alle tag machine
     * @return la tag machine che rappresenta la composizione
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
        }
        return tmComp;
    }
    
    public int stepRange = 1000;
    public ArrayList<ArrayList<Object>> results;
    
    // TODO: controllare che le TMs abbiano dimensione > 0
    public long simulate(ArrayList<Morphism> tagMorphismList, Tag tagInstance, int steps, boolean random) throws Exception {
        Scanner scan = new Scanner(System.in);
        FileWriter xFile = new FileWriter("plots/x_without_control.txt");
        FileWriter awFile = new FileWriter("plots/aw_without_control.txt");
        
        Runtime rt = Runtime.getRuntime();
        results = new ArrayList<>(steps/stepRange);
        long startTime = System.nanoTime();
        
        boolean eterComposition = (tagMorphismList != null);
        
        if(this.size() < 2) // TODO: simulazione va bene anche con una sola TM
            throw new Exception("la composizione richiede almeno due TagMachine");
        else if(eterComposition && tagMorphismList.size() != this.size())
            throw new Exception("la lunghezza della lista dei tagMorphism deve corrispondere a quella di TagMachineSet");    
        
        if(!eterComposition)
            tagInstance = this.get(0).getTagInstance();
        
        
        HashMap<String, Integer> varMap = new HashMap<>(this.get(0).getVarMap());
        ArrayList<ArrayList<String>> sharedVars = new ArrayList<>(this.size());
        int pos = varMap.size();
        for(int i=1; i<this.size(); i++){
            sharedVars.add(new ArrayList<>());
            for(String var : this.get(i).getVarMap().keySet()){
                Integer val = varMap.putIfAbsent(var, pos);
                if(val == null) // variabile non presente
                    pos++;
                else
                    sharedVars.get(i-1).add(var);
            }
        }
        
        // inizializziamo currentState con gli stati iniziali di tutte le TM
        // e varValues con i valori iniziali delle variabili
        ArrayList<Integer> currentState = new ArrayList<>(this.size());                
        HashMap<String, Var> varValues = new HashMap<>(varMap.size());
        
        for(int i=0; i<this.size(); i++){
            TagMachine tm = this.get(i);
            currentState.add(tm.getInitialState());
            
            // inizializzazione varValues
            for(Map.Entry<String, Integer> entry : tm.getVarMap().entrySet()){
                Var currentVar = varValues.putIfAbsent(entry.getKey(), tm.getInitialVarValues()[entry.getValue()]);
                if(currentVar != null && !currentVar.equals(tm.getInitialVarValues()[entry.getValue()])){
                    throw new Exception("I valori iniziali delle variabili non sono uguali tra le tag machine");
                }
            }
        }
        
        // inizializziamo tagVector con il tag identità
        ArrayList<Tag> tagVector = new ArrayList<>(varMap.size());
        for(int i=0; i<varMap.size(); i++)
            tagVector.add(tagInstance.getIdentity());
        
        // visitiamo le TM in parallelo verificando che la transizione sia unificabile
        for(int step=0; step<steps; step++){
            boolean exhausted = false;
            
            if(step % stepRange == 0){
                ArrayList<Object> entry = new ArrayList<>();
                entry.add(step);
                entry.add(System.nanoTime()-startTime);
                entry.add(rt.totalMemory() - rt.freeMemory());
                results.add(entry);
            }
            
            // inizializziamo currentEdge con la tupla corrispondente alla prima transizione di ogni TM
            ArrayList<Integer> currentEdge = new ArrayList<>(this.size());
            for(int i=0; i<this.size(); i++){
                if(this.get(i).getEdges().get(currentState.get(i)).isEmpty()){
                    exhausted = true; // nessuna transizione possibile
                    break;
                }
                currentEdge.add(0);
            }
            
            // salviamo le transizioni unificabili ed i valori delle variabili calcolati
            ArrayList<ArrayList<Integer>> validEdges = new ArrayList<>();
            ArrayList<HashMap<String, Var>> varValuesPrime = new ArrayList<>();
            ArrayList<TagPiece> tpPrime = new ArrayList<>();
            
            // consideriamo ogni combinazione di transizioni
            while(!exhausted){

                // controlliamo che i tag piece siano unificabili    
                boolean unifiable = true;
                Tag[][] matrixComp = new Tag[varMap.size()][varMap.size()];
                for(int i=0; i<matrixComp.length; i++)
                    for(int j=0; j<matrixComp.length; j++)
                        matrixComp[i][j] = tagInstance.getEpsilon();
                
                TagPiece tp0 = this.get(0).getEdges().get(currentState.get(0)).get(currentEdge.get(0)).getTagPiece();
                if(eterComposition && !tp0.morphismApplied)
                    tp0.applyMorphism(tagMorphismList.get(0));
                
                for(int i=0; i<tp0.getMatrix().length; i++)
                    for(int j=0; j<tp0.getMatrix().length; j++)
                        matrixComp[i][j] = tp0.getMatrix()[i][j];
                
                TagPiece tpComp = new TagPiece(matrixComp, varMap);
                for(int i=1; i<this.size() && unifiable; i++){
                    TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();

                    // applichiamo il morphismo al tag piece se non già applicato
                    if(eterComposition && !tp.morphismApplied){
                        tp.applyMorphism(tagMorphismList.get(i));
                    }

                    if(!TagPiece.isUnifiable(tpComp, tp, sharedVars.get(i-1))){
                        unifiable = false;
                    } else {
                        TagPiece.union(tpComp, tp);
                    }
                }
                
                // controlliamo che le labeling function siano unificabili
                if(unifiable){
                    HashMap<String, Var> varValuesComp = new HashMap<>(varMap.size());
                    for(int i=0; i<this.size() && unifiable; i++){
                        TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();
                        HashMap<String, Var> varValuesTm = tp.applyLabelingFunction(varValues);
                        
                        for(Map.Entry<String, Var> var : varValuesTm.entrySet()){
                            Var currentVar = varValuesComp.putIfAbsent(var.getKey(), var.getValue());
                            if(currentVar != null && !var.getValue().equals(currentVar)){
                                unifiable = false;
                                break;
                            }                                
                        }
                    }

                    if(unifiable){
                        validEdges.add(new ArrayList<>(currentEdge));
                        varValuesPrime.add(varValuesComp);
                        tpPrime.add(tpComp);
                    }                        
                }                
                
                // aggiornamento di currentEdge
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
            
            // viene scelta una transizione (random o dall'utente)
            int choice = -1;
            if(random || validEdges.size() == 1) {
                choice = ThreadLocalRandom.current().nextInt(validEdges.size());
            } else {
                choice = 0; // override choice to select always the same transition
                while(choice < 0 || choice >= validEdges.size()){
                    System.out.println(validEdges);
                    System.out.print(String.format("Choose the transition [0-%d]: ", validEdges.size()));
                    choice = scan.nextInt();
                }
            }
            
            // aggiornamento currentEdge, tagVector e varValues
            for(int i=0; i<this.size(); i++){
                Edge e = this.get(i).getEdges().get(currentState.get(i)).get(validEdges.get(choice).get(i));
                currentState.set(i, e.getToState());
            }
            varValues = varValuesPrime.get(choice);
            tagVector = tpPrime.get(choice).apply(tagVector);
            
//            xFile.write(String.format("%s %s\n", varValues.get(0).get("x11").toString(), varValues.get(0).get("x21").toString()));
//            awFile.write(String.format("%s %s\n", tagVector.get(0).get(0).toString(), varValues.get(0).get("aw").toString()));
        }

        xFile.close();
        awFile.close();
        
//        System.gc();
        long usedByte = (rt.totalMemory() - rt.freeMemory());
        return usedByte;
    }
    
    public long simulate2(ArrayList<Morphism> tagMorphismList, Tag tagInstance, int steps, boolean random) throws Exception {
        Scanner scan = new Scanner(System.in);
        
        Runtime rt = Runtime.getRuntime();
        results = new ArrayList<>(steps/stepRange);
        long startTime = System.nanoTime();
        
        boolean eterComposition = (tagMorphismList != null);
        
        if(this.size() < 2) // TODO: simulazione va bene anche con una sola TM
            throw new Exception("la composizione richiede almeno due TagMachine");
        else if(eterComposition && tagMorphismList.size() != this.size())
            throw new Exception("la lunghezza della lista dei tagMorphism deve corrispondere a quella di TagMachineSet");    
        
        if(eterComposition)
            tagInstance = this.get(0).getTagInstance();
        
        
        // costruzione degli array varMap e sharedVars della i-esima TM rispetto alla composizione di quelle precedenti
        HashMap<String, Integer> varMap = new HashMap<>(this.get(0).getVarMap());
        ArrayList<ArrayList<String>> sharedVars = new ArrayList<>(this.size());
        int pos = varMap.size();
        for(int i=1; i<this.size(); i++){
            sharedVars.add(new ArrayList<>());
            for(String var : this.get(i).getVarMap().keySet()){
                Integer val = varMap.putIfAbsent(var, pos);
                if(val == null) // variabile non presente
                    pos++;
                else
                    sharedVars.get(i-1).add(var);
            }
        }
        
        // inizializziamo currentState con gli stati iniziali di tutte le TM,
        // tagVector con il tag identità e varValues con i valori iniziali delle variabili
        ArrayList<Integer> currentState = new ArrayList<>(this.size());        
        ArrayList<ArrayList<Tag>> tagVector = new ArrayList<>(this.size());        
        ArrayList<HashMap<String, Var>> varValues = new ArrayList<>(this.size());
        
        for(int i=0; i<this.size(); i++){
            TagMachine tm = this.get(i);
            currentState.add(tm.getInitialState());
            
            // inizializzazione tagVector
            ArrayList<Tag> tv = new ArrayList<>(tm.getVarMap().size());
            for(int j=0; j<tm.getVarMap().size(); j++)
                tv.add(tagInstance.getIdentity());            
            tagVector.add(tv);
            
            // inizializzazione varValues
            HashMap<String, Var> vv = new HashMap<>(tm.getInitialVarValues().length);
            for(Map.Entry<String, Integer> entry : tm.getVarMap().entrySet())
                vv.put(entry.getKey(), tm.getInitialVarValues()[entry.getValue()]);
            varValues.add(vv);
        }
        
//        Tag[] tagVector = new Tag[varMap.size()];
//        for(int i=0; i<tagVector.length; i++)
//            tagVector[i] = tagInstance.getIdentity();
                
        // array memory = 4*capacity
//        long capacity = Runtime.getRuntime().freeMemory()/(4+32+4+128)/100;
//        System.out.println("free memory: "+capacity);
//        if(capacity > Integer.MAX_VALUE)
//            capacity = (long)Integer.MAX_VALUE;
        HashMap<String, Boolean> unifiableTpMap = new HashMap<>();
//        HashMap<String, TagPiece> tpMap = new HashMap<>();                
        
        // visitiamo le TM in parallelo verificando che la transizione sia unificabile
        for(int step=0; step<steps; step++){
            boolean exhausted = false;
            
            if(step % stepRange == 0){
                ArrayList<Object> entry = new ArrayList<>();
                entry.add(step);
                entry.add(System.nanoTime()-startTime);
                entry.add(rt.totalMemory() - rt.freeMemory());
                results.add(entry);
            }
            
            // inizializziamo currentEdge con la tupla corrispondente alla prima transizione di ogni TM
            ArrayList<Integer> currentEdge = new ArrayList<>(this.size());
            for(int i=0; i<this.size(); i++){
                if(this.get(i).getEdges().get(currentState.get(i)).isEmpty()){
                    exhausted = true; // nessuna transizione possibile
                    break;
                }
                currentEdge.add(0);
            }
            
            // salviamo le transizioni unificabili ed i valori delle variabili calcolati
            ArrayList<ArrayList<Integer>> validEdges = new ArrayList<>();
            ArrayList<ArrayList<HashMap<String, Var>>> varValuesPrime = new ArrayList<>();
//            ArrayList<TagPiece> tpCompEdges = new ArrayList<>();
            
            // consideriamo ogni combinazione di transizioni
            while(!exhausted){
                String transitionId=currentEdge.toString()+currentState.toString();
                
                // controlliamo che i tag piece siano unificabili    
                boolean unifiable = true;
                if(unifiableTpMap.get(transitionId)!=null){
                    unifiable = unifiableTpMap.get(transitionId);
                    
                } else {
                    Tag[][] matrixComp = new Tag[varMap.size()][varMap.size()];
                    for (int i = 0; i < matrixComp.length; i++) {
                        for (int j = 0; j < matrixComp.length; j++) {
                            matrixComp[i][j] = tagInstance.getEpsilon();
                        }
                    }

                    TagPiece tp0 = this.get(0).getEdges().get(currentState.get(0)).get(currentEdge.get(0)).getTagPiece();
                    if (eterComposition && !tp0.morphismApplied) {
                        tp0.applyMorphism(tagMorphismList.get(0));
                    }

                    for (int i = 0; i < tp0.getMatrix().length; i++) {
                        for (int j = 0; j < tp0.getMatrix().length; j++) {
                            matrixComp[i][j] = tp0.getMatrix()[i][j];
                        }
                    }

                    TagPiece tpComp = new TagPiece(matrixComp, varMap);                
                    for(int i=1; i<this.size() && unifiable; i++){
                        TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();

                        // applichiamo il morphismo al tag piece se non già applicato
                        if(eterComposition && !tp.morphismApplied){
                            tp.applyMorphism(tagMorphismList.get(i));
                        }

                        if(!TagPiece.isUnifiable(tpComp, tp, sharedVars.get(i-1))){
                            unifiable = false;
                        } else {
                            TagPiece.union(tpComp, tp);
                        }
                    }
                    
//                    if(unifiableTpMap.size() % 100000 == 0)
//                        System.out.println(unifiableTpMap.size());

                    int freeMemThr = 1024*1024*10;
                    if(Runtime.getRuntime().freeMemory() < freeMemThr && !unifiableTpMap.keySet().isEmpty()){
                        unifiableTpMap.remove(unifiableTpMap.keySet().iterator().next());
//                        unifiableTpMap.clear();
                    }
                    unifiableTpMap.put(transitionId, unifiable);
//                    tpMap.put(transitionId, tpComp);
                }
//                tpCompEdges.add(tpMap.get(transitionId));
                
                // controlliamo che le labeling function siano unificabili
                if(unifiable){
                    ArrayList<HashMap<String, Var>> varValuesEdge = new ArrayList<>(this.size());
                    HashMap<String, Var> varValuesComp = new HashMap<>(varMap.size());
                    for(int i=0; i<this.size() && unifiable; i++){
                        TagPiece tp = this.get(i).getEdges().get(currentState.get(i)).get(currentEdge.get(i)).getTagPiece();
                        HashMap<String, Var> varValuesTm = tp.applyLabelingFunction(varValues.get(i));
                        varValuesEdge.add(varValuesTm);
                        
                        for(Map.Entry<String, Var> var : varValuesTm.entrySet()){
                            Var currentVar = varValuesComp.putIfAbsent(var.getKey(), var.getValue());
                            if(currentVar != null && !var.getValue().equals(currentVar)){
                                unifiable = false;
                                break;
                            }                                
                        }
                    }

                    if(unifiable){
                        validEdges.add(new ArrayList<>(currentEdge));
                        varValuesPrime.add(varValuesEdge);
                    }                        
                }                
                
                // aggiornamento di currentEdge
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
            
            // viene scelta una transizione (random o dall'utente)
            int choice = -1;
            if(random || validEdges.size() == 1) {
                choice = ThreadLocalRandom.current().nextInt(validEdges.size());
            } else {
                choice = 0; // override choice to select always the same transition
                while(choice < 0 || choice >= validEdges.size()){
                    System.out.println(validEdges);
                    System.out.print(String.format("Choose the transition [0-%d]: ", validEdges.size()));
                    choice = scan.nextInt();
                }
            }
            
            // aggiornamento currentEdge, tagVector e varValues
            for(int i=0; i<this.size(); i++){
                Edge e = this.get(i).getEdges().get(currentState.get(i)).get(validEdges.get(choice).get(i));
                currentState.set(i, e.getToState());
                
                tagVector.set(i, e.getTagPiece().apply(tagVector.get(i)));
            }
            varValues = varValuesPrime.get(choice);
//            tagVector = tpCompEdges.get(choice).apply(tagVector);
        }

//        System.gc();
        long usedByte = (rt.totalMemory() - rt.freeMemory());
        return usedByte;
    }
    
    public long simulate(int steps, boolean random) throws Exception {
        return simulate(null, null, steps, random);
    }
    
}
