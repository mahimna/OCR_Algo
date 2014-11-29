package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class layer {

	// Assuming associative array of neural network	
	public List<neuron> neurons;
	
	public layer (){
		
	}
	
	public layer(List<neuron> neurons){
		this.neurons = neurons;		
	}
}
