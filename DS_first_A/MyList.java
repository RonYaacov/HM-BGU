public class MyList{
    private int size;
    private Node mediaNode;
    private Node head;
    private Node tail;

    public MyList(){
        this.size = 0;
        mediaNode = null;
        head = null;
        tail = null;
    }
    public MyList(Node head, Node tail){
        this.size = 0;
        mediaNode = null;
        this.head = head;
        this.tail = tail;
    } 
    public void add(Node n){
        add(n, size);

    }
    public void add(Node n, int i){
        size++;
        if(size%2 ==0){
            if(mediaNode.getNext()!= null)
                this.mediaNode = mediaNode.getNext();
        }
        if(head == null && i == 0){
            head = n;
            tail = n;
            mediaNode = n;
            return;
        }
        if(i == 0){
            n.setNext(head);
            head.setPrev(n);
            return;
        }
        if(i == size-1){
            tail.setNext(n);
            n.setPrev(tail);
            tail = n;
            return;
        }   
    }
    public void removeNode(){
        size--;
    }
    public Node getFirst(){
        return head;
    }
    public Node getLast(){
        return tail;
    }
    public int getSize(){
        return size;
    }

    public void removeLast(){
        size--;
        if(tail != null && tail.getPrev() != null){
            Node newPrev = tail.getPrev();
            newPrev.setNext(null);
            this.tail = newPrev;
            return;
        }
        if(tail != null){
            tail = null;
            return;
        }
    }
    public void removeFirst(){
        size--;
        if(head != null && head.getNext() != null){
            Node newHead = head.getNext();
            newHead.setPrev(null);
            this.head = newHead;
            return;
        }
        if(head != null){
            head = null;
            return;
        }
    }
    public Node getMedian(){
        return mediaNode;
    }
}
