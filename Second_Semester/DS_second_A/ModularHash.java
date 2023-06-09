import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    
    private int a;
    private int b;
    private long p;
    private HashingUtils utils;

    public ModularHash() {
        utils = new HashingUtils();
    }

    private void assignRandFields(){
        Random rand = new Random();
        a = rand.nextInt(Integer.MAX_VALUE);
        b = a;
        while (b == a || b == 0) {
            b = rand.nextInt(Integer.MAX_VALUE);
        }
        boolean isPrime = false;
        while(!isPrime){
            p = utils.genLong(Integer.MAX_VALUE, Long.MAX_VALUE);
            if(HashingUtils.mod(p, 2) == 0){
                continue;
            }
            isPrime = utils.runMillerRabinTest(p, 10);          
        }
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {

        assignRandFields();
        Functor result = new Functor(a, b, p,(int)HashingUtils.mod((int)Math.pow(2, k), Integer.MAX_VALUE));// in the descrption k is always valid 
        return result;
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor(int a, int b, long p, int m){
            this.a = a;
            this.b = b;
            this.p = p;
            this.m = m;
        }

        @Override
        public int hash(Integer key) {
            return (int)HashingUtils.mod(HashingUtils.mod((a*key) + b, p),m);// m is int so the result of utils.mod(something,m) can always be cast to int 
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
