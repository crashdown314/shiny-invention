import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Huffman {

	private static final String USAGE = "Huffman toEncode [saveFile]";

	public static void main(String[] args) throws IOException{
		if (args.length<1){
			System.out.println(USAGE);
			System.exit(1);
		}

		File f = new File(args[0]);
		List<String> lines;
		if (f.exists()){
			lines = Files.readAllLines(f.toPath());
		}else{
			lines = new ArrayList<String>();
			for (String line : args[0].split("\n")){
				lines.add(line);
			}
		}
	

		// Adds a linebreak to the end of each line so that the algorithm don't have
		// to.
		for(int i = 0;i<lines.size();i++){
			lines.set(i, lines.get(i) + '\n');
		}
	
		Huffman h = new Huffman(lines);
		h.createTree();
		
		byte[] output = h.encode();
		if(args.length>1)
		{
			File save = new File(args[1]);
			Path p = Files.write(save.toPath(), output);
		}else
		{
			// TODO:Print to screen?
		}
	}

	private byte[] encode() {
		// TODO Auto-generated method stub
		return null;
	}

	List<String> lines;

	public Huffman(List<String> inputLines) {
		this.lines = inputLines;
	}

	public void createTree(){
			
		HashMap<Character,Integer> chars = new HashMap<Character,Integer>();
		for (String line : lines){
			for (char c : line.toCharArray()){
				Integer freq = chars.get(c);
				if (freq == null){
					System.out.println("DEBUG: Huffman.createTree():  New character: "+prettyPrint(c));
					chars.put(c,1);
				}else{
					chars.put(c,freq + 1);
				}
			}
	
	
		}
		Queue<Node> queue = new PriorityQueue<Node>();
		
		
		for(Entry<Character, Integer> e : chars.entrySet())	{
			Node n = new Node(e.getKey(), e.getValue());
			queue.add(n);
		}
		
		while(queue.size()>1){
			Node n1 = queue.poll();
			Node n2 = queue.poll();
			System.out.printf("DEBUG: Huffman.createTree(): Removed node %s\n",n1);
			System.out.printf("DEBUG: Huffman.createTree(): Removed node %s\n",n2);
			
			Node inner = new Node(n1,n2);
			queue.add(inner);
		}
		
		Node root = queue.poll();
		
		HashMap<Character, List<Boolean>> encKey = new HashMap<Character, List<Boolean>>();
		
		for(Character c : chars.keySet()){
			Stack<Boolean> bit = getBits(root,c);
			System.out.printf("%s : ",prettyPrint(c));
			for (boolean b : bit)
				System.out.printf("%d",b?1:0);
			System.out.println();
		}
		
		printNode(root, "");
	}
	
	static String prettyPrint(char c){
		if (c=='\n')return "[::NEW_LINE::]";
		if (c==' ') return "[::SPACE::]";
		return ""+c;
	}

	private Stack<Boolean> getBits(Node n, Character toFind){
		if (n.getCharacter() != null){
			if (n.getCharacter().equals(toFind))
				return new Stack<Boolean>();
			else
				return null;
		}
		
		Stack<Boolean> ans = getBits(n.getLeft(),toFind);
		if (ans==null){
			ans = getBits(n.getRight(),toFind);
			if (ans==null) return null;
			ans.push(true);
		}else{
			ans.push(false);
		}
		
		return ans;
	}
	
	public void printNode(Node n,String prefix){
		System.out.println(prefix+n);
		if (!n.isLeaf()){
			printNode(n.getLeft(),prefix+"  ");
			printNode(n.getRight(),prefix+"  ");
		}
	}
}

class Node implements Comparable<Node>{
	private Node child1;
	private Node child2;
	private int value;
	private Character character;

	public Node(Node child1, Node child2){
		this(child1,child2,child1.value + child2.value,null);
	}

	public Object getCharacter() {
		return character;
	}
	
	public Node getLeft(){
		return child1;
	}
	public Node getRight(){
		return child2;
	}

	public Node(Character character, int value){
		this(null,null,value,character);
	}

	private Node(Node child1, Node child2, int value, Character character){
		this.child1 = child1;
		this.child2 = child2;
		this.value = value;
		this.character = character;
	}
	
	public boolean isLeaf(){
		return child1==child2 && child1==null;
	}

	@Override
	public int compareTo(Node n){
		if (n==null) return -1;
		return value - n.value;
	}
	
	@Override
	public String toString() {
		if (isLeaf()){
			return "{L|"+Huffman.prettyPrint(character)+"|"+value+"}";
		}else{
			return "{N|"+value+"}";
		}
	}
}
