import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> { 
    class Node{
        private Pair<K,V> data;
        private Node next;

        public Node(Pair<K,V> data, Node next){
            this.next = next;
            this.data = data;
        }
        public Pair<K, V> getData() {
            return data;
        }
        public Node getNext() {
            return next;
        }
        public void setNext(Node next) {
            this.next = next;
        }
    }
    
    
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private LinkedList<Node>[] table;
    private int size;
    private int k;


    /*
     * You should add additional private members as needed.
     */
    private boolean isAtCapacity(){
        return size/(double)capacity >= maxLoadFactor;

    }

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.k = k;
        this.capacity = 1 << k;
        this.size = 0;
        this.table = new LinkedList[this.capacity];
        this.hashFunc = hashFactory.pickHash(k);
    }

    public V search(K key) {
        int index = this.hashFunc.hash(key);
        LinkedList<Node> list = this.table[index];
        V result  = null;
        if(list == null || list.peek() == null){
            return result;
        }
        for(Node node: list){
            Pair<K,V> pair = node.getData();
            if(pair.first() == key){
                result = pair.second();
                return result;
            }
        }
        return result;
    }
    private void reHash(){
        this.capacity = capacity*2;
        this.k++;
        this.hashFunc = this.hashFactory.pickHash(this.k);
        LinkedList<Node>[] newTable = new LinkedList[this.capacity];
        for(LinkedList<Node> list : this.table){
            if(list == null){
                continue;
            }
            Node first = list.peek();
            if(first == null){
                continue;
            }
            int index = this.hashFunc.hash(first.getData().first());
            newTable[index] = list;
        }
        this.table = newTable;
    }

    public void insert(K key, V value) {
        size++;
        if(isAtCapacity()){
            reHash();
        }
        int index  = this.hashFunc.hash(key);
        if(this.table[index] == null){
            table[index] = new LinkedList<Node>();
        }
        this.table[index].addFirst(new Node(new Pair<K,V>(key, value), table[index].peek()));
    }

    public boolean delete(K key) {
        int index = this.hashFunc.hash(key);
        LinkedList<Node> list = this.table[index];
        
        if(list.getFirst().getData().first() == key){
            list.removeFirst();
            return true;
        }
        for(Node node: list){
            if(node.getNext() == null)
                break;
            Pair<K,V> pair = node.getNext().getData();
            if(pair.first() == key){
                node.setNext(node.getNext().getNext());
                return true;
            }
        }
        return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
}
