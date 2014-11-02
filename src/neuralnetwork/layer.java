package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class layer {

	// Assuming associative array of neural network	
	public List<neuron> neurons;
	public RealMatrix weights;
	
	public layer (){
		
	}
	
	public layer(List<neuron> neurons, boolean getWeights){
		this.neurons = neurons;
		if(getWeights)
			weights = getWeightValues(neurons);	
	}
	
	private RealMatrix getWeightValues(List<neuron> neurons){
		List<List<Double>> weightValues = new ArrayList<List<Double>>();
		for (neuron neuron: neurons){
			weightValues.add(neuron.weights);
		}
		double [][] transformedWeightValues = new double[neurons.size()][neurons.get(0).inputs.size()+1];
		for (int i = 0; i<neurons.size();i++){
			List<Double> curWeights = neurons.get(i).weights;
			for (int j = 0; j<curWeights.size();j++){
				transformedWeightValues[i][j] = curWeights.get(j);
			}
		}
		return MatrixUtils.createRealMatrix(transformedWeightValues);
	}
}
