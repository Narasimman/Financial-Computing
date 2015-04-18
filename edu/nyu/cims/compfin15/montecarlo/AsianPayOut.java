package edu.nyu.cims.compfin15.montecarlo;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

/**
 * Created by Narasimman on 4/17/2015.
 */
public class AsianPayOut implements IPayOut {
    /* Strike price of the option*/
    private double strikePrice;

    public AsianPayOut(Option option) {
        this.setStrikePrice(option.getStrikePrice());
    }
    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    @Override
    public double getValue(IPath path) {
        ArrayList<Pair> price =  path.getPrices();

        double average = (Double) price.get(0).getValue();
        double sum  = 0.0;
        for (int i=1; i < price.size(); i++) {
            sum += (Double)price.get(i).getValue();
        }
        average = sum / price.size();

        return Math.max(average - strikePrice,0);
    }
}
