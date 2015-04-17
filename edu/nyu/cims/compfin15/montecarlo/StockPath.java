package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/15/2015.
 */
public class StockPath implements IPath {
    @Override
    /**
     * Get the value on the path at time T0
     */
    public double getT0() {
        return 0;
    }

    @Override
    public double getT1() {
        return 0;
    }

    @Override
    public double getDelta() {
        return 0;
    }

    @Override
    public double[] getPath() {
        return new double[0];
    }
}
