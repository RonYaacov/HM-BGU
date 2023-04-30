import java.util.HashMap;
import java.util.Map;

public class Node {
    private int data;
    private HashMap<Integer, Container> map;
    private Node next;
    private Node prev;
    
    public Node(int data, Container container, Node next, Node prev){
        this.map = new HashMap<Integer, Container>();
        this.data = data;
        this.next = next;
        this.prev = prev;
        int y =container.getData().getY(); 
        map.put(y, container);
    }    
    public void addContainer(Container c){
        int y = c.getData().getY(); 
        map.put(y,c);
    }

    public Map<Integer, Container> getContainersMap(){
        return map;
    }

    public int getData(){
        return data;
    }
    public Node getNext(){
        return next;
    }
    public Node getPrev(){
        return prev;
    }

    public void setPrev(Node prev){
        this.prev = prev;
    }
    public void setNext(Node next){
        this.next = next;
    }
}
