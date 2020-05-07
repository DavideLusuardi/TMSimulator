/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        
        HashMap<String, Integer> varMap = new HashMap<>(tm1.getVarMap());
        ArrayList<String> sharedVars = new ArrayList<>(tm2.getVarMap().size());
        int pos = varMap.size();
        for(String var : tm2.getVarMap().keySet()){
            Integer val = varMap.putIfAbsent(var, pos);
            if(val == null){ // chiave non presente
                pos++;
            } else {
                sharedVars.add(var);
            }
        }
        
        int state1 = tm1.getInitialState();
        int state2 = tm2.getInitialState();
        
        
        
        return tm1;
    }
}
