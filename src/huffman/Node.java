package huffman;

/**
 * An interface to use when creating the nodes for a huffman-tree
 * @author fnils
 *
 */
public interface Node extends Comparable<Node>{
	/**
	 * @return The node in the left branch.
	 */
	Node getLeft();
	/**
	 * @return The node in the right branch.
	 */
	Node getRight();
	/**
	 * @return The data stored in the node
	 */
	byte getData();
	/**
	 * @return the value of the node
	 */
	int getValue();
	/**
	 * @return <b>true</b> if the node is a leaf.
	 */
	boolean isLeaf();
}
