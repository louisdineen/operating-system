package operatingSystem;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Bootup {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new MainFrame("OS Simulator");
				frame.setSize(320, 150);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
