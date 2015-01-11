package utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ImageConversionAndSave {

	private BufferedImage image;
	public double [][] matrixValues;
	private int divider;
	private int [] solution;
	private int sol;
	private final String path = "C:\\Users\\mgdave\\Documents\\1B\\OCR_Algo\\OCR_Algo\\Logs\\";
	
		
	public ImageConversionAndSave(BufferedImage image, int sol, int divider){
		this.image = image;
		this.divider = divider;
		convertImageToValues(image);
		this.sol = sol;
		
	}
	
	public ImageConversionAndSave(BufferedImage image, int [] solution, int divider){
		this.image = image;
		this.divider = divider;
		convertImageToValues(image);
		this.solution = solution;
		
	}
	
	private void convertImageToValues(BufferedImage image){
		int heightInterval = image.getHeight()/divider;
		int widthInterval = image.getWidth()/divider;
		matrixValues = new double [heightInterval][widthInterval];
		for (int i = 0;i<heightInterval;i++){
			for (int j = 0;j<widthInterval;j++){
				double sum = 0;
				double number = 0.0;
				for (int k = i*divider;k<(i+1)*divider&&k<image.getHeight();k++){
					for (int l = j*divider;l<(j+1)*divider&&l<image.getWidth();l++){
						int pix = image.getRGB(l,k);
						int r = (pix>>16)&0xFF;
						int g = (pix>>8)&0xFF;
						int b = pix & 0xFF;
						sum+= (r+g+b)/3.0;
						number++;
					}
				}
				matrixValues[i][j] = (255-sum/number);
			}
		}
	}
	
	public void saveToFile(){
		String fileName = path + String.valueOf(System.currentTimeMillis()) + ".txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			out.write(matrixValues.length+"\n");
			for (int i = 0; i < matrixValues.length;i++){
				for (int j = 0; j < matrixValues[0].length;j++){
					out.write(matrixValues[i][j] + " ");
				}
				out.write("\n");
			}
			out.write(sol+"");
			out.close();
		} catch (IOException e){
			
		}
		ImageReader imReader = new ImageReader(fileName);
		imReader.readFromImage();
	}
	
	
	
	
}
