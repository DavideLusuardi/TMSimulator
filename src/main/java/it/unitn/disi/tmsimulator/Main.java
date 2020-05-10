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
    
    MaxPlusInteger eps = MaxPlusInteger.EPSILON;
    MaxPlusInteger id = MaxPlusInteger.IDENTITY;
    MaxPlusInteger one = new MaxPlusInteger(1);    
    MaxPlusInteger two = new MaxPlusInteger(2);

    MaxPlusInteger[][] m0ee0 = {{id, eps},{eps, id}};
    IntVar[] l0ee0 = {null, null};        

    MaxPlusInteger[][] m1111 = {{one,one},{one,one}};
    IntVar[] l1111 = {new IntVar(0), new IntVar(0)};

    MaxPlusInteger[][] m01e1 = {{id,one},{eps,one}};
    IntVar[] l01e1 = {null, new IntVar(0)};

    MaxPlusInteger[][] m1ee1 = {{one, eps},{eps, one}};
    IntVar[] l1ee1 = {new IntVar(0), new IntVar(0)};

    MaxPlusInteger[][] m0ee1 = {{id, eps},{eps, one}};
    IntVar[] l0ee1 = {null, new IntVar(0)};

    MaxPlusInteger[][] m2ee1 = {{two, eps},{eps, one}};
    IntVar[] l2ee1 = {new IntVar(0), new IntVar(0)};
    
    
    public TagMachine generateTm1() throws Exception {
        String[] variables = {"x", "y"};
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new IntVar(0), new IntVar(0)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m1ee1, l1ee1)));
        edgesFrom0.add(new Edge(0, 1, new TagPiece(m1111, l1111)));

        edgesFrom1.add(new Edge(1, 1, new TagPiece(m1ee1, l1ee1)));
        edgesFrom1.add(new Edge(1, 0, new TagPiece(m01e1, l01e1)));        
        
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusInteger());
        return tm;        
    }
    
    public TagMachine generateTm2() throws Exception {
        String[] variables = {"x", "z"};
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new IntVar(0), new IntVar(0)};
        
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
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusInteger());
        return tm;        
    }
    
    public TagMachine generateTm3() throws Exception {
        String[] variables = {"x", "y"};
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new IntVar(0), new IntVar(0)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(m1ee1, l1ee1)));        
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(m0ee1, l0ee1)));        
        
        edgesFrom2.add(new Edge(2, 2, new TagPiece(m0ee0, l0ee0)));
        edgesFrom2.add(new Edge(2, 1, new TagPiece(m2ee1, l2ee1)));        
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusInteger());
        return tm;        
    }
    
    public TagMachine generateTm4() throws Exception {
        String[] variables = {"x", "z"};
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new IntVar(0), new IntVar(0)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(m0ee1, l0ee1)));        
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(m0ee1, l0ee1)));        
        
        edgesFrom2.add(new Edge(2, 2, new TagPiece(m0ee0, l0ee0)));
        edgesFrom2.add(new Edge(2, 1, new TagPiece(m2ee1, l2ee1)));        
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusInteger());
        return tm;        
    }
    
    public void runExample1() throws Exception {
        TagMachine tm1 = generateTm1();
        System.out.println("TM1 ------------------------------------------------");
        System.out.println(tm1);
        System.out.println("random run of tm1");
        tm1.simulate(20, true, true);
        
        TagMachine tm2 = generateTm2();
        System.out.println("TM2 ------------------------------------------------");
        System.out.println(tm2);
        System.out.println("random run of tm2");
        tm2.simulate(20, true, true);
        
        TagMachineSet tmSet = new TagMachineSet();
        tmSet.add(tm1);
        tmSet.add(tm2);
        TagMachine tmComp = tmSet.compose();
        System.out.println("TMcomposition --------------------------------------");
        System.out.println(tmComp);
        System.out.println("random run of tmComp");
        tmComp.simulate(20, true, true);
    }
    
    public void runExample2() throws Exception {
        TagMachine tm1 = generateTm3();
        System.out.println("TM1 ------------------------------------------------");
        System.out.println(tm1);
        System.out.println("random run of tm1");
        tm1.simulate(20, true, true);
        
        TagMachine tm2 = generateTm4();
        System.out.println("TM2 ------------------------------------------------");
        System.out.println(tm2);
        System.out.println("random run of tm2");
        tm2.simulate(20, true, true);
        
        TagMachineSet tmSet = new TagMachineSet();
        tmSet.add(tm1);
        tmSet.add(tm2);
        TagMachine tmComp = tmSet.compose();
        System.out.println("TMcomposition --------------------------------------");
        System.out.println(tmComp);
        System.out.println("random run of tmComp");
        tmComp.simulate(20, true, true);
    }
    
    public static void main(String[] args) throws Exception {
        Main m = new Main();
        m.runExample2();
        
    }
}
