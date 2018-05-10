import java.io.*;
import java.util.Scanner;
class command{
	public static void main(String[]args){
		if(args.length==0)	System.out.println("need to give an input text file as args");
		else
			try{
				int counter=0;
				AVLTree t = new AVLTree();
				BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				if(args.length==2 && Integer.parseInt(args[1])!=0){
					for(int i=0; i<Integer.parseInt(args[1]) && (t.satz = reader.readLine()) != null; ++i){
						t.doIt(++counter);
						if(t.root!=null)	t.traverse(t.root, "");
						System.out.println();
					}
					if(t.root!=null)	
						t.results(Integer.parseInt(args[1]));
				}
				while((t.satz = reader.readLine()) != null)
					t.doIt(++counter);
					if(t.root!=null)	t.results(-1);
					Scanner yourIn = new Scanner(System.in);
					System.out.print("\tshow traversals with paths? (y/X)");
					char yesOrNo = yourIn.next().charAt(0);
					if(yesOrNo=='y')	
						if(t.root!=null)	t.traverse(t.root, "");
				reader.close();
			}catch(IOException e){
				System.out.println("i/o exc "+ e);
			}catch(NumberFormatException e){
				System.out.println("num format exc "+ e);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("args error"+ e);
			}
			catch(NullPointerException e){	System.out.println("null ptr exc "+ e);	}
	}	// main
}