/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.variables.FloatVar;
import it.unitn.disi.tmsimulator.variables.Var;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author davide
 */
public class FloatLinearLabelingFunction extends LabelingFunction {
    
    private HashMap<String, Float> params;

    public FloatLinearLabelingFunction(HashMap<String, Float> params) {
        this.params = params;
    }
    
    // TODO: verificare varNames e params con la stessa lunghezza
    public FloatLinearLabelingFunction(String[] varNames, Float[] params) {
        this.params = new HashMap<>(varNames.length);
        for(int i=0; i<varNames.length; i++){
            this.params.put(varNames[i], params[i]);
        }
    }

    @Override
    public Var apply(HashMap<String, Var> varValues) {
        Float sum = (float) 0;
        for(Map.Entry<String, Float> p : this.params.entrySet()){
            if(p.getValue() == null)
                continue;
            
            Float floatValue = (Float) varValues.get(p.getKey()).getValue();
            sum += floatValue * p.getValue();
        }
        return new FloatVar(sum); // TODO: sistemare
    }

    public HashMap<String, Float> getParams() {
        return params;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof LabelingFunction))
            return false;
        
        if(!(obj instanceof FloatLinearLabelingFunction))
            return super.equals(obj);
        
        FloatLinearLabelingFunction lf2 = (FloatLinearLabelingFunction) obj;
        return this.params.equals(lf2.getParams());
    }
    
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
