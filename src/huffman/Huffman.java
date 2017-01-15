package huffman;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Huffman {
	private static final boolean DEBUG = false;
	private static final String USAGE = "Huffman encode|decode file output";

	public static void main(String[] args) throws IOException{
		if (args.length<3){
			System.out.println(USAGE);
			System.exit(1);
		}

		String cmd = args[0];
		File f = new File(args[1]);
		byte[] data = null;
		if (f.exists()){
			data = Files.readAllBytes(f.toPath());
		}else{
			System.out.println(USAGE);
			System.exit(1);
		}
		
		byte[] output;
		if ("encode".equals(cmd)){
			Huffman hEnc = new Huffman(data);
			output = hEnc.encode();
		}else if ("decode".equals(cmd)){	
			Huffman hDec = new Huffman(data);
			output = hDec.decode();
		}else{
			output = null; //For compiler
			System.out.println(USAGE);
			System.exit(1);
		}
		
		/*
		System.out.println("       DATA | OUT ");
		int N = data.length;
		int M = output.length;
		for (int i = 0;i<Math.max(N, M);i++){
			byte d = i>=N?0:data[i];
			byte o = i>=M?0:output[i];
			if (d!=o)
				System.out.printf("%6d 0x%02X | 0x%02X\n",i,d,o);
		}
		if (data.length != output.length) System.out.println("Unequal length: "+data.length+","+output.length);
		//*/
		
		//*
		
		File save = new File(args[2]);
		if (!save.exists())
			save.createNewFile();
		Files.write(save.toPath(), output);
		
	}
	
	private static final int TABLE_LENGTH = 256;
	byte[] data;

	public Huffman(byte[] data) {
		this.data = data;
	}
	
	public byte[] encode(){
		Node root = createTree();
		HashMap<Byte,List<Boolean>> cMap = makeCodeMap(root);
		Byte[] tree = encodeTree(cMap);
		Byte[] output = encode(data,cMap);
		byte[] bytes = new byte[tree.length + output.length];
		for (int i = 0;i<bytes.length;i++){
			bytes[i] = i<tree.length?tree[i]:output[i-tree.length];
		}
		return bytes;
	}
	
	public byte[] decode(){
		Node root = createTree(data);
		return decode(root);
	}
	
	private byte[] decode(Node root){
		List<Byte> output = new ArrayList<Byte>();
		int i = toUnsigned(data[0],data[1]);
		int lastLength = data[i++];
		int j = 0;
		Node working = root;
		int mask = 0x80;
		int index = i+j/8;
		while(index<data.length){
			byte b = data[i+j/8];
			if (DEBUG && i<256 && j%8==0)System.out.printf("DEBUG: Huffman.decode(root): byteRead 0x%02X\n",b);
			int tmp = b<<(j%8);
			boolean right = (tmp & mask) == mask;
			
			if (working.isLeaf()){
				if (DEBUG && i<256) System.out.printf("DEBUG: Huffman.decode(root) : Next byte: 0x%02X | j=%d i=%d l=%d\n",working.getData(),j,i,data.length);
				output.add(working.getData());
				if (index==data.length-1){
					if (j>lastLength)
						break;
				}
				i+=j/8;
				j%=8;
				working = root;
			}else{
				working = right?working.getRight():working.getLeft();
				j++;
			}
			index = i+j/8;
			
		}
		byte[] ret = new byte[output.size()];
		for (int k = 0;k<ret.length;k++){
			ret[k] = output.get(k);
		}
		return ret;
	}
	

	/**
	 * Translates a huffman tree into bytes.
	 * The first two values are the big-endian 16 bit length. </br>
	 * 
	 * Then there is a series of bytes following the following schema:</br>
	 * <ul>
	 * <li>The first byte is the actual <b>byte</b> to be read/used in the tree.</li>
	 * <li>The second byte is the number of bits (N) used to store the huffman-encoding
	 * of the <b>byte</b></li>
	 * <li>The next N bits are are the huffman-encoding of the <b>byte</b>
	 * <li>The remaining bits needed to make a multiple of 8 are 0 and should be ignored</li>
	 * </ul> 
	 * @param cMap The hashMap containg the encoding table.
	 * @return An array of bytes to representing the tree
	 */
	private Byte[] encodeTree(HashMap<Byte, List<Boolean>> cMap){
		
		List<Byte> encoded = new ArrayList<Byte>();
		encoded.add((byte) 0); //Placeholder for size
		encoded.add((byte) 0);
		
		//Itterate through all the bytes that can be encoded
		for (byte value : cMap.keySet()){
			List<Boolean> bits = cMap.get(value);
			
			//TODO: Better handeling
			if (bits.size()>127){
				System.err.println("Unplaned value inn encodeTree");
				System.exit(1);
			}
			
			//The value and the size of the encoding
			encoded.add(value);
			encoded.add((byte) bits.size());
			
			if (DEBUG) System.out.printf("DEBUG: Huffman.encodeTree(...) : 0x%02X - ",value);
			
			byte b = 0; //Holds the encoding
			int i = 0;  //Number of bytes used
			
			for (boolean bit : bits){
				b |= bit?0b1:0b0;
				if (DEBUG) System.out.print(bit?0b1:0b0);
				
				if (i<7){ //Keep going
					i++;
					b<<=1;
				}else{ //Rolling over into a new byte
					i=0;
					encoded.add(b);
					b=0;
				}
			}
			
			//Padding and addding the final byte
			b<<=(7-i);
			encoded.add(b);
			
			if (DEBUG) System.out.println();
		}
		
		//Adds the size of the encoding array (including these two bytes)
		int size = encoded.size();
		encoded.set(0,(byte) (size>>8));
		encoded.set(1,(byte) size);
			
		Byte[] ret = new Byte[encoded.size()];
		return encoded.toArray(ret);
	}

	/**
	 * Encodes the bytes with the huffman-code</br>
	 * The first byte is the number of bits to be read from the last byte
	 * @param toEncode The bytes to encode
	 * @param cMap The encoding-table
	 * @return
	 */
	private Byte[] encode(byte[] toEncode, HashMap<Byte, List<Boolean>> cMap) {
		List<Byte> encoded = new ArrayList<Byte>();
		encoded.add((byte) 0b0); //placeholder;
	
		byte b = 0;
		int i = 0;
		for (byte enc : toEncode){
			
			for (boolean bool : cMap.get(enc)){
				b |= bool?0b1:0b0;
				if (i<7){
					i++;
					b<<=1;
				}else{
					encoded.add(b);
					i = 0;
					b = 0;
				}
			}
		}
		encoded.set(0,(byte) i);
		
		b<<=(7-i);
		encoded.add(b);
		
		Byte[] ret = new Byte[encoded.size()];
		return encoded.toArray(ret);
	}

	/**
	 * Creates a huffman-tree from the bytes of a stored tree</br>
	 * The new created tree does not contain any information about frequencies,
	 * but for the propose of decoding data, this is not necessary.
	 * See {@link huffman.Huffman#encodeTree(HashMap) encodeTree} for the encoding schema.
	 * @param storedTree The bytes to read, can be more than the actual tree.
	 * @return The root of the huffman tree.
	 */
	private Node createTree(byte[] storedTree){
		ReadNode root = new ReadNode();
		byte mask = (byte) 0x80;
		int treeLength = toUnsigned(storedTree[0],storedTree[1]);
		
		//Itterate through the bytes containg the data
		for (int i = 2;i<treeLength;){
			byte data = storedTree[i++]; //The byte to be read
			int length = storedTree[i++]; //Number of bits to read
			
			if (DEBUG) System.out.printf("DEBUG: Huffman.createTree(storedTree) : 0x%02X : ",data);
			
			//Holds the node we are working on.
			ReadNode working = root;
			for (int bit = 0;bit<length;bit++){
				
				byte byteToRead = storedTree[i+bit/8];
				byte tmp = (byte) (byteToRead<<bit%8);
				boolean right = (tmp & mask) == mask; //Use the right branch if true
				
				if (DEBUG) System.out.printf("%d",right?1:0);
				ReadNode child = (ReadNode) (right?working.getRight():working.getLeft());
				if (child==null){ //Create a new child node
					child = new ReadNode();
					if (right)
						working.setRight(child);
					else
						working.setLeft(child);
				}
				working = child;
			}
			if (DEBUG) System.out.println();
			//working is now the leaf node in the huffman-tree that contains that byte.
			working.setData(data);
			i += length/8 + 1;			
		}
				
		return root;
	}
	
	private int toUnsigned(byte... bytes) {
		int ret = 0;
		for (byte b : bytes){
			ret |= (b+256)%256;
			ret <<= 8;
		}
		ret >>=8;
		return ret;
	}

	/**
	 * Creates the huffman-tree from the bytes in the data. </br>
	 * See {@link <a href="https://en.wikipedia.org/wiki/Huffman_coding#Basic_technique">wikipedia</a>}
	 * for how the huffman-tree is created.
	 * @return The root of the huffman tree
	 */
	private Node createTree(){
		
		//Holds the relative frequencies
		int[] frequencyTable = new int[TABLE_LENGTH];
		
		for (int b : data){
			if (b<0) b+=TABLE_LENGTH;
			if (DEBUG && frequencyTable[b]==0){
				System.out.printf("DEBUG: Huffman.createTree() new byte: 0x%02X\n",b);
			}
			frequencyTable[b]++;
		}
		
		Queue<Node> queue = new PriorityQueue<Node>();

		//Create a new node for each byte that has a non-zero frequency, and adds it to the queue.
		for(int b = 0;b<TABLE_LENGTH;b++){
			int v = frequencyTable[b];
			if (v==0) continue;
			Node n = new ConstructionNode((byte) b,v);
			queue.add(n);
		}
		
		/* Creates a new (inner) node from the two two nodes with the smallest frequencies.
		 * The new node has a frequency equal to the sum of its children, this new node is then
		 * added to the queue.
		 */
		while(queue.size()>1){
			Node n1 = queue.poll();
			Node n2 = queue.poll();
			if (DEBUG){
				System.out.printf("DEBUG: Huffman.createTree(): Removed node %s\n",n1);
				System.out.printf("DEBUG: Huffman.createTree(): Removed node %s\n",n2);
			}
			
			Node inner = new ConstructionNode(n1,n2);
			queue.add(inner);
		}
		
		//The only node in the queue is now the root.
		return queue.poll();
	}
	
	/**
	 * Creates an encoding table for the huffman tree
	 * @param root The root of the tree
	 * @return A hashMap, where the key is the byte to be encoded 
	 * 		   and the value is a list of booleans, represneting bits.
	 */
	private HashMap<Byte,List<Boolean>> makeCodeMap(Node root){
		HashMap<Byte, List<Boolean>> encKey = new HashMap<Byte, List<Boolean>>();
		populateEncTree(root,new ArrayList<Boolean>(),encKey);
		
		if (DEBUG){
			for (Byte c : encKey.keySet()){
				System.out.printf("DEBUG: Huffman.makeCodeMap(...) : 0x%02X : ",c);
				for (boolean b : encKey.get(c)){
					System.out.print(b?1:0);
				}
				System.out.println();
			}
		}
		return encKey;
	}
	/**
	 * Recursivly creates a list of booleans for each leaf-node in the huffman-tree
	 * @param n The node to be worked on
	 * @param arrayList An arraylist containing the ancestor-nodes' path to node <b>n</b>.
	 * @param encKey The hashMap where <b>arrayList</b> is stored.
	 */
	private void populateEncTree(Node n, List<Boolean> arrayList, HashMap<Byte, List<Boolean>> encKey) {
		if (n.isLeaf()){
			encKey.put(n.getData(), arrayList);
			return;
		}
		
		List<Boolean> leftList = new ArrayList<Boolean>(arrayList);
		leftList.add(false);
		arrayList.add(true);
		
		populateEncTree(n.getLeft(), leftList, encKey);
		populateEncTree(n.getRight(), arrayList, encKey);
		
	}
	
	
	
	//Used to print a tree (badly, for debug only)
		private String printTree(Node n, String prefix){
			String output = prefix + n + '\n';
			if (!n.isLeaf()){
				output+=printTree(n.getLeft(), prefix + '\u2500');
				output+=printTree(n.getRight(), prefix + '\u2500');
			}
			return output;
		}
		
}
