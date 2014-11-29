package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

public class neuron {

	private List<neuron> inputs;
	private double value;
	
	public neuron (){}
	
	public neuron (double value){
		inputs = new ArrayList<neuron>();
		this.value = value;
	}
	
	public neuron (List<neuron> inputs){
		this.inputs = inputs;
	}
	
	public double computeValueFromInput(double [] weights){
		double dotProduct = 0;
		for (int i = 0; i<weights.length; i++){
			dotProduct += inputs.get(i).value*weights[i];
		}
		return sigmoidFunction(dotProduct);
	}
	
	public double computeValueFromInput(List<neuron>inputs,double[]weights){
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
	
	public List<neuron> getInputs(){
		return inputs;
	}
	
	public void setInputs(List<neuron> inputs){
		this.inputs = inputs;
	}
}
