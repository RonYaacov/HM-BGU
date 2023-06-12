import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class MyDataStructure {
    private HashTable<Integer, Integer> hashTable;
    private IndexableSkipList skipList;
    /*
     * You may add any members that you wish to add.
     * Remember that all the data-structures you use must be YOUR implementations,
     * except for the List and its implementation for the operation Range(low, high).
     */

    /***
     * This function is the Init function described in Part 4.
     *
     * @param N The maximal number of items expected in the DS.
     */
    public MyDataStructure(int N) {
        int k = N >> 1;
        HashFactory<Integer> hashFactory = new  ModularHash();
        this.hashTable = new ProbingHashTable<Integer,Integer>(hashFactory, k, 0.75);
        this.skipList = new IndexableSkipList(0.5);

    }

    /*
     * In the following functions,
     * you should REMOVE the place-holder return statements.
     */
    public boolean insert(int value) {
        try{
            hashTable.insert(value, value);
            skipList.insert(value);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean delete(int value) {
        boolean result = hashTable.delete(value);
        AbstractSkipList.Node nodeToDelete = new AbstractSkipList.Node(hashTable.search(value));
        result =  skipList.delete(nodeToDelete);
        return result;
    }

    public boolean contains(int value) {
        return hashTable.search(value) == null;
    }

    public int rank(int value) {
        return -1;
    }

    public int select(int index) {
        return Integer.MIN_VALUE;
    }

    public List<Integer> range(int low, int high) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        AbstractSkipList.Node currentNode = new AbstractSkipList.Node(hashTable.search(low));
        while(currentNode.key() <= high){
            list.addLast(currentNode.key());
            currentNode = currentNode.getNext(0);
        }
        return list;  
    }
}
