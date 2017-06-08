package utilities;

import gui.Main;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.*;


public class DataManipulation {

	//This method normalizes the matrix with the formula: (x - mu)/sigma
	public static INDArray dataPreprocess(INDArray image){
		Number mean = image.meanNumber();
		Number std = image.stdNumber();
		image = image.sub(mean).div(std);
		return image;
	}
	//This method returns the String representation of the ID.
	public static String idToString(int n){

		String drawing = null;
		try{
			InputStream is = Main.class.getResourceAsStream("/text/dictionary.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			for (int x = 0; x < n; x++){
				br.readLine();
			}
			drawing = br.readLine();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return drawing;

	}


	
	
}
