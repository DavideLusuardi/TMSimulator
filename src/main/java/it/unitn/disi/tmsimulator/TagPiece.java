/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.morphism.Morphism;
import it.unitn.disi.tmsimulator.labelingfunction.LabelingFunction;
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
    
    public boolean morphismApplied = false;
    
    /**
     * Matrice dei tag.
     */
    private Tag[][] matrix;
    
    /**
     * Rappresenta la labeling function.
     * Quando si compongono due tag piece, le labeling function di 
     * entrambi vengono aggiunte alla lista. In tal modo si può verificare
     * durante la simulazione della tag machine se si sincronizzano.
     */
    // TODO: decidere se labelingFunction può contenere null
    private ArrayList<ArrayList<LabelingFunction>> labelingFunction; // TODO: implementare una struttura dati con complessità di concatenzaione costante
    
    /**
     * Mappa hash che associa ad ogni nome di variabile la sua posizione negli
     * array.
     */
    private HashMap<String, Integer> varMap;
    
    /**
     * Costruisce un nuovo tag piece senza labeling function.
     * 
     * @param matrix matrice dei tag.
     * @param varMap mapping variabile-posizione.
     * @throws Exception
     */
    public TagPiece(Tag[][] matrix, HashMap<String, Integer> varMap) throws Exception {
        setMatrix(matrix);
        this.varMap = varMap;
    }
    
    /**
     * Costruisce un nuovo labeled tag piece.
     * 
     * @param matrix matrice dei tag.
     * @param labelingFunction labeling function.
     * @param varMap mapping variabile-posizione.
     * @throws Exception 
     */
    public TagPiece(Tag[][] matrix, ArrayList<ArrayList<LabelingFunction>> labelingFunction, 
            HashMap<String, Integer> varMap) throws Exception {
        
        setMatrix(matrix);
        if(labelingFunction.size() != matrix.length){
            throw new Exception("il vettore labelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }

        this.labelingFunction = labelingFunction;        
        this.varMap = varMap;
    }
    
    /**
     * Costruisce un nuovo labeled tag piece.
     * 
     * @param matrix matrice dei tag.
     * @param labelingFunction labeling function.
     * @param varMap mapping variabile-posizione.
     * @throws Exception 
     */
    public TagPiece(Tag[][] matrix, LabelingFunction[] labelingFunction, 
            HashMap<String, Integer> varMap) throws Exception {
        
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
    
    /**
     * Costruisce un nuovo labeled tag piece.
     * 
     * @param matrix matrice dei tag.
     * @param constLabelingFunction labeling function con valore costante.
     * @param varMap mapping variabile-posizione.
     * @throws Exception 
     */
    public TagPiece(Tag[][] matrix, Var[] constLabelingFunction, 
            HashMap<String, Integer> varMap) throws Exception {
        
        setMatrix(matrix);
        
        if(constLabelingFunction.length != matrix.length){
            throw new Exception("il vettore constLabelingFunction ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        setLabelingFunction(constLabelingFunction);
        this.varMap = varMap;
    }

    /**
     * Restituisce la matrice dei tag.
     * @return la matrice dei tag.
     */
    public Tag[][] getMatrix() {
        return matrix;
    }

    /**
     * Restituisce il mapping variabile-posizione.
     * @return il mapping variabile-posizione.
     */
    public HashMap<String, Integer> getVarMap() {
        return varMap;
    }
    
    private void setMatrix(Tag[][] matrix) throws Exception {
        if(matrix.length == 0 || matrix.length != matrix[0].length){
            throw new Exception("la matrice TagPiece deve essere quadrata");
        }
        this.matrix = matrix;
    }

    /**
     * Restituisce le labeling function.
     * @return le labeling function.
     */
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
     * @param tagVector vettore dei tag.
     * @return il nuovo vettore dei tag.
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
     * Sulla base del tag vector passato, calcola il nuovo tag vector utilizzando
     * la matrice dei tag.
     * 
     * @param tagVector vettore dei tag.
     * @return il nuovo vettore dei tag.
     * @throws Exception 
     */
    public ArrayList<Tag> apply(ArrayList<Tag> tagVector) throws Exception {
        if(tagVector.size() != this.matrix.length){
            throw new Exception("il vettore dei tag ha lunghezza incompatibile con la matrice TagPiece");
        }
        
        // TODO: gestire quando è presente epsilon e quando ordinamento parziale (-> non esiste max)
        ArrayList<Tag> tagVectorPrime = new ArrayList<>(tagVector.size());
        for(int i=0; i < tagVector.size(); i++){
            Tag tMax = tagVector.get(i); // nel caso in cui tag piece ha una colonna di epsilon, il tag non cambia
            
            for(int j=0; j < tagVector.size(); j++){
                Tag t = tagVector.get(j).concatenate(matrix[j][i]);
                if(t.gt(tMax)){
                    tMax = t;
                }
            }
            
            tagVectorPrime.add(tMax);
        }
        
        return tagVectorPrime;
    }
    
    /**
     * Verifica che le labeling function calcolate in funzione dei valori delle 
     * variabili siano unificabili.
     * 
     * @param varValues valori delle variabili.
     * @return true se le labeling function sono unificabili, false altrimenti.
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
     * @param varValues valori delle variabili.
     * @return i nuovi valori delle variabili calcolati.
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
     * @param tagMorphism morfismo dei tag.
     * @throws Exception 
     */
    public void applyMorphism(Morphism tagMorphism) throws Exception {
        if(tagMorphism == null)
            return;
        
        Tag[][] m = new Tag[this.matrix.length][this.matrix.length];
        
        for(int i=0; i<this.matrix.length; i++){
            for(int j=0; j<this.matrix.length; j++){
                m[i][j] = tagMorphism.map(this.matrix[i][j]);
            }
        }
        this.matrix = m;
        this.morphismApplied = true;
    }
    
    /**
     * Verifica che due tag piece siano unificabili.
     * 
     * @param tp1 primo tag piece.
     * @param tp2 secondo tag piece.
     * @param sharedVars variabili condivise tra i due tag piece.
     * @return true se sono unificabili, false altrimenti.
     */
    public static boolean isUnifiable(TagPiece tp1, TagPiece tp2, ArrayList<String> sharedVars){
        
        HashMap<String, Integer> varMap1 = tp1.getVarMap();
        HashMap<String, Integer> varMap2 = tp2.getVarMap();
        
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
                if(!(m1[i1][j1].equals(m2[i2][j2]))){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Creazione del labeled tag piece rappresentante l'unione di due labeled tag piece.
     * 
     * @param tp1 primo tag piece.
     * @param tp2 secondo tag piece.
     * @param varMapComp mapping variabile-posizione del tag piece unione.
     * @param epsilon tag epsilon.
     * @return il nuovo tag piece unione degli altri due.
     * @throws Exception 
     */
    // TODO: migliorare passando solo var non comuni
    public static TagPiece union(TagPiece tp1, TagPiece tp2, 
            HashMap<String, Integer> varMapComp, Tag epsilon) throws Exception {
        
        HashMap<String, Integer> varMap1 = tp1.getVarMap();
        HashMap<String, Integer> varMap2 = tp2.getVarMap();
        
        Tag[][] matrix = new Tag[varMapComp.size()][varMapComp.size()];
        ArrayList<ArrayList<LabelingFunction>> labelingFunction = new ArrayList<>(varMapComp.size());
        
        // inizializzazione della matrice dei tag con epsilon
        for(int i=0; i<varMapComp.size(); i++){
            labelingFunction.add(new ArrayList<>());
            for(int j=0; j<matrix[0].length; j++){
                matrix[i][j] = epsilon;
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap1.entrySet()){
            // aggiunta delle labeling function presenti nel primo tag piece
            for(LabelingFunction lf : tp1.getLabelingFunction().get(v.getValue())){
                labelingFunction.get(varMapComp.get(v.getKey())).add(lf);
            }
            
            // inserimento dei tag appartenenti al primo tag piece nella matrice
            for(Map.Entry<String, Integer> w : varMap1.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp1.getMatrix()[v.getValue()][w.getValue()];
            }
        }
        
        for(Map.Entry<String, Integer> v : varMap2.entrySet()){
            // aggiunta delle labeling function presenti nel secondo tag piece
            for(LabelingFunction lf : tp2.getLabelingFunction().get(v.getValue())){
                labelingFunction.get(varMapComp.get(v.getKey())).add(lf);
            }
            
            // inserimento dei tag appartenenti al secondo tag piece nella matrice
            for(Map.Entry<String, Integer> w : varMap2.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp2.getMatrix()[v.getValue()][w.getValue()];
            }
        }        
        
        TagPiece tpComp = new TagPiece(matrix, labelingFunction, varMapComp);
        return tpComp;
    }
    
    /**
     * Unione incrementale del tag piece al tag piece rappresentante la composizione.
     * Viene aggiornata solo la matrice dei tag e non le labeling function.
     * 
     * @param tpComp tag piece che rappresenta la composizione finale.
     * @param tp tag piece da unire.
     * @throws Exception 
     */
    public static void union(TagPiece tpComp, TagPiece tp) throws Exception {
        
        HashMap<String, Integer> varMapComp = tpComp.getVarMap();
        HashMap<String, Integer> varMap = tp.getVarMap();
        
        Tag[][] matrix = tpComp.getMatrix();
        
        for(Map.Entry<String, Integer> v : varMap.entrySet()){
            for(Map.Entry<String, Integer> w : varMap.entrySet()){
                matrix[varMapComp.get(v.getKey())][varMapComp.get(w.getKey())] = 
                        tp.getMatrix()[v.getValue()][w.getValue()];
            }
        }        
        
    }

    /**
     * Rappresentazione del tag piece come stringa.
     * @return la stringa che rappresenta il tag piece
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tag[] row : getMatrix()) {
            sb.append("|");
            for (int i=0; i<row.length; i++) {
                sb.append(row[i].toString());
                if(i+1<row.length) sb.append(", ");
            }
            sb.append("|\n");
        }

        for (Map.Entry<String, Integer> entry : this.varMap.entrySet()) {
            LabelingFunction l = getLabelingFunction().get(entry.getValue()).get(0);

            if (l != null) {
                sb.append(String.format("%s = %s\n", entry.getKey(), l.toString()));
            } else {
                sb.append(String.format("%s = -\n", entry.getKey()));
            }
        }
        
        return sb.toString();
    }

}
