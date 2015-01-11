package userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;
import javax.swing.JLabel;

import neuralnetwork.Training;
import neuralnetwork.Network;
import utils.ImageConversionAndSave;
import utils.ImageReader;

import java.awt.Font;
import java.io.File;

public class AddToTrainingSetScreen extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSave, btnTrain, btnClear;
	private PaintComponent paintPanel;

	/**
	 * Create the frame.
	*/
	
	public AddToTrainingSetScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		paintPanel = new PaintComponent();
		paintPanel.setVisible(true);
		paintPanel.setBounds(10, 52, 100, 100);
		contentPane.add(paintPanel);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(313, 52, 89, 49);
		btnSave.addActionListener(this);
		contentPane.add(btnSave);
		
		textField = new JTextField();
		textField.setBounds(199, 71, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblSavingPanel = new JLabel("Saving Panel");
		lblSavingPanel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblSavingPanel.setBounds(152, 11, 139, 23);
		contentPane.add(lblSavingPanel);
		
		JLabel lblDrawingArea = new JLabel("Drawing Area:");
		lblDrawingArea.setBounds(10, 33, 76, 14);
		contentPane.add(lblDrawingArea);
		
		JLabel lblSolutionValue = new JLabel("Solution Value:");
		lblSolutionValue.setBounds(199, 52, 89, 14);
		contentPane.add(lblSolutionValue);
		
		JLabel lblTrainingPanel = new JLabel("Training Panel");
		lblTrainingPanel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblTrainingPanel.setBounds(137, 165, 139, 23);
		contentPane.add(lblTrainingPanel);
		
		btnTrain = new JButton("Train");
		btnTrain.setBounds(10, 202, 89, 23);
		btnTrain.addActionListener(this);
		contentPane.add(btnTrain);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(this);
		btnClear.setBounds(114, 52, 65, 23);
		contentPane.add(btnClear);
		setVisible(true);
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnSave){
			ImageConversionAndSave save = new ImageConversionAndSave(paintPanel.curImage, Integer.parseInt(textField.getText()),10);
			save.saveToFile();
		}
		else if (e.getSource()==btnTrain){
			// Do stuff here
			int [] numNeuronsInEachLayer = {100,9,1};
			Network network = new Network(3,numNeuronsInEachLayer);
			Training trainer = new Training(network,5000,0.01,0.05);
			String path = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\trainingset\\";
			File folder = new File(path);
			File lastFile = folder.listFiles()[folder.listFiles().length-1];
			ImageReader imgReader = new ImageReader(lastFile.getAbsolutePath());
			network.PropogateForward(imgReader.values);
			System.out.println(network.getLayers().get(network.getLayers().size()-1).neurons.get(0).getValue());
		}
		else if (e.getSource()==btnClear){
			paintPanel.refreshImage();
		}
	}
}
