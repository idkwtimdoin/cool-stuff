class AVLTree{
	AVLTree(){
		satz="";
	}
	String satz, errors="";
	Node root;
	int comps=0, adopt=0, adds=0, successfulFinds=0, totalFinds=0, successfulRemoves=0, totalRemoves=0;
	int threshold=3, errorCount=0;
	void doIt(int count){
		if(satz.length()==0)	return;
		int n;
		if(satz.substring(1).matches("\\d+"))
			n = Integer.parseInt(satz.substring(1));
		else{
			record(count);
			return;
		}
		if(root==null){
			switch(satz.charAt(0)){
				case'a':{
				root = new Node(n);
				adds++;
				//	System.out.printf("%-10s%-8d%s%d%n", "added:", n, "d:", depth(root));
				break;
				}
				case'r':{
				//	System.out.printf("%-10s%-8d%s%d%n", "remove:", n, "d:", depth(root));
					totalRemoves++;
					break;
				}
				case'f':{
				//	System.out.printf("%-10s%-8d%s%d %s%n", "find:", n, "d:", depth(root), "false");
					totalFinds++;
					break;
				}
				default:{ record(count); }
			}	// switch
			return;
		}
		switch(satz.charAt(0)){
			case'a':{
				root = add(root, n);
				adds++;
			//	System.out.printf("%-10s%-8d%s%d%n", "added:", n, "d:", depth(root));
				break;
			}
			case'r':{
				totalRemoves++;
				root = del(root, n);
			//	System.out.printf("%-10s%-8d%s%d%n", "remove:", n, "d:", depth(root));
				break;
			}
			case'f':{
				totalFinds++;
			//	System.out.printf("%-10s%-8d%s%d %b%n", "find:", n, "d:", depth(root), find(root, n));
				if(find(root, n)) successfulFinds++;
				break;
			}
			default:{ record(count); }
		}	// switch
	}	// doIt
	boolean find(Node head, int n){
		if(head==null)	return false;
		comps++;
		if(head.weight<n){
			return find(head.right, n);
		} else
			if(head.weight>n){
				comps++;
				return find(head.left, n);
			} else	return true;	// found
	}	// find
	Node add(Node head, int n){
		if(head==null)	head = new Node(n);
		else{
			comps++;
			if(head.weight<n){	// insert to right
				head.right = add(head.right, n);
				if(depth(head.right)-depth(head.left)>=threshold){	// check as we go back up in stack
				//	traverse(head, "b");
					comps++;
					if(head.right.weight<n){	// if left not stuffed rotate left once
					//	System.out.println("zigR: "+head.weight);
						head = zigRight(head);
					} else{	// if left stuffed rotate left twice
					//	System.out.println("zigzagR: "+head.weight);
						head = zigzagRigh(head);
					}
				}
			} else{
				comps++;
				if(head.weight>n){	// still have to include if smaller because no duplicate vals
					head.left = add(head.left, n);
					if(depth(head.left)-depth(head.right)>=threshold){
					//	traverse(head, "b");		
						comps++;
						if(head.left.weight>n){	// if right isnt stuffed rotate right once
						//	System.out.println("zigL: "+head.weight);
							head = zigLeft(head);
						} else{	// if right stuffed	rotate right twice
						//	System.out.println("zigzagL: "+head.weight);
							head = zigzagLeft(head);
						}
					}
				}	// if smaller
			}
		}	// if not null
		return head;
	}	// add
	Node del(Node head, int n){
		if(head==null)	return null;
		comps++;
		if(head.weight<n){
			head.right = del(head.right, n);
			if(Math.abs(depth(head.right)-depth(head.left))>=threshold){
				if(depth(head.left)-depth(head.right)>=threshold){
				//	System.out.println("zigL: "+head.weight);
					return head = zigLeft(head);
				} else{
				//	System.out.println("zigR: "+head.weight);
					return head = zigRight(head);
				}
			}
		} else{
			comps++;
			if(head.weight>n){
				head.left = del(head.left, n);
				if(Math.abs(depth(head.right)-depth(head.left))>=threshold){
					if(depth(head.left)-depth(head.right)>=threshold){
					//	System.out.println("zigL: "+head.weight);
						return head = zigLeft(head);
					} else{
					//	System.out.println("zigR: "+head.weight);
						return head = zigRight(head);
					}
				}
			} else{	// found
				successfulRemoves++;
				if(head.right==null){ 
					if(head.left!=null)	adopt++;
					return head.left; 
				} else
					if(head.left==null){
						if(head.right!=null)	adopt++; 
						return head.right; 
					} else{	// if(has left and right) replace with left's largest; thus depth(left) can only be smaller
						adopt++;
						Node leftBig = head.left;
						while(leftBig.right!=null)	leftBig = leftBig.right;
						int swap = leftBig.weight;	// save the value before deletion
						head = del(head, swap);	// delete left's right most
						successfulRemoves--;	// next remove doesn't count; it removes the swapped one
						head.weight = swap;	// the deleted node gets the saved value
						return head;
					}
			}	// if found
		}
		return head;
	}	// del
	Node zigLeft(Node n){
		adopt+=2;
		Node newL = n.left;
		n.left = newL.right;
		newL.right = n;
		return newL;
	}	// zigLeft
	Node zigRight(Node n){
		adopt+=2;
		Node newR = n.right;
		n.right = newR.left;
		newR.left = n;
		return newR;
	}	// zigRight
	Node zigzagLeft(Node n){
		n.left = zigRight(n.left);
		return zigLeft(n);
	}	// zigzagLeft
	Node zigzagRigh(Node n){
		n.right = zigLeft(n.right);
		return zigRight(n);
	}	// zigzagRigh
	void traverse(Node head, String path){
		if(head.left!=null)
			traverse(head.left, path.concat("0"));
		if(head.right!=null)
			traverse(head.right, path.concat("1"));
		System.out.printf(String.format("\t%-20s %d%n", path, head.weight).replaceAll(" ", "."));
	}	// traverse
	void quickTrav(Node head){
		if(head.left!=null)
			quickTrav(head.left);
		if(head.right!=null)
			quickTrav(head.right);
		System.out.printf("%d%c", head.weight, (head!=root?',':'\n'));
	}	// quickTrav
	void results(int stop){
		if(stop>0)	System.out.printf("%s%d: ", "Traversal at ", stop);
		else	System.out.printf("%s: ", "Traversal until end");
		quickTrav(root);
		System.out.printf("%d %s%n", comps, "compares");
		System.out.printf("%d %s%n", adopt, "parents changed");
		System.out.printf("%d %s, %d %s%n", successfulFinds, "successful finds", (totalFinds-successfulFinds), "failed finds");
		System.out.printf("%d %s, %d %s%n", successfulRemoves, "successful removes", (totalRemoves-successfulRemoves), "failed removes");
		System.out.printf("%d %s%n", adds, "values added");
		if(errorCount>0)
			System.out.printf("%d %s %s%n", errorCount, "erroneous commands at lines", errors);
	}	// results
	int depth(Node n){
		if(n==null)	return 0;
		int rd = depth(n.right);
		int ld = depth(n.left);
		return rd>ld? rd+1 : ld+1;
	}	// funky depth function
	void record(int count){
		if(errors.length()!=0)	errors= errors.concat(",");
		errorCount++;
		errors= errors.concat(Integer.toString(count));
	}
}