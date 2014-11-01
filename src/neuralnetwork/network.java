package neuralnetwork;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class network {

	List<layer> layers;
	List<RealMatrix> weights;
	
	public network (){
		
	}
	
	public network(List<layer> layers){
		this.layers = layers;
		for (int i = 1;i<layers.size();i++){
			weights.add(layers.get(i).weights);
		}
	}
}
