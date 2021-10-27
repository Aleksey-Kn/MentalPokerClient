package baseAlghoritms;

import java.util.Random;

public class PrimeNumber {
    private final static PrimeNumber instance = new PrimeNumber();
    private final Random random;

    private PrimeNumber(){
        random = new Random();
    }

    public boolean isNotPrime(int v){
        return (v < 2 || v > 2 && SpecialMath.powOnModule(2, v - 1, v) != 1);
    }

    private int positiveRandom(){
        return Math.abs(random.nextInt());
    }

    public int randomPrime(int min, int max){
        int delta = max - min;
        int now = positiveRandom() % delta + min;
        while (isNotPrime(now)){
            now = positiveRandom() % delta + min;
        }
        return now;
    }

    public int randomPrime(){
        int now = positiveRandom();
        while (isNotPrime(now)){
            now = positiveRandom();
        }
        return now;
    }

    public static PrimeNumber getInstance() {
        return instance;
    }
}
