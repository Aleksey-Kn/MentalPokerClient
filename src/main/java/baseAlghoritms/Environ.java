package baseAlghoritms;

import java.util.Random;

public class Environ {
    private static final Environ instance = new Environ();
    private int q, p, g;

    private Environ(){
        Random random = new Random();
        do{
            q = PrimeNumber.getInstance().randomPrime(2, 1_000_000_000);
            p = 2 * q + 1;
        } while (PrimeNumber.getInstance().isNotPrime(p) || p < 512);
        do{
            g = Math.abs(random.nextInt()) % (p - 2) + 1;
        } while (SpecialMath.powOnModule(g, q, p) == 1);
    }

    public int getQ() {
        return q;
    }

    public int getP() {
        return p;
    }

    public int getG() {
        return g;
    }

    public static Environ getInstance() {
        return instance;
    }
}
