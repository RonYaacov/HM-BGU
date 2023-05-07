
public class Node {
    private int data;
    private Container container;
    private Node next;
    private Node prev;
    
    public Node(int data, Container container, Node next, Node prev){
        this.container = container;
        this.data = data;
        this.next = next;
        this.prev = prev;
        int y =container.getData().getY(); 
    }    
    public void setContainer(Container c){
        this.container = c; 
    }
    public Container getContainer(){
        return this.container; 
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
