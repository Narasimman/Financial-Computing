package edu.nyu.cims.compfin15.montecarlo;

import edu.nyu.cims.compfin15.montecarlo.RandomVectorGenerator;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

/**
 * Created by Narasimman on 4/16/2015.
 */
public class MonteCarlo {

    public static void main(String[] args) {

        RandomVectorGenerator rvg;
        AntitheticRandomVectorGenerator arvg;
        BrownianStockPath stockPath;
        double mean;

        /**
         * Problem 1
         */

        /* Input option */
        Option option1 = new Option("IBM", 152.35, 0.01, 0.0001, 252, 165, "Normal");

        /* Generate Random number */
        rvg = new RandomVectorGenerator(option1.getNumOfDays());
        arvg = new AntitheticRandomVectorGenerator(rvg);

        /*  Generate GBM */
        stockPath = new BrownianStockPath(option1, arvg);

        /* Calculate payout for 252 days */
        EuropeanPayOut payOut = new EuropeanPayOut(option1);

        MonteCarlo mc = new MonteCarlo();
        mean = mc.simulate(payOut, stockPath);

        double price1 = mean * Math.exp(- option1.getRateOfInterest() * option1.getNumOfDays() );
        System.out.println("The price of the option is: " + price1 );

        /**
         * Problem 2
         */
        Option option2 = new Option("IBM", 152.35, 0.01, 0.0001, 252, 164, "Asian");

        rvg = new RandomVectorGenerator(option2.getNumOfDays());
        arvg = new AntitheticRandomVectorGenerator(rvg);
        stockPath = new BrownianStockPath(option2, arvg);
        AsianPayOut asianPayOut = new AsianPayOut(option2);

        mean = mc.simulate(asianPayOut, stockPath);

        double price2 = mean * Math.exp(- option2.getRateOfInterest() * option2.getNumOfDays() );
        System.out.println("The price of the option is: " + price2 );


    }

    public double simulate(IPayOut payOut, IPath stockPath) {
        Statistics statistics = new Statistics();
        int count = 0;
        double a = 0.0;
        while (true) {
            statistics.add(payOut.getValue(stockPath));

            if(count % 10000 == 0) {
               // System.out.println(2.0537489106318234 * statistics.getStdVar()/ Math.sqrt(count));

            }

            if (2.0537489106318234 * statistics.getStdVar()/ Math.sqrt(count) < 0.01 && count > 10000 ) {
                System.out.println(count+" times simulations!");
                break;
            }
            count++;
        }
        System.out.println("The value of option in expiration date is : " + statistics.getMean());
        return statistics.getMean();
    }
}
