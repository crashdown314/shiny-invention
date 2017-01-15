package huffman;

public class ReadNode implements Node {
	private ReadNode child1;
	private ReadNode child2;
	private int value;
	private Byte data;
	
	/**
	 * Sets the data of the node
	 * @param data
	 */
	public void setData(byte data) {
		this.data = data;	
	}

	/**
	 * Sets the left node in this nodes branch.s
	 * @param child
	 */
	public void setLeft(ReadNode child) {
		this.child1 = child;
	}

	/**
	 * Sets the right node in this nodes branch.
	 * @param child
	 */
	public void setRight(ReadNode child) {
		this.child2 = child;
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
		if (n==null) return -1;
		return value - n.getValue();
	}	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		Node n = (Node) obj;
		
		if (isLeaf() == n.isLeaf()){
			if (isLeaf()){
				return data == n.getData();
			}else{
				return child1.equals(n.getLeft()) && child2.equals(n.getRight());
			}
		}
		return false;
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
	public byte getData() {
		return data;
	}
	@Override
	public int getValue() {
		return 0;
	}
}
