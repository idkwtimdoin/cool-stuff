public class List{
	public List next;
	public Node curr;
	int gew;
	public List(int g, Node N){
		gew = g;
		curr = N;
	}
	public void push(int l, Node n){
		if(next==null)	next = new List(l, n);
		else	next.push(l, n);
	}	// push
	public void show(){
		System.out.println("\t"+gew+"\t"+curr.chr+" "+(int)curr.chr);
		if(next!=null)	next.show();
		else System.out.println();
	}	// show
}