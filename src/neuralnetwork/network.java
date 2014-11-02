package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class network {

	public List<layer> layers;
	List<RealMatrix> weights;
	
	public network (){
		
	}
	
	public network(int numLayers, int [] numNeuronsInEachLayer){
		layers = new ArrayList<layer>();
		weights = new ArrayList<RealMatrix>();
		List<neuron> firstLayer = new ArrayList<neuron>();
		for (int i = 0; i<numNeuronsInEachLayer[0];i++){
			neuron neuron = new neuron (0);
			firstLayer.add(neuron);
		}
		layers.add(new layer(firstLayer,false));
		initializeLayers(numLayers, numNeuronsInEachLayer,firstLayer);		
	}
	
	public network(int numLayers, int [] numNeuronsInEachLayer, ArrayList<Double>input){
		layers = new ArrayList<layer>();
		weights = new ArrayList<RealMatrix>();
		List<neuron> firstLayer = new ArrayList<neuron>();
		for (int i = 0; i<input.size();i++){
			neuron neuron = new neuron (input.get(i));
			firstLayer.add(neuron);
		}
		layers.add(new layer(firstLayer,false));
		initializeLayers(numLayers, numNeuronsInEachLayer,firstLayer);		
	}
	
	public network(int numLayers, int [] numNeuronsInEachLayer, List<neuron> input){
		layers = new ArrayList<layer>();
		weights = new ArrayList<RealMatrix>();
		layers.add(new layer(input,false));
		initializeLayers(numLayers,numNeuronsInEachLayer,input);
	}
	
	public network(List<layer> layers){
		this.layers = layers;
		weights = new ArrayList<RealMatrix>();
		for (int i = 1;i<layers.size();i++){
			weights.add(layers.get(i).weights);
		}
	}
		
	public void initializeLayers(int numLayers, int [] numNeuronsInEachLayer,List<neuron> input){
		for(int i = 1; i<numLayers;i++){
			List<neuron> newLayer = new ArrayList<neuron>();
			List<Double> weights = new ArrayList<Double>();
			for (int j = 0; j<numNeuronsInEachLayer[i];j++){
				weights.add(Math.random());
			}
			for (int j = 0; j<numNeuronsInEachLayer[i];j++){
				neuron neuron = new neuron();
				neuron.inputs = layers.get(i-1).neurons;
				neuron.weights = weights;	
				newLayer.add(neuron);
			}
			layers.add(new layer(newLayer,true));
		}
		for (int i = 1;i<layers.size();i++){
			weights.add(layers.get(i).weights);
		}
	}
	
	public void PropogateForward(List<Double> inputs){
		for (neuron neuron: layers.get(0).neurons){
			neuron.value = inputs.get(0);
		}
		for (int i = 1; i<layers.size();i++){
			for (neuron neuron: layers.get(i).neurons){
				neuron.value = neuron.computeValueFromInput(neuron.inputs, neuron.weights);
			}
		}
	}
	
	public RealMatrix[] getErrorMatrices (List<Double> output){
		RealMatrix [] errorMatrices = new RealMatrix[layers.size()];
		double [][] levelErrorValues = new double[output.size()][1];
		for (int i = 0; i<output.size();i++){
			levelErrorValues[i][0] = layers.get(layers.size()-1).neurons.get(i).value-output.get(i);
		}
		errorMatrices[layers.size()-1] = MatrixUtils.createRealMatrix(levelErrorValues);
		
		for (int i = layers.size()-2;i>0;i--){
			List<neuron> neurons = layers.get(i).neurons;
			double [][] neuronValues = new double[neurons.size()][1];
			double [][] oneValues = new double [neurons.size()][1];
			for (int j = 0; j<neuronValues.length;j++){
				neuronValues[j][0] = neurons.get(j).value;
				oneValues[j][0] = 1;
			}
			RealMatrix neuronVals = MatrixUtils.createRealMatrix(neuronValues);
			RealMatrix oneVals = MatrixUtils.createRealMatrix(oneValues);
			RealMatrix firstPartOfErrorMatrix = (weights.get(i)).transpose().multiply(errorMatrices[i+1]);
			RealMatrix subtractedFromOnes = oneVals.subtract(neuronVals);
			double[][] neuronMultipliedOnes = new double[subtractedFromOnes.getRowDimension()][subtractedFromOnes.getColumnDimension()];
			for (int j = 0; i<subtractedFromOnes.getRowDimension();j++){
				neuronMultipliedOnes[j][0] = neuronVals.getEntry(j, 0)*(subtractedFromOnes.getEntry(j, 0));
			}
			double [][] finalErrorValues = new double[neuronMultipliedOnes.length][1];
			for (int j = 0; j<neuronMultipliedOnes.length;j++){
				finalErrorValues[j][0] = firstPartOfErrorMatrix.getEntry(j, 0)*neuronMultipliedOnes[j][0];
			}
			
			errorMatrices[i] = MatrixUtils.createRealMatrix(finalErrorValues);
		}
		return errorMatrices;
	}
	
	public List<RealMatrix> getGradientValue (List<Double> output, List<RealMatrix> currentGradients){
		RealMatrix [] errorMatrices = getErrorMatrices(output);
		
		for (int i = 0; i<weights.size();i++){
			List<neuron>neurons = layers.get(i).neurons;
			double [][] neuronValues = new double[layers.get(i).neurons.size()][1];
			for (int j = 0; j<neuronValues.length;j++){
				neuronValues[j][0] = neurons.get(j).value;
			}
			RealMatrix neuronVals = MatrixUtils.createRealMatrix(neuronValues);
			currentGradients.set(i, currentGradients.get(i).add(errorMatrices[i+1].multiply(neuronVals.transpose())));
		}		
		return currentGradients;
	}
}
 