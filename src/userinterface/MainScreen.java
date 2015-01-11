package userinterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import utils.DefaultTrainingSetReader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainScreen {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//DefaultTrainingSetReader reader = new DefaultTrainingSetReader(5);
	}

	/**
	 * Create the application.
	 */
	public MainScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton trainButton = new JButton("Train");
		trainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TrainingScreen trainScreen = new TrainingScreen();
				//AddToTrainingSetScreen trainScreen = new AddToTrainingSetScreen();
				trainScreen.setVisible(true);
				
			}
		});
		
		JButton classifyButton = new JButton("Classify");
		classifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClassifyingScreen classifyScreen = new ClassifyingScreen();
				classifyScreen.setVisible(true);
			}
		});
		
		panel.add(trainButton);
		panel.add(classifyButton);
	}

}
