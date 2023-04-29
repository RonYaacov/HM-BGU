
//Don't change the class name
public class Container{
	private Point data;//Don't delete or change this field;
	private YNode yNode;

	public Container(Point p, YNode yNode){
		this.data = p;
		this.yNode = yNode;
	}
	
	//Don't delete or change this function
	public Point getData()
	{
		return data;
	}

	public YNode getYNode(){
		return yNode;
	}
}
