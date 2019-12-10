import java.io.BufferedInputStream;
import java.util.Scanner;

/**Driver for your code**/

public class SeamDemo {
	
	static int vertIterations = 150;
	static int horizIterations = 100;
	static int seamDelay = 10;
	static int iterDelay = 1;
	static String file = "cat.jpg";
	static String outFile = "";
	

	public static void main(String[] args) {
		if (args.length == 1) {
			if (args[0].equals("--help") || args[0].equals("-h")) {
				System.out.println("Seam Carving");
				System.out.println("This program shrinks a JPEG image one pixel at a time,");
				System.out.println("both vertically and horizontally, based on pixel \'energy\'.");
				System.out.println("Syntax:\n\tjava SeamDemo --file <input file path> [options]");
				System.out.println("\nOptions and parameters:");
				System.out.println("\t-h | --help:\n\t\tDisplays help for the program (this screen)\n");
				System.out.println("\t--file:\n\t\tSpecifies the input file. If no argument is given, the sample image cat.jpg will be uses\n");
				System.out.println("\t--vertical-iterations:\n\t\tThis argument specifies the amount of times the algorithm is used to shrink the input image vertically.");
				System.out.println("\t\tThis argument must be less than the width of the image. If no argument is supplied it will default to 150 iterations\n");	
				System.out.println("\t--horizontal-iterations:\n\t\tThis argument specifies the amount of times the algorithm is used to shrink the input image horizontally.");
				System.out.println("\t\tThis argument must be less than the height of the image. If no argument is supplied it will default to 150 iterations\n");
				System.out.println("\t--seam-delay:\n\t\tThis argument represents the amount of time (in milliseconds) that the program delays");
				System.out.println("\t\tafter highlighting the seam on the image being modified. For smaller images, this is helpful if the program is moving");
				System.out.println("\t\ttoo fast to observe the seam being painted. This default value is 10 milliseconds.\n");
				System.out.println("\t--iteration-delay:\n\t\tThis parameter controls the delay between seam shrinking iterations (in milliseconds).");
				System.out.println("\t\tThe default value is 1 millisecond\n");
				System.out.println("\t--output-file\n\t\tThis parameter is used to save the modified image as a png image file.");
				System.out.println("\t\tThe supplied filename must have a .png file extension.\n\t\tIf no argument is supplied, the modified file will not be saved.\n");
				return;
			}
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--vertical-iterations")) vertIterations = Integer.parseInt(args[i+1]);
			if (args[i].equals("--horizontal-iterations")) horizIterations = Integer.parseInt(args[i+1]);
			if (args[i].equals("--seam-delay")) seamDelay = Integer.parseInt(args[i+1]);
			if (args[i].equals("--iteration-delay")) iterDelay = Integer.parseInt(args[i+1]);
			if (args[i].equals("--file")) file = args[i+1];
			if (args[i].equals("--output-file")) outFile = args[i+1];
		}
				
		UWECImage im = new UWECImage(file);
		
		if (im.getWidth() < vertIterations) {
			System.out.println("Image is too small for that many horizontal iterations");
			System.out.println("Please select a new image or use less horizontal iterations");
			return;
			
		}
		
		if (im.getHeight() < horizIterations) {
			System.out.println("Image is too small for that many vertical iterations");
			System.out.println("Please select a new image or use less vertical iterations");	
			return;
		}
			
		im.openNewDisplayWindow();	

		Seam s = new Seam();//you write this
		//watch it shrink vertically!
		s.setDelay(seamDelay);
		for (int i = 0; i < vertIterations; i++) {			
			s.verticalSeamShrink(im);//you write this
			im.repaintCurrentDisplayWindow();	
			try {
				Thread.sleep(iterDelay);
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
			
		}
		//now horizontally!
		for (int i = 0; i < horizIterations; i++) {

			s.horizontalSeamShrink(im);//you write this
			
			im.repaintCurrentDisplayWindow();	
			try {
				Thread.sleep(iterDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		if (!outFile.equals("")) {
			im.write(outFile);
		}
	
	}

}
