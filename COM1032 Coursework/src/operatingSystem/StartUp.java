package operatingSystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class StartUp extends JFrame {
		public StartUp(String title) {
			super(title);
			
			setLayout(new BorderLayout());
			final JButton start = new JButton("Start");
			final JTextArea text = new JTextArea();
			Container c = getContentPane();
			c.add(text, BorderLayout.NORTH);
			c.add(start, BorderLayout.SOUTH);
			String ram = "16GB";
			int numProcessors = 1;
			
			Bootup boot = new Bootup();
			
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					text.append("Starting...\n\n");
					text.append("OS Simulator\n");
					text.append("Version 1.0\n\n");
					text.append("RAM - "+ ram + "\n");
					text.append("Processors - "+ numProcessors + "\n");
					ActionListener taskPerformer1 = new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							setVisible(false);
							boot.main(null);
					      	}
					  	};
					  	Timer timer = new Timer(4000, taskPerformer1);
					  	timer.start();
					  	timer.setRepeats(false);
					  	}
				});					          
	}
}
