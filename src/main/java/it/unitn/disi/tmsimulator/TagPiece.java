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
    private Var[] labelingFunction;
    private Integer[] domLabelingFunction;

    // TODO: controllare che tag cambia quando var appartiene a Dom(v)
    public TagPiece(Tag[][] matrix, Var[] labelingFunction) throws Exception {
        if(matrix.length == 0 || matrix.length != matrix[0].length){
            throw new Exception("la matrice TagPiece deve essere quadrata");
        }        
        this.matrix = matrix;
        
        if(labelingFunction.length != matrix.length){
            throw new Exception("il vettore varAssignment ha lunghezza incompatibile con la matrice TagPiece");
        }
        this.labelingFunction = labelingFunction;
        
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0; i<labelingFunction.length; i++){
            if(labelingFunction[i] != null)
                indexes.add(i);
        }
        domLabelingFunction = indexes.toArray(new Integer[0]);
    }

    public Var labelingFunction(int varIndex) {
        return labelingFunction[varIndex];
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
    
    
    public Integer[] domLabelingFunction(){
        return domLabelingFunction;
    }

    public Tag[][] getMatrix() {
        return matrix;
    }
    
    public static boolean unifiable(TagPiece tp1, TagPiece tp2, 
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
                
                if(!m1[i1][j1].equals(m2[i2][j2]) || !(tp1.labelingFunction(j1) == null && 
                        tp2.labelingFunction(j2) == null || tp1.labelingFunction(j1) != null && 
                        tp1.labelingFunction(j1).equals(tp2.labelingFunction(j2))) ){
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
        Var[] labelingFunction = new Var[varMapComp.size()];
        
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[0].length; j++){
                matrix[i][j] = epsilon;
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap1.entrySet()){
            labelingFunction[varMapComp.get(v.getKey())] = tp1.labelingFunction(v.getValue());
            for(Map.Entry<String, Integer> w : varMap1.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp1.getMatrix()[v.getValue()][w.getValue()];
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap2.entrySet()){
            labelingFunction[varMapComp.get(v.getKey())] = tp2.labelingFunction(v.getValue());
            for(Map.Entry<String, Integer> w : varMap2.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp2.getMatrix()[v.getValue()][w.getValue()];
            }
        }        
        
        TagPiece tpComp = new TagPiece(matrix, labelingFunction);
        return tpComp;
    }

}
