package neuralnetwork;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import utils.ImageReader;

public class Training {

	public Network network;
	private double alpha, lambda;
	private BufferedImage bimage;
	private List<List<Double>> inputs, outputs;  
	private Graphics2D g2d;
	private JPanel imagePanel;
	
	private String path = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\trainingset\\";
	private String logPath = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\Logs\\TrainingError.txt";
	
	public Training(Network network, int iterations, double alpha, double lambda,List<List<Double>> inputs, List<List<Double>> outputs){
		this.network = network;
		this.alpha = alpha;
		this.inputs = inputs;
		this.outputs = outputs;
		performGradientDescent (iterations,alpha,lambda);
	}
	
	public Training(Network network, double maxError, double alpha, double lambda,List<List<Double>> inputs, List<List<Double>> outputs){
		this.network = network;
		this.alpha = alpha;
		this.inputs = inputs;
		this.outputs = outputs;
		performGradientDescent (maxError,alpha,lambda);		
	}
	
	public Training(Network network, int iterations, double alpha, double lambda){
		this.network = network;
		this.alpha = alpha;
		this.lambda = lambda;
		setUpInputsOutputs();
		performGradientDescent (iterations,alpha,lambda);
	}
	
	public Training (Network network, double alpha, double lambda, String path){
		this.network = network;
		this.alpha = alpha;
		this.lambda = lambda;
		this.path = path;
	}
	
	private void setUpErrorPlot(){
		JFrame f = new JFrame();
	    f.setLayout(new BorderLayout());
	    imagePanel = new JPanel(){	    	
	    	@Override
	    	protected void paintComponent(Graphics g){
	    		super.paintComponent(g);
	    		g.drawImage(bimage,0,0,null);
	    	}
	    };
	    imagePanel.setSize(200, 200);
	    f.add(imagePanel,BorderLayout.SOUTH);
	    f.pack();
	    f.setSize(200, 200);
	    f.setVisible(true);
	}
	
	private void setUpInputsOutputs(){
		File folder = new File(path);
		File [] listOfFiles = folder.listFiles();
		
		inputs = new ArrayList<List<Double>>();
		outputs = new ArrayList<List<Double>>();
		for (int i = 0; i<listOfFiles.length;i++){
			ImageReader imgReader = new ImageReader(listOfFiles[i].getAbsolutePath());
			imgReader.readFromImage();
			List<Double> input = imgReader.values;
			List<Double> output = imgReader.solution;
			inputs.add(input);
			outputs.add(output);
		}
	}
	
	private static double calculateCost(List<List<Double>> inputs, List<List<Double>> outputs, Network network){
		int m = inputs.size();
		double cost = 0;
		for (int i = 0; i<m; i++){
			List<Double> input = inputs.get(i);
			List<Double> output = outputs.get(i);
			network.PropogateForward(input);
			List<Neuron> hypothesisValues = network.getLayers().get(network.getLayers().size()-1).neurons;
			for (int j = 0; j<output.size(); j++){
				double curValue = hypothesisValues.get(j).getValue();
				cost += -1*output.get(j)*Math.log(curValue) - (1-output.get(j))*Math.log(1-curValue);
			}
		}		
		return cost/m;
	}
	
	private void performGradientDescent (int iterations, double alpha, double lambda){
		try {		
			BufferedWriter out = new BufferedWriter(new FileWriter(logPath));
			for (int i = 0; i<iterations; i++){
				List<RealMatrix> newWeights = GradientBasedLearning.performGradientDescent(inputs, outputs, network, alpha, lambda);		
				network.updateWeights(newWeights);			
				double cost = calculateCost(inputs, outputs, network);
				out.write(cost + "" + "\n");
//				g2d.setColor(Color.black);
//				g2d.fillOval(i,(int)cost,2,2);
//				new Runnable() {
//				
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						imagePanel.repaint();
//					}
//				}.run();
			}
			out.close();
		} catch (IOException e){
			
		}
	}
	
	private void performGradientDescent (double maxError, double alpha, double lambda){
		try {		
			BufferedWriter out = new BufferedWriter(new FileWriter(logPath));
			double cost = 100000;
			while (cost > maxError){
				List<RealMatrix> newWeights = GradientBasedLearning.performGradientDescent(inputs, outputs, network, alpha, lambda);		
				network.updateWeights(newWeights);			
				cost = calculateCost(inputs, outputs, network);
				out.write(cost + "" + "\n");
			}
			out.close();
		} catch (IOException e){
			
		}
	}
	
	public void outputErrors (List<List<Double>> inputs, List<List<Double>> outputs, List<List<Double>> testInputs, List<List<Double>> testOutputs){
		
		double totalTraining = 0, totalTest = 0;
		for (int i = 0; i < inputs.size(); i++){
			int curValue = -1;
			List<Double> curOutputs = outputs.get(i);
			for (int j = 0; j < curOutputs.size(); j++){
				if(curOutputs.get(j)==1){
					curValue = j;
				}
			}
			if (network.getValue(inputs.get(i))==curValue){
				totalTraining++;
			}
		}
		System.out.println(totalTraining/inputs.size());
		for (int i = 0; i < testInputs.size(); i++){
			int curValue = -1;
			List<Double> curOutputs = testOutputs.get(i);
			for (int j = 0; j < curOutputs.size(); j++){
				if(curOutputs.get(j)==1){
					curValue = j;
				}
			}
			if (network.getValue(testInputs.get(i))==curValue){
				totalTest++;
			}
		}
		System.out.println(totalTest/testInputs.size());
		
	}
	
}
