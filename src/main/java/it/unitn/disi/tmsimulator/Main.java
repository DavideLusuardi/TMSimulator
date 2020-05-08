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
    
    public static void printEdges(TagMachine tm){
        int i = 0;
        for(ArrayList<Edge> edgeList : tm.getEdges()){
            System.out.print(String.format("state %d: ", i++));
            for(Edge e : edgeList){
                System.out.print(String.format("%d ,", e.getToState()));
            }
            System.out.println();
        }
    }
    
    public static TagMachine generateTm1() throws Exception {
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
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m1, l1)));
        edgesFrom0.add(new Edge(0, 1, new TagPiece(m2, l2)));
        
        TagIntPlus[][] m3 = {{id,one},{eps,one}};
        VarInt[] l3 = {null, new VarInt(0)};
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m1, l1)));
        edgesFrom1.add(new Edge(1, 0, new TagPiece(m3, l3)));        
        
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new TagIntPlus());
        return tm;        
    }
    
    public static TagMachine generateTm2() throws Exception {
        String[] variables = {"x", "z"};
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new VarInt(0), new VarInt(0)};
        
        TagIntPlus id = TagIntPlus.IDENTITY;
        TagIntPlus one = new TagIntPlus(1);
        TagIntPlus eps = TagIntPlus.EPSILON;
        
        TagIntPlus[][] m0ee0 = {{id, eps},{eps, id}};
        VarInt[] l0ee0 = {null, null};        
        
        TagIntPlus[][] m1111 = {{one,one},{one,one}};
        VarInt[] l1111 = {new VarInt(0), new VarInt(0)};
        
        TagIntPlus[][] m01e1 = {{id,one},{eps,one}};
        VarInt[] l01e1 = {null, new VarInt(0)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(m01e1, l01e1)));        
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(m01e1, l01e1)));        
        
        edgesFrom2.add(new Edge(2, 2, new TagPiece(m0ee0, l0ee0)));
        edgesFrom2.add(new Edge(2, 1, new TagPiece(m1111, l1111)));        
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new TagIntPlus());
        return tm;        
    }
    
    public static void main(String[] args) throws Exception {

        TagMachine tm1 = generateTm1();
        System.out.println("TM1 ------------------------------------------------");
        printEdges(tm1);
        System.out.println("random run of tm1");
        tm1.simulate(20, true, true);
        
        TagMachine tm2 = generateTm2();
        System.out.println("TM2 ------------------------------------------------");
        printEdges(tm2);
        System.out.println("random run of tm2");
        tm2.simulate(20, true, true);
        
        TagMachineSet tmSet = new TagMachineSet();
        tmSet.add(tm1);
        tmSet.add(tm2);
        TagMachine tmComp = tmSet.compose();
        System.out.println("TMcomposition --------------------------------------");
        printEdges(tmComp);
        System.out.println("random run of tmComp");
        tmComp.simulate(20, true, true);
        
    }
}
