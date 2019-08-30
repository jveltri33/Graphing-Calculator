
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		double[][] array1 = {{2, 1, 7, -7, 2},
							{-3, 4, -5, -6, 3},
							{1, 1, 4, -5, 2}};
		double[][] array2 = {{1, 2, 3},
							 {4, 5, 6},
							 {7, 8, 10}};
		Matrix A = new Matrix(array2);
		//System.out.println(A.inverse());
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame window = new GraphingCalculator();
				window.setVisible(true);
			}
		});
	}
}