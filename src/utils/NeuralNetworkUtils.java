package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import neuralnetwork.Layer;
import neuralnetwork.Network;

public class NeuralNetworkUtils {
	
	public static void saveNetwork(String path, Network network){
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			List<Layer> layers = network.getLayers();
			int amountOfLayers = layers.size();
			out.write(amountOfLayers + "\n");
			
			for (int i = 0; i < amountOfLayers; i++){				
				out.write(layers.get(i).neurons.size()+"\n");				
			}
			
			List<RealMatrix> weights = network.getWeights();
			int amountOfWeights = weights.size();
			
			for (int i = 0; i < amountOfWeights; i++){
				RealMatrix curMatrix = weights.get(i);
				int rows = curMatrix.getRowDimension();
				int columns = curMatrix.getColumnDimension();
				out.write(rows + " " + columns + "\n");
				
				for (int j = 0; j<rows;j++){
					for (int k = 0; k<columns; k++){
						out.write(curMatrix.getEntry(j, k) + " ");
					}
					out.write("\n");
				}
				
			}
			out.close();
		} catch (IOException e){
			
		}
		
	}

	public static Network readNetwork (String filename){
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int amountOfLayers = Integer.parseInt(br.readLine());
			int [] amountOfNeuronsPerLayer = new int [amountOfLayers];
			
			for (int i = 0; i<amountOfLayers; i++){
				amountOfNeuronsPerLayer[i] = Integer.parseInt(br.readLine()) - 1;
				if (i==amountOfLayers-1){
					amountOfNeuronsPerLayer[i]++;
				}
					
			}
			Network network = new Network(amountOfLayers, amountOfNeuronsPerLayer);
			
			List<RealMatrix> weights  = new ArrayList<RealMatrix> ();
			
			for (int i = 0; i<amountOfLayers-1; i++){
				String rowsColsLine = br.readLine();
				int spaceIndex = rowsColsLine.indexOf(" ");
				int rows = Integer.parseInt(rowsColsLine.substring(0,spaceIndex));
				int columns = Integer.parseInt(rowsColsLine.substring(spaceIndex+1));
				RealMatrix mat  = MatrixUtils.createRealMatrix(rows, columns);
				
				for (int j = 0; j < rows; j++){
					
					String curRowLine = br.readLine();				
					
					for (int k = 0; k < columns; k++){
						int rowSpaceIndex  = curRowLine.indexOf(" ");
						double value = Double.parseDouble(curRowLine.substring(0,rowSpaceIndex));
						curRowLine = curRowLine.substring(rowSpaceIndex+1);
						mat.setEntry(j, k, value);						
					}		
				}
				weights.add(mat);
			}
			network.updateWeights(weights);
			br.close();
			return network;
			
		} catch (IOException e){
			return null;
		}
	}
}
