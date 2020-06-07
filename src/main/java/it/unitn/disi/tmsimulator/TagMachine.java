/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
    
    /*
    public TagMachine applyMorphism(Morphism tagMorphism) throws Exception {
        if(tagMorphism == null)
            return this;
        
        ArrayList<ArrayList<Edge>> edgesM = new ArrayList<>(this.edges.size());
        for(ArrayList<Edge> edgeList : this.edges){
            ArrayList<Edge> edgeListM = new ArrayList<>(edgeList.size());
            edges.add(edgeListM);
            for(Edge e : edgeList){
                edgeListM.add(e.getTagPiece().applyMorphism(tagMorphism));
            }
        }
        
        return new TagMachine(this.varMap, edgesM, this.initialState, this.acceptingStates, this.initialVarValues, tagMorphism.getTagInstance());
    }
    */
    
    // TODO: gestire quando c'è un solo stato o nessuno
    public void simulate(int steps, boolean random, boolean debug) throws Exception {
        Scanner scan = new Scanner(System.in);
        FileWriter xFile = new FileWriter("x_paper.txt");
        FileWriter awFile = new FileWriter("aw_paper.txt");
        FileWriter jFile = new FileWriter("j_paper.txt");
        FileWriter oFile = new FileWriter("output.txt");
        
        Tag[] tagVector = new Tag[varMap.size()];        
        for(int i=0; i<varMap.size(); i++){
            tagVector[i] = tagInstance.getIdentity();
        }
        
        HashMap<String, Var> varValues = new HashMap<>(this.initialVarValues.length);
        for(Map.Entry<String, Integer> entry : this.varMap.entrySet()){
            varValues.put(entry.getKey(), this.initialVarValues[entry.getValue()]);
        }
        
        int state=initialState;
        for(int i=0; i<steps; i++){
            
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
            
            
            int nextStateIndex = -1;                        
            if(random || nextStateIndexes.size() == 1) {
                int rnd = ThreadLocalRandom.current().nextInt(nextStateIndexes.size());
                nextStateIndex = nextStateIndexes.get(rnd);
            } else {
                int choice = -1;
                while(choice < 0 || nextStateIndex >= nextStateIndexes.size()){
                    System.out.print(String.format("State %d, choice the next state [0-%d]: ", state, nextStateIndexes.size()-1));
                    choice = scan.nextInt();
                }
                nextStateIndex = nextStateIndexes.get(choice);
            }
            
            oFile.write("step: "+i+"\n");
            for(Tag t : tagVector) oFile.write(t.toString()+", ");
            oFile.write(String.format("\nrot=%s, cutoff=%s, torque=%s\n", varValues.get("rot"), varValues.get("cutoff"), varValues.get("u")));            
            
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
            xFile.write(String.format("%s %s\n", varValues.get("x11").toString(), varValues.get("x21").toString()));
            awFile.write(String.format("%s %s\n", tagVector[0].toString(), varValues.get("aw").toString()));
            // jFile.write(String.format("%s %s\n", tagVector[0].toString(), varValues.get("j").toString()));
            
            state = edges.get(state).get(nextStateIndex).getToState();
            
        }
        
        xFile.close();
        awFile.close();
        jFile.close();
        oFile.close();
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
        
        /*
        for(ArrayList<Edge> edgeList : this.edges){
            i=0;
            for(Edge e : edgeList){
                sb.append(String.format("\n%d: %d -> %d\n", i, e.getFromState(), e.getToState()));
                i++;
                
                for(Map.Entry<String, Integer> entry : this.varMap.entrySet()){
                    LabelingFunction l = e.getTagPiece().getLabelingFunction(entry.getKey());
                    
                    if(l != null)
                        sb.append(String.format("%s = %s\n", entry.getKey(), l.toString()));
                    else
                        sb.append(String.format("%s = -\n", entry.getKey()));
                }
                
            }
        }
        */
        
        return sb.toString();
    }
    
    /*
    public static TagMachine parseJSON(String filename) throws FileNotFoundException {
        // Gson gson = new Gson();
        // JsonReader reader = new JsonReader(new FileReader(filename));
        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
    }
    */
    
}
