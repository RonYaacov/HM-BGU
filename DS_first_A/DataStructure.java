import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class DataStructure implements DT {
	
	private LinkedList<Node> xList;
	private LinkedList<Node> yList;
	private int pointsCounter;

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure()
	{
		this.xList = new LinkedList<Node>();
		this.yList = new LinkedList<Node>();
		this.pointsCounter = 0;
	}

	@Override
	public void addPoint(Point point) {
		Container c = new Container(point, null, null);
		Node xNode = new Node(point.getX(), c, null, null);
		Node yNode = new Node(point.getY(), c, null, null);
		xNode = addNodeToList(xList, xNode);
		yNode = addNodeToList(yList, yNode);
		xNode.addContainer(c);
		yNode.addContainer(c);
		c.setXNode(xNode);
		c.setYNode(yNode);
		pointsCounter++;	
	}
	

	private Node addNodeToList(LinkedList<Node> list, Node n){
		if (list.size() == 0){
			list.add(n);
			return n;
		}
		Node current = list.getFirst();
		int i =0;
		while((n.getData() > current.getData()) && (current.getNext() != null)){
			current = current.getNext();
			i++;
		}
		if(n.getData() == current.getData())
			return current;
		if(n.getData() < current.getData()){
			if(current.getPrev() == null){
				current.setPrev(n);
				n.setNext(current);
				list.addFirst(n);
				return n;
			}
			Node prev = current.getPrev();
			prev.setNext(n);
			current.setPrev(n);
			n.setNext(current);
			n.setPrev(prev);
			list.add(i, n);
			return n;
		}
		current.setNext(n);
		n.setPrev(current);
		list.addLast(n);
		return n;
	}

	@Override
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		List<Container> arrList = new ArrayList<Container>();
		LinkedList<Node> list;
		if(axis)list = xList;
		else list = yList;
		if(list.size() == 0){
			return new Point[0];
		}
		Node current = list.getFirst();
		while(current != null  && current.getData() < min )
			current = current.getNext();
		if(current == null){
			return new Point[0];
		}
		while(current != null && current.getData() >= min && current.getData() <= max){
			arrList.addAll(current.getContainersMap().values());
			current = current.getNext();
		}
		Point[] result = new Point[arrList.size()];
		for(int i=0;i<result.length; i++){
			result[i] = arrList.get(i).getData();
		}
		return result;
	}

	@Override
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		List<Container> arrList = new ArrayList<Container>();
		LinkedList<Node> list;
		if(!axis)list = xList;
		else list = yList;
		if(list.size() == 0){
			return new Point[0];
		}
		Node current = list.getFirst();
		while(current != null){
			arrList.addAll(current.getContainersMap().values());//constent time for HashMap.values()
			current = current.getNext();
		}
		List<Point> fillter = new ArrayList<Point>();
		for(Container c : arrList){
			if(c.getXNode().getData() <= max && c.getXNode().getData() >= min){
				fillter.add(c.getData());
			}
		}
		Point[] result = new Point[fillter.size()];
		for(int i=0;i<result.length; i++){
			result[i] = fillter.get(i);
		}
		
		return result;
	}

	@Override
	public double getDensity() {
		SimpleEntry<Integer, Integer> sizes = getAxisSizes();
		return pointsCounter/((sizes.getKey())*(sizes.getValue()));

	}

	@Override
	public void narrowRange(int min, int max, Boolean axis) {
		Node current;
		LinkedList<Node> list;
		if(axis){
			list = xList;
		}
		else{
			list = yList;
		}
		//from max to -inf
		if(list.size() == 0){
			return;
		}
		current = list.getLast();
		while(current.getData()> max && current != null){
			clearMapForNerrowRange(current, axis);
			current = current.getPrev();
			list.removeLast();	
		}
		//from -inf to min
		if(list.size() == 0){
			return;
		}
		current = list.getFirst();
		while(current.getData()< min && current != null){
			clearMapForNerrowRange(current, axis);
			current = current.getNext();
			list.removeFirst();
		}	
	}
	
	private void clearMapForNerrowRange(Node current, Boolean axis){
		Map<Integer, Container> containers = current.getContainersMap(); 
		pointsCounter -= containers.size();
		for (Map.Entry<Integer,Container> e : containers.entrySet()) {
			Container c = e.getValue();
			Node other;
			if(axis){
				other = c.getYNode();
			}
			else{
				other = c.getXNode();
			}
			Node otherNext = other.getNext();
			Node otherPrev = other.getPrev();
			if(otherNext != null && otherPrev != null){
				otherNext.setPrev(otherPrev);
				otherPrev.setNext(otherNext);
			}
			else if(otherNext != null){
				otherNext.setPrev(null);
			}
			else{
				otherPrev.setNext(null);
			}
		}
				
	}

	@Override
	public Boolean getLargestAxis() {
		SimpleEntry<Integer, Integer> sizes = getAxisSizes();
		return (sizes.getKey())>(sizes.getValue());
	}

	@Override
	public Container getMedian(Boolean axis) {

		LinkedList<Node> list;
		if(axis)list = xList;
		else list=yList;
		Node fast = list.getFirst(); 
		Node slow = list.getFirst();
		while(slow.getNext() != null && fast.getNext()!= null &&  fast.getNext().getNext().getNext() != null){
			slow = slow.getNext();
			fast = fast.getNext().getNext();
		}
		if(fast.getNext() != null) slow = slow.getNext();
		Collection<Map.Entry<Integer, Container>> set =  slow.getContainersMap().entrySet();
		for(Map.Entry<Integer,Container> c : set){
			return c.getValue();//gets a random container in the midian set of containers
			
		}
		return null;//if the hashmap is of size 0 it will return null but it never happens do to our implementation
	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width, Boolean axis) {
		// Point[] result = new Point[2];
		// Node node;
		// int selfPos;
		// if(axis){
		// 	node = container.getXNode();
		// 	selfPos = container.getData().getX();
		// }
		// else{
		// 	node = container.getYNode();
		// 	selfPos = container.getData().getY();
		// }	
		// double sideWidth = width/2;
		// Node next = node.getNext();
		// Node prev = node.getPrev();
		// if(next == null || prev == null)
		// 	return result;
		// if(!((next.getData() - selfPos <= sideWidth) || (selfPos - prev.getData()) < sideWidth))
		// 	return result;
		
		// Point selfPoint = container.getData();
		// int closestDis = Integer.MAX_VALUE;
		// Container currentColser = null;
		// Collection<Map.Entry<Integer, Container>> nextSet =  next.getContainersMap().entrySet();
		// for(Map.Entry<Integer, Container> e : nextSet){
		// 	Container c = e.getValue();
			
		// 	// i will return to it later... this method uses method thet need to be done before
		// }
		Point[] result = new Point[2];
		result[0] = container.getData();
		result[1] = container.getData();
		return result;
		
	}

	@Override
	public Point[] nearestPair() {
		// TODO Auto-generated method stub
		return null;
	}
	private SimpleEntry<Integer,Integer> getAxisSizes(){
		if(xList.size() == 0 || yList.size() == 0){
			return new SimpleEntry<Integer,Integer>((0),(0));
		}
		int xMin = xList.getFirst().getData();
		int yMin = yList.getFirst().getData();
		int xMax = xList.getLast().getData();
		int yMax = xList.getLast().getData();
		return new SimpleEntry<Integer,Integer>((xMax-xMin),(yMax-yMin));

	}
	private double getDis(Point point1, Point point2){
		double xDis = Math.pow(point1.getX()-point2.getX(),2);
		double yDis = Math.pow(point1.getY()-point2.getY(), 2);
		return Math.sqrt(xDis+yDis);

	}
	
	//TODO: add members, methods, etc.
	
}

