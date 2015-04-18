package edu.nyu.cims.compfin15.montecarlo;

import org.apache.commons.math3.util.Pair;
import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by Narasimman on 4/15/2015.
 */
public class BrownianStockPath implements IPath {
    private RandomVectorGenerator generator;
    private Option option;
    private Pair<DateTime, Double> path;
    private ArrayList<Pair> paths;
    double t0, t1;

    public BrownianStockPath(Option option, RandomVectorGenerator rvg) {
        this.generator = rvg;
        this.option = option;
        this.paths = new ArrayList<Pair>();
    }

    @Override
    /**
     * Get the value on the path at time T0
     */
    public double getT0() {
        return path.getValue();
    }

    @Override
    public double getT1() {
        return path.getValue();
    }

    @Override
    public double getDelta() {
        return t1 - t0;
    }

    public ArrayList<Pair> getPrices() {
        this.getPath();
        return paths;
    }

    @Override
    /**
     * This calculates the price from initial stock price to the number of days specified on the option.
     * The formula for the browninan stock path is
     * Strikeprice * Math.exp((option.getRateOfInterest() - volatility*volatility/2)+volatility*random)
     */
    public void getPath() {
        this.paths.add(new Pair(DateTime.now(), option.getInitialPrice()));
        double[] arvg = generator.getUniformRandomNumber();
        double St = option.getInitialPrice();
        DateTime t = option.getInitialTime();
        for(int i=0; i< arvg.length;i++) {
            t = t.plusDays(1);
            double volatility = option.getVolatility();
            St = St * Math.exp((option.getRateOfInterest() - volatility*volatility/2)+volatility*arvg[i]);
            this.paths.add(new Pair(t, St));
        }
    }
}
