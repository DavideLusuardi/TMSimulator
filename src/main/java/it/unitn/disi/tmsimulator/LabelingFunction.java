/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.HashMap;

/**
 *
 * @author davide
 */
public abstract class LabelingFunction {
    public abstract Var apply(HashMap<String, Var> varValues);

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof LabelingFunction))
            return false;
        
        LabelingFunction lf2 = (LabelingFunction) obj;

        try {
            return this.apply(null).equals(lf2.apply(null)); // TODO: sistemare il controllo
        } catch(Exception e){
            return true;
        }
    }
}
