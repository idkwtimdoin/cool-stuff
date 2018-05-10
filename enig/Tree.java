import java.io.*;
class Tree{
	char o='I', l='l';
	int[]alpha = new int[(int)Math.pow(2,8)];
	String[]paths = new String[(int)Math.pow(2,8)];
	List list;
	int largestPath =0;
	public Tree(String name){
		String satz = "", line = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(name));
			while ((line = reader.readLine()) != null)	satz = satz.concat(line);
			reader.close();
		}catch(IOException e){
			System.out.println("i/o exc "+ e);
		}
		if(assemble(satz))	// build tree
			while(list.next!=null){
				merge();
				if(list.next!=null)
					insert();
				// list.show();
			}
		else{
			System.out.println("\t*"+name+" has only 1 char... no encryption");
			System.exit(1);
		}
	}
	void merge(){
		List ptr = list;
		while(ptr.next!=null && ptr.next.next!=null)	ptr = ptr.next;
		ptr.gew += ptr.next.gew;
		ptr.curr = new Node(ptr.curr, ptr.next.curr);
		ptr.next = null;
	}	// merge
	void handle(String name){
		String satz = "", line = "";
		if(name.length()>4 && name.substring(name.length()-4).equals(".txt"))
			try{
				BufferedReader reader = new BufferedReader(new FileReader(name));
				while ((line = reader.readLine()) != null)	satz = satz.concat(line);
				reader.close();
				if(check(satz))
					decrypt(name, satz);
				else 
					encrypt(name, satz);
			}catch(IOException e){
				System.out.println("i/o exc "+ e);
			}
		else
			if(check(name))
				System.out.println(decode(name));
			else
				System.out.println(encode(name));
	}	// handle
	boolean assemble(String satz){
		int count=0;
		for(int i=0; i<satz.length(); ++i){
			if(alpha[(char)satz.charAt(i)]==0)	++count;
			alpha[(char)satz.charAt(i)]++;
		}
		if(count<2)	return false;
		boolean first = true;
		for(int i=0; i<count; ++i){
			int max=0, x=0;
			for(int k=0; k<alpha.length; ++k)
				if(alpha[k]>max){
					max=alpha[k];
					x=k;
				}
			if(first){
				list = new List(max, new Node((char)x));
				first = false;
			}else
				list.push(max, new Node((char)x));
			alpha[x]=0;	
		}
		return true;
	}	// assemble
	void insert(){
		List ptr=list, zeiger=list, del = list;
		while(ptr.next!=null)	ptr = ptr.next;
		while(del.next!=null && del.next.next!=null)	del = del.next;
		if(ptr.gew>list.gew){
			ptr.next = list;
			del.next = null;
			list = ptr;
		} else{
			while(zeiger.next.gew>ptr.gew && zeiger.next!=null)	zeiger = zeiger.next;
			List temp = zeiger.next;
			ptr.next = temp;
			del.next = null;
			zeiger.next = ptr;
		}
	}	// insert
	boolean check(String binary){
		if(binary.length()==0)	return false;
		for(int i=0; i<binary.length(); ++i)
			if(binary.charAt(i)!=o && binary.charAt(i)!=l)
				return false;
		return true;
	}	// check
	public void encrypt(String name, String satz){
		try{
			PrintWriter writer = new PrintWriter (new FileOutputStream("enc-"+name), true);		
			writer.print(encode(satz));
			writer.flush();
			writer.close();
		}catch(IOException e){
			System.out.println("i/o exc "+ e);
		}
	}	// encrypt
	String encode(String s){
		String code = "";
		for(int i=0; i<s.length(); ++i)
			code = code.concat(paths[s.charAt(i)]);
		return code;
	}	// encode
	public void decrypt(String name, String satz){
		try{
			PrintWriter writer = new PrintWriter (new FileOutputStream("dec-"+name), true);		
			writer.print(decode(satz));
			writer.flush();
			writer.close();					
		}catch(IOException e){
			System.out.println("i/o exc "+ e);
		}
	}	// dencrypt	
	String decode(String bin){
		String s = "";
		Node ptr = list.curr;
		for(int i=0, charpath=0; i<bin.length(); ++i){
			if(bin.charAt(i)==o)	ptr = ptr.left;
			else	ptr = ptr.right;
			if(ptr.left==null){
				s = s+ptr.chr;
				ptr = list.curr;
				charpath=0;
			} else{
				charpath++;
				if(charpath>largestPath){
					System.out.println(largestPath+" "+charpath);
					ptr = list.curr;
					s = s+'?';
				}
			}
		}
		return s;
	}	// decode
	public void store(Node head, String path){ 
		if(head.left!=null)
			store(head.left, path+o);
		if(head.right!=null)
			store(head.right, path+l);
		if(head.right==null && head.left==null){	// only leaves
			paths[head.chr] = path;
			if(largestPath<path.length())	largestPath = path.length();
		}
	}	// store
	public void trav(Node head, String path){ 
		if(head.left!=null)
			trav(head.left, path+o);
		if(head.right!=null)
			trav(head.right, path+l);
		if(head.right==null && head.left==null)
			System.out.printf("%30s %s\n", path, head.disp());
	}	// trav	
}