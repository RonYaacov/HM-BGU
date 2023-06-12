import java.util.Random;



public class IndexableSkipList extends AbstractSkipList {
    final protected double probability;
    public IndexableSkipList(double probability) {
        super();
        this.probability = probability;
    }

    @Override
    public Node find(int val) {
        Node curr = head;
        for (int level = head.height(); level >=0; --level){
            while ((curr.getNext(level) != tail) && (curr.getNext(level).key() <= val)){
                curr = curr.getNext(level);
            }
        }
        return curr;
    }

    public int generateHeight() {
        boolean p = true;
        int hight = 0;

        while (p){
            hight++;
            p = Math.random()<probability;
        }
        return hight;
    }

    public int rank(int val) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }

    public int select(int index) {
        throw new UnsupportedOperationException("Replace this by your implementation");
    }
}
