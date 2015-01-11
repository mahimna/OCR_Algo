package userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

import utils.Constants;
import utils.DefaultTrainingSetReader;
import utils.NeuralNetworkUtils;
import neuralnetwork.Training;
import neuralnetwork.Network;

public class TrainingScreen extends JFrame {

	private JPanel contentPane;
	private String destinationFolder;
	
	/**
	 * Create the frame.
	 */
	public TrainingScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel textLabelPanel = new JPanel();
		textLabelPanel.setLayout(new BoxLayout(textLabelPanel,BoxLayout.X_AXIS));
		JLabel textLabel = new JLabel("Neural Network Training");
		textLabelPanel.add(textLabel);
		contentPane.add(textLabelPanel);
		
		JPanel hiddenNeuronsInput = new JPanel();
		hiddenNeuronsInput.setLayout(new BoxLayout(hiddenNeuronsInput,BoxLayout.X_AXIS));		
		hiddenNeuronsInput.setMaximumSize(new Dimension(450,25));
		final JLabel hNeuronsTextLabel = new JLabel("Choose # of hidden units: ");
		hiddenNeuronsInput.add(hNeuronsTextLabel);
		final JTextField hNeuronsTextField = new JTextField(10);
		hiddenNeuronsInput.add(hNeuronsTextField);
		contentPane.add(hiddenNeuronsInput);
		
		JPanel methodologyInput = new JPanel();
		methodologyInput.setLayout(new BoxLayout(methodologyInput,BoxLayout.X_AXIS));
		methodologyInput.setMaximumSize(new Dimension(450,25));
		JLabel methodologyTextLabel = new JLabel("Choose Methodology: ");
		methodologyInput.add(methodologyTextLabel);
		String [] methods = {"Gradient Descent"};
		JComboBox<String> methodologyTypes = new JComboBox(methods);
		methodologyInput.add(methodologyTypes);
		contentPane.add(methodologyInput);
		
		JPanel trainingSetInput = new JPanel();
		trainingSetInput.setLayout(new BoxLayout(trainingSetInput, BoxLayout.X_AXIS));
		trainingSetInput.setMaximumSize(new Dimension(450,25));
		JLabel trainingSetTextLabel = new JLabel("Choose training set: ");
		trainingSetInput.add(trainingSetTextLabel);
		// TODO: make a constants file and add the path to the default training set on there
		final JTextField trainingSetEditText = new JTextField(Constants.BASE_PATH + Constants.DEFAULT_TRAINING_SET,10);
		trainingSetEditText.setEditable(false);
		trainingSetInput.add(trainingSetEditText);
		JButton chooseTrainingSet = new JButton("Choose");
		trainingSetInput.add(chooseTrainingSet);
		contentPane.add(trainingSetInput);
		
		final JFileChooser fileChooser = new JFileChooser();
		chooseTrainingSet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue =  fileChooser.showOpenDialog(TrainingScreen.this);
				
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					String filePath = file.getAbsolutePath();
					trainingSetEditText.setText(filePath);
				}
			}
		});
		
		JPanel trainingSetLabelInput = new JPanel();
		trainingSetLabelInput.setLayout(new BoxLayout(trainingSetLabelInput, BoxLayout.X_AXIS));
		trainingSetLabelInput.setMaximumSize(new Dimension(450,25));
		JLabel trainingSetLabelTextLabel = new JLabel("Choose training set labels: ");
		trainingSetLabelInput.add(trainingSetLabelTextLabel);
		final JTextField trainingSetLabelEditText = new JTextField(Constants.BASE_PATH + Constants.DEFAULT_LABEL_SET,10);
		trainingSetLabelEditText.setEditable(false);
		trainingSetLabelInput.add(trainingSetLabelEditText);
		JButton chooseTrainingSetLabel = new JButton("Choose");
		trainingSetLabelInput.add(chooseTrainingSetLabel);
		contentPane.add(trainingSetLabelInput);				
		chooseTrainingSetLabel.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue =  fileChooser.showOpenDialog(TrainingScreen.this);
				
				if(returnValue == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					String filePath = file.getAbsolutePath();
					trainingSetLabelEditText.setText(filePath);
				}
			}
		});
		
		
		JPanel saveFilePanel = new JPanel();
		saveFilePanel.setLayout(new BoxLayout(saveFilePanel, BoxLayout.X_AXIS));
		saveFilePanel.setMaximumSize(new Dimension(450,25));
		final JLabel saveFileTextLabel = new JLabel("Write file name and choose folder: ");
		saveFilePanel.add(saveFileTextLabel);
		final JTextField fileNameTextField = new JTextField("File Name",10);
		saveFilePanel.add(fileNameTextField);
		contentPane.add(saveFilePanel);						
		
		JPanel trainPanel = new JPanel();
		trainPanel.setLayout(new BoxLayout(trainPanel,BoxLayout.X_AXIS));
		JButton startTrain = new JButton("Train");
		trainPanel.add(startTrain);
		startTrain.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int numberInHiddenLayer = Integer.parseInt(hNeuronsTextField.getText());
				int [] numNeuronsInEachLayer = {784,numberInHiddenLayer,10};
				Network network = new Network(3,numNeuronsInEachLayer);
				
				// TODO: Get the images and text files and do stuff accordingly
				DefaultTrainingSetReader defaultTraining = new DefaultTrainingSetReader(trainingSetEditText.getText(),trainingSetLabelEditText.getText(),1000);
				DefaultTrainingSetReader.printDatasetToFile(9, defaultTraining.getInputs(), defaultTraining.getOutputs());
				
//				Training trainer = new Training(network,(int) 2000,1,0,defaultTraining.getInputs(),defaultTraining.getOutputs());
//				network = trainer.network;
//				trainer.outputErrors(defaultTraining.getInputs(),defaultTraining.getOutputs(),defaultTraining.getTestInputs(),defaultTraining.getTestOutputs());
//				String path = Constants.BASE_PATH + Constants.SAVED_NETWORK_DATA + "\\" + fileNameTextField.getText() + ".txt";
//				NeuralNetworkUtils.saveNetwork(path,network);		
				
			}			
		});
		contentPane.add(trainPanel);
	}

}
