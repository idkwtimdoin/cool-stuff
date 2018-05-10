import java.io.*;
import java.util.Scanner;
class command extends Tree{
	static int comps=0, adopt=0, adds=0, successfulFinds=0, totalFinds=0, successfulRemoves=0, totalRemoves=0;
	static int threshold=3;
	public static void main(String[]args){
		if(args.length==0)	System.out.println("need to give an input text file as args");
		else
			try{
				BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				if(args.length==2 && Integer.parseInt(args[1])!=0){
					for(int i=0; i<Integer.parseInt(args[1]) && (satz = reader.readLine()) != null; ++i){
						doIt();
						traverse(root, "");
						System.out.println();
					}
					traverse(root, "");
					results(Integer.parseInt(args[1]));
				}
				while((satz = reader.readLine()) != null)
					doIt();
				Scanner yourIn = new Scanner(System.in);
				System.out.print("\tshow all results? (y/X)");
				char yesOrNo = yourIn.next().charAt(0);
				if(yesOrNo=='y')	results(-1);
				System.out.print("\tshow traversals with paths? (y/X)");
				yesOrNo = yourIn.next().charAt(0);
				if(yesOrNo=='y')	traverse(root, "");
				reader.close();
			}catch(IOException e){
				System.out.println("i/o exc "+ e);
			}catch(NumberFormatException e){
				System.out.println("num format exc "+ e);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("args error"+ e);
			}
		//	catch(NullPointerException e){	System.out.println("null ptr exc "+ e);	}
	}	// main
	static void doIt(){
		int n = Integer.parseInt(satz.substring(1));
		if(satz.length()==0)	return;
		if(root==null)
			if(satz.charAt(0)!='a'){	// if no tree, only care whether the operation is adding
				switch(satz.charAt(0)){
					case'r':{
					//	System.out.printf("%-10s%-8d%s%d%n", "remove:", n, "d:", depth(root));
					//	totalRemoves++;	// doesn't count; tree is empty
						break;
					}
					case'f':{
					//	System.out.printf("%-10s%-8d%s%d %s%n", "find:", n, "d:", depth(root), "false");
					//	totalFinds++;	// doesn't count; tree is empty
						break;
					}
				}	// switch
				return;
			} else{
				root = new Node(n);
				adds++;
			//	System.out.printf("%-10s%-8d%s%d%n", "added:", n, "d:", depth(root));
				return;
			}	// if no tree to begin with
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
		}	// switch
	}	// doIt
	static boolean find(Node head, int n){
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
	static Node add(Node head, int n){
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
	static Node del(Node head, int n){
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
	static Node zigLeft(Node n){
		adopt+=2;
		Node newL = n.left;
		n.left = newL.right;
		newL.right = n;
		return newL;
	}	// zigLeft
	static Node zigRight(Node n){
		adopt+=2;
		Node newR = n.right;
		n.right = newR.left;
		newR.left = n;
		return newR;
	}	// zigRight
	static Node zigzagLeft(Node n){
		n.left = zigRight(n.left);
		return zigLeft(n);
	}	// zigzagLeft
	static Node zigzagRigh(Node n){
		n.right = zigLeft(n.right);
		return zigRight(n);
	}	// zigzagRigh
	static void traverse(Node head, String path){
		if(head.left!=null)
			traverse(head.left, path.concat("0"));
		if(head.right!=null)
			traverse(head.right, path.concat("1"));
		System.out.printf(String.format("\t%-20s %d%n", path, head.weight).replaceAll(" ", "."));
	}	// traverse
	static void quickTrav(Node head){
		if(head.left!=null)
			quickTrav(head.left);
		if(head.right!=null)
			quickTrav(head.right);
		System.out.printf("%d%c", head.weight, (head!=root?',':'\n'));
	}	// quickTrav
	static void results(int stop){
		if(stop>0)	System.out.printf("%s%d: ", "Traversal at ", stop);
		else	System.out.printf("%s: ", "Traversal until end");
		quickTrav(root);
		System.out.printf("%d %s%n", comps, "compares");
		System.out.printf("%d %s%n", adopt, "parents changed");
		System.out.printf("%d %s, %d %s%n", successfulFinds, "successful finds", (totalFinds-successfulFinds), "failed finds");
		System.out.printf("%d %s, %d %s%n", successfulRemoves, "successful removes", (totalRemoves-successfulRemoves), "failed removes");
		System.out.printf("%d %s%n", adds, "values added");
	}	// results
}