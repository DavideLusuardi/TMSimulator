/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author davide
 */
public class TagPiece {
    private Tag[][] matrix;
    private LabelingFunction[] labelingFunction;
    private Integer[] domLabelingFunction;
    private HashMap<String, Integer> varMap;

    // TODO: controllare che tag cambia quando var appartiene a Dom(v)
    public TagPiece(Tag[][] matrix, LabelingFunction[] labelingFunction, HashMap<String, Integer> varMap) throws Exception {
        setMatrix(matrix);
        if(labelingFunction.length != matrix.length){
            throw new Exception("il vettore labelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }

        this.labelingFunction = labelingFunction;
        setDomLabelingFunction(labelingFunction);
        
        this.varMap = varMap;
    }
    
    public TagPiece(Tag[][] matrix, Var[] constLabelingFunction, HashMap<String, Integer> varMap) throws Exception {
        setMatrix(matrix);
        
        if(constLabelingFunction.length != matrix.length){
            throw new Exception("il vettore constLabelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        setLabelingFunction(constLabelingFunction);
        setDomLabelingFunction(labelingFunction);
        this.varMap = varMap;
    }
    
    public TagPiece(Tag[][] matrix, Var[] constLabelingFunction, String[] varNames) throws Exception {
        setMatrix(matrix);
        
        if(constLabelingFunction.length != matrix.length){
            throw new Exception("il vettore constLabelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        setLabelingFunction(constLabelingFunction);
        setDomLabelingFunction(labelingFunction);
        
        if(varNames.length != matrix.length || varNames.length != constLabelingFunction.length){
            throw new Exception("il vettore varNames ha lunghezza incompatibile");
        }
        
        this.varMap = new HashMap<>(varNames.length);
        for(int i=0; i<varNames.length; i++){
            this.varMap.put(varNames[i], i);
        }
    }

    private void setMatrix(Tag[][] matrix) throws Exception {
        if(matrix.length == 0 || matrix.length != matrix[0].length){
            throw new Exception("la matrice TagPiece deve essere quadrata");
        }
        this.matrix = matrix;
    }

    private void setDomLabelingFunction(LabelingFunction[] labelingFunction) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0; i<labelingFunction.length; i++){
            if(labelingFunction[i] != null)
                indexes.add(i);
        }
        this.domLabelingFunction = indexes.toArray(new Integer[0]);
    }

    private void setLabelingFunction(Var[] constLabelingFunction) {
        this.labelingFunction = new LabelingFunction[constLabelingFunction.length];
        for(int i=0; i<constLabelingFunction.length; i++){
            final Var v = constLabelingFunction[i]; // TODO: controllare che tutto funzioni correttamente quando v == null
            
            if(v == null)
                this.labelingFunction[i] = null;
            else
                this.labelingFunction[i] = new LabelingFunction() {
                    @Override
                    public Var apply(HashMap<String, Var> varValues) {
                        return v;
                    }

                    @Override
                    public String toString() {
                        return v.getValue().toString();
                    }
                };
        }
    }
    

    public Var labelingFunction(String varName, HashMap<String, Var> varVector) {
        return labelingFunction[this.varMap.get(varName)].apply(varVector);
    }
    
    // crea un nuovo varVector con i valori delle variabili calcolati basandosi su quello vecchio
    public HashMap<String, Var> labelingFunction(HashMap<String, Var> varVector) {
        HashMap<String, Var> varVectorPrime = new HashMap<>(varVector);
        for(Map.Entry<String, Var> varValue : varVector.entrySet()){
            varVectorPrime.put(varValue.getKey(), labelingFunction(varValue.getKey(), varVector));
        }
        return varVectorPrime;
    }
    
    public Tag[] apply(Tag[] tagVector) throws Exception {
        if(tagVector.length != this.matrix.length){
            throw new Exception("il vettore dei tag ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        // TODO: gestire quando è presente epsilon e quando ordinamento parziale (-> non esiste max)
        Tag[] tagVectorPrime = new Tag[tagVector.length];
        for(int i=0; i < tagVector.length; i++){
            Tag tMax = tagVector[i]; // nel caso in cui tag piece ha una colonna di epsilon, il tag non cambia
            
            for(int j=0; j < tagVector.length; j++){
                Tag t = tagVector[j].concatenate(matrix[j][i]);
                if(t.gt(tMax)){
                    tMax = t;
                }
            }
            
            tagVectorPrime[i] = tMax;
        }
        
        return tagVectorPrime;
    }

    public LabelingFunction[] getLabelingFunction() {
        return labelingFunction;
    }
    
    public LabelingFunction getLabelingFunction(String varName) {
        return labelingFunction[this.varMap.get(varName)];
    }
    
    public Integer[] domLabelingFunction(){
        return domLabelingFunction;
    }

    public Tag[][] getMatrix() {
        return matrix;
    }
    
    // TODO: fix java.lang.ArrayStoreException
    public void applyMorphism(Morphism tagMorphism) throws Exception {
        Tag[][] m = new Tag[this.matrix.length][this.matrix.length];
        
        for(int i=0; i<this.matrix.length; i++){
            for(int j=0; j<this.matrix.length; j++){
                m[i][j] = tagMorphism.map(this.matrix[i][j]);
            }
        }
        this.matrix = m;
    }
    
    public static boolean isLabelingFunctionUnifiable(TagPiece tp1, TagPiece tp2, ArrayList<String> sharedVars){
        for(String varName : sharedVars){
            LabelingFunction l1 = tp1.getLabelingFunction(varName);
            LabelingFunction l2 = tp2.getLabelingFunction(varName);
            
            if(l1 == null){
                if(l2 != null)
                    return false;
            } else if(!l1.equals(l2)){
                return false;
            }
        }
        return true;
    }
    
    // TODO: bisogna aggiungere il controllo che le labeling function siano uguali
    public static boolean isUnifiable(TagPiece tp1, TagPiece tp2, 
            HashMap<String, Integer> varMap1, HashMap<String, Integer> varMap2, 
            ArrayList<String> sharedVars){
        
        Tag[][] m1 = tp1.getMatrix();
        Tag[][] m2 = tp2.getMatrix();
        for(String w : sharedVars){
            for(String v : sharedVars){
                Integer i1 = varMap1.get(w);
                Integer j1 = varMap1.get(v);
                Integer i2 = varMap2.get(w);
                Integer j2 = varMap2.get(v);
                
                Tag t1 = m1[i1][j1];
                Tag t2 = m2[i2][j2];
                if(!(m1[i1][j1].equals(m2[i2][j2]) && isLabelingFunctionUnifiable(tp1, tp2, sharedVars))
                        /*|| !(tp1.labelingFunction(j1) == null && 
                        tp2.labelingFunction(j2) == null || tp1.labelingFunction(j1) != null && 
                        tp1.labelingFunction(j1).equals(tp2.labelingFunction(j2)))*/ ){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
    // TODO: migliorare passando solo var non comuni
    public static TagPiece union(TagPiece tp1, TagPiece tp2, 
            HashMap<String, Integer> varMap1, HashMap<String, Integer> varMap2, 
            HashMap<String, Integer> varMapComp, Tag epsilon) throws Exception {
        
        Tag[][] matrix = new Tag[varMapComp.size()][varMapComp.size()];
        // Var[] labelingFunction = new Var[varMapComp.size()];
        LabelingFunction[] labelingFunction = new LabelingFunction[varMapComp.size()];
        
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[0].length; j++){
                matrix[i][j] = epsilon;
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap1.entrySet()){
            labelingFunction[varMapComp.get(v.getKey())] = tp1.getLabelingFunction()[v.getValue()];
            for(Map.Entry<String, Integer> w : varMap1.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp1.getMatrix()[v.getValue()][w.getValue()];
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap2.entrySet()){
            labelingFunction[varMapComp.get(v.getKey())] = tp2.getLabelingFunction()[v.getValue()];
            for(Map.Entry<String, Integer> w : varMap2.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp2.getMatrix()[v.getValue()][w.getValue()];
            }
        }        
        
        TagPiece tpComp = new TagPiece(matrix, labelingFunction, varMapComp);
        return tpComp;
    }

}
