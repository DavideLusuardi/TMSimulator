/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;

/**
 *
 * @author davide
 */
public class TagPiece {
    Tag[][] matrix;
    VarValue[] labelingFunction;

    // TODO: controllare che tag cambia quando var appartiene a Dom(v)
    public TagPiece(Tag[][] matrix, VarValue[] labelingFunction) throws Exception {
        if(matrix.length == 0 || matrix.length != matrix[0].length){
            throw new Exception("la matrice TagPiece deve essere quadrata");
        }        
        this.matrix = matrix;
        
        if(labelingFunction.length != matrix.length){
            throw new Exception("il vettore varAssignment ha lunghezza incompatibile con la matrice TagPiece");
        }
        this.labelingFunction = labelingFunction;
    }
    
    public Tag[] apply(Tag[] tagVector) throws Exception {
        if(tagVector.length != this.matrix.length){
            throw new Exception("il vettore dei tag ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        // TODO: gestire quando è presente epsilon
        Tag[] tagVectorPrime = new Tag[tagVector.length];
        for(int i=0; i < tagVector.length; i++){
            Tag tMax = tagVector[0].concatenate(matrix[0][i]);
            
            for(int j=1; j < tagVector.length; j++){
                if(matrix[j][i].isEpsilon())
                    continue;
                
                Tag t = tagVector[j].concatenate(matrix[j][i]);
                if(t.gt(tMax)){
                    tMax = t;
                }
            }
            
            tagVectorPrime[i] = tMax;
        }
        
        return tagVectorPrime;
    }
    
    
    public ArrayList<Integer> domLabelingFunction(){
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0; i<labelingFunction.length; i++){
            if(labelingFunction[i] != null)
                indexes.add(i);
        }
        
        return indexes;
    }

}
