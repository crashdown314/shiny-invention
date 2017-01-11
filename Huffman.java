import java.io.File;

publlic class Huffman{
  
  private static final String USAGE = "Huffman toEncode [saveFile]";
  public static void main(String[] args){
    if (args.length<1){
      System.out.println(USAGE);
    }
    
    File f = new File(args[0]);
    List<String> lines;
    if (f.exits()){
      lines = Files.readAllLines(f.getPath());
    }else{
      lines = new ArrayList<String>();
      for (String line : args[0].split("\n"))
        lines.add(line);
      }
    }
    
    //Adds a linebreak to the end of each line so that the algorithm don't have to.
    for (int i = 0;i<lines.size();i++){
      lines.set(i,lines.get(i)+'\n');
    }
    
    Huffman h = new Huffman(lines);
    h.createTree();
    byte[] output = h.encode();
    if (args.length>1){
      File save = new File(args[1]);
      Path p = Files.write(save.toPath(),output);
    }else{
      //TODO:Print to screen?
    }
  }
  
  List<String> lines;
  public Huffman(List<String> inputLines){
    this.lines = inputLines;
  }
  
  public void createTree(){
    Map<Character,Integer> chars = new HashMap<Character,Integer>();
    for (String line : lines){
      for (char c : line.toCharArray(){
        Integer freq = chars.get(c);
        if (freq == null){
          chars.put(c,1);
        }else{
          chars.put(c,freq + 1);
        }
      }
      
      Queue<Node> q = new PriorityQueue<Node>();
      
      for (Entry<Character,Integer> e : chars.entrySet()){
        Node n = new Node(e.getKey(), e.getValue());
        q.add(n);
      }
      
      while(q.size()>1){
        Node inner = new Node(q.poll(),q.poll());
        q.add(inner);
      }
      
      Node root = q.poll();
      
      HashMap<Character,List<Boolean>> encKey = new HashMap<Character,List<Boolean>>();
      for (Character c : chars.keys()){
        List<Boolean> bit = getBits(root,c);
    }
    
  }
  
  private List<Boolean> getBits(Node n, Character toFind){
    if (n.character != null && n.character.equals(toFind)){
      return new
}

class Node implements Comperable<Node>{
  private Node child1;
  private Node child2;
  private int value;
  private Character character;
  
  public Node(Node child1, Node child2){
    this(child1,child2,child1.value + child2.value,null);
  }
  
  public Node(int value, Character character){
    this(null,null,value,character);
  }
  
  public Node(Node child1, Node child2, int value, Character character){
    this.child1 = child1;
    this.child2 = child2;
    this.value = value;
    this.character = character;
  }
  
  @Override
  public int compareTo(Node n){
    return value - n.value;
  }
