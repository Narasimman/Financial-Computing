package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/15/2015.
 */
public interface IPayOut {
    public double getValue(IPath path);
}
