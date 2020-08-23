/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.tmsimulator;

import it.unitn.disi.tmsimulator.morphisms.Morphism;
import it.unitn.disi.tmsimulator.labelingFunctions.FloatLinearLabelingFunction;
import it.unitn.disi.tmsimulator.labelingFunctions.LabelingFunction;
import it.unitn.disi.tmsimulator.tags.Tag;
import it.unitn.disi.tmsimulator.tags.IntegerTag;
import it.unitn.disi.tmsimulator.tags.FloatTag;
import it.unitn.disi.tmsimulator.variables.IntVar;
import it.unitn.disi.tmsimulator.variables.FloatVar;
import it.unitn.disi.tmsimulator.variables.BoolVar;
import it.unitn.disi.tmsimulator.variables.Var;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contiene alcuni esempi di composzioni sia omogenee che eterogenee.
 * 
 * @author davide
 */
public class Main {
    
    IntegerTag eps = IntegerTag.EPSILON;
    IntegerTag id = IntegerTag.IDENTITY;
    IntegerTag one = new IntegerTag(1);    
    IntegerTag two = new IntegerTag(2);

    IntegerTag[][] m0ee0 = {{id, eps},{eps, id}};
    IntVar[] l0ee0 = {null, null};        

    IntegerTag[][] m1111 = {{one,one},{one,one}};
    IntVar[] l1111 = {new IntVar(0), new IntVar(0)};

    IntegerTag[][] m01e1 = {{id,one},{eps,one}};
    IntVar[] l01e1 = {null, new IntVar(0)};

    IntegerTag[][] m1ee1 = {{one, eps},{eps, one}};
    IntVar[] l1ee1 = {new IntVar(0), new IntVar(0)};

    IntegerTag[][] m0ee1 = {{id, eps},{eps, one}};
    IntVar[] l0ee1 = {null, new IntVar(0)};

    IntegerTag[][] m2ee1 = {{two, eps},{eps, one}};
    IntVar[] l2ee1 = {new IntVar(0), new IntVar(0)};
    
