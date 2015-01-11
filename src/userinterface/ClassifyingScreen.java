package userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.Constants;
import utils.ImageConversionAndSave;
import utils.NeuralNetworkUtils;
import neuralnetwork.Network;

public class ClassifyingScreen extends JFrame {

	private JPanel contentPane;
	private PaintComponent paintComponent;
	private Network network;
	
	String defaultNetworkInformation = Constants.BASE_PATH + Constants.DEFAULT_NETWORK_INFORMATION;
	
	public ClassifyingScreen() {
		
		final JFileChooser fileChooser = new JFileChooser();
		network = NeuralNetworkUtils.readNetwork(defaultNetworkInformation);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel loadNetworkPanel = new JPanel();
		loadNetworkPanel.setLayout(new BoxLayout(loadNetworkPanel, BoxLayout.X_AXIS));
		JLabel loadNetworkLabel = new JLabel("Choose network information: ");
		loadNetworkPanel.add(loadNetworkLabel);
		final JTextField loadNetworkEditText = new JTextField(defaultNetworkInformation,10);
		loadNetworkEditText.setEditable(false);
		loadNetworkPanel.add(loadNetworkEditText);
		JButton chooseNetworkButton = new JButton("Choose");
		loadNetworkPanel.add(chooseNetworkButton);
		contentPane.add(loadNetworkPanel,BorderLayout.NORTH);				
		chooseNetworkButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue =  fileChooser.showOpenDialog(ClassifyingScreen.this);				
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					String filePath = file.getAbsolutePath();
					loadNetworkEditText.setText(filePath);
					network = NeuralNetworkUtils.readNetwork(filePath);
				}
			}
		});	
		
		paintComponent = new PaintComponent();
		paintComponent.setPreferredSize(new Dimension(112, 112));
		contentPane.add(paintComponent, BorderLayout.WEST);
		
		JPanel classifyPanel = new JPanel();
		classifyPanel.setLayout(new BoxLayout(classifyPanel,BoxLayout.X_AXIS));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));
		classifyPanel.add(buttonsPanel);
		
		JButton classifyButton = new JButton("Classify");
		buttonsPanel.add(classifyButton);
		JButton clearButton = new JButton("Clear");
		buttonsPanel.add(clearButton);
		
		final JLabel classifyValue = new JLabel("-1");
		classifyPanel.add(classifyValue);
		contentPane.add(classifyPanel,BorderLayout.CENTER);
		
		classifyButton.addActionListener(new ActionListener (){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImageConversionAndSave converter = new ImageConversionAndSave(paintComponent.curImage, 0, 4);
				converter.saveToFile();				
				double [][] imageValues = converter.matrixValues;
				List<Double> input = new ArrayList<Double>();
				for (int i = 0; i< 28; i++){
					for (int j = 0; j<28; j++){
						input.add(imageValues[i][j]);
					}
				}
				
				int value = network.getValue(input);
				classifyValue.setText("" + value);
				
			}
			
		});
		
		clearButton.addActionListener(new ActionListener (){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paintComponent.refreshImage();
				
			}
			
		});
		
		pack();
	}

}
