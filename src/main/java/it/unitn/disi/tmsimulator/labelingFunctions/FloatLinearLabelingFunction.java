/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.labelingFunctions;

import it.unitn.disi.tmsimulator.labelingFunctions.LabelingFunction;
import it.unitn.disi.tmsimulator.variables.FloatVar;
import it.unitn.disi.tmsimulator.variables.Var;
import java.util.HashMap;
import java.util.Map;

/**
 * Labeling function come funzione lineare dei valori delle variabili.
 * Il risultato della labeling function è una variabile di tipo 
 * {@link it.unitn.disi.tmsimulator.variables.FloatVar}.
 * 
 * @author davide
 */
public class FloatLinearLabelingFunction extends LabelingFunction {
    
    /**
     * Coefficienti di tipo Float associati alle variabili.
     */
    private HashMap<String, Float> params;

    /**
     * Costruisce una labeling function.
     * 
     * @param params coefficienti di tipo Float associati alle variabili.
     */
    public FloatLinearLabelingFunction(HashMap<String, Float> params) {
        this.params = params;
    }
    
    /**
     * Costruisce una labeling function.
     * 
     * @param varNames nomi delle variabili da cui dipende la labeling function.
     * @param params coefficienti di tipo Float associati alle variabili.
     */
    public FloatLinearLabelingFunction(String[] varNames, Float[] params) {
        this.params = new HashMap<>(varNames.length);
        for(int i=0; i<varNames.length; i++){
            this.params.put(varNames[i], params[i]);
        }
    }

    /**
     * Applica la labeling function restituendo il valore calcolato.
     * 
     * @param varValues valori delle variabili da cui dipende la labeling function.
     * @return il valore calcolato della labeling function.
     */
    @Override
    public Var apply(HashMap<String, Var> varValues) {
        Float sum = (float) 0;
        for(Map.Entry<String, Float> p : this.params.entrySet()){
            if(p.getValue() == null)
                continue;
            
            Float floatValue = (Float) varValues.get(p.getKey()).getValue(); // TODO: throw exception se var non float
            sum += floatValue * p.getValue();
        }
        return new FloatVar(sum);
    }

    /**
     * Restituisce i coefficienti associati alle variabili.
     * @return i coefficienti di tipo Float associati alle variabili.
     */
    public HashMap<String, Float> getParams() {
        return params;
    }

    /**
     * Verifica l'uguaglianza rispetto ai coefficienti con un'altra labeling 
     * function dello stesso tipo.
     * 
     * @param lf labeling function da uguagliare.
     * @return true se lf ha gli stessi coefficienti ed è dello stesso tipo, 
     * false altrimenti.
     */
    @Override
    public boolean equals(Object lf) {
        if(lf == null || !(lf instanceof LabelingFunction))
            return false;
        
        if(!(lf instanceof FloatLinearLabelingFunction))
            return super.equals(lf);
        
        FloatLinearLabelingFunction lf2 = (FloatLinearLabelingFunction) lf;
        return this.params.equals(lf2.getParams());
    }
    
    /**
     * Rappresentazione come stringa della labeling function.
     * 
     * @return la stringa che rappresenta la labeling function.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, Float> p : this.params.entrySet()){
            if(p.getValue() != null){
                stringBuilder.append(String.format("%f*%s+", p.getValue(), p.getKey()));
            }
        }
        return stringBuilder.toString();
    }
    
    
}
