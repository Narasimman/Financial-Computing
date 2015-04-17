package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/17/2015.
 */
public class AntitheticRandomVectorGenerator implements IRandomVectorGenerator {
    private long seed;
    private RandomVectorGenerator generator;
    private double[] antitheticRandomNumber;
    private boolean flip = false;

    public AntitheticRandomVectorGenerator(RandomVectorGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public double[] getUniformRandomNumber() {
        if(!flip){
            antitheticRandomNumber = generator.getUniformRandomNumber();
        } else {
            for (int i =0; i < antitheticRandomNumber.length; i++) {
                antitheticRandomNumber[i] = - antitheticRandomNumber[i];
            }
        }
        flip = !flip;
        return this.antitheticRandomNumber;
    }
}
