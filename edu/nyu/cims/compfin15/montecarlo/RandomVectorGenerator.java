package edu.nyu.cims.compfin15.montecarlo;

import org.apache.commons.math3.random.*;


/**
 * Created by Narasimman on 4/15/2015.
 */
public class RandomVectorGenerator implements IRandomVectorGenerator {
    private UncorrelatedRandomVectorGenerator generator;
    long seed;

    public RandomVectorGenerator(int mean){
        JDKRandomGenerator rnd = new JDKRandomGenerator();
        GaussianRandomGenerator gr = new GaussianRandomGenerator(rnd);
        this.generator = new UncorrelatedRandomVectorGenerator(mean, gr);
    }

    @Override
    public void setSeed(long s) {
        seed = s;
    }

    @Override
    /**
     * Creates a Guassian random generator from a randomnumber generator.
     * Then we create an instance of the RandomVectorGenerator
     */
    public double[] getUniformRandomNumber() {
        return generator.nextVector();

    }
}
