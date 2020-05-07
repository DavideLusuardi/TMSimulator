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
public class VarInt extends Var {
    Integer value;

    public VarInt(Integer value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value.toString();
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Var other) {
        if(other == null || !(other instanceof VarInt))
            return false;
        
        return this.value.equals(((VarInt)other).getValue());
    }
}
