package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

	private List<Neuron> inputs;
	private double value;
	
	public Neuron (){}
	
	public Neuron (double value){
		inputs = new ArrayList<Neuron>();
		this.value = value;
	}
	
	public Neuron (List<Neuron> inputs){
		this.inputs = inputs;
	}
	
	public double computeValueFromInput(double [] weights){
		double dotProduct = 0;
		for (int i = 0; i<weights.length; i++){
			dotProduct += inputs.get(i).value*weights[i];
		}
		return sigmoidFunction(dotProduct);
	}
	
	public double computeValueFromInput(List<Neuron>inputs,double[]weights){
		double dotProduct = 0;
		for (int i = 0; i<weights.length;i++){
			dotProduct += inputs.get(i).value*weights[i];
		}
		return sigmoidFunction(dotProduct);
	}
	
	private double sigmoidFunction(double z){
		double sigResult = 1/(1+Math.exp(-1*z));
		return sigResult;
	}
	
	public double getValue(){
		return value;
	}
	
	public void setValue(double value){
		this.value = value;
	}	
	
	public List<Neuron> getInputs(){
		return inputs;
	}
	
	public void setInputs(List<Neuron> inputs){
		this.inputs = inputs;
	}
}
