import java.security.DrbgParameters.Capability;
import java.util.Collections; // can be useful

public class HashingExperimentUtils {
    final private static int k = 16;
    public static Pair<Double, Double> measureOperationsChained(double maxLoadFactor) {
        HashingUtils utils = new HashingUtils();
        HashFactory<Integer> hashFactory = new ModularHash();
        ChainedHashTable<Integer,Integer> cTable = new ChainedHashTable<Integer,Integer>(hashFactory, k, maxLoadFactor);
        int capacity = 1 << 16 ;
        double itemsToInsert = (capacity*maxLoadFactor)-1;
        Integer[] randomIntegers = utils.genUniqueIntegers(capacity*2);
        long start = System.nanoTime();
        for(int i = 0; i<itemsToInsert; i++){
            cTable.insert(randomIntegers[i],randomIntegers[i]);
        } 
        long end = System.nanoTime();
        double insertTime = (end-start)/itemsToInsert;
        start = System.nanoTime();
        for(int i=0; i< itemsToInsert/2;i++){
            cTable.search(randomIntegers[i]);
        }
        for(int i=0; i< itemsToInsert/2;i++){
            cTable.search(randomIntegers[i+(int)(itemsToInsert/2)]);
        }
        end = System.nanoTime();
        return new Pair<Double,Double>(insertTime,(end-start/itemsToInsert));
    }
    public static Pair<Double, Double> measureOperationsProbing(double maxLoadFactor) {
        HashingUtils utils = new HashingUtils();
        HashFactory<Integer> hashFactory = new ModularHash();
        ProbingHashTable<Integer,Integer> pTable = new ProbingHashTable<Integer, Integer>(hashFactory, k, maxLoadFactor);
        int capacity = 1 << 16 ;
        double itemsToInsert = (capacity*maxLoadFactor);
        Integer[] randomIntegers = utils.genUniqueIntegers(capacity*2);
        long start = System.nanoTime();
        for(int i = 0; i<itemsToInsert; i++){
            pTable.insert(randomIntegers[i],randomIntegers[i]);
        } 
        long end = System.nanoTime();
        double insertTime = (end-start)/itemsToInsert;
        start = System.nanoTime();
        for(int i=0; i< itemsToInsert/2;i++){
            pTable.search(randomIntegers[i]);
        }
        for(int i=0; i< itemsToInsert/2;i++){
            pTable.search(randomIntegers[i+(int)(itemsToInsert/2)]);
        }
        end = System.nanoTime();
        return new Pair<Double,Double>(insertTime,(end-start/itemsToInsert));
    }    

    public static Pair<Double, Double> measureLongOperations() {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public static Pair<Double, Double> measureStringOperations() {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public static void main(String[] args) {

        double[] loadFactors = new double[]{0.5, 0.75, 0.878, 0.9375};
        for(double loadFactor: loadFactors){
            long insertTime = 0;
            long searchTime = 0;
            for(int i=0; i<30;i++){
                Pair<Double,Double> pair = measureOperationsProbing(loadFactor);
                insertTime += pair.first();
                searchTime += pair.second();
            }
            double avgInsert = insertTime/(double)30;
            double avgSearch = searchTime/(double)30;
            System.out.println("the avg time for probing with load factor of " + loadFactor +" is "+ "Inserting -- "+avgInsert+ " Searching " + avgSearch);
        }
        double[] loadFactors2 = new double[]{0.5, 0.75, 1, 1.5, 2};
        for(double loadFactor: loadFactors2){
            long insertTime = 0;
            long searchTime = 0;
            for(int i=0; i<30;i++){
                Pair<Double,Double> pair = measureOperationsChained(loadFactor);
                insertTime += pair.first();
                searchTime += pair.second();
            }
            double avgInsert = insertTime/(double)30;
            double avgSearch = searchTime/(double)30;
            System.out.println("the avg time for chaining with load factor of " + loadFactor +" is "+ "Inserting -- "+avgInsert+ " Searching " + avgSearch);
        }
        
        
    }
}
