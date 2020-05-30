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
    
    public TagMachine generateTorqueTM() throws Exception {
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
        Tag[][] mu = {{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1},{d1,d1,d1,d1,d1,d1,d1,d1,d1,d1,d1}};
        
        float z = (float)0;
        Float p1[] = {1+d*a11, d*a12, z, z, z, z, z, z, z, d*b11, z}; // x11 + d*(a11*x11 + a12*x21 + b11*torque)
        Float p2[] = {d*a21, 1+d*a22, z, z, z, z, z, z, z, d*b21, z}; // x21 + d*(a21*x11 + a22*x21 + b21*torque)
        Float p3[] = {z, z, 1+d*ap11, d*ap12, d*ap13, z, z, z, z, bp11, z}; // sm11 + d*(ap11*sm11 + ap12*sm21 + ap13*sm31) + bp11*torque
        Float p4[] = {z, z, d*ap21, 1+d*ap22, d*ap23, z, z, z, z, bp21, z}; // sm21 + d*(ap21*sm11 + ap22*sm21 + ap23*sm31) + bp21*torque
        Float p5[] = {z, z, d*ap31, d*ap32, 1+d*ap33, z, z, z, z, bp31, z}; // sm31 + d*(ap31*sm11 + ap32*sm21 + ap33*sm31) + bp31*torque
        Float p6[] = {z, z, z, d/60, z, (float)1, z, z, z, z, z}; // fai + d*sm21/60
        Float p10[] = {z, z, z, z, z, z, z, z, z, z, z}; // 0
        Float p11[] = {c12, c13, z, z, z, z, z, z, z, z, z}; // c12*x11+c13*x21
        
        LabelingFunction lf1 = new FloatLinearLabelingFunction(p1);
        LabelingFunction lf2 = new FloatLinearLabelingFunction(p2);
        LabelingFunction lf3 = new FloatLinearLabelingFunction(p3);
        LabelingFunction lf4 = new FloatLinearLabelingFunction(p4);
        LabelingFunction lf5 = new FloatLinearLabelingFunction(p5);
        LabelingFunction lf6 = new FloatLinearLabelingFunction(p6);
        LabelingFunction lf10 = new FloatLinearLabelingFunction(p10);
        LabelingFunction lf11 = new FloatLinearLabelingFunction(p11);
        
        LabelingFunction lf7 = new LabelingFunction() {
            // ((fai-pfai>=i1) && (fai-pfai<=i2))*fai + (1-((fai-pfai>=i1) && (fai-pfai<=i2)))*pfai
            @Override
            public Var apply(Var[] varValues) {
                Float value = ((Float)varValues[6].getValue());
                Float fai_pfai = ((Float)varValues[5].getValue())-((Float)varValues[6].getValue());
                if(fai_pfai >= i1 && fai_pfai <= i2){
                    value = ((Float)varValues[5].getValue());
                }
                return varValues[0].newInstance(value);
            }
        };
        
        LabelingFunction lf8 = new LabelingFunction() {
            // (fai-pfai>=i1) && (fai-pfai<=i2)
            @Override
            public Var apply(Var[] varValues) {
                Float fai_pfai = ((Float)varValues[5].getValue())-((Float)varValues[6].getValue());
                if(fai_pfai >= i1 && fai_pfai <= i2){
                    return new BoolVar(Boolean.TRUE);
                }
                return new BoolVar(Boolean.FALSE);
            }
        };
        
        LabelingFunction lf9 = new LabelingFunction() {
            // cutoff || (v11*x11+v12*x21>=0)
            @Override
            public Var apply(Var[] varValues) {
                Float res = v11*((Float)varValues[0].getValue()) + v12*((Float)varValues[1].getValue());
                if((Boolean)varValues[8].getValue() || res >= 0){
                    return new BoolVar(Boolean.TRUE);
                }
                return new BoolVar(Boolean.FALSE);
            }
        };
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, new LabelingFunction[]{lf1, lf2, lf3, lf4, lf5, lf6, lf7, lf8, lf9, lf10, lf11})));
        
        LabelingFunction lf10p = new LabelingFunction() {
            @Override
            public Var apply(Var[] varValues) {
                return new FloatVar(M);
            }
        };
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, new LabelingFunction[]{lf1, lf2, lf3, lf4, lf5, lf6, lf7, lf8, lf9, lf10p, lf11})));
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusFloat());
        return tm;        
    }
    
    public TagMachine generateControlTM() throws Exception {
        float M    = (float) 12.41;
                
        String[] variables = {"rot", "cutoff", "j", "torque"};
        int initialState = 0;
        int[] acceptingStates = {0};
        Var[] initialVarValues = {new BoolVar(false), new BoolVar(false), new IntVar(1), new FloatVar(M)};
        
        ArrayList<ArrayList<Edge>> edges = new ArrayList<>();
        ArrayList<Edge> edgesFrom0 = new ArrayList<>();
        edges.add(edgesFrom0);
        
        MaxPlusFloat d1 = new MaxPlusFloat((float)0.0011810); // TODO: 0.0011810 ??
        Tag[][] mu = {{d1,d1,d1,d1},{d1,d1,d1,d1},{d1,d1,d1,d1},{d1,d1,d1,d1}};
        
        Var[] l1 = {new BoolVar(true), new BoolVar(true), new IntVar(0), new FloatVar(0)};
        Var[] l2 = {new BoolVar(true), new BoolVar(false), new IntVar(1), new FloatVar(M)};
        Var[] l3 = {new BoolVar(false), new BoolVar(true), null, new FloatVar(0)};
        Var[] l4 = {new BoolVar(false), new BoolVar(false), null, new FloatVar(M)};
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l1)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l2)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l3)));
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l4)));
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusFloat());
        return tm;        
    }
    
    public TagMachine generatePiston1TM() throws Exception {
        String[] variables = {"rot"};
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
        
        MaxPlusInteger d1 = new MaxPlusInteger(1);
        Tag[][] mu = {{d1}}; // TODO: la matrice mu è l'unica cosa riutilizzabile
        
        Var[] l1 = {new BoolVar(true)};
        Var[] l2 = {new BoolVar(false)};
        
        edgesFrom0.add(new Edge(0, 1, new TagPiece(mu, l1)));
        edgesFrom1.add(new Edge(1, 2, new TagPiece(mu, l1)));
        edgesFrom2.add(new Edge(2, 3, new TagPiece(mu, l1)));
        edgesFrom3.add(new Edge(3, 0, new TagPiece(mu, l1)));
        
        edgesFrom0.add(new Edge(0, 0, new TagPiece(mu, l2)));
        edgesFrom1.add(new Edge(1, 1, new TagPiece(mu, l2)));
        edgesFrom2.add(new Edge(2, 2, new TagPiece(mu, l2)));
        edgesFrom3.add(new Edge(3, 3, new TagPiece(mu, l2)));
        
        HashMap<String, Integer> varMap = new HashMap<>(variables.length);
        for(int i=0; i<variables.length; i++){
            varMap.put(variables[i], i);
        }
        TagMachine tm = new TagMachine(varMap, edges, initialState, acceptingStates, initialVarValues, new MaxPlusInteger());
        return tm;        
    }
    
    public void runExampleEtherogeneous() throws Exception {
        TagMachine tmControl = generateControlTM();
        System.out.println("Control TM -----------------------------------------");
        System.out.println(tmControl);
        //System.out.println("random run");
        //tm1.simulate(20, true, true);
        
        TagMachine tmPiston1 = generatePiston1TM();
        System.out.println("Piston1 TM -----------------------------------------");
        System.out.println(tmPiston1);
        //System.out.println("random run");
        //tm2.simulate(20, true, true);
        
        TagMachine tmTorque = generateTorqueTM();
        System.out.println("Torque TM ------------------------------------------");
        System.out.println(tmTorque);
        //System.out.println("random run");
        //tm3.simulate(20, true, true);
        
        
        ArrayList<Morphism> mm = new ArrayList<>();        
        Morphism m = new Morphism() {
            @Override
            public Tag map(Tag tag) throws Exception {
                if(tag.isEpsilon())
                    return getTagInstance().getEpsilon();
                else
                    return new MaxPlusFloat(((float)0.0011810) * ((MaxPlusInteger) tag).getTag());
            }

            @Override
            public Tag getTagInstance() {
                return new MaxPlusFloat();
            }
        };        
        mm.add(null);
        mm.add(null);
        mm.add(m);
        
        TagMachineSet tmSet = new TagMachineSet();
        tmSet.add(tmTorque);        
        tmSet.add(tmControl);
        tmSet.add(tmPiston1);
        TagMachine tmComp = tmSet.compose(mm);
        System.out.println("TMcomposition --------------------------------------");
        System.out.println(tmComp);
        System.out.println("random run of tmComp");
        tmComp.simulate(10, true, true);
        
    }
    
    public static void main(String[] args) throws Exception {
        Main m = new Main();
        m.runExampleEtherogeneous();
    }
}
