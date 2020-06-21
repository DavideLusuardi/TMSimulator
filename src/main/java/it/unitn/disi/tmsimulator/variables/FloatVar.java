/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.variables;

/**
 * Rappresenta una variabile di tipo Float.
 * 
 * @author davide
 */
public class FloatVar extends Var {
    Float value;

    public FloatVar(Float value) {
        this.value = value;
    }
    
    public FloatVar(Integer value){
        this((float) value);
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public Var newInstance(Object value) {
        return new FloatVar((Float)value);
    }

    @Override
    public boolean equals(Var other) {
        if(other == null || !(other instanceof FloatVar))
            return false;
        
        return this.value.equals(((FloatVar)other).getValue());
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
