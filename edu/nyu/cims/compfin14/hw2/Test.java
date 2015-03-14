package edu.nyu.cims.compfin14.hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Narasimman on 3/10/2015.
 */
public class Test {
    public static void main(String[] args) {

        Test test = new Test();
        /* A half year zero coupon bond with face value of 100$ traded at 95$ maturing in half a year */
        Bond b1 = new Bond(100, 95, 0.5);
        /* Another one with face value of 1000$ traded at 895$ maturing in a year */
        Bond b2 = new Bond(1000, 895, 1);

        List<Bond> bonds = new ArrayList<Bond>(10);
        bonds.add(b1);
        bonds.add(b2);

        /* Print the yield curve */
        YieldCurve yc = new YieldCurve(bonds);
        System.out.println("Q2. a. Yield Curve:");
        System.out.println(yc);

        /* What is getInterestRate(0.75) returns? */
        System.out.println("b. The getInterestRate(0.75) returns: " + String.format("%.3f", yc.getInterestRate(0.75)));

        /* A 5% coupon bond with 500$ face value, which pays semi-annually and duration of three years */
        Bond b3 = new Bond(500, 5, 3, 2);

        YieldCurve initialYieldCurve = new YieldCurve();
        /* Print the price */
        double priceOfBond = test.getPrice(initialYieldCurve, b3);
        System.out.println("Q3. The price of the bond is : " + priceOfBond);

        System.out.println(test.getYTM(b3, priceOfBond));
       
    }

    /**
     * Returns the value of a coupon bearing bond
     * Given yield curve and bond
     * @param yc
     * @param bond
     * @return
     */
    public double getPrice(YieldCurve yc, Bond bond) {
        double price = 0.0, pf = 0.0;
        Map<Double, Double> cashFlow = bond.getCashFlow();
        if (bond.getPaymentFrequency() > 0) {
            pf = 1 / bond.getPaymentFrequency();
        }
        for (double time = pf; time <= bond.getMaturity(); time += pf) {
            price += cashFlow.get(time) / Math.pow(Math.E, yc.getInterestRate(time) * time);
        }
        return price;
    }

    /**
     * Return YTM
     * @param bond
     * @param price
     * @return
     */
    public double getYTM(Bond bond, double price) {
        //return Math.log(bond.getFaceValue()/price) / bond.getMaturity();
        return 0.0;
    }
}
