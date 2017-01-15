package huffman;

public class ConstructionNode implements Node {
	private Node child1;
	private Node child2;
	private int value;
	private Byte data;
	

	public ConstructionNode(Node child1, Node child2){
		this(child1,child2,child1.getValue() + child2.getValue(),null);
	}
	public ConstructionNode(Byte data, int value){
		this(null,null,value,data);
	}
	
	private ConstructionNode(Node child1, Node child2, int value, Byte data){
		this.child1 = child1;
		this.child2 = child2;
		this.value = value;
		this.data = data;
	}

	@Override
	public byte getData() {
		return data;
	}
	
	@Override
	public Node getLeft(){
		return child1;
	}
	@Override
	public Node getRight(){
		return child2;
	}
	@Override
	public boolean isLeaf(){
		return child1==child2 && child1==null;
	}

	@Override
	public int compareTo(Node n){
		if (n==null || (n instanceof ReadNode)) return -1;
		return value - n.getValue();
	}
	
	@Override
	public String toString() {
		if (isLeaf()){
			return String.format("{L|0x%02X|%d}",data,value);
		}else{
			return "{N|"+value+"}";
		}
	}
	@Override
	public int getValue() {
		return value;
	}
}
