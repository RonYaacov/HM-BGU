import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    private long a;
    private HashingUtils utils;

    public MultiplicativeShiftingHash() {
        utils = new HashingUtils();
        assignRandFields();
    }

    private void assignRandFields(){
        Long[] options = utils.genUniqueLong(Integer.MAX_VALUE);
        Random rand = new Random();
        a = 1;
        while(a<=1){
            a = options[rand.nextInt(options.length)];
        }

    
    }
    @Override
    public HashFunctor<Long> pickHash(int k) {
        Functor result = new Functor(a, k);// in the descrption k is always valid 
        assignRandFields();
        return result;
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(long a, long k){
            this.a = a;
            this.k = k;
        }

        @Override
        public int hash(Long key) {
            return (int)(a*key)>>>(64-k); //as defined in the instractions key is valid 
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
