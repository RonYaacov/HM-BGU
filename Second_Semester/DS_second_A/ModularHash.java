import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    
    private int a;
    private int b;
    private long p;
    private HashingUtils utils;

    public ModularHash() {
        utils = new HashingUtils();
        assignRandFields();
    }

    private void assignRandFields(){
        Integer[] intOptions = utils.genUniqueIntegers(Integer.MAX_VALUE);
        Random rand = new Random();
        a = intOptions[rand.nextInt(intOptions.length)];
        b = a;
        while (b == a || b == 0) {
            b = intOptions[rand.nextInt(intOptions.length)];
        }
        boolean isPrime = false;
        while(!isPrime){
            p = utils.genLong(Integer.MAX_VALUE, Long.MAX_VALUE);
            isPrime = utils.runMillerRabinTest(p, 30);
        }
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {

        Functor result = new Functor(a, b, p,(int)HashingUtils.mod((int)Math.pow(2, k), Integer.MAX_VALUE));// in the descrption k is always valid 
        assignRandFields();
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
