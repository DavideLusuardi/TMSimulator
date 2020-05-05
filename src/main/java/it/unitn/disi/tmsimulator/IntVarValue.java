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
public class IntVarValue extends VarValue {
    Integer value;

    public IntVarValue(Integer value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
}
