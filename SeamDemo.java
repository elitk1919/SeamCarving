/**Driver for your code**/


public class SeamDemo {

	public static void main(String[] args) {

		int delay = 1;
				
		UWECImage im = new UWECImage("cat.jpg");
			
		im.openNewDisplayWindow();	

		Seam s = new Seam();//you write this
		//watch it shrink vertically!
		for (int i = 0; i < 150; i++) {
			//System.out.println("In main loop");
			//s.showVerticalSeam(im);
			//im.repaintCurrentDisplayWindow();
			/*
			s.verticalSeamShrink(im);//you write this
			im.repaintCurrentDisplayWindow();	
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
			*/
		}
		//now horizontally!
		for (int i = 0; i < 100; i++) {

			s.horizontalSeamShrink(im);//you write this
			
			im.repaintCurrentDisplayWindow();	
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	
	}

}
