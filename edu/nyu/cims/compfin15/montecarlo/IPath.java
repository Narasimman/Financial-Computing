package edu.nyu.cims.compfin15.montecarlo;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

/**
 * Created by Narasimman on 4/15/2015.
 */
public interface IPath {
    public double getT0();
    public double getT1();
    public double getDelta();
    public void getPath();
    public ArrayList<Pair> getPrices();
}
