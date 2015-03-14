package edu.nyu.cims.compfin14.hw2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Narasimman on 3/10/2015.
 */
public class Bond {
    private double price;
    private double coupon;
    private double maturity;
    private double faceValue;
    private double paymentFrequency;
    private Map<Double,Double> cashFlow;

    Bond(double fv, double pr, double mt){
        this(fv, 0, pr, mt, 0);
    }

    /**
     * Zero Coupon Bond Constructor
     * @param fv - Face value
     * @param cp - coupon
     * @param pr - price
     * @param mt - Maturity
     * @param pf - payment frequency
     */
    Bond(double fv, double cp, double pr, double mt, double pf){
        faceValue = fv;
        coupon = cp;
        price = pr;
        maturity = mt;
        paymentFrequency = pf;
    }

    /**
     * Coupon Bearing Bond
     * @param fv - Face Value
     * @param cp - Coupon value
     * @param mt - Maturity year
     * @param pf - Number of times the coupon payment is made.
     */
    Bond(double fv, double cp, double mt, double pf){
        faceValue = fv;
        coupon = cp;
        maturity = mt;
        paymentFrequency = pf;
    }

    public double getPrice() {
        return price;
    }

    public double getPaymentFrequency() {
        return paymentFrequency;
    }

    public double getCoupon() {
        coupon = faceValue * (coupon / paymentFrequency);
        return coupon;
    }

    public double getMaturity() {
        return maturity;
    }

    public double getFaceValue() {
        return faceValue;
    }

    public Map<Double,Double> getCashFlow() {
        Map<Double, Double> cashFlow = new HashMap<Double, Double>();

        double coupon = getCoupon();
        double pf = 1/getPaymentFrequency();
        cashFlow.put(0.0, 0.0);

        for (double time = pf; time < getMaturity(); time += pf){
            cashFlow.put(time, coupon);
        }

        /* At the time of maturity */
        cashFlow.put(getMaturity(), getFaceValue() + coupon);

        return cashFlow;
    }
}