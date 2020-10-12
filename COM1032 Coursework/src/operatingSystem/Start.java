package operatingSystem;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Start {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new StartUp("OS Simulator");
				frame.setSize(300, 180);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}