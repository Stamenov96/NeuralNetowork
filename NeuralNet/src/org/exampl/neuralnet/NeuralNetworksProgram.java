package org.exampl.neuralnet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


//import android.graphics.Bitmap;
//import android.util.Log;

public class NeuralNetworksProgram {
	
	static int label;
	
	public static void main(String[] args) throws IOException {
		
		 String imagess = "/home/stefo/Desktop/NeuralNetwork/NeuralNetowork/NeuralNet/src/org/exampl/neuralnet/train-images.idx3-ubyte";
		String labelss = "/home/stefo/Desktop/NeuralNetwork/NeuralNetowork/NeuralNet/src/org/exampl/neuralnet/train-labels.idx1-ubyte";
			
			
	    DataInputStream labels = new DataInputStream(new FileInputStream(labelss));
	    DataInputStream images = new DataInputStream(new FileInputStream(imagess));
	    int magicNumber = labels.readInt();
	    if (magicNumber != 2049) {
	      System.err.println("Label file has wrong magic number: " + magicNumber + " (should be 2049)");
	      System.exit(0);
	    }
	    magicNumber = images.readInt();
	    if (magicNumber != 2051) {
	      System.err.println("Image file has wrong magic number: " + magicNumber + " (should be 2051)");
	      System.exit(0);
	    }
	    int numLabels = labels.readInt();
	    int numImages = images.readInt();
	    int numRows = images.readInt();
	    int numCols = images.readInt();
	    if (numLabels != numImages) {
	      System.err.println("Image file and label file do not contain the same number of entries.");
	      System.err.println("  Label file contains: " + numLabels);
	      System.err.println("  Image file contains: " + numImages);
	      System.exit(0);
	    }

	   // long start = System.currentTimeMillis();
	    int numLabelsRead = 0;
	    int numImagesRead = 0;

		int numofinput =numCols*numRows;
		int numofhidden=1000;
		int numofoutput=10;		
		NeuralNetwork nn = new NeuralNetwork(numofinput, numofhidden, numofoutput);

	    
	    while (labels.available() >0 && numLabelsRead < numLabels) {
	        byte label = labels.readByte();
	       // System.out.print(label+" L ");
	        numLabelsRead++;
	        double[][] image = new double[numCols][numRows];
	        double[] xValues = new double[numCols*numRows];
	        int itter = 0;
	        for (int colIdx = 0; colIdx < numCols; colIdx++) {
	          for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
	            image[colIdx][rowIdx] = images.readUnsignedByte();
	            image[colIdx][rowIdx]= (image[colIdx][rowIdx]/255);
	            xValues[itter]=image[colIdx][rowIdx];
	            itter++;
	          }
	        }
	        
	        numImagesRead++;
	      System.out.println("===========================================");
	      System.out.printf(numImagesRead+" "+label);
	      System.out.println("============================================================");
	    
	    
		
			try {
				System.out.println("\nBegin Neural Network demo\n");


				String path = "/home/stefo/Desktop/";
				//String path = appDir.toString();
				OutputStream fOut;
				File file = new File(path, "statsText.txt"); // the File to save to
				
				if (!file.exists()&& !file.isFile()) {
				
				
				try {
					FileWriter out = new FileWriter(file);

					int size = (numofinput * numofhidden) + numofhidden+(numofhidden * numofoutput) + numofoutput;
					for (int i = 0; i < size; i++) {
						double random = new Random().nextDouble();
						out.write(random + "\n");
					}
					out.flush();
					out.close();
					
				} catch (IOException e) {
					System.out.println("ERROR");
				}
				
				
				}else{System.out.println("FILE EXISTS");}
				
				
				
				Scanner scan = new Scanner(file);
				ArrayList<String> temps = new ArrayList<String>();

				int count = 0;
				while (scan.hasNext()) {
					String line = scan.nextLine();
					count++;
				}
				
				Scanner newscan = new Scanner(file);
				double[] weights = new double[count];
				for (int i = 0; i < count; i++) {
					String line = newscan.nextLine();
					double a = Double.parseDouble(line);
					weights[i]=a;
				}
				
				nn.SetWeights(weights);
				
				//double[] initialOutputs = nn.ComputeOutputs(xValues);
				double[] tValues =new double[10];
				for (int i = 0; i < tValues.length; i++) {
					tValues[i]=0.0;
				}
				tValues[label]= 1;// target
				double eta = Math.pow(2,80 /*58*/); // learning rate - controls the maginitude of
									// the increase in the change in weights.
									// found by trial and error.
				double alpha = Math.pow(2,22);//90000; // momentum - to discourage oscillation.
										// found by trial and error.
				int ctr = 0;
				double[] yValues = nn.ComputeOutputs(xValues); // prime the
																// back-propagation
																// loop
				
				//System.out.println("OUTPUTS:");
				//Helpers.ShowVector(yValues);
				
				System.out.println("===========-------------------===========================");
				
				Double error = Error(tValues, yValues);
				System.out.println("ERRRORRR   "+error);
				
				//while (ctr < 500 && (error > 0.1)) {
				//for (int i = 0; i < 2;i++) {
					
				System.out
						.println("===================================================");
				//System.out.println("iteration = " + ctr);
				System.out
						.println("Updating weights and biases using back-propagation");
				if(error>0.01){
				nn.UpdateWeights(tValues, eta, alpha);
				}
				//Helpers.ShowVector(weights);
				//System.out.println("Computing new outputs:");
				//yValues = nn.ComputeOutputs(xValues);
				//Helpers.ShowVector(yValues);
				//System.out.println("\nComputing new error");
				//error = Error(tValues, yValues);
				//System.out.println("Error = " + error);
				//++ctr;
				//System.out.println("OUTPUTS:");
				//yValues = nn.ComputeOutputs(xValues);
				//Helpers.ShowVector(yValues);
				
			
			//}
				System.out
						.println("===================================================");
				System.out.println("Error = " + error);
				System.out.println("\nBest weights and biases found:");
				System.out.println("otuputs : ");
				//Helpers.ShowVector(yValues);
				System.out.println("===================================================");
				
				/*double[] bestWeights = nn.GetWeights();
				
				
				try {
										FileWriter out = new FileWriter(file);
										for (int i = 0; i < bestWeights.length; i++) {
											out.write(Double.toString(bestWeights[i]) + "\n");
										}
									out.flush();
										out.close();
										
									} catch (IOException e) {
										System.out.printf("weightsandbiases", "Exception in saving", e);
									}*/
				
				//Helpers.ShowVector(bestWeights);

				System.out.println("End Neural Network demo\n");
				
				// Console.ReadLine();
			} catch (Exception ex) {
				System.out.println("Fatal: " + ex);
				// Console.ReadLine();
			}
		
	    }
	    
	} // Main

	static double Error(double[] tValues, double[] yValues) // sum absolute error.
															// could put into
															// NeuralNetwork
															// class.

	
	{
		//System.out.println("TARGETS");
		//Helpers.ShowVector(target);
		double sum = 0.0;
		for (int i = 0; i < tValues.length; ++i)
			//sum =sum.add((tValues[i].subtract( yValues[i])).abs());
			sum+=Math.abs(tValues[i]-yValues[i]);

		return sum;
	}
} // class NeuralNetworksProgram
// } // ns

// }