/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author davide
 */
public class Main {
    
    public static void tm1Simulation() throws Exception {
        String[] variables = {"x", "y"};
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new VarInt(0), new VarInt(0)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        
        TagIntPlus id = TagIntPlus.IDENTITY;
        TagIntPlus one = new TagIntPlus(1);
        TagIntPlus eps = TagIntPlus.EPSILON;
        
        TagIntPlus[][] m1 = {{id, eps},{eps, id}};
        VarInt[] l1 = {null, null};        
        
        TagIntPlus[][] m2 = {{one,one},{one,one}};
        VarInt[] l2 = {new VarInt(0), new VarInt(0)};
        edgesFrom0.add(new Edge(0, 1, new TagPiece(m2, l2)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m1, l1)));
        
        TagIntPlus[][] m3 = {{id,one},{eps,one}};
        VarInt[] l3 = {null, new VarInt(0)};
        edgesFrom1.add(new Edge(1, 0, new TagPiece(m3, l3)));
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m1, l1)));
        
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new TagIntPlus());
        tm.simulate(20, true, true);
    }
    
    public static void main(String[] args) throws Exception {
        final int N = 4;
        
        Tag[][] matrix = new Tag[N][N];
        Tag[] tagVector = new Tag[N];
        for(int i=0; i<N; i++) {
            tagVector[i] = new TagIntPlus(i);
            for(int j=0; j<N; j++)
                matrix[i][j] = new TagIntPlus(i+j);
        }
        
//        TagPiece mu = new TagPiece(matrix);                
//        
//        Tag[] tagVectorPrime = mu.apply(tagVector);
//        for(Tag t : tagVectorPrime)
//            System.err.println(((TagIntPlus) t).tag);

        tm1Simulation();
    }
}
