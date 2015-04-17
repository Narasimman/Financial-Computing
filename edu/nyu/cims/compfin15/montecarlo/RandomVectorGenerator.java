package edu.nyu.cims.compfin15.montecarlo;

import org.apache.commons.math3.random.*;


/**
 * Created by Narasimman on 4/15/2015.
 */
public class RandomVectorGenerator implements IRandomVectorGenerator {
    UncorrelatedRandomVectorGenerator r;
    long seed;

    void RandomVectorGenerator(){
    }

    @Override
    public void setSeed(long s) {
        seed = s;
    }

    @Override
    public double[] getUniformRandomNumber() {
        JDKRandomGenerator rnd = new JDKRandomGenerator();
        GaussianRandomGenerator gr = new GaussianRandomGenerator(rnd);
        r = new UncorrelatedRandomVectorGenerator(1, gr);

        return r.nextVector();

    }
}
