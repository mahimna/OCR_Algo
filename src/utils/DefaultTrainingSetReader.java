package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DefaultTrainingSetReader {
	
	private List<List<Double>> inputs, outputs, testInputs, testOutputs;
	private static String pathForPrintingDataset = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\Logs\\printedDatasetValue.txt";
	
	
	private String trainingSetImagesFile = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\MNISTDatabase\\train-images.idx3-ubyte";
	private String trainingSetLabelsFile = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\MNISTDatabase\\train-labels.idx1-ubyte";
	private String testSetImagesFile = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\MNISTDatabase\\test-images.idx3-ubyte";
	private String testSetLabelsFile = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\MNISTDatabase\\test-labels.idx1-ubyte";
	
	public DefaultTrainingSetReader(String trainingSetImagesFile, String trainingSetLabelsFile, int length){
		this.trainingSetImagesFile = trainingSetImagesFile;
		this.trainingSetLabelsFile = trainingSetLabelsFile;
		this.inputs = new ArrayList<List<Double>>();
		this.outputs = new ArrayList<List<Double>>();
		this.testInputs = new ArrayList<List<Double>>();
		this.testOutputs = new ArrayList<List<Double>>();
		try{
			byte [] trainingSetImages = readBinaryFile(trainingSetImagesFile);
			byte [] trainingSetLabels = readBinaryFile(trainingSetLabelsFile);
			byte [] testSetImages = readBinaryFile(testSetImagesFile);
			byte [] testSetLabels = readBinaryFile(testSetLabelsFile);
			inputs = readImagesFile(trainingSetImages,length);
			outputs = readLabelsFile(trainingSetLabels,length);
			testInputs = readImagesFile(testSetImages,-1);
			testOutputs = readLabelsFile(testSetLabels,-1); 
			
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public DefaultTrainingSetReader(int length){	
		inputs = new ArrayList<List<Double>>();
		outputs = new ArrayList<List<Double>>();
		try{
			byte [] trainingSetImages = readBinaryFile(trainingSetImagesFile);
			byte [] trainingSetLabels = readBinaryFile(trainingSetLabelsFile);
			readImagesFile(trainingSetImages,length);
			readLabelsFile(trainingSetLabels,length);
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public byte[] readBinaryFile(String filename) throws IOException{
		Path path = Paths.get(filename);
		return Files.readAllBytes(path);
	}	
	
	private List<List<Double>> readLabelsFile(byte[] trainingSetLabels, int length){
		
		List<List<Double>> labelsInformation = new ArrayList<List<Double>>();
		
		int start = 0;
		int magicNumber = readInteger(trainingSetLabels, start);
		start += 4;
		int numberLabels = readInteger(trainingSetLabels, start);
		start += 4;
		
		if(length<0||length>numberLabels){
			length = numberLabels;
		}
		
		for (int i = 0; i < length; i++){
			List<Double> curImageOutput = new ArrayList<Double>();
			// Initialize the list to contain only 0s
			for (int j = 0; j<10; j++){
				curImageOutput.add(0.0);
			}
			// Get the index value which is the same as the label
			int index = convertUnsignedByte(trainingSetLabels[start]);
			start++;
			curImageOutput.set(index, 1.0);
			labelsInformation.add(curImageOutput);
		} 		
		
		return labelsInformation;
		
	}
	
	private List<List<Double>> readImagesFile(byte[] trainingSetData, int length){
		
		List<List<Double>> imagesInformation = new ArrayList<List<Double>>();
		
		int start = 0;
		int magicNumber = readInteger(trainingSetData,start);
		start += 4;
		int numberImages = readInteger(trainingSetData,start);
		start += 4;
		int numRows = readInteger(trainingSetData,start);
		start += 4;
		int numColumns = readInteger(trainingSetData,start);
		start += 4;
		
		if(length<0||length>numberImages){
			length = numberImages;
		}
		
		for (int i = 0; i < length; i++){
			List<Double> curImageInput = new ArrayList<Double>();
			for (int curRow = 0; curRow<numRows; curRow++){
				for (int curCol = 0; curCol<numColumns; curCol++){
					curImageInput.add((double) convertUnsignedByte(trainingSetData[start]));
					start++;
				}
			}
			imagesInformation.add(curImageInput);
		}		
		return imagesInformation;
	}
	
	private int convertUnsignedByte (byte toConvertByte){
		return (int) toConvertByte & 0xFF;		
	}
	
	private int readInteger(byte[] bytes, int start){
		
		byte[] curInteger = new byte[4];
		
		for (int i = 0; i < 4; i++){
			curInteger[i] = bytes[start+i];
		}		
		
		return  ByteBuffer.wrap(curInteger).getInt();
	}
	
	public static void printDatasetToFile (int digit, List<List<Double>> inputs, List<List<Double>> outputs){
		boolean notFoundDigit = true;
		for (int i = 0; i < outputs.size() && notFoundDigit; i++){			
			List<Double> curOutputLabel = outputs.get(i);
			int curDigit = -1;
			for(int j = 0; j < curOutputLabel.size(); j++){
				if (curOutputLabel.get(j)==1){
					curDigit = j;
				}
			}
			
			if(curDigit == digit){
				notFoundDigit = false;
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(pathForPrintingDataset));
					List<Double> curInput = inputs.get(i);
					for (int j = 0; j < 28; j++){
						for (int k = 0; k < 28; k++){
							out.write(curInput.get(j*28+k)+" ");
						}	
						out.write("\n");
					}
					out.close();					
				} catch (IOException e){
					
				}
			}			
		}
	}
	
	public List<List<Double>> getInputs(){
		return inputs;
	}
	
	public List<List<Double>> getOutputs(){
		return outputs;
	}
	
	public List<List<Double>> getTestInputs(){
		return testInputs;
	}
	
	public List<List<Double>> getTestOutputs(){
		return testOutputs;
	}
}