    /**
     * esempio di Figura 1(a)
     */
    public TagMachine generateTm1() throws Exception {
        String[] variables = {"x", "y"};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        int initialState = 0;
        int[] acceptingStates = {1};
        Var[] initialVarValues = {new IntVar(0), new IntVar(0)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom0.add(new Edge(0, 1, new TagPiece(m1111, l1111, varMap)));

        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom1.add(new Edge(1, 0, new TagPiece(m01e1, l01e1, varMap)));        
        
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
                
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;        
    }
    
    // esempio di Figura 1(b)
    public TagMachine generateTm2() throws Exception {
        String[] variables = {"x", "z"};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
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
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(m01e1, l01e1, varMap)));        
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(m01e1, l01e1, varMap)));        
        
        edgesFrom2.add(new Edge(2, 2, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom2.add(new Edge(2, 1, new TagPiece(m1111, l1111, varMap)));        
                
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;        
    }
    
    /**
     * Esempio Figura 1. Composizione delle tag machine di Figura 1(a) e 1(b).
     * @throws Exception 
     */
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
    
    // esempio di Figura 2(a)
    public TagMachine generateTm3() throws Exception {
        String[] variables = {"x", "y"};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
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
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(m1ee1, l1ee1, varMap)));        
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(m0ee1, l0ee1, varMap)));        
        
        edgesFrom2.add(new Edge(2, 2, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom2.add(new Edge(2, 1, new TagPiece(m2ee1, l2ee1, varMap)));        
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;        
    }
    
    // esempio di Figura 2(b)
    public TagMachine generateTm4() throws Exception {
        String[] variables = {"x", "z"};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
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
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(m0ee1, l0ee1, varMap)));        
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(m0ee1, l0ee1, varMap)));        
        
        edgesFrom2.add(new Edge(2, 2, new TagPiece(m0ee0, l0ee0, varMap)));
        edgesFrom2.add(new Edge(2, 1, new TagPiece(m2ee1, l2ee1, varMap)));        
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;        
    }
    
    /**
     * Esempio Figura 2. Composizione delle tag machine di Figura 2(a) e 2(b).
     * @throws Exception 
     */
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
    
    FloatTag e = FloatTag.EPSILON;
    
    public TagMachine generateTorqueTMPaper() throws Exception {
        //                     0       1       2       3      4       5       6      7        8        9       10
        String[] variables = {"x11", "x21", "sm11", "sm21", "sm31", "fai", "pfai", "rot", "cutoff", "u", "aw"}; // u = torque
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        int initialState = 0;
        int[] acceptingStates = {0};
        Var[] initialVarValues = {new FloatVar(36), new FloatVar(1), new FloatVar((float)-4.4857183), 
            new FloatVar(2660), new FloatVar((float)352.0831), new FloatVar(0), 
            new FloatVar(0), new BoolVar(false), new BoolVar(false), new FloatVar((float)12.41), new FloatVar((float)1.3652334)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        edges.add(edgesFrom0);
        
        FloatTag d1 = new FloatTag(d);
        Tag[][] mu = {{d1,e,e,e,e,e,e,e,e,e,e},{e,d1,e,e,e,e,e,e,e,e,e},{e,e,d1,e,e,e,e,e,e,e,e},{e,e,e,d1,e,e,e,e,e,e,e},{e,e,e,e,d1,e,e,e,e,e,e},
                      {e,e,e,e,e,d1,e,e,e,e,e},{e,e,e,e,e,e,d1,e,e,e,e},{e,e,e,e,e,e,e,d1,e,e,e},{e,e,e,e,e,e,e,e,d1,e,e},{e,e,e,e,e,e,e,e,e,d1,e},{e,e,e,e,e,e,e,e,e,e,d1}};
        
        Float p1[] = {1+d*a11, d*a12, null, null, null, null, null, null, null, d*b11, null}; // x11 + d*(a11*x11 + a12*x21 + b11*torque)
        Float p2[] = {d*a21, 1+d*a22, null, null, null, null, null, null, null, d*b21, null}; // x21 + d*(a21*x11 + a22*x21 + b21*torque)
        Float p3[] = {null, null, 1+d*ap11, d*ap12, d*ap13, null, null, null, null, bp11, null}; // sm11 + d*(ap11*sm11 + ap12*sm21 + ap13*sm31) + bp11*torque
        Float p4[] = {null, null, d*ap21, 1+d*ap22, d*ap23, null, null, null, null, bp21, null}; // sm21 + d*(ap21*sm11 + ap22*sm21 + ap23*sm31) + bp21*torque
        Float p5[] = {null, null, d*ap31, d*ap32, 1+d*ap33, null, null, null, null, bp31, null}; // sm31 + d*(ap31*sm11 + ap32*sm21 + ap33*sm31) + bp31*torque
        Float p6[] = {null, null, null, d/60, null, (float)1, null, null, null, null, null}; // fai + d*sm21/60
        Float p11[] = {c12, c13, null, null, null, null, null, null, null, null, null}; // c12*x11+c13*x21
        
        LabelingFunction lf1 = new FloatLinearLabelingFunction(variables, p1);
        LabelingFunction lf2 = new FloatLinearLabelingFunction(variables, p2);
        LabelingFunction lf3 = new FloatLinearLabelingFunction(variables, p3);
        LabelingFunction lf4 = new FloatLinearLabelingFunction(variables, p4);
        LabelingFunction lf5 = new FloatLinearLabelingFunction(variables, p5);
        LabelingFunction lf6 = new FloatLinearLabelingFunction(variables, p6);
        LabelingFunction lf11 = new FloatLinearLabelingFunction(variables, p11);
        
        LabelingFunction lf7 = new LabelingFunction() {
            // ((fai-pfai>=i1) && (fai-pfai<=i2))*fai + (1-((fai-pfai>=i1) && (fai-pfai<=i2)))*pfai
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                String res;
                Float fai_pfai = ((Float)varValues.get("fai").getValue())-((Float)varValues.get("pfai").getValue());
                if(fai_pfai >= i1 && fai_pfai <= i2){
                    res = "fai";
                } else {
                    res = "pfai";
                }
                return varValues.get("fai").newInstance(varValues.get(res).getValue());
            }
        };
        
        LabelingFunction lf8 = new LabelingFunction() {
            // (fai-pfai>=i1) && (fai-pfai<=i2)
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                Float fai_pfai = ((Float)varValues.get("fai").getValue())-((Float)varValues.get("pfai").getValue());
                if(fai_pfai >= i1 && fai_pfai <= i2){
                    return new BoolVar(Boolean.TRUE);
                }
                return new BoolVar(Boolean.FALSE);
            }
        };
        
        LabelingFunction lf9 = new LabelingFunction() {
            // cutoff || (v11*x11+v12*x21>=0)
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                Float res = v11*((Float)varValues.get("x11").getValue()) + v12*((Float)varValues.get("x21").getValue());
                if((Boolean)varValues.get("cutoff").getValue() || res >= 0){
                    return new BoolVar(Boolean.TRUE);
                }
                return new BoolVar(Boolean.FALSE);
            }
        };                
        
        LabelingFunction lf10p = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                return new FloatVar(M);
            }
        };
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, new LabelingFunction[]{lf1, lf2, lf3, lf4, lf5, lf6, lf7, lf8, lf9, lf10p, lf11}, varMap)));
        
        LabelingFunction lf10 = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                return new FloatVar(0);
            }
        };
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, new LabelingFunction[]{lf1, lf2, lf3, lf4, lf5, lf6, lf7, lf8, lf9, lf10, lf11}, varMap)));
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new FloatTag());
        return tm;        
    }        
    
    public TagMachine generateControlTMPaper() throws Exception {        
        String[] variables = {"rot", "cutoff", "u"};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        int initialState = 0;
        int[] acceptingStates = {0};
        Var[] initialVarValues = {new BoolVar(false), new BoolVar(false), new FloatVar(M)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        edges.add(edgesFrom0);
        
        FloatTag d1 = new FloatTag(d);
        Tag[][] mu = {{d1,e,e},{e,d1,e},{e,e,d1}};        
        
        // labeling function di Hoa Le
        Var[] l1 = {new BoolVar(true), new BoolVar(true), new FloatVar(0)};
        Var[] l2 = {new BoolVar(true), new BoolVar(false), new FloatVar(M)};
        Var[] l3 = {new BoolVar(false), new BoolVar(true), new FloatVar(0)};
        Var[] l4 = {new BoolVar(false), new BoolVar(false), new FloatVar(M)};
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l3, varMap)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l1, varMap)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l2, varMap)));        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l4, varMap)));
                
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new FloatTag());
        return tm;        
    }
    
    public TagMachine generatePiston1TMPaper() throws Exception {
        String[] variables = {"rot"};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        int initialState = 0;
        int[] acceptingStates = {0,1,2,3}; // H, I, C, E
        Var[] initialVarValues = {new BoolVar(false)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        ArrayList<Edge> edgesFrom3 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        edges.add(edgesFrom3);
        
        IntegerTag d1 = new IntegerTag(1);
        Tag[][] mu = {{d1}}; // NB: la matrice mu è l'unica cosa riutilizzabile
        
        Var[] l1 = {new BoolVar(true)};
        Var[] l2 = {new BoolVar(false)};
        
        edgesFrom0.add(new Edge(0, 1, new TagPiece(mu, l1, varMap)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(mu, l1, varMap)));
        edgesFrom2.add(new Edge(2, 3, new TagPiece(mu, l1, varMap)));
        edgesFrom3.add(new Edge(3, 0, new TagPiece(mu, l1, varMap)));
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l2, varMap)));
        edgesFrom1.add(new Edge(1, 1, new TagPiece(mu, l2, varMap)));
        edgesFrom2.add(new Edge(2, 2, new TagPiece(mu, l2, varMap)));
        edgesFrom3.add(new Edge(3, 3, new TagPiece(mu, l2, varMap)));
                
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;        
    }
    
    public void runExampleEterPaper() throws Exception {
        
        for(int z=0; z<simulationName.length; z++){
            String sn = simulationName[z];
            FileWriter resultsFile = new FileWriter(String.format("plots/automotive_results_%s.csv", sn));
            resultsFile.write("steps, time(s), memory(MB)\n");

            TagMachine tmTorque = generateTorqueTMPaper();
            TagMachine tmControl = generateControlTMPaper();
            TagMachine tmPiston1 = generatePiston1TMPaper();
            
            TagMachineSet tmSet = new TagMachineSet();
            tmSet.add(tmTorque);
            tmSet.add(tmControl);
            tmSet.add(tmPiston1);

            ArrayList<Morphism> mm = new ArrayList<>();
            Morphism m = new Morphism() {
                @Override
                public Tag map(Tag tag) throws Exception {
                    if (tag.isEpsilon()) {
                        return getTagInstance().getEpsilon();
                    } else {
                        return new FloatTag(((float) 0.0011810) * ((IntegerTag) tag).getTag());
                    }
                }

                @Override
                public Tag getTagInstance() {
                    return new FloatTag();
                }
            };
            mm.add(null);
            mm.add(null);
            mm.add(m);            

            System.gc();
            int numSteps = 1000001;
            long usedByte;
            ArrayList<ArrayList<Object>> results;
            
            System.out.println("start simulation");
            long startTime = System.nanoTime();
            
            switch (z) {
                case 0:
                    TagMachine tmComp = tmSet.compose(mm);
                    tmComp.startTime = startTime;
                    usedByte = tmComp.simulate(numSteps, RND, false);
                    results = tmComp.results;
                    break;
                case 1:
                    usedByte = tmSet.simulate(mm, m.getTagInstance(), numSteps, RND);
                    results = tmSet.results;
                    break;
                default:
                    usedByte = tmSet.simulate2(mm, m.getTagInstance(), numSteps, RND);
                    results = tmSet.results;
                    break;
            }
            
            for (ArrayList<Object> entry : results) {
                int steps = (int) entry.get(0);
                double executionTime = ((long) entry.get(1)) / 1000000000.0;
                double usedMB = ((long) entry.get(2)) / (double) (1024 * 1024);
                resultsFile.write(steps + ", " + executionTime + ", " + usedMB + "\n");
            }
            
            resultsFile.close();
        }
    }
    
    public void runExampleEterPaper2() throws Exception {
        
        TagMachine tmTorque = generateTorqueTMPaper();
        TagMachine tmControl = generateControlTMPaper();
        TagMachine tmPiston1 = generatePiston1TMPaper();

        TagMachineSet tmSet = new TagMachineSet();
        tmSet.add(tmTorque);
//        tmSet.add(tmControl);
        tmSet.add(tmPiston1);

        ArrayList<Morphism> mm = new ArrayList<>();
        Morphism m = new Morphism() {
            @Override
            public Tag map(Tag tag) throws Exception {
                if (tag.isEpsilon()) {
                    return getTagInstance().getEpsilon();
                } else {
                    return new FloatTag(((float) 0.0011810) * ((IntegerTag) tag).getTag());
                }
            }

            @Override
            public Tag getTagInstance() {
                return new FloatTag();
            }
        };
        mm.add(null);
//        mm.add(null);
        mm.add(m);            

        System.out.println("start simulation");
        long startTime = System.nanoTime();

        TagMachine tmComp = tmSet.compose(mm);
        tmComp.startTime = startTime;
        tmComp.simulate(2000, false, false);

    }
    
    int NUM_VARS = 10;
    int NUM_STATES = 4;
    int NUM_TM = 4;
    int NUM_STEPS = 100;
    boolean RND = true;
    
    public TagMachine generateFullyConnectedTM() throws Exception {                
        String[] variables = new String[NUM_VARS];        
        int initialState = 0;
        int[] acceptingStates = {0};
        Var[] initialVarValues = new Var[NUM_VARS];
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        
        for(int i=0; i<NUM_VARS; i++){
            variables[i] = Integer.toString(i);
            initialVarValues[i] = new BoolVar(Boolean.TRUE);
            varMap.put(variables[i], i);
        }
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>(NUM_STATES);
        
        for(int from=0; from<NUM_STATES; from++){
            ArrayList<Edge> edgesFrom = new ArrayList<>(NUM_STATES);
            for(int to=0; to<NUM_STATES; to++){
                
                Tag[][] mu = new Tag[NUM_VARS][NUM_VARS];
                LabelingFunction[] ll = new LabelingFunction[NUM_VARS];
                for(int j=0; j<NUM_VARS; j++){
                    ll[j] = new LabelingFunction() {
                        @Override
                        public Var apply(HashMap<String, Var> varValues) {
                            return new BoolVar(Boolean.TRUE);
                        }
                    };

                    for(int z=0; z<NUM_VARS; z++){
                        mu[j][z] = IntegerTag.IDENTITY;
                    }
                }
                
                edgesFrom.add(new Edge(from, to, new TagPiece(mu, ll, varMap)));
            }
            edges.add(edgesFrom);
        }
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;
    }
    
    String[] simulationName = {"static_composition", "dynamic_composition", "dynamic_composition2"};
    public void runExpExample() throws Exception {                
        
        for(int z=0; z<simulationName.length; z++){
            String sn = simulationName[z];
            FileWriter resultsFile;
            if(RND)
                resultsFile = new FileWriter(String.format("plots/%s_rnd.csv", sn));
            else
                resultsFile = new FileWriter(String.format("plots/%s.csv", sn));
            resultsFile.write("#tm, #states, #transitions, steps, time(s), memory(MB)\n");

            try{
                for(int j=0; j<6; j++){
                    System.gc();
                    
                    if((z==0 && j>=4))
                        continue;
                        
                    TagMachineSet tmSet = new TagMachineSet();
                    for(int i=0; i<2+j; i++){
                        tmSet.add(generateFullyConnectedTM());
                    }

                    System.out.println(sn+" - start simulation "+j);

                    int numSteps = NUM_STEPS;                
                    long startTime = System.nanoTime();
                    long usedByte;
                    
                    switch (z) {
                        case 0:
                            TagMachine tmComp = tmSet.compose();
                            usedByte = tmComp.simulate(numSteps, RND, false);
                            break;
                        case 1:
                            usedByte = tmSet.simulate(numSteps, RND);                                    
                            break;
                        default:
                            usedByte = tmSet.simulate2(null, new IntegerTag(), numSteps, RND);
                            break;
                    }

                    double usedMB = usedByte / (double) (1024*1024);
                    double executionTime = (System.nanoTime()-startTime) / 1000000000.0;
                    int numTms = tmSet.size();
                    int numStates = (int) Math.pow(NUM_STATES, numTms);
                    int numTrans = (int) Math.pow(NUM_STATES*NUM_STATES, numTms);

                    resultsFile.write(numTms+", "+numStates+", "+numTrans+", "+numSteps+", "+executionTime+", "+usedMB+"\n");
                }
            } catch(Exception e) {                
            } finally {
                resultsFile.close();
            }
            
            
            if(RND)
                resultsFile = new FileWriter(String.format("plots/%s_rnd_steps.csv", sn));
            else
                resultsFile = new FileWriter(String.format("plots/%s_steps.csv", sn));
            resultsFile.write("#tm, #states, #transitions, steps, time(s), memory(MB)\n");

            try{
                System.gc();

                TagMachineSet tmSet = new TagMachineSet();
                for(int i=0; i<NUM_TM; i++){
                    tmSet.add(generateFullyConnectedTM());
                }

                System.out.println(sn+" steps - start simulation ");

                int numSteps = 10000;                
                long startTime = System.nanoTime();
                long usedByte;
                ArrayList<ArrayList<Object>> results;

                switch (z) {
                    case 0:
                        TagMachine tmComp = tmSet.compose();
                        tmComp.startTime = startTime;
                        usedByte = tmComp.simulate(numSteps, RND, false);
                        results = tmComp.results;
                        break;
                    case 1:
                        usedByte = tmSet.simulate(numSteps, RND);      
                        results = tmSet.results;
                        break;
                    default:
                        usedByte = tmSet.simulate2(null, new IntegerTag(), numSteps, RND);
                        results = tmSet.results;
                        break;
                }

                int numTms = tmSet.size();
                int numStates = (int) Math.pow(NUM_STATES, numTms);
                int numTrans = (int) Math.pow(NUM_STATES*NUM_STATES, numTms);
                
                for(ArrayList<Object> entry : results){
                    int steps = (int) entry.get(0);
                    double executionTime = ((long) entry.get(1)) / 1000000000.0;
                    double usedMB = ((long) entry.get(2)) / (double)(1024*1024);
                    resultsFile.write(numTms+", "+numStates+", "+numTrans+", "+steps+", "+executionTime+", "+usedMB+"\n");
                }                    

            } catch(Exception e) {
            } finally {
                resultsFile.close();
            }
        }
    }
    
    public void runTMSteps() throws Exception {
        int numTm = 5;
        
        for(int z=0; z<simulationName.length; z++){
            String sn = simulationName[z];
            FileWriter resultsFile;
            
            if(RND)
                resultsFile = new FileWriter(String.format("plots/%s_rnd_steps_%dtm.csv", sn, numTm));
            else
                resultsFile = new FileWriter(String.format("plots/%s_steps_%dtm.csv", sn, numTm));
            resultsFile.write("#tm, #states, #transitions, steps, time(s), memory(MB)\n");

            try{
                System.gc();

                TagMachineSet tmSet = new TagMachineSet();
                for(int i=0; i<numTm; i++){
                    tmSet.add(generateFullyConnectedTM());
                }

                System.out.println(sn+" steps - start simulation ");

                int numSteps = 10000;                
                long startTime = System.nanoTime();
                long usedByte;
                ArrayList<ArrayList<Object>> results;

                switch (z) {
                    case 0:
                        TagMachine tmComp = tmSet.compose();
                        tmComp.startTime = startTime;
                        usedByte = tmComp.simulate(numSteps, RND, false);
                        results = tmComp.results;
                        break;
                    case 1:
                        usedByte = tmSet.simulate(numSteps, RND);      
                        results = tmSet.results;
                        break;
                    default:
                        usedByte = tmSet.simulate2(null, new IntegerTag(), numSteps, RND);
                        results = tmSet.results;
                        break;
                }

                int numTms = tmSet.size();
                int numStates = (int) Math.pow(NUM_STATES, numTms);
                int numTrans = (int) Math.pow(NUM_STATES*NUM_STATES, numTms);
                
                for(ArrayList<Object> entry : results){
                    int steps = (int) entry.get(0);
                    double executionTime = ((long) entry.get(1)) / 1000000000.0;
                    double usedMB = ((long) entry.get(2)) / (double)(1024*1024);
                    resultsFile.write(numTms+", "+numStates+", "+numTrans+", "+steps+", "+executionTime+", "+usedMB+"\n");
                }

            } catch(Exception e) {
            } finally {
                resultsFile.close();
            }
        }
    }
    
    public TagMachine generateTank() throws Exception {
        Tag zero = new FloatTag((float)0.0);
        Tag zp5 = new FloatTag((float)0.5);
        Tag e1 = FloatTag.EPSILON;
        Integer OP = 0;
        Integer CL = 1;
        
        String[] variables = {"cmd", "x"};        
        int initialState = 0;
        int[] acceptingStates = {1,2,3,4,5};
        Var[] initialVarValues = {new IntVar(OP), new FloatVar(0)};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        ArrayList<Edge> edgesFrom3 = new ArrayList<>();
        ArrayList<Edge> edgesFrom4 = new ArrayList<>();
        ArrayList<Edge> edgesFrom5 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        edges.add(edgesFrom3);
        edges.add(edgesFrom4);
        edges.add(edgesFrom5);
        
        Tag[][] mu0 = {{zero,zp5},{e1,zp5}};
        Tag[][] mu1 = {{zp5,zp5},{zp5,zp5}};
        
        Var[] l0 = {null, new FloatVar(0)};
        Var[] l1 = {new IntVar(OP), new FloatVar(0)};
        Var[] l2 = {null, new FloatVar((float)0.5)};
        Var[] l3 = {null, new FloatVar(1)};
        Var[] l4 = {new IntVar(CL), new FloatVar(1)};
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu0, l0, varMap)));
        edgesFrom0.add(new Edge(0, 1, new TagPiece(mu1, l1, varMap)));
        
        edgesFrom1.add(new Edge(1, 2, new TagPiece(mu0, l2, varMap)));
        
        edgesFrom2.add(new Edge(2, 3, new TagPiece(mu0, l3, varMap)));
        edgesFrom2.add(new Edge(2, 4, new TagPiece(mu1, l4, varMap)));
        
        edgesFrom3.add(new Edge(3, 3, new TagPiece(mu0, l3, varMap)));
        edgesFrom3.add(new Edge(3, 4, new TagPiece(mu1, l4, varMap)));
        
        edgesFrom4.add(new Edge(4, 5, new TagPiece(mu0, l2, varMap)));
        
        edgesFrom5.add(new Edge(5, 0, new TagPiece(mu0, l0, varMap)));
        edgesFrom5.add(new Edge(5, 1, new TagPiece(mu1, l1, varMap)));        
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new FloatTag());
        return tm;
    }
    
    public TagMachine generateTankController() throws Exception {
        Tag zero = new IntegerTag(0);
        Tag one = new IntegerTag(1);
        Tag e2 = IntegerTag.EPSILON;
        Integer OP = 0;
        Integer CL = 1;
        
        String[] variables = {"cmd", "x"};
        int initialState = 0;
        int[] acceptingStates = {1,2};
        Var[] initialVarValues = {new IntVar(OP), new FloatVar(0)};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        
        Tag[][] mu0 = {{zero,one},{e2,one}};
        Tag[][] mu1 = {{one,one},{one,one}};
        
        Var[] l1 = {new IntVar(OP), new FloatVar(0)};
        Var[] l2 = {null, new FloatVar((float)0.5)};
        Var[] l3 = {new IntVar(CL), new FloatVar(1)};
        
        edgesFrom0.add(new Edge(0, 1, new TagPiece(mu1, l1, varMap)));
        edgesFrom0.add(new Edge(0, 2, new TagPiece(mu1, l3, varMap)));
        
        edgesFrom1.add(new Edge(1, 1, new TagPiece(mu0, l2, varMap)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(mu1, l3, varMap)));
        
        edgesFrom2.add(new Edge(2, 1, new TagPiece(mu1, l1, varMap)));
        edgesFrom2.add(new Edge(2, 2, new TagPiece(mu0, l2, varMap)));
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;
    }
    
    public void runTankExample() throws Exception {
        RND=true;
        for(int z=0; z<simulationName.length; z++){
            String sn = simulationName[z];
            FileWriter resultsFile = new FileWriter(String.format("plots/tank_results_%s_rnd.csv", sn));
            resultsFile.write("steps, time(s), memory(MB)\n");

            TagMachine tankTM = generateTank();
            TagMachine tankControllerTM = generateTankController();

            TagMachineSet tmSet = new TagMachineSet();
            tmSet.add(tankTM);
            tmSet.add(tankControllerTM);

            ArrayList<Morphism> mm = new ArrayList<>();        
            Morphism m = new Morphism() {
                @Override
                public Tag map(Tag tag) throws Exception {
                    if(tag.isEpsilon())
                        return getTagInstance().getEpsilon();
                    else
                        return new FloatTag(((float)0.5) * ((IntegerTag) tag).getTag());
                }

                @Override
                public Tag getTagInstance() {
                    return new FloatTag();
                }
            };
            mm.add(null);
            mm.add(m);

            System.gc();
            int numSteps = 1000001;
            long usedByte;
            ArrayList<ArrayList<Object>> results;
            
            long startTime = System.nanoTime();
            
            switch (z) {
                case 0:
                    TagMachine tmComp = tmSet.compose(mm);
                    tmComp.startTime = startTime;
                    usedByte = tmComp.simulate(numSteps, RND, false);
                    results = tmComp.results;
                    break;
                case 1:
                    usedByte = tmSet.simulate(mm, new FloatTag(), numSteps, RND);
                    results = tmSet.results;
                    break;
                default:
                    usedByte = tmSet.simulate2(mm, new FloatTag(), numSteps, RND);
                    results = tmSet.results;
                    break;
            }
            
            for (ArrayList<Object> entry : results) {
                int steps = (int) entry.get(0);
                double executionTime = ((long) entry.get(1)) / 1000000000.0;
                double usedMB = ((long) entry.get(2)) / (double) (1024 * 1024);
                resultsFile.write(steps + ", " + executionTime + ", " + usedMB + "\n");
            }
            
            resultsFile.close();
        }
    }
    
    public TagMachine generateCaldaiaTM() throws Exception {
        String[] variables = {"C", "T"};        
        int initialState = 0;
        int[] acceptingStates = {};
        Var[] initialVarValues = {new BoolVar(Boolean.TRUE), new FloatVar(0)};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        edges.add(edgesFrom0);
        
        Tag[][] mu = {{one,eps},{eps,one}};
        
        LabelingFunction lfCaldaiaOpen = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                return new BoolVar(Boolean.TRUE);
            }
        };
        
        LabelingFunction lfCaldaiaClose = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                return new BoolVar(Boolean.FALSE);
            }
        };
        
        LabelingFunction lfTemp = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                Float temp = ((FloatVar) varValues.get("T")).getValue();
                Boolean C = ((BoolVar) varValues.get("C")).getValue();
                if(C)
                    return new FloatVar(temp+(float)0.5);
                else
                    return new FloatVar(temp-(float)0.5);
            }
        };
        
        LabelingFunction[] ll0 = {lfCaldaiaClose, lfTemp};
        LabelingFunction[] ll1 = {lfCaldaiaOpen, lfTemp};
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, ll0, varMap)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, ll1, varMap)));
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;
    }
    
    public TagMachine generateControllerCaldaiaTM() throws Exception {
        String[] variables = {"C", "T"};        
        int initialState = 0;
        int[] acceptingStates = {};
        Var[] initialVarValues = {new BoolVar(Boolean.TRUE), new FloatVar(0)};
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        ArrayList<Edge> edgesFrom1 = new ArrayList<>();
        ArrayList<Edge> edgesFrom2 = new ArrayList<>();
        ArrayList<Edge> edgesFrom3 = new ArrayList<>();
        edges.add(edgesFrom0);
        edges.add(edgesFrom1);
        edges.add(edgesFrom2);
        edges.add(edgesFrom3);
        
        Tag[][] mu = {{one,eps},{eps,one}};
        
        LabelingFunction lfCaldaiaOpen = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                return new BoolVar(Boolean.TRUE);
            }
        };
        
        LabelingFunction lfCaldaiaClose = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                return new BoolVar(Boolean.FALSE);
            }
        };
        
        LabelingFunction lfTemp = new LabelingFunction() {
            @Override
            public Var apply(HashMap<String, Var> varValues) {
                Float temp = ((FloatVar) varValues.get("T")).getValue();
                Boolean C = ((BoolVar) varValues.get("C")).getValue();
                if(C)
                    return new FloatVar(temp+(float)0.5);
                else
                    return new FloatVar(temp-(float)0.5);
            }
        };
        
        LabelingFunction[] ll0 = {lfCaldaiaClose, lfTemp};
        LabelingFunction[] ll1 = {lfCaldaiaOpen, lfTemp};
        
        edgesFrom0.add(new Edge(0, 1, new TagPiece(mu, ll1, varMap)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(mu, ll0, varMap)));
        edgesFrom2.add(new Edge(2, 3, new TagPiece(mu, ll0, varMap)));
        edgesFrom3.add(new Edge(3, 0, new TagPiece(mu, ll1, varMap)));
        
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new IntegerTag());
        return tm;
    }
    
    public void runCaldaiaExample() throws Exception {
        TagMachine caldaia = generateCaldaiaTM();
        TagMachine controller = generateControllerCaldaiaTM();
        
        TagMachineSet tmSet = new TagMachineSet();
        tmSet.add(caldaia);
        tmSet.add(controller);
        
        TagMachine tmComp = tmSet.compose();
        System.out.println("TMcomposition --------------------------------------");
        System.out.println(tmComp);
        System.out.println("run of tmComp");
        tmComp.simulate(10, false, true);
    }
    
    public static void main(String[] args) throws Exception {
        Main m = new Main();
//        m.runExampleEterPaper2();
//        m.runExample1();
//        m.runExample2();
//        m.runExpExample();
        m.runTankExample();
//        m.runCaldaiaExample();
//        m.runSimulation2();
//        m.runTMSteps();
    }
}
