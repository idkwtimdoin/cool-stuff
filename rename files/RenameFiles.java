/*
 * place in directory where the files to be renamed reside
 * command line args: java RenameFiles extension [starting integer] [initial word]
 * for safety make a copy of the directory before performing the operation
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class RenameFiles{
	public static void main(String[]args){	
		String ext="";
		if(args.length>0)
			ext = args[0];
		else{
			System.out.println("CLI args: java RenameFiles extension [starting integer] [initial word]");
			System.exit(0);
		}
		int counter = args.length>1?Integer.parseInt(args[1]):0;
		String init = args.length>2?args[2]:"";
		File dir = new File(System.getProperty("user.dir"));
		if (dir.isDirectory()) {
			for (final File f : dir.listFiles()) {
				try {
					if(f.getName().matches(String.format(".*.").concat(ext))){
						File newfile =new File(String.format("%s%d.%s", init, ++counter, ext));
						System.out.println(f.getName()+ " changed to "+ String.format("%s%d.%s", init, counter, ext));
						f.renameTo(newfile);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("\t"+counter+" files renamed");
		}
		else
			System.out.println("not a dir");
	}	// main
}