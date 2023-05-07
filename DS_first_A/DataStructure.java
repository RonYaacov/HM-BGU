import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class DataStructure implements DT {
	
	private MyList xList;
	private MyList yList;
	private int pointsCounter;

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure()
	{
		this.xList = new MyList();
		this.yList = new MyList();
		this.pointsCounter = 0;
	}

	@Override
	public void addPoint(Point point) {
		Container c = new Container(point, null, null);
		Node xNode = new Node(point.getX(), c, null, null);
		Node yNode = new Node(point.getY(), c, null, null);
		xNode = addNodeToList(xList, xNode);
		yNode = addNodeToList(yList, yNode);
		xNode.setContainer(c);
		yNode.setContainer(c);
		c.setXNode(xNode);
		c.setYNode(yNode);
		pointsCounter++;
	}
	

	private Node addNodeToList(MyList list, Node n){
		if (list.getSize() == 0){
			list.add(n);
			return n;
		}
		Node current = list.getFirst();
		int i =0;
		while((n.getData() > current.getData()) && (current.getNext() != null)){
			current = current.getNext();
			i++;
		}
		if(n.getData() <= current.getData()){
			if(current.getPrev() == null){
				current.setPrev(n);
				n.setNext(current);
				list.add(n, 0);
				return n;
			}
			Node prev = current.getPrev();
			prev.setNext(n);
			current.setPrev(n);
			n.setNext(current);
			n.setPrev(prev);
			list.add(n, i);
			return n;
		}
		current.setNext(n);
		n.setPrev(current);
		list.add(n);
		return n;
	}

	@Override
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		List<Container> arrList = new ArrayList<Container>();
		MyList list;
		if(axis)list = xList;
		else list = yList;
		if(list.getSize() == 0){
			return new Point[0];
		}
		Node current = list.getFirst();
		while(current != null  && current.getData() < min )
			current = current.getNext();
		if(current == null){
			return new Point[0];
		}
		while(current != null && current.getData() >= min && current.getData() <= max){
			arrList.add(current.getContainer());
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
		MyList list;
		if(!axis)list = xList;
		else list = yList;
		if(list.getSize() == 0){
			return new Point[0];
		}
		Node current = list.getFirst();
		while(current != null){
			arrList.add(current.getContainer());//constent time for HashMap.values()
			current = current.getNext();
		}
		List<Point> fillter = new ArrayList<Point>();
		if(axis){
			for(Container c : arrList){
				if(c.getXNode().getData() <= max && c.getXNode().getData() >= min){
					fillter.add(c.getData());
				}
			}
		}
		else{
			for(Container c : arrList){
				if(c.getYNode().getData() <= max && c.getYNode().getData() >= min){
					fillter.add(c.getData());
				}
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
		return pointsCounter/(double)((sizes.getKey())*(sizes.getValue()));
	}

	@Override
	public void narrowRange(int min, int max, Boolean axis) {
		Node current;
		MyList list;
		list = axis? xList: yList;
		//from max to -inf
		if(list.getSize() == 0){
			return;
		}
		current = list.getFirst();
		while(current.getData()> max && current != null){
			clearForNerrowRange(current, axis);
			current = current.getPrev();
			list.removeLast();	
		}
		//from -inf to min
		if(list.getSize() == 0){
			return;
		}
		current = list.getFirst();
		while(current.getData()< min && current != null){
			clearForNerrowRange(current, axis);
			current = current.getNext();
			list.removeFirst();
		}	
	}
	
	private void clearForNerrowRange(Node current, Boolean axis){
		pointsCounter --;
		Container c = current.getContainer();
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
			
	

	@Override
	public Boolean getLargestAxis() {
		SimpleEntry<Integer, Integer> sizes = getAxisSizes();
		return (sizes.getKey())>(sizes.getValue());
	}

	@Override
	public Container getMedian(Boolean axis) {
		MyList list;
		if(axis)list = xList;
		else list=yList;
		return list.getMedian().getContainer();
	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width, Boolean axis) {
		double min;
		double max;
		double currentDis;
		double minDis = Double.MAX_VALUE;
		Point firstPoint = null;
		Point secondPoint = null;
		Point[] result;
		if(axis){
			min = container.getData().getX()- width/2;
			max = container.getData().getX() + width/2;
		}
		else{
			min = container.getData().getY()- width/2;
			max = container.getData().getY() + width/2;

		}
		Point[] inStrip = getPointsInRangeRegAxis((int)min, (int)max+1, axis);
		for(int i = 0; i<inStrip.length; i++){
			for(int x = i+1; x<Math.min(i+7, inStrip.length); x++){
				currentDis = this.getDis(inStrip[i], inStrip[x]);
				if(currentDis < minDis){
					firstPoint = inStrip[i];
					secondPoint = inStrip[x];
					minDis = currentDis;
				}
			}
		}
		if(firstPoint != null && secondPoint != null){
			result = new Point[2];
			result[0] = firstPoint;
			result[1] = secondPoint;
		}
		else{
			result = new Point[0];
		}
		return result;
		
	}
	private Point[] nearestPair(int min, int max, boolean axis){
		Point[] allPoints = getPointsInRangeRegAxis(min, max, axis);
		Point[] result;
		if(allPoints.length < 2){
			return new Point[0];
		}
		if(allPoints.length == 2){
			result = new Point[2];
			int count = 0;
			MyList list = axis? xList: yList;
			Node node = list.getFirst();
			while(node != null){
				Container c = node.getContainer();
				result[count] = c.getData();
				count++;
				if(count == 2){
					return result;
				}
			}
		}
		Point median = allPoints[allPoints.length/2];
		Container cMedian = new Container(median);
		int medianValue = axis? median.getX(): median.getY();  
		Point[] leftPoints = nearestPair(min, medianValue, axis);
		Point[] rightPoints = nearestPair(medianValue, max, axis);
		if(leftPoints.length == 0){
			return rightPoints;
		}
		if(rightPoints.length == 0){
			return leftPoints;
		}
		double leftDis = getDis(leftPoints[0], leftPoints[1]);
		double rightDis = getDis(rightPoints[0], rightPoints[1]);
		Point[] closestPoints = Math.min(leftDis,rightDis ) == leftDis? leftPoints: rightPoints;
		Point[] closestPointsInStrip = nearestPairInStrip(cMedian, getDis(closestPoints[0], closestPoints[1])*2, axis);
		double preDis = getDis(closestPoints[0],closestPoints[1]);
		double newDis = getDis(closestPointsInStrip[0], closestPointsInStrip[1]);
		return Math.min(preDis, newDis) == preDis? closestPoints: closestPointsInStrip;
	}
	

	@Override
	public Point[] nearestPair() {
		boolean largestAxix = getLargestAxis();
		MyList list =  largestAxix? xList: yList;
		return nearestPair(list.getFirst().getData(), list.getLast().getData(), largestAxix);
	}
	private SimpleEntry<Integer,Integer> getAxisSizes(){
		if(xList.getSize() == 0 || yList.getSize() == 0){
			return new SimpleEntry<Integer,Integer>((0),(0));
		}
		int xMin = xList.getFirst().getData();
		int yMin = yList.getFirst().getData();
		int xMax = xList.getLast().getData();
		int yMax = yList.getLast().getData();
		return new SimpleEntry<Integer,Integer>((xMax-xMin),(yMax-yMin));

	}
	private double getDis(Point point1, Point point2){
		double xDis = Math.pow(point1.getX()-point2.getX(),2);
		double yDis = Math.pow(point1.getY()-point2.getY(), 2);
		return Math.sqrt(xDis+yDis);

	}
	
	
}

