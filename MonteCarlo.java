import edu.nyu.cims.compfin15.montecarlo.RandomVectorGenerator;

/**
 * Created by Narasimman on 4/16/2015.
 */
public class MonteCarlo {

    public static void main(String[] args) {
        RandomVectorGenerator rvg = new RandomVectorGenerator();
        for(int i = 0; i < 6; i++) {
            System.out.println();
            double[] test = rvg.getUniformRandomNumber();
            for (int j = 0; j < test.length; j++) {
                System.out.println(test[j]);
            }
        }

    }


}
