
public class YNode {
    private int y;
    private YNode next;
    private YNode prev;
    
    public YNode(int y, YNode next, YNode prev){
        this.y = y;
        this.next = next;
        this.prev = prev;
    }    

    public int getData(){
        return y;
    }
    public YNode getNext(){
        return next;
    }
    public YNode getPrev(){
        return prev;
    }
}
