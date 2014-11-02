package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class neuron {

	List<neuron> inputs;
	List<Double> weights;
	
	public double value;
	
	public neuron (){
		
	}
	
	public neuron (double value){
		inputs = new ArrayList<neuron>();
		weights = new ArrayList<Double>();
		this.value = value;
	}
	
	public neuron (List<neuron> inputs, List<Double> weights){
		this.inputs = inputs;
		this.weights = weights;
		if(weights.size()==inputs.size()+1)
			value = computeValueFromInput(inputs, weights);
		else 
			value = 0;		
	}
	
	public double computeValueFromInput(List<neuron>inputs,List<Double>weights){
		double dotProduct = 0;
		for (int i = 0; i<weights.size();i++){
			if(i==0)
				dotProduct += weights.get(i);
			else{
				dotProduct += inputs.get(i-1).value*weights.get(i);
			}
		}
		return sigmoidFunction(dotProduct);
	}
	
	private double sigmoidFunction(double z){
		double sigResult = 1/(1+Math.exp(-1*z));
		return sigResult;
	}
	
}
