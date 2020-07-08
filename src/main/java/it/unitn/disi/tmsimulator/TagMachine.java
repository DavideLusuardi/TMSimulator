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
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Rappresenta una tag machine.
 * 
 * @author davide
 */
public class TagMachine {
    /**
     * Mappa hash che associa ad ogni nome di variabile la sua posizione negli
     * array.
     */
    private HashMap<String, Integer> varMap;
    
    /**
     * Rappresenta le transizioni tra gli stati.
     */
    private ArrayList<ArrayList<Edge>> edges;
    
    /**
     * Stato iniziale.
     */
    private int initialState;
    
    /**
     * Lista degli stati finali (accepting states).
     */
    private int[] acceptingStates;
    
    /**
     * Valori iniziali delle variabili.
     */
    private Var[] initialVarValues;
    
    /**
     * Istanza di classe {@link it.unitn.disi.tmsimulator.tags.Tag} utilizzata 
     * per generare tag identità o epsilon.
     * L'istanza rapresenta la tag structure della tag machine.
     */
    private Tag tagInstance;

    /**
     * Costruisce una nuova tag machine.
     * 
     * @param varMap mapping variabile-posizione
     * @param edges transizioni
     * @param initialState stato iniziale
     * @param acceptingStates stati finali
     * @param initialVarValues valori iniziali della variabili
     * @param tagInstance istanza di tag
     * @throws Exception 
     */
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
        this.acceptingStates = acceptingStates;
        this.initialVarValues = initialVarValues;
        this.tagInstance = tagInstance;        
    }

    /**
     * Restituisce il mapping variabile-posizione.
     * @return mapping variabile.posizione
     */
    public HashMap<String, Integer> getVarMap() {
        return varMap;
    }

    /**
     * Restituisce le transizioni tra stati.
     * @return le transizioni tra stati
     */
    public ArrayList<ArrayList<Edge>> getEdges() {
        return edges;
    }

    /**
     * Restituisce lo stato iniziale.
     * @return lo stato iniziale
     */
    public int getInitialState() {
        return initialState;
    }

    /**
     * Restituisce gli stati finali.
     * @return gli stati finali
     */
    public int[] getAcceptingStates() {
        return acceptingStates;
    }

    /**
     * Restituisce le variabili iniziali.
     * @return le variabili iniziali
     */
    public Var[] getInitialVarValues() {
        return initialVarValues;
    }

    /**
     * Restituisce l'istanza di tag.
     * @return l'istanza di tag
     */
    public Tag getTagInstance() {
        return tagInstance;
    }
    
    /**
     * Applica il morfismo alla tag machine.
     * Il morfismo viene applicato ad ogni tag piece.
     * 
     * @param tagMorphism morfismo da applicare
     * @throws Exception se il morfismo non può essere applicato alla tag machine
     */
    public void applyMorphism(Morphism tagMorphism) throws Exception {
        if(tagMorphism == null)
            return;
        
        this.tagInstance = tagMorphism.getTagInstance();
        
        for(ArrayList<Edge> edgeList : this.edges){
            for(Edge e : edgeList){
                e.getTagPiece().applyMorphism(tagMorphism);
            }
        }
    }
    
    public long startTime;
    public int stepRange = 1000;
    public ArrayList<ArrayList<Object>> results;
    /**
     * Esegue una simulazione della tag machine percorrendo le sue transizioni.
     * 
     * @param steps numero di transizioni da percorrere
     * @param random indica se scegliere la prossima transizione in modo casuale
     * @param debug indica se stampare i messaggi di debug
     * @throws Exception 
     */
    // TODO: gestire quando c'è un solo stato o nessuno
    public long simulate(int steps, boolean random, boolean debug) throws Exception {
        Scanner scan = new Scanner(System.in);
        FileWriter xFile = new FileWriter("plots/x_without_control.txt");
        FileWriter awFile = new FileWriter("plots/aw_without_control.txt");
        FileWriter oFile = new FileWriter("output.txt");
        
        Runtime rt = Runtime.getRuntime();
        results = new ArrayList<>(steps/stepRange);
        
        Tag[] tagVector = new Tag[varMap.size()];        
        for(int i=0; i<varMap.size(); i++){
            tagVector[i] = tagInstance.getIdentity();
        }
        
        HashMap<String, Var> varValues = new HashMap<>(this.initialVarValues.length);
        for(Map.Entry<String, Integer> entry : this.varMap.entrySet()){
            varValues.put(entry.getKey(), this.initialVarValues[entry.getValue()]);
        }
        
        int state=initialState;
        for(int step=0; step<steps; step++){
            
            if(step % stepRange == 0){
                ArrayList<Object> entry = new ArrayList<>();
                entry.add(step);
                entry.add(System.nanoTime()-startTime);
                entry.add(rt.totalMemory() - rt.freeMemory());
                results.add(entry);
            }
            
            ArrayList<Integer> nextStateIndexes = new ArrayList<>(edges.get(state).size());
            for(int j=0; j<edges.get(state).size(); j++){
                TagPiece tp = edges.get(state).get(j).getTagPiece();
                if(tp.isLabelingFunctionUnifiable(varValues)){
                    nextStateIndexes.add(j);
                }
            }
            
            if(nextStateIndexes.isEmpty()){
                System.out.println("nessuna transizione possibile");
                break;
            }
            
            if(nextStateIndexes.size()==1 && debug){
                System.out.println("una sola transizione possibile");
            }
            
            
            int nextStateIndex = -1;                        
            if(random || nextStateIndexes.size() == 1) {
                int rnd = ThreadLocalRandom.current().nextInt(nextStateIndexes.size());
                nextStateIndex = nextStateIndexes.get(rnd);
            } else {
                int choice = -1;
                choice = 0; // override choice to select always the same transition
                while(choice < 0 || nextStateIndex >= nextStateIndexes.size()){
                    System.out.print(String.format("State %d, choose the next state [0-%d]: ", state, nextStateIndexes.size()-1));
                    choice = scan.nextInt();
                }
                nextStateIndex = nextStateIndexes.get(choice);
            }
            
//            oFile.write("step: "+i+"\n");
//            for(Tag t : tagVector) oFile.write(t.toString()+", ");
//            oFile.write(String.format("\nrot=%s, cutoff=%s, torque=%s\n", varValues.get("rot"), varValues.get("cutoff"), varValues.get("u")));            
            
            TagPiece tagPiece = edges.get(state).get(nextStateIndex).getTagPiece();
            tagVector = tagPiece.apply(tagVector); // update tagVector
            varValues = tagPiece.applyLabelingFunction(varValues); // update varValues // TODO: controllare che la funzione restituisca il risultato corretto
            
            if(debug){
                System.out.println(String.format("\n%d -> %d", edges.get(state).get(nextStateIndex).getFromState(), 
                        edges.get(state).get(nextStateIndex).getToState()));
                System.out.println("<var> : <tag> , <value>");
                for(Map.Entry<String, Integer> entry : varMap.entrySet()){
                    String var = entry.getKey();
                    Integer j = entry.getValue();
                    System.out.println(String.format("%s : %s , %s", var, tagVector[j].toString(), varValues.get(var).toString()));
                }
                System.out.println("");
            }
//            xFile.write(String.format("%s %s\n", varValues.get("x11").toString(), varValues.get("x21").toString()));
//            awFile.write(String.format("%s %s\n", tagVector[0].toString(), varValues.get("aw").toString()));
//            jFile.write(String.format("%s %s\n", tagVector[0].toString(), varValues.get("j").toString()));
            
            state = edges.get(state).get(nextStateIndex).getToState();
            
        }
        
        xFile.close();
        awFile.close();
        oFile.close();
        
//        System.gc();
        long usedByte = (rt.totalMemory() - rt.freeMemory());
        return usedByte;
    }

    /**
     * Rappresentazione della tag machine come stringa.
     * @return la stringa che rappresenta la tag machine
     */
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
        
        for(ArrayList<Edge> edgeList : this.edges){
            i=0;
            for(Edge e : edgeList){
                sb.append(String.format("\n%d: %d -> %d\n", i, e.getFromState(), e.getToState()));
                i++;
                
                sb.append(e.getTagPiece().toString());
            }
        }
        
        return sb.toString();
    }
    
}
