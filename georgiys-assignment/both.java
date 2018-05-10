import java.util.Random;

class both{
	static double[]ptr;
	static int threshold = 10;
	static long t1;
	static String path = "";
	
	public static void main(String[]args){
		for(int i=0; i<=100; i+=5)
			test(100000, 1, i, 0);	// test for 100k, seed=1, threshold = i, without printing the array; to enabe printing change the 0 
/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+*/
		node head = new node(14);	// head of the heap
		for(int i=13; i>=8; --i){
			head = add(head, new node(i));	// add a new node with int = i
			System.out.println(depth(head)+" adding "+i);	
			traverse(head, "");		// display paths of all nodes 0->left, 1->right
		}
		System.out.println("");
		head = add(head, new node(20));	// now add 20 to the bottom
		traverse(head, "");
		System.out.println("max: " + findMax(head));	// findMax O(1) returns 20 checked
	}
	
	static void 	(int size, int seed, int th, int disp){
		threshold = th;
		double[]arr = gen(size, seed);
		if(disp!=0)	disp(arr);
		System.out.println("time(ms): "+ qs(arr)+ "\tth: "+ th);
		if(disp!=0)	disp(arr);
	}
	
	static float qs(double[]nums){
		ptr = nums;
		t1 = System.nanoTime();
		if(nums.length<=threshold)	insertion(0, nums.length-1);
		else	rqs(0, nums.length-1);		// the recursive function
		return (float)((System.nanoTime()-t1)/Math.pow(10,6));
	}
	
	static void rqs(int i, int j){
		int pivot = i+(int)(Math.random()*(j+1-i));		// select a random pivot
		swap(pivot, j);									// swap pivot with last element
		int part = partition(i-1, j, ptr[j]);			// part is the index of the partition
		swap(part, j);
		if(part-i > 1)
			if(part-i<=threshold)	insertion(i, part-1);	// threshold condition
			else	rqs(i, part-1);
		if(j-part > 1)
			if(j-part<=threshold)	insertion(part+1, j);	// threshold condition
			else	rqs(part+1, j);
	}	// rqs
	
	static int partition(int l, int h, double pivot){
		do{
			while(ptr[++l]<pivot);		// keep going right until finding larger than pivot
			while(ptr[--h]>pivot && h!=0);	// keep going left until finding less than pivot
			swap(l, h);					// put them into the correct spots
		}while(l < h);
		swap(l, h);					// this is because of the absence of a flag for l<h before swapping; the last swap is bad; need to be reswapped
		return l;
	}	// partition
	
	public static void swap(int a, int b){
		double temp = ptr[a];
		ptr[a] = ptr[b];
		ptr[b] = temp;
	}
	
	static void insertion(int l, int h){
		for(int i=l+1; i<=h; ++i){
			int j = i-1;
			double temp = ptr[i];
			while(j>=0 && ptr[j]>temp){
				ptr[j+1] = ptr[j];
				--j;
			}
			ptr[j+1] = temp;
		}
	}	// insertion
	
	static void disp(double[]arr){
		for(int i=0; i<arr.length; ++i)
			System.out.println(arr[i]+(i+1==arr.length?"\n":" "));		
	}	// disp an array

	static double[] gen(int N, int seed){
		Random gen = new Random();
		double[]ptr = new double[N];
		gen.setSeed(seed);
		for(int i=0; i<ptr.length; ++i)
			ptr[i]= gen.nextDouble()*gen.nextInt()/(N*N);
		return ptr;
	}	// generate a random array of size N and seed seed
/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+*/

	static node add(node head, node inc){
		if(head.left == null){	// give the incoming node to left if left is null
			if(head.x < inc.x)	swap(head, inc);		// swap if the child is larger
			head.left = inc;	
			return head; 
		}
		else if(head.right == null){		// else if right is null, give it to right
			if(head.x < inc.x)	swap(head, inc);
				head.right = inc;
				return head; 
			}else{					// else check if the right and left subtrees number of nodes
				if(nodes(head.left) == nodes(head.right))
					head.left = add(head.left, inc);		// if equal then left takes it
				else{
					if(full(head.left))	head.right = add(head.right, inc);		// else check if left subtree is full -> if yes: then right takes it
					else head.left = add(head.left, inc);		// else left takes it
				}
				if(head.right!=null && head.right.x > head.x)	swap(head, head.right);		// swap if either child is larger than parent as we recurse back
				if(head.left!=null && head.left.x > head.x)	swap(head, head.left);	
				return head;
			}
	}	// add
	
	static int nodes(node head){
		if(head == null)	return 0;
		return 1+ nodes(head.left)+ nodes(head.right);
	}	// #nodes

	static boolean full(node n){
		return nodes(n)==(Math.pow(2,depth(n))-1);
	}	// whether subtree is full
	
	static int depth(node n){
		if(n==null)	return 0;
		int rd = depth(n.right);
		int ld = depth(n.left);
		return rd>ld? rd+1 : ld+1;
	}	// depth
	
	static void traverse(node head, String path){
		if(head.left!=null)
			traverse(head.left, path.concat("0"));
		if(head.right!=null)
			traverse(head.right, path.concat("1"));
		System.out.printf(String.format("\t%-20s %d%n", path, head.x).replaceAll(" ", "."));
	}	// traverse paths 0->left, 1->right
	
	static void swap(node a, node b){
		int temp = a.x;
		a.x = b.x;
		b.x = temp;
	}	// swap elements
	
	static int findMax(node head){ return head.x; }
	// returns max element in heap
	
}