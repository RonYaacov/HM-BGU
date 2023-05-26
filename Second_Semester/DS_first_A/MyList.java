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
        size++;
        if(size % 2 == 0){
            if(mediaNode.getNext()!= null)
                this.mediaNode = mediaNode.getNext();
        }
    }
    public void addLast(Node n){
        size++;
        
        if(head == null && tail == null){
            head = n;
            tail = n;
            mediaNode = n;
            return;
        }
        Node old = tail;
        old.setNext(n);
        n.setPrev(old);
        tail = n;
        if(size%2 ==0){
            if(mediaNode.getNext()!= null)
                this.mediaNode = mediaNode.getNext();
        }
    }
    public void addFirst(Node n){
        size++;
        if(head == null && tail == null){
            head = n;
            tail = n;
            mediaNode = n;
            return;
        }
        Node old = head;
        old.setPrev(n);
        n.setNext(old);
        head = n;
        if(size % 2 != 0){
            if(mediaNode.getPrev()!= null)
                this.mediaNode = mediaNode.getPrev();
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
        if(size % 2 != 0){
            if(mediaNode.getPrev()!= null)
                this.mediaNode = mediaNode.getPrev();
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
        if(size % 2 == 0){
            if(mediaNode.getNext()!= null)
                this.mediaNode = mediaNode.getNext();
        }
    }
    public Node getMedian(){
        return mediaNode;
    }
}
