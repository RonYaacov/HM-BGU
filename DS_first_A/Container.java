
//Don't change the class name
public class Container{
	private Point data;//Don't delete or change this field;
	private Node xNode;
	private Node yNode;

	public Container(Point p, Node xNode, Node yNode){
		this.data = p;
		this.xNode = xNode;
		this.yNode = yNode;
	}
	
	//Don't delete or change this function
	public Point getData()
	{
		return data;
	}

	public Node getXNode(){
		return xNode;
	}
	public void setXNode(Node xNode){
		this.xNode = xNode;
	}
	public void setYNode(Node yNode){
		this.yNode = yNode;
	}
	public Node getYNode(){
		return yNode;
	}
}
