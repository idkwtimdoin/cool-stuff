class Node{
	Node right, left;
	int weight =0;
	char val;
	public Node(){}
	public Node(int gew){
		weight = gew;
	}
	public Node(Node n){
		val = n.val;
		weight = n.weight;
		left = n.left;
		right = n.right;
	}
	public char value(){
		return val;
	}
	public Node(Node n1, Node n2){
		if(n1!=null)	weight = n1.weight + n2.weight;
		else	weight = n2.weight;
		if(n2!=null)	weight = n1.weight + n2.weight;
		else	weight = n1.weight;
		left = n1;
		right = n2;
	//	val = '^';	// just indecating a merged node for testing huffman
	}
	public void set(char value, int gew){
		val = value;
		weight = gew;
	}	// set
	public void disp(){
		System.out.printf("%c%8d%n", val, weight);
	}	// disp	
}