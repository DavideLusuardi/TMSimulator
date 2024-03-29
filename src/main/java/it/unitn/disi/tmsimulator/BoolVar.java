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
public class BoolVar extends Var {
    Boolean value;

    public BoolVar(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }
        
    @Override
    public Var newInstance(Object value) {
        return new BoolVar((Boolean)value);
    }

    @Override
    public boolean equals(Var other) {
        if(other == null || !(other instanceof BoolVar))
            return false;
        
        return this.value.equals(((BoolVar)other).getValue());
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
