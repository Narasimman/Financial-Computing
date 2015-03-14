package edu.nyu.cims.compfin14.hw2;

import java.util.*;

/**
 * Created by Narasimman on 3/10/2015.
 */
public class YieldCurve {
    private double interestRate;
    private double forwardRate;
    private double discountFactor;

    private Map<Double, Double> yCurve = new TreeMap<Double, Double>();

    /**
     * Initializing the yield curve
     */
    YieldCurve(){
        /* initialize the yield curve*/
        yCurve.put(0.0, 0.0);
        yCurve.put(1.0, 2.0);
        yCurve.put(2.0, 2.3);
        yCurve.put(3.0, 3.0);
    }

    /**
     * Yield curve that takes list of bonds
     * @param bonds
     */
    YieldCurve(List<Bond> bonds) {
        double rate;
        for(Bond bond : bonds) {
            rate = Math.log(bond.getFaceValue() / bond.getPrice()) * bond.getMaturity();
            yCurve.put(bond.getMaturity(), rate);
        }
    }

    /**
     * returns the yield curve map
     * @return
     */
    public Map<Double, Double> getYieldCurveMap() {
        return yCurve;
    }

    /**
     * calculate the interest rate for a given time.
     * This is given by the formula to find distance between two points
     * @param time
     * @return
     */
    public double getInterestRate(double time) {
        if(yCurve.containsKey(time)) {
            return yCurve.get(time);
        }

        Set<Double> keys = yCurve.keySet();
        double t1 = 0, t2 = 0, max = 0;
        for(double t : keys) {
            if(t < time) {
                t1 = t;
            } else if(t2 < time && t > time) {
                t2 = t;
            }
            max = t;
        }

        /* If the given time is greater than the maximum ytm available in the yield curve */
        if(time > max || time < 0) return Double.NaN;

        double rt1 = yCurve.get(t1);
        double rt2 = yCurve.get(t2);

        /* We need to calculate only when t1 and t2 are different */
        if(Math.abs(t2 - t1) > 0) {
            interestRate = ((t2 - time) * rt1 + (time - t1) * rt2) / (t2 - t1);
        }
        return interestRate;
    }

    /**
     * Calculate forward rate for a given period
     * @param t1
     * @param t2
     * @return
     */
    public double getForwardRate(double t1, double t2) {
        double rt1 = getInterestRate(t1);
        double rt2 = getInterestRate(t2);
        forwardRate = ((rt2 * t2) - (rt1 * t1)) / (t2 -t1);
        return forwardRate;
    }

    /**
     * Calculate discount factor for a given Time
     * @param T
     * @return
     */
    public double getDiscountFactor(double T) {
        double rT = getInterestRate(T);
        discountFactor = Math.pow(Math.E, rT * T);
        return discountFactor;
    }

    @Override
    public String toString() {
        String s = "";
        Formatter formatter = new Formatter();
        s += String.format("%-10s | %s", "Year", "Rate\n");
        for (Map.Entry<Double, Double> entry : yCurve.entrySet()) {
            double year = entry.getKey();
            double rate = entry.getValue();
            if(year == 0) continue;
            s += String.format("%-10s | %.3f", year, rate);
            s += "\n";
        }
        return s;
    }
}
