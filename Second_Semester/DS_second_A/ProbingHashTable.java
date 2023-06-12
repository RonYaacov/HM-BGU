import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private int k;
    private int size;
    private Pair<Pair<K,V>,Boolean>[] table;
    private AbstractSkipList.Node node; //for MyDataStructure
    


    /*
     * You should add additional private members as needed.
     */

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.k = k;
        this.table = new Pair[capacity];
        this.size = 0;
        this.node = null;
    }
    public void setNode(AbstractSkipList.Node node){
        this.node = node;
    }
    public AbstractSkipList.Node getNode(){
        return node;
    }
    private boolean isAtCapacity(){
        return size/(double)capacity >= maxLoadFactor;

    }
    private boolean isOccupied(int i){ 
        return !(table[i] == null || !table[i].second());
    }

    private void reHash(){
        capacity = capacity*2;
        k++;
        hashFunc = hashFactory.pickHash(k);
        Pair<Pair<K,V>,Boolean>[] newTable = new Pair[capacity];
        Pair<Pair<K,V>,Boolean>[] oldTable = table;
        this.table = newTable;
        for(Pair<Pair<K,V>,Boolean> pPair: oldTable){
            if(pPair == null){
                continue;
            }
            if(pPair.second()){
                Pair<K,V> pair = pPair.first();
                insert(pair.first(), pair.second());
            }
        }
    }

    public V search(K key) {
        int index = hashFunc.hash(key);
        int startVal = index;
        while(isOccupied(index)){
            if(table[index].second()){
                if(table[index].first().first() == key)
                    return table[index].first().second();
            }
            index++;
            index = index%capacity;
            if(index == startVal)
                break;
        }
        return null;
    }

    public void insert(K key, V value) {
        size++;
        if(isAtCapacity()){
            reHash();
        }
        int index = hashFunc.hash(key);
        while(isOccupied(index)){
            index++;
            index = HashingUtils.mod(index, this.capacity);
        }
        Pair<K,V> pair =  new Pair<K,V>(key, value);
        table[index] = new Pair<Pair<K,V>,Boolean>(pair,true);
    }

    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        int startVal = index;
        while(isOccupied(index)){
            if(table[index].second()){
                if(table[index].first().first() == key){
                    Pair<K,V> pair = table[index].first();
                    table[index] = new Pair<Pair<K,V>,Boolean>(pair, false);
                    size--;
                    return true;
                }
                    
            }
            index++;
            index = index%capacity;
            if(index == startVal)
                break;
        }
        return false;
        
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
}
