package edu.nyu.cims.compfin15.montecarlo;

import edu.nyu.cims.compfin15.montecarlo.RandomVectorGenerator;

import java.util.ArrayList;

/**
 * Created by Narasimman on 4/16/2015.
 */
public class MonteCarlo {

    public static void main(String[] args) {

        Option option1 = new Option("IBM", 152.35, 0.01, 0.0001, 252, 165, "Normal");

        RandomVectorGenerator rvg = new RandomVectorGenerator(option1.getNumOfDays());

        AntitheticRandomVectorGenerator arvg = new AntitheticRandomVectorGenerator(rvg);

        BrownianStockPath stockPath = new BrownianStockPath(option1, arvg);


        ArrayList test = stockPath.getPrices();
        for (int j = 0; j < test.size(); j++) {
            System.out.println(test.get(j));
        }
    }


}
