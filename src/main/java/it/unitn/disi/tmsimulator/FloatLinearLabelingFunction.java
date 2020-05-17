/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

/**
 *
 * @author davide
 */
public class FloatLinearLabelingFunction extends LabelingFunction {
    
    private Float params[];

    public FloatLinearLabelingFunction(Float[] params) {
        this.params = params;
    }

    @Override
    public Var apply(Var[] varValues) {
        Float sum = (float) 0;
        for(int i=0; i<varValues.length; i++){
            Var v = varValues[i];
            Float value = (Float)v.getValue();            
            sum += params[i]*value;
        }
        return varValues[0].newInstance(sum);
    }
    
    
}
