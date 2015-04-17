package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/15/2015.
 */
public class StockPath implements IPath {
    double t0, t1;
    @Override
    /**
     * Get the value on the path at time T0
     */
    public double getT0() {
        return t0;
    }

    @Override
    public double getT1() {
        return t1;
    }

    @Override
    public double getDelta() {
        return t1 - t0;
    }

    @Override
    public double[] getPath() {
        return new double[0];
    }
}
