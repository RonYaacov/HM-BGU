import java.util.Random;


public class StringHash implements HashFactory<String> {
    private int q;
    private int c;
    private HashingUtils utils;


    public StringHash() {
        utils = new HashingUtils();
        assignRandFields();    
    }
    private void assignRandFields(){
        Random rand  = new Random();
        boolean isPrime = false;
        while(!isPrime){
            q = (int)utils.genLong(Integer.MAX_VALUE/2+1, Integer.MAX_VALUE+1);
            isPrime = utils.runMillerRabinTest(q, 50);
        }
        c = rand.nextInt(q-2)+2;
    }

    @Override
    public HashFunctor<String> pickHash(int k) {
        HashFunctor<Integer> carterWegman = new ModularHash().pickHash(k);
        Functor result = new Functor(q,c, carterWegman);// in the descrption k is always valid 
        assignRandFields();
        return result;

    }

    public class Functor implements HashFunctor<String> {
        final private HashFunctor<Integer> carterWegmanHash;
        final private int c;
        final private int q;
        
        public Functor(int q, int c, HashFunctor<Integer> carterWegman ){
            this.q = q;
            this.c = c;
            this.carterWegmanHash = carterWegman;

        } 

        @Override
        public int hash(String key) {
            int sum = 0;
            char[] arr = key.toCharArray();
            for(int i=1; i<=arr.length;i++){
                int cPow = HashingUtils.mod((int)Math.pow(c, arr.length-i),q); 
                long innerMod = HashingUtils.mod(arr[i-1]*cPow,q);
                sum += HashingUtils.mod(innerMod, q);
            }
            return carterWegmanHash.hash(sum);
        }

        public int c() {
            return c;
        }

        public int q() {
            return q;
        }

        public HashFunctor carterWegmanHash() {
            return carterWegmanHash;
        }
    }
}
