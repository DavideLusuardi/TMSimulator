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
    
    public TagMachine generateTm5() throws Exception {
        float a11  = (float) -2.671;
        float a12  = (float) -21.54;
        float a21  = (float) 21.54;
        float a22  = (float) -2.671;
        float ap11 = (float) 0;
        float ap12 = (float) 1;
        float ap13 = (float) -7.556;
        float ap21 = (float) -448.1;
        float ap22 = (float) -5.186;
        float ap23 = (float) 30.87;
        float ap31 = (float) 3.042;
        float ap32 = (float) 0.02773;
        float ap33 = (float) -0.2105;  
        float M    = (float) 12.41;
        float b11  = (float) 1.92339;
        float b21  = (float) -14.323085;
        float bp11 = (float) 0;
        float bp21 = (float) 15.05;
        float bp31 = (float) 0;
        float d    = (float) 0.0011810;  
        float v11  = (float) 0.0101152;
        float v12  = (float) -0.9999488;  
        float i1   = (float) 0.4;
        float i2   = (float) 0.6;
        float c12  = (float) 0.0379945;
        float c13  = (float) -0.0025700; 
        
        String[] variables = {"x11", "x21", "sm11", "sm21", "sm31", "fai", "pfai", "rot", "cutoff", "torque", "aw"};
        int initialState = 0;
        int[] acceptingStates = {0};
        Var[] initialVarValues = {new FloatVar(36), new FloatVar(1), new FloatVar((float)-4.4857183), 
            new FloatVar(2660), new FloatVar((float)352.0831), new FloatVar(0), 
            new FloatVar(0), new BoolVar(false), new BoolVar(false), new FloatVar((float)12.41), new FloatVar((float)1.3652334)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        edges.add(edgesFrom0);
        
        MaxPlusFloat d1 = new MaxPlusFloat(d);
        MaxPlusFloat[][] mu = {{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1}};
        
        LabelingFunction lb = new LabelingFunction() {
            @Override
            public Var apply(Var[] varValues) {
                
            }
        }
        
        Var[] l = {
            new FloatVar(x11 + d*(a11*x11 + a12*x21 + b11*torque)),
            new FloatVar(x21 + d*(a21*x11 + a22*x21 + b21*torque)),
            new FloatVar(c12*x11+c13*x21),
            new FloatVar(sm11 + d*(ap11*sm11 + ap12*sm21 + ap13*sm31) + bp11*torque),
            new FloatVar(sm21 + d*(ap21*sm11 + ap22*sm21 + ap23*sm31) + bp21*torque),
            new FloatVar(sm31 + d*(ap31*sm11 + ap32*sm21 + ap33*sm31) + bp31*torque),
            new FloatVar(fai + d*sm21/60),
            new BoolVar(((fai-pfai>=i1) && (fai-pfai<=i2))*fai + (1-((fai-pfai>=i1) && (fai-pfai<=i2)))*pfai),
            new BoolVar((fai-pfai>=i1) && (fai-pfai<=i2)),
            new FloatVar(cutoff || (v11*x11+v12*x21>=0)),
            new FloatVar(0)
        };
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l)));
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusInteger());
        return tm;        
    }
    
    public static void main(String[] args) throws Exception {
        Main m = new Main();
        m.runExample2();
        
    }
}
