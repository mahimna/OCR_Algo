package neuralnetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import utils.ImageReader;

public class Training {

	public network network;
	private double alpha, lambda;
		
	private final String path = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\trainingset\\";
	
	public Training(network network, int iterations, double alpha, double lamda){
		this.network = network;
		this.alpha = alpha;
		this.lambda = lamda;
		performGradientDescent (iterations,alpha,lambda);
	}
	
	private void performGradientDescent (int iterations, double alpha, double lambda){
		for (int i = 0; i<iterations; i++){
			File folder = new File(path);
			File [] listOfFiles = folder.listFiles();
			int m = listOfFiles.length;
			List<RealMatrix> gradients = performBackPropogation(listOfFiles);
			for (int j = 0; j<gradients.size();j++){
				if(j==0){
					gradients.set(j, gradients.get(j).scalarMultiply(1.0/m));
				}
				else{
					RealMatrix toSet = gradients.get(j).scalarMultiply(1.0/m).add(network.weights.get(j).scalarMultiply(lambda));
					gradients.set(j, toSet);
				}
			}
			for (int j = 0; j<gradients.size();j++){
				network.weights.set(j, network.weights.get(j).subtract(gradients.get(j).scalarMultiply(alpha)));
			}
		}
	}
	
	private List<RealMatrix> performBackPropogation (File [] listOfFiles){
		
		List<RealMatrix> gradients = new ArrayList<RealMatrix>();
		for (int i = 0; i<network.weights.size();i++){
			RealMatrix curWeight = network.weights.get(i);
			RealMatrix curGradient = MatrixUtils.createRealMatrix(curWeight.getColumnDimension(),curWeight.getRowDimension());
			gradients.add(curGradient);
		}
		
		for (int i = 0; i<listOfFiles.length;i++){
			ImageReader imgReader = new ImageReader(listOfFiles[i].getAbsolutePath());
			imgReader.readFromImage();
			List<Double> input = imgReader.values;
			List<Double> output = imgReader.solution;
			network.PropogateForward(input);
			gradients = network.getGradientValue(output, gradients);
		}
		return gradients;
	}
	
	
}
