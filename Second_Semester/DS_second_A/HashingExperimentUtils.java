import java.security.DrbgParameters.Capability;
import java.util.Collections; // can be useful
import java.util.List;

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
        double itemsToInsert = (capacity*maxLoadFactor) -1;
        Integer[] randomIntegers = utils.genUniqueIntegers(capacity*2);
       long start = System.nanoTime();
        for(int i = 0; i<itemsToInsert; i++){
            pTable.insert(randomIntegers[i],randomIntegers[i]);
        } 
        pTable.delete(0);
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
        HashingUtils utils = new HashingUtils();
        HashFactory<Long> hashFactory = new MultiplicativeShiftingHash();
        ChainedHashTable<Long,Long> pTable = new ChainedHashTable<Long, Long>(hashFactory, k, 1);
        int capacity = 1 << 16 ;
        double itemsToInsert = (capacity)-1;
        Long[] randomLongs = utils.genUniqueLong(capacity*2);
        long start = System.nanoTime();
        for(int i = 0; i<itemsToInsert; i++){
            pTable.insert(randomLongs[i],randomLongs[i]);
        } 
        long end = System.nanoTime();
        double insertTime = (end-start)/itemsToInsert;
        start = System.nanoTime();
        for(int i=0; i< itemsToInsert/2;i++){
            pTable.search(randomLongs[i]);
        }
        for(int i=0; i< itemsToInsert/2;i++){
            pTable.search(randomLongs[i+(int)(itemsToInsert/2)]);
        }
        end = System.nanoTime();
        return new Pair<Double,Double>(insertTime,(end-start/itemsToInsert));
    }

    public static Pair<Double, Double> measureStringOperations() {
        HashingUtils utils = new HashingUtils();
        HashFactory<String> hashFactory = new StringHash();
        ChainedHashTable<String,String> pTable = new ChainedHashTable<String, String>(hashFactory, k, 1);
        int capacity = 1 << 16 ;
        double itemsToInsert = (capacity);
        List<String> randomStrings = utils.genUniqueStrings(capacity*2,10, 20);
        long start = System.nanoTime();
        for(int i = 0; i<itemsToInsert; i++){
            pTable.insert(randomStrings.get(i),randomStrings.get(i));
        } 
        long end = System.nanoTime();
        double insertTime = (end-start)/itemsToInsert;
        start = System.nanoTime();
        for(int i=0; i< itemsToInsert/2;i++){
            pTable.search(randomStrings.get(i));
        }
        for(int i=0; i< itemsToInsert/2;i++){
            pTable.search(randomStrings.get(i+(int)(itemsToInsert/2)));
        }
        end = System.nanoTime();
        return new Pair<Double,Double>(insertTime,(end-start/itemsToInsert));
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
        long insertTime = 0;
        long searchTime = 0;
        for(int i=0; i<30;i++){
            Pair<Double,Double> pair = measureLongOperations();
            insertTime += pair.first();
            searchTime += pair.second();
        }
        double avgInsert = insertTime/(double)30;
        double avgSearch = searchTime/(double)30;
        System.out.println("the avg time for Dietzfelbinger with load factor of " + 1 +" is "+ "Inserting -- "+avgInsert+ " Searching " + avgSearch);
        
        insertTime = 0;
        searchTime = 0;
        for(int i=0; i<30;i++){
            Pair<Double,Double> pair = measureLongOperations();
            insertTime += pair.first();
            searchTime += pair.second();
        }
        avgInsert = insertTime/(double)30;
        avgSearch = searchTime/(double)30;
        System.out.println("the avg time for Carter-Wegman for Strings with load factor of " + 1 +" is "+ "Inserting -- "+avgInsert+ " Searching " + avgSearch);
    
        
        
        
        
    }
}
