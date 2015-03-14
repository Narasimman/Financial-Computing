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

        YieldCurve initialyc = new YieldCurve();
        /* Print the initial/Q3 yield curve object */
        System.out.println("Q1.");
        System.out.println(initialyc);

        /* A half year zero coupon bond with face value of 100$ traded at 95$ maturing in half a year */
        Bond b1 = new Bond(100, 95, 0.5);
        /* Another one with face value of 1000$ traded at 895$ maturing in a year */
        Bond b2 = new Bond(1000, 895, 1);

        List<Bond> bonds = new ArrayList<Bond>(10);
        bonds.add(b1);
        bonds.add(b2);

        /* Print the yield curve */
        YieldCurve yc = new YieldCurve(bonds);
        System.out.println();
        System.out.println("Q2. a.");
        System.out.println("Yield Curve:");
        System.out.println(yc);

        /* What is getInterestRate(0.75) returns? */
        System.out.println("b. The getInterestRate(0.75) returns: " + String.format("%.3f", yc.getInterestRate(0.75)));

        /* A 5% coupon bond with 500$ face value, which pays semi-annually and duration of three years */
        Bond b3 = new Bond(500, 0.05, 3, 2);

        YieldCurve initialYieldCurve = new YieldCurve();
        /* Print the price */
        double priceOfBond = test.getPrice(initialYieldCurve, b3);
        System.out.println();
        System.out.println("Q3. a. The price of the bond is : " + String.format("%.3f", priceOfBond));

        double ytm = test.getYTM(b3, priceOfBond);
        System.out.println("b. YTM for the bond is : " + String.format("%.3f", ytm));

    }

    /**
     * Returns the value of a coupon bearing bond
     * Given yield curve and bond
     * @param yc
     * @param bond
     * @return
     */
    public double getPrice(YieldCurve yc, Bond bond) {
        double price = 0.0;
        Map<Double, Double> cashFlow = bond.getCashFlow();

        for(Map.Entry<Double, Double> entry: cashFlow.entrySet()){
            double coupon = entry.getValue();
            double year = entry.getKey();

            price += coupon / Math.exp((yc.getInterestRate(year) * year) / 100);
        }
        return price;
    }

    /**
     * Return YTM
     * We calculate the ytm by binary search
     * @param bond
     * @param price
     * @return
     */
    public double getYTM(Bond bond, double price) {
        double currentPrice = 0.0;
        double lbYTM = 0.0, hbYTM = 1.0;

        do {
            currentPrice = getPriceForYTM(bond, hbYTM);

            /* Binary search the value of final ytm */
            if(currentPrice > price) {
                lbYTM = hbYTM;
                hbYTM *= 2;
            } else if (currentPrice < price) {
                hbYTM = lbYTM + (hbYTM - lbYTM) /2;
            } else if (lbYTM == hbYTM) {
                break;
            }

        } while ((hbYTM - lbYTM) > 0.001);

        return hbYTM;
    }

    private double getPriceForYTM(Bond bond, double ytm) {
        double price = 0.0;
        Map<Double, Double> cashFlow = bond.getCashFlow();
        for (Map.Entry<Double, Double> entry : cashFlow.entrySet()) {
            double coupon = entry.getValue();
            double year = entry.getKey();
            price += coupon / (Math.exp((ytm * year) / 100));
        }
        return price;
    }

    /**
     * Return price given ytm and bond
     * @param bond
     * @param ytm
     * @return
     */
    public double getPrice(Bond bond, double ytm) {
        double price = 0.0;
        price = bond.getFaceValue() * Math.exp(-(ytm * bond.getMaturity()));
        return price;
    }
}
