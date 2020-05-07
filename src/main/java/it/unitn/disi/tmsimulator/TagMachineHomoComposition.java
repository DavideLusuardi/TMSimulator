/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;

/**
 *
 * @author davide
 */
public class TagMachineHomoComposition extends ArrayList<TagMachine> {
    
    public TagMachine compose() throws Exception {
        if(this.size() < 2)
            throw new Exception("la composizione richiede almeno due TagMachine");
        
        TagMachine tm1 = this.get(0);
        TagMachine tm2 = this.get(1);
        
        return tm1;
    }
}
