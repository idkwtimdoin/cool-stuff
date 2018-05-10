class heap{
	private int zeiger;
	private node h;
	public heap(node n){ h = n; }
	
	int pop(){
		int x = h.x;
		h = delete(h);
		return x;
	}	// del
	
	node delete(node h){
		if(h.left==null && h.right==null){
			zeiger--;
			return null;
		}
		swap( h, nodeAt(h, path(zeiger-1)) );
		if((zeiger-1)%2==0)
			nodeAt(h, path((zeiger-1-1)/2)).right = null;
		else	
			nodeAt(h, path((zeiger-1-1)/2)).left = null;
		zeiger--;
		return bubble(h);
	}	// delete
	
	node bubble(node h){
		if(h.left!=null && h.left.x > h.x)
			if(h.right!=null && h.right.x > h.left.x){
				swap(h, h.right);
				h.right = bubble(h.right);
			}else{
				swap(h, h.left);
				h.left = bubble(h.left);
			}
		else
			if(h.right!=null && h.right.x > h.x)
				if(h.left!=null && h.left.x > h.right.x){
					swap(h, h.left);
					h.left = bubble(h.left);
				}else{
					swap(h, h.right);
					h.right = bubble(h.right);
				}
		return h;
	}	// bubble
	
	void popAll(){
		while(zeiger>0)
			System.out.print(pop() + (" "));
		System.out.println("");
	}
	node nodeAt(node h, String path){
		if(path.length()==0)	return h;
		else	return (path.charAt(0)=='0'? nodeAt(h.left, path.substring(1)) : nodeAt(h.right, path.substring(1)));
	}	// node at
	
	void push(node inc){
		h = add(h, inc);
	}	// add
	
	node add(node h, node inc){
		if (h==null){
			zeiger++;
			return inc;
		}
		String path = path(zeiger);
		++zeiger;
		return addr(h, inc, path);
	}	// add

	node addr(node h, node inc, String p){
		if(p.length()==1){
			if(h.x < inc.x)	swap(h, inc);
			put(h, inc, p.charAt(0));
			return h;
		}else{
			if(p.charAt(0)=='0')	h.left = addr(h.left, inc, p.substring(1));
			else	h.right = addr(h.right, inc, p.substring(1));
		}
		if(h.left.x > h.x)	swap(h.left, h);
		if(h.right.x > h.x)	swap(h.right, h);
		return h;
	}	// addr

	void put(node n, node inc, char c){
		if(c=='0')	n.left = inc;
		else	n.right = inc;
	}	// put

	void swap(node a, node b){
		int temp = a.x;
		a.x = b.x;
		b.x = temp;
	}	// swap
	
	String path(int in){
		if(in==0)	return "";
		int p = 1, i = 0;
		while(in>=p)
			p+= Math.pow(2, ++i);
		p-= Math.pow(2, i);
		String path = Integer.toBinaryString(in-p);
		while(path.length()<i)	
			path = "0" + path;
		return path;
	}	// path 0->left, 1->right
	
	int getMax(){ return h.x; }
	// get max
	
	void traverse(){
		if(h!=null)	trav(h, "");
		else	System.out.printf(String.format("\t%-15s ", "").replaceAll(" ", "."));
	}	// traverse paths 0->left, 1->right
	
	void trav(node h, String path){
		if(h.left!=null)
			trav(h.left, path.concat("0"));
		if(h.right!=null)
			trav(h.right, path.concat("1"));
		System.out.printf(String.format("\t%-15s %d%n", path, h.x).replaceAll(" ", "."));
	}	// trav
	
}