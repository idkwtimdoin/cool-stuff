class enig{
	public static void main(String[]args){
		if(args.length>=2){
			Tree t = new Tree(args[0]);
			t.store(t.list.curr, "");
			String arg2 = "";
			for(int i=1; i<args.length; ++i){
				arg2 = arg2.concat(args[i]);
				if(i+1!=args.length)	arg2 = arg2+' ';
			}
			// t.trav(t.list.curr, "");
			t.handle(arg2);
		}
		else
			System.out.println("\t*java enig <essaytext.txt> <msg.txt/binarymsg.txt>\n\t*or java enig <essaytext.txt> <msg/binarymsg>");
	}	// main
}