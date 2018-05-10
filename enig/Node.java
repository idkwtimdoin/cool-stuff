class Node{
	Node right, left;
	char chr = '^';
	public Node(char gew){
		chr = gew;
	}
	public Node(Node n1, Node n2){
		left = n1;
		right = n2;
	}
	public String disp(){
		return String.format("%c\t%d", chr, (int)chr);
	}	// disp	
}