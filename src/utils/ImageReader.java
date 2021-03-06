package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageReader {

	public List<Double>values;
	public List<Double> solution;
	private String fileName;
	
	public ImageReader (String fileName){
		this.fileName = fileName;
		values = new ArrayList<Double>();
		solution = new ArrayList<Double>();
	}
	
	public void readFromImage(){
		String line;
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int numRows = Integer.parseInt(br.readLine());
			for (int i = 0; i<numRows; i++){
				line = br.readLine();
				while (line.contains(" ")){
					int index = line.indexOf(" ");
					String val = line.substring(0, index);
					values.add(Double.parseDouble(val));
					line = line.substring(index+1);
				}
				
			}
			line = br.readLine();
	/*		while (line.contains(" ")){
				int index = line.indexOf(" ");
				String val = line.substring(0, index);
				solution.add(Double.parseDouble(val));
				line = line.substring(index+1);
			}*/
			solution.add(Double.parseDouble(line));
			br.close();
		}catch(IOException e){
			
		}
		
	}
	
	
}
