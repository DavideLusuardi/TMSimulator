/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.variables;

/**
 * Rappresenta una variabile di tipo Integer.
 *
 * @author davide
 */
public class IntVar extends Var {
    Integer value;

    public IntVar(Integer value) {
        this.value = value;
    }
    
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Var newInstance(Object value) {
        return new IntVar((Integer)value);
    }

    @Override
    public boolean equals(Var other) {
        if(other == null || !(other instanceof IntVar))
            return false;
        
        return this.value.equals(((IntVar)other).getValue());
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
