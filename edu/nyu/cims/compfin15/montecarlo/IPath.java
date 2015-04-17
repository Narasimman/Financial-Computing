package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/15/2015.
 */
public interface IPath {
    public double getT0();
    public double getT1();
    public double getDelta();
    public double[] getPath();
}
