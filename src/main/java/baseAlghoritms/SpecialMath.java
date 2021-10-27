package baseAlghoritms;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SpecialMath {
    public static double log2(int v) {
        return Math.log(v) / Math.log(2);
    }

    public static int powOnModule(int a, int x, int p) {
        if(x == 0)
            return 1;
        int lim = (int)log2(x) + 1;
        long[] intermediate = new long[lim];
        BigInteger result = BigInteger.ONE;

        intermediate[0] = a % p;
        for(int i = 1; i < lim; i++){
            intermediate[i] = BigInteger.valueOf(intermediate[i - 1]).pow(2).mod(BigInteger.valueOf(p)).longValue();
        }

        for (int i = 0; x != 0; i++, x = x >> 1){
            if((x & 1) == 1)
                result = result.multiply(BigInteger.valueOf(intermediate[i]));
        }
        return result.mod(BigInteger.valueOf(p)).intValue();
    }

    public static int[] nod(int a, int b){
        if(b > a){
            int t = a;
            a = b;
            b = t;
        }
        int q;

        int[] u = new int[3];
        int[] v = new int[3];
        int[] t = new int[3];

        u[0] = a;
        u[1] = 1;
        v[0] = b;
        v[2] = 1;

        do {
            q = u[0] / v[0];

            t[0] = u[0] % v[0];
            t[1] = u[1] - q * v[1];
            t[2] = u[2] - q * v[2];

            u = v.clone();
            v = t.clone();
        } while (t[0] != 0);

        return u;
    }

    public static int diffihellman(Coder z){
        Random random = new Random();
        int x = Math.abs(random.nextInt()) % (Environ.getInstance().getP() - 2) + 1;
        while (powOnModule(x, Environ.getInstance().getQ(), Environ.getInstance().getP()) == 1)
            x = Math.abs(random.nextInt()) % (Environ.getInstance().getP() - 2) + 1;

        int y = powOnModule(Environ.getInstance().getG(), x, Environ.getInstance().getP());
        z.setX(x);
        return y;
    }

    public static int steps(int a, int p, int y){
        int m = (int)Math.ceil(Math.sqrt(p));
        Map<Integer, Integer> map = new HashMap<>(m);
        for(int j = 0; j < m; j++){
            map.put(BigInteger.valueOf(powOnModule(a, j, p))
                    .multiply(BigInteger.valueOf(y % p))
                    .mod(BigInteger.valueOf(p)).intValue(), j);
        }
        for(int i = 1, t; i <= m; i++){
            t = powOnModule(a, i * m, p);
            if(map.containsKey(t))
                return i * m - map.get(t);
        }
        return -1;
    }
}
