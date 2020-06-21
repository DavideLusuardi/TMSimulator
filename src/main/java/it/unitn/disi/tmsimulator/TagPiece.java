/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.tags.Tag;
import it.unitn.disi.tmsimulator.variables.Var;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta un tag piece.
 * 
 * @author davide
 */
public class TagPiece {
    
    /**
     * Matrice dei tag.
     */
    private Tag[][] matrix;
    
    /**
     * Rappresenta la labeling function.
     * Quando si compongono/uniscono due tag piece, le labeling function di 
     * entrambi vengono aggiunte alla lista. In tal modo si può verificare
     * durante la simulazione della tag machine se si sincronizzano.
     */
    // TODO: decidere se labelingFunction può contenere null
    private ArrayList<ArrayList<LabelingFunction>> labelingFunction; // TODO: implementare una struttura dati con complessità di concatenzaione costante
    
    /**
     * Mappa hash che associa ad ogni nome di variabile la sua posizione negli
     * array
     */
    private HashMap<String, Integer> varMap;
    
    public TagPiece(Tag[][] matrix, ArrayList<ArrayList<LabelingFunction>> labelingFunction, 
            HashMap<String, Integer> varMap) throws Exception {
        
        setMatrix(matrix);
        if(labelingFunction.size() != matrix.length){
            throw new Exception("il vettore labelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }

        this.labelingFunction = labelingFunction;        
        this.varMap = varMap;
    }
    
    public TagPiece(Tag[][] matrix, LabelingFunction[] labelingFunction, HashMap<String, 
            Integer> varMap) throws Exception {
        
        setMatrix(matrix);
        if(labelingFunction.length != matrix.length){
            throw new Exception("il vettore labelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }

        this.labelingFunction = new ArrayList<>(labelingFunction.length);
        for(LabelingFunction lf : labelingFunction){
            ArrayList<LabelingFunction> lflist = new ArrayList<>();
            lflist.add(lf);
            this.labelingFunction.add(lflist);
        }
        
        this.varMap = varMap;
    }
    
    public TagPiece(Tag[][] matrix, Var[] constLabelingFunction, 
            HashMap<String, Integer> varMap) throws Exception {
        
        setMatrix(matrix);
        
        if(constLabelingFunction.length != matrix.length){
            throw new Exception("il vettore constLabelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        setLabelingFunction(constLabelingFunction);
        this.varMap = varMap;
    }
    
    public TagPiece(Tag[][] matrix, Var[] constLabelingFunction, 
            String[] varNames) throws Exception {
        
        setMatrix(matrix);
        
        if(constLabelingFunction.length != matrix.length){
            throw new Exception("il vettore constLabelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        setLabelingFunction(constLabelingFunction);
        
        if(varNames.length != matrix.length || varNames.length != constLabelingFunction.length){
            throw new Exception("il vettore varNames ha lunghezza incompatibile");
        }
        
        this.varMap = new HashMap<>(varNames.length);
        for(int i=0; i<varNames.length; i++){
            this.varMap.put(varNames[i], i);
        }
    }

    public Tag[][] getMatrix() {
        return matrix;
    }
    
    private void setMatrix(Tag[][] matrix) throws Exception {
        if(matrix.length == 0 || matrix.length != matrix[0].length){
            throw new Exception("la matrice TagPiece deve essere quadrata");
        }
        this.matrix = matrix;
    }

    public ArrayList<ArrayList<LabelingFunction>> getLabelingFunction() {
        return labelingFunction;
    }
    
    private void setLabelingFunction(Var[] constLabelingFunction) {
        this.labelingFunction = new ArrayList<>(constLabelingFunction.length);
        for(final Var v : constLabelingFunction){
            ArrayList<LabelingFunction> lflist = new ArrayList<>();
            
            if(v == null)
                lflist.add(null); // TODO: controllare che tutto funzioni correttamente quando v == null
            else
                lflist.add(new LabelingFunction() {
                    @Override
                    public Var apply(HashMap<String, Var> varValues) {
                        return v;
                    }

                    @Override
                    public String toString() {
                        return v.getValue().toString();
                    }
                });
            
            this.labelingFunction.add(lflist);
        }
    }
    
    /**
     * Sulla base del tag vector passato, calcola il nuovo tag vector utilizzando
     * la matrice dei tag.
     * 
     * @param tagVector
     * @return Il nuovo vettore dei tag.
     * @throws Exception 
     */
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
    
    /**
     * Verifica che le labeling function applicate ai valori delle variabili 
     * siano sincronizzabili.
     * 
     * @param varValues
     * @return 
     */
    public boolean isLabelingFunctionUnifiable(HashMap<String, Var> varValues){
        for(ArrayList<LabelingFunction> lflist : this.labelingFunction){
            LabelingFunction lf0 = lflist.get(0);
            Var v0 = null;
            if(lf0 != null)
                v0 = lf0.apply(varValues); // TODO: assicurarsi ci sia almeno un elemento
            
            for(int i=1; i<lflist.size(); i++){
                LabelingFunction lfi = lflist.get(i);
                if(lf0 == null){
                    if(lfi != null)
                        return false;
                } else if(!v0.equals(lfi.apply(varValues))){ // TODO: verificare v0 != null
                    return false;
                }                
            }
            
        }
        
        return true;
    }
    
    /**
     * Calcola i nuovi valori delle variabili applicando la labeling function.
     * 
     * @param varValues
     * @return 
     */
    public HashMap<String, Var> applyLabelingFunction(HashMap<String, Var> varValues){
        HashMap<String, Var> varValuesPrime = new HashMap<>(varValues);
        
        for(Map.Entry<String, Integer> entry : this.varMap.entrySet()){
            Var v;
            if(this.labelingFunction.get(entry.getValue()).get(0) != null)
                v = this.labelingFunction.get(entry.getValue()).get(0).apply(varValues); // TODO: assicurarsi ci sia almeno un elemento
            else
                v = varValues.get(entry.getKey()); // la variabile mantiene lo stesso valore perchè la labeling function non è definita su di essa
            
            varValuesPrime.put(entry.getKey(), v);
        }
        
        return varValuesPrime;
    }
    
    /**
     * Applica il morfismo al tag piece andando ad aggiornare la matrice dei tag.
     * 
     * @param tagMorphism
     * @throws Exception 
     */
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
    
    /**
     * Verifica che due tag piece siano unificabili.
     * 
     * @param tp1
     * @param tp2
     * @param varMap1
     * @param varMap2
     * @param sharedVars
     * @return 
     */
    // TODO: aggiungere il controllo che le labeling function siano sincronizzabili?
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
                if(!(m1[i1][j1].equals(m2[i2][j2]) /*&& isLabelingFunctionUnifiable(tp1, tp2, sharedVars)*/)
                        /*|| !(tp1.labelingFunction(j1) == null && 
                        tp2.labelingFunction(j2) == null || tp1.labelingFunction(j1) != null && 
                        tp1.labelingFunction(j1).equals(tp2.labelingFunction(j2)))*/ ){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Crea il tag piece unione di due tag piece.
     * 
     * @param tp1
     * @param tp2
     * @param varMap1
     * @param varMap2
     * @param varMapComp
     * @param epsilon
     * @return
     * @throws Exception 
     */
    // TODO: migliorare passando solo var non comuni
    public static TagPiece union(TagPiece tp1, TagPiece tp2, 
            HashMap<String, Integer> varMap1, HashMap<String, Integer> varMap2, 
            HashMap<String, Integer> varMapComp, Tag epsilon) throws Exception {
        
        Tag[][] matrix = new Tag[varMapComp.size()][varMapComp.size()];
        ArrayList<ArrayList<LabelingFunction>> labelingFunction = new ArrayList<>(varMapComp.size());
        
        for(int i=0; i<varMapComp.size(); i++){
            labelingFunction.add(new ArrayList<>());
            for(int j=0; j<matrix[0].length; j++){
                matrix[i][j] = epsilon;
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap1.entrySet()){
            for(LabelingFunction lf : tp1.getLabelingFunction().get(v.getValue())){
                labelingFunction.get(varMapComp.get(v.getKey())).add(lf);
            }
            
            for(Map.Entry<String, Integer> w : varMap1.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp1.getMatrix()[v.getValue()][w.getValue()];
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap2.entrySet()){
            for(LabelingFunction lf : tp2.getLabelingFunction().get(v.getValue())){
                labelingFunction.get(varMapComp.get(v.getKey())).add(lf);
            }
            
            for(Map.Entry<String, Integer> w : varMap2.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp2.getMatrix()[v.getValue()][w.getValue()];
            }
        }        
        
        TagPiece tpComp = new TagPiece(matrix, labelingFunction, varMapComp);
        return tpComp;
    }

}
