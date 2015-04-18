package edu.nyu.cims.compfin15.montecarlo;

/**
 * Created by Narasimman on 4/15/2015.
 */

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

/**
 * European Payout
 * Payout is the positive value of the final price minus the strick price.
 */
public class EuropeanPayOut implements IPayOut {
    /* Strike price of the option*/
    private double strikePrice;

    public EuropeanPayOut(Option option) {
        this.setStrikePrice(option.getStrikePrice());
    }
    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    @Override
    /**
     * get the value of the stock path.
     * Payout is the positive value of the final price minus the strick price.
     */
    public double getValue(IPath path) {
        ArrayList<Pair> price =  path.getPrices();
        return Math.max((Double)(price.get(price.size() - 1).getValue()) - strikePrice, 0);
    }
}
