package operatingSystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	public MainFrame(String title) {
		super(title);
		
		setLayout(new BorderLayout());
		final JButton button = new JButton("Process Scheduler");
		final JButton button1 = new JButton("Memory Management");
		final JButton quit = new JButton("Quit");
		Container c = getContentPane();
		c.add(button, BorderLayout.WEST);
		c.add(button1, BorderLayout.EAST);
		c.add(quit, BorderLayout.SOUTH);
		
		ProcessScheduler scheduler = new ProcessScheduler();
		MemoryManagementUnit memory = new MemoryManagementUnit();
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				scheduler.main(null);	
			}
		});
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				memory.main(null);
			}	
		});
		
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}
}
