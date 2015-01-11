package neuralnetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import utils.ImageReader;

public class GradientBasedLearning {
	
	public static List<RealMatrix> performGradientDescent (List<List<Double>>inputs, List<List<Double>> outputs, Network network, double alpha, double lambda){
		int m = inputs.size();
		List<RealMatrix> gradients = performBackPropogation(inputs,outputs,network);
		for (int j = 0; j<gradients.size();j++){
			if(j==0){
				gradients.set(j, gradients.get(j).scalarMultiply(1.0/m));
			}
			else{
				RealMatrix toSet = gradients.get(j).scalarMultiply(1.0/m).add(network.getWeights().get(j).scalarMultiply(lambda));
				gradients.set(j, toSet);
			}
		}
		List<RealMatrix> newWeights = new ArrayList<RealMatrix>();
		for (int j = 0; j<gradients.size();j++){
			newWeights.add(network.getWeights().get(j).subtract(gradients.get(j).scalarMultiply(alpha)));
		}			
		return newWeights;
	}
	
	public static List<RealMatrix> performBackPropogation (List<List<Double>> inputs,List<List<Double>> outputs, Network network){		
		List<RealMatrix> gradients = new ArrayList<RealMatrix>();
		for (int i = 0; i<network.getWeights().size();i++){
			RealMatrix curWeight = network.getWeights().get(i);
			RealMatrix curGradient = MatrixUtils.createRealMatrix(curWeight.getRowDimension(),curWeight.getColumnDimension());
			gradients.add(curGradient);
		}
		
		for (int i = 0; i<inputs.size();i++){
			List<Double> input = inputs.get(i);
			List<Double> output = outputs.get(i);
			network.PropogateForward(input);
			gradients = network.getGradientValue(output, gradients);
		}
		return gradients;
	}
	
	
}
