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
	    while (labels.available() > 0 && numLabelsRead < 1) {
	      label = labels.readByte();
	      System.out.print(label+" L ");
	      numLabelsRead++;
	      int[][] image = new int[numCols][numRows];
//	      for (int colIdx = 0; colIdx < numCols; colIdx++) {
//	        for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
//	          image[colIdx][rowIdx] = images.readUnsignedByte();
//	         // System.out.println("IMAGE?");
//	          //System.out.println(image[colIdx][rowIdx]);
//	        }
//	      }
	     // numImagesRead++;
	    }
		
		
		
		
		
		
		
		
		
		
		{
			try {
				System.out.println("\nBegin Neural Network demo\n");

				//System.out
					//	.println("Creating a 3-input, 4-hidden, 2-output neural network");
				//System.out
					//	.println("Using sigmoid function for input-to-hidden activation");
				//System.out
					//	.println("Using tanh function for hidden-to-output activation");
				int numofinput =numCols*numRows;
				int numofhidden=1000;
				int numofoutput=10;
				NeuralNetwork nn = new NeuralNetwork(numofinput, numofhidden, numofoutput);

//				double[] weights = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6,
//						0.7, 0.8, 0.9, 1.0, 1.1, 1.2, -2.0, -6.0, -1.0, -7.0,
//						1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5, -5.0 };

//				double[] weightsbase = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6,
//						0.7, 0.8, 0.9, 1.0, 1.1, 1.2, -2.0, -6.0, -1.0, -7.0,
//						1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5, -5.0 };
//				
				String path = "/home/stefo/Desktop/";
				//String path = appDir.toString();
				OutputStream fOut;
				File file = new File(path, "statsText.txt"); // the File to save to
				
			//	if (!file.exists()&& !file.isFile()) {
				
				
				try {
					//fOut = new FileOutputStream(file);
					FileWriter out = new FileWriter(file);
					

				/*	double[] weightsbase = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6,
							0.7, 0.8, 0.9, 1.0, 1.1, 1.2, -2.0, -6.0, -1.0, -7.0,
							1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5, -5.0 };*/
					
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
				
				
			//	}else{System.out.println("FILE EXISTS");}
				
				
				/*File statText = new File("/home/stefo/Desktop/statsTest.txt");
				FileWriter out = new FileWriter(statText);
				for (int i = 0; i < weightsbase.length; i++) {
					out.write(Double.toString(weightsbase[i]) + "\n");
				}
				out.close();
				*/
				//System.out.println(file);
				//System.out.println("<<<<<<<<<<<<<<<<<<<<<<");
				
				Scanner scan = new Scanner(file);
				ArrayList<String> temps = new ArrayList<String>();

				int count = 0;
				while (scan.hasNext()) {
					String line = scan.nextLine();
					count++;
				}
				//System.out.println(count);
				//System.out.println("<<<<<<<<<<<COUNT<<<<<<<<<<<<");

				Scanner newscan = new Scanner(file);
				//System.out.println(">>>>>>" + count);
				//BigDecimal[] weights = new BigDecimal[count];
				double[] weights = new double[count];
				for (int i = 0; i < count; i++) {
					String line = newscan.nextLine();
					double a = Double.parseDouble(line);
					//weights[i] = new BigDecimal(a);
					weights[i]=a;
				}
				
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
//				Helpers.ShowVector(weights3);
//				System.out.println("===================----------------------------======================");
				
				//System.out.println("\nWeights and biases are:");
				//Helpers.ShowVector(weights);

				//System.out.println("Loading neural network weights and biases");
				nn.SetWeights(weights);

				//System.out.println("\nSetting inputs:");
				
				double[] xValues = MNISTReader.main(args);
				//System.out.println("XVALSSSSSSSSSSSSSSSSSSSSSSS");
				//Helpers.ShowVector(xValues);

				//System.out.println("Loading inputs and computing outputs");
				double[] initialOutputs = nn.ComputeOutputs(xValues);

				//System.out.println("\nNeural network outputs are:");
				//Helpers.ShowVector(initialOutputs);

				double[] tValues =new double[10];
				//Arrays.fill(tValues,(Double) null);
				for (int i = 0; i < tValues.length; i++) {
					tValues[i]=0.0;
				}
				tValues[label-1]= 1;
				
				
				
				// target
																		// (desired)
																		// values.
																		// note
																		// these
																		// only
																		// make
																		// sense
																		// for
																		// tanh
																		// output
																		// activation
				//System.out.println("Target outputs to learn are:");
				//Helpers.ShowVector(tValues);

				double eta = 0.8; // learning rate - controls the maginitude of
									// the increase in the change in weights.
									// found by trial and error.
				double alpha = 0.04; // momentum - to discourage oscillation.
										// found by trial and error.
				//System.out.println("Setting learning rate (eta) = " + eta
					//	+ " and momentum (alpha) = " + alpha);

//				System.out
//						.println("\nEntering main back-propagation compute-update cycle");
//				System.out
//						.println("Stopping when sum absolute error <= 0.01 or 1,000 iterations\n");
				int ctr = 0;
				double[] yValues = nn.ComputeOutputs(xValues); // prime the
																// back-propagation
																// loop
				
				//System.out.println("OUTPUTS:");
				//yValues = nn.ComputeOutputs(xValues);
				//Helpers.ShowVector(yValues);
				
				Double error = Error(tValues, yValues);
				System.out.println("ERRRORRR   "+error);
				
				while (ctr < 1000 && (error/*.compareTo(new BigDecimal(0.01))*/ > 0.01)) {
				for (int i = 0; i < 2;i++) {
					
				System.out
						.println("===================================================");
				System.out.println("iteration = " + ctr);
				System.out
						.println("Updating weights and biases using back-propagation");
				nn.UpdateWeights(tValues, eta, alpha);
				//Helpers.ShowVector(weights);
				System.out.println("Computing new outputs:");
				yValues = nn.ComputeOutputs(xValues);
				//Helpers.ShowVector(yValues);
				System.out.println("\nComputing new error");
				error = Error(tValues, yValues);
				System.out.println("Error = " + error);
				++ctr;
				//System.out.println("OUTPUTS:");
				//yValues = nn.ComputeOutputs(xValues);
				//Helpers.ShowVector(yValues);
				
			}
			}
				System.out
						.println("===================================================");
				System.out.println("Error = " + error);
				System.out.println("\nBest weights and biases found:");
				System.out.println("otuputs : ");
				Helpers.ShowVector(yValues);
				System.out.println("===================================================");
				
				double[] bestWeights = nn.GetWeights();
				
				
//				try {
//					FileWriter out = new FileWriter(file);
//					for (int i = 0; i < bestWeights.length; i++) {
//						out.write((bestWeights[i].toPlainString()) + "\n");
//					}
//				out.flush();
//					out.close();
//					
//				} catch (IOException e) {
//					System.out.println("weightsandbiases"+"Exception in saving"+e);
//				}
				
				
				
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
