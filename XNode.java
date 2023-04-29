import java.util.HashMap;

public class XNode {
    private int x;
    private HashMap<Integer, Container> map;
    private XNode next;
    private XNode prev;
    
    public XNode(int x, Container container, XNode next, XNode prev){
        this.map = new HashMap<Integer, Container>();
        this.x = x;
        this.next = next;
        this.prev = prev;
        int y =container.getData().getY(); 
        map.put(y, container);
    }    
    public void addContainer(Container c){
        int y = c.getData().getY(); 
        map.put(y,c);
    }

    public int getData(){
        return x;
    }
    public XNode getNext(){
        return next;
    }
    public XNode getPrev(){
        return prev;
    }
}
