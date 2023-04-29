import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDensity() {
		int xMin = xList.getFirst().getData();
		int yMin = yList.getFirst().getData();
		int xMax = xList.getLast().getData();
		int yMax = xList.getLast().getData();
		return pointsCounter/((xMax-xMin)*(yMax-yMin));

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
		current = list.getLast();
		while(current.getData()> max && current != null){
			clearMapForNerrowRange(current, axis);
			current = current.getPrev();
			list.removeLast();	
		}
		//from -inf to min
		current = list.getFirst();
		while(current.getData()< min && current != null){
			clearMapForNerrowRange(current, axis);
			current = current.getNext();
			list.removeFirst();
		}

			
	}
	
		
	
	private void clearMapForNerrowRange(Node current, Boolean axis){
		HashMap<Integer, Container> containers = current.getContainers(); 
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container getMedian(Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width, Boolean axis) { // i changed the signachure of this method do to an error (int vs double) need to check in the forum if its ok !!!!!!!!!!
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point[] nearestPair() {
		// TODO Auto-generated method stub
		return null;
	}

	
	//TODO: add members, methods, etc.
	
}

