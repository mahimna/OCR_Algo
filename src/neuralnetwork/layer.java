package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Layer {

	// Assuming associative array of neural network	
	public List<Neuron> neurons;
	
	public Layer (){
		
	}	
	public Layer(List<Neuron> neurons){
		this.neurons = neurons;		
	}
}
