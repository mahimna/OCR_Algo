package neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Network {

	private List<Layer> layers;
	private List<RealMatrix> weights;
	
	public Network (){
		
	}
	
	public Network(int numLayers, int [] numNeuronsInEachLayer){
		layers = new ArrayList<Layer>();
		weights = new ArrayList<RealMatrix>();
		List<Neuron> firstLayer = new ArrayList<Neuron>();
		for (int i = 0; i<numNeuronsInEachLayer[0]+1;i++){
			Neuron neuron;
			if(i==0)
				neuron = new Neuron(1);
			else 
				neuron = new Neuron (Math.random());
			firstLayer.add(neuron);
		}
		layers.add(new Layer(firstLayer));
		initializeLayers(numLayers, numNeuronsInEachLayer,firstLayer);		
	}
	
	public Network(int numLayers, int [] numNeuronsInEachLayer, ArrayList<Double>input){
		layers = new ArrayList<Layer>();
		weights = new ArrayList<RealMatrix>();
		List<Neuron> firstLayer = new ArrayList<Neuron>();
		for (int i = 0; i<input.size()+1;i++){
			Neuron neuron;
			if(i==0)
				neuron = new Neuron(1);
			else
				neuron = new Neuron (input.get(i-1));
			firstLayer.add(neuron);
		}
		layers.add(new Layer(firstLayer));
		initializeLayers(numLayers, numNeuronsInEachLayer,firstLayer);		
	}
	
	public Network(int numLayers, int [] numNeuronsInEachLayer, List<Neuron> input){
		layers = new ArrayList<Layer>();
		weights = new ArrayList<RealMatrix>();
		input.add(0, new Neuron(1));
		layers.add(new Layer(input));
		initializeLayers(numLayers,numNeuronsInEachLayer,input);
	}
	
	public void initializeLayers(int numLayers, int [] numNeuronsInEachLayer,List<Neuron> input){
		for(int i = 1; i<numLayers;i++){
			List<Neuron> newLayer = new ArrayList<Neuron>();
			if(i==numLayers-1){
				double [][] weightValues = new double [numNeuronsInEachLayer[i]][numNeuronsInEachLayer[i-1]+1];
				for (int j = 0; j<numNeuronsInEachLayer[i];j++){
					Neuron neuron = new Neuron();
					neuron.setInputs(layers.get(i-1).neurons);
					for (int k = 0;k<(numNeuronsInEachLayer[i-1]+1);k++){						
						weightValues[j][k] = -1+Math.random()*2;
					}
					newLayer.add(neuron);
				}
				RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightValues);
				weights.add(weightMatrix);
			}else {
				double [][] weightValues = new double [numNeuronsInEachLayer[i]][numNeuronsInEachLayer[i-1]+1];
				for (int j = 0; j<numNeuronsInEachLayer[i]+1;j++){
					Neuron neuron = new Neuron();
					neuron.setInputs(layers.get(i-1).neurons);
					if(j==0){
						neuron.setValue(1);
					}
					else {
						for (int k = 0;k<(numNeuronsInEachLayer[i-1]+1);k++){						
							weightValues[j-1][k]= -1+Math.random()*2;
						}					
					}
					newLayer.add(neuron);
				}
				RealMatrix weightMatrix = MatrixUtils.createRealMatrix(weightValues);
				weights.add(weightMatrix);
			}
			layers.add(new Layer(newLayer));
		}
	}
	
	public void PropogateForward(List<Double> inputs){
		for (int i = 0; i<inputs.size()+1;i++){
			Neuron neuron = layers.get(0).neurons.get(i);
			if(i==0){
				neuron.setValue(1);
			}
			else{
				neuron.setValue(inputs.get(i-1));
			}
				
		}
		for (int i = 1; i<layers.size();i++){
			if(i!=layers.size()-1){				
				for (int j = 0;j<layers.get(i).neurons.size();j++){
					Neuron neuron = layers.get(i).neurons.get(j);
					if (j==0){
						neuron.setValue(1);
					}else {
					    neuron.setValue(neuron.computeValueFromInput(weights.get(i-1).getRow(j-1)));
					}
				}
			}
			else{
				for (int j = 0;j<layers.get(i).neurons.size();j++){
					Neuron neuron = layers.get(i).neurons.get(j);
					neuron.setValue(neuron.computeValueFromInput(weights.get(i-1).getRow(j)));				
				}
			}
		}
	}
	
	public RealMatrix[] getErrorMatrices (List<Double> output){
		RealMatrix [] errorMatrices = new RealMatrix[layers.size()];
		double [][] levelErrorValues = new double[output.size()][1];
		for (int i = 0; i<output.size();i++){
			levelErrorValues[i][0] = layers.get(layers.size()-1).neurons.get(i).getValue()-output.get(i);
		}
		errorMatrices[layers.size()-1] = MatrixUtils.createRealMatrix(levelErrorValues);
		
		for (int i = layers.size()-2;i>0;i--){
			List<Neuron> neurons = layers.get(i).neurons;
			double [][] neuronValues = new double[neurons.size()][1];
			double [][] oneValues = new double [neurons.size()][1];
			for (int j = 0; j<neuronValues.length;j++){
				neuronValues[j][0] = neurons.get(j).getValue();
				oneValues[j][0] = 1;
			}
			RealMatrix neuronVals = MatrixUtils.createRealMatrix(neuronValues);
			RealMatrix oneVals = MatrixUtils.createRealMatrix(oneValues);
			RealMatrix firstPartOfErrorMatrix = (weights.get(i)).transpose().multiply(errorMatrices[i+1]);
			RealMatrix subtractedFromOnes = oneVals.subtract(neuronVals);
			double[][] neuronMultipliedOnes = new double[subtractedFromOnes.getRowDimension()][subtractedFromOnes.getColumnDimension()];
			for (int j = 0; j<subtractedFromOnes.getRowDimension();j++){
				neuronMultipliedOnes[j][0] = neuronVals.getEntry(j, 0)*(subtractedFromOnes.getEntry(j, 0));
			}
			double [][] finalErrorValues = new double[neuronMultipliedOnes.length][1];
			for (int j = 0; j<neuronMultipliedOnes.length;j++){
				finalErrorValues[j][0] = firstPartOfErrorMatrix.getEntry(j, 0)*neuronMultipliedOnes[j][0];
			}
			
			errorMatrices[i] = MatrixUtils.createRealMatrix(finalErrorValues);
			
			// Remove first value from the error matrix
			errorMatrices[i] = errorMatrices[i].getSubMatrix(1, neuronMultipliedOnes.length-1, 0, 0);
		}
		return errorMatrices;
	}
	
	public List<RealMatrix> getGradientValue (List<Double> output, List<RealMatrix> currentGradients){
		RealMatrix [] errorMatrices = getErrorMatrices(output);
		
		for (int i = 0; i<weights.size();i++){
			List<Neuron>neurons = layers.get(i).neurons;
			double [][] neuronValues = new double[layers.get(i).neurons.size()][1];
			for (int j = 0; j<neuronValues.length;j++){
				neuronValues[j][0] = neurons.get(j).getValue();
			}
			RealMatrix neuronVals = MatrixUtils.createRealMatrix(neuronValues);
			currentGradients.set(i, currentGradients.get(i).add(errorMatrices[i+1].multiply(neuronVals.transpose())));
		}		
		return currentGradients;
	}
	
	public int getValue (List<Double> inputs){
		PropogateForward(inputs);
		List<Neuron> lastLayer = layers.get(layers.size()-1).neurons;
		
		int index = -1;		
		double maxProb = -1;
		for (int i = 0; i < lastLayer.size(); i++){
			
			if(lastLayer.get(i).getValue()>maxProb){
				index = i;
				maxProb = lastLayer.get(i).getValue();
			}
		}
		
		return index;
	}
	
	public void updateWeights(List<RealMatrix> updatedWeights){
		for (int i = 0; i < updatedWeights.size(); i++){	
			weights.set(i, updatedWeights.get(i));	
		}
	}
	
	public List<Layer> getLayers(){
		return layers;
	}
	
	public List<RealMatrix> getWeights(){
		return weights;
	}
	
	public void setWeights(List<RealMatrix> weights){
		this.weights = weights;
	}
}
 