package edu.nyu.cims.compfin15.montecarlo;

import java.util.ArrayList;

/**
 * Created by Narasimman on 4/18/2015.
 */
public class Statistics {
    private ArrayList<Double> array;
    private double mean = 0;
    private double stdVar = 0;
    private double meanSq = 0;

    public Statistics() {
        this.array = new ArrayList<Double>();
    }

    /**
     * Add new element
     * @param x new element.
     */
    public void add(double x) {
        int n = array.size()+1;
        array.add(new Double(x));
        mean =(n - 1.0)/n*mean + x/n;
        meanSq =(n - 1.0)/n*meanSq + x*x/n;
        stdVar = Math.sqrt(meanSq-mean*mean);
    }


    /**
     * @return the mean
     */
    public double getMean() {
        return mean;
    }

    /**
     * @return the stdVar
     */
    public double getStdVar() {
        return stdVar;
    }

}
