/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator.variables;

/**
 * Rappresenta una variabile.
 * 
 * @author davide
 */
public abstract class Var {
    
    /**
     * Restituisce il valore della variabile.
     * @return 
     */
    public abstract Object getValue();
    
    /**
     * Genera una nuova variabile avente come valore quello passato.
     * @param value
     * @return 
     */
    public abstract Var newInstance(Object value);
    
    /**
     * Verifica se due variabili hanno lo stesso valore.
     * @param other
     * @return 
     */
    public abstract boolean equals(Var other);
}
