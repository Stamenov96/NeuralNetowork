package com.example.drawing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import android.graphics.Bitmap;
import android.util.Log;

public class NeuralNetworksProgram {
	public static void main(String[] args) {
		{
			try {
				System.out.println("\nBegin Neural Network demo\n");

				//System.out
					//	.println("Creating a 3-input, 4-hidden, 2-output neural network");
				//System.out
					//	.println("Using sigmoid function for input-to-hidden activation");
				//System.out
					//	.println("Using tanh function for hidden-to-output activation");
				int numofinput =3;
				int numofhidden=4;
				int numofoutput=2;
				NeuralNetwork nn = new NeuralNetwork(numofinput, numofhidden, numofoutput);

//				double[] weights = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6,
//						0.7, 0.8, 0.9, 1.0, 1.1, 1.2, -2.0, -6.0, -1.0, -7.0,
//						1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5, -5.0 };

//				double[] weightsbase = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6,
//						0.7, 0.8, 0.9, 1.0, 1.1, 1.2, -2.0, -6.0, -1.0, -7.0,
//						1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5, -5.0 };
//				
				
				String path = MainActivity.appDir.toString();
				OutputStream fOut;
				File file = new File(path, "wandb.txt"); // the File to save to
				
				if (!file.exists()&& !file.isFile()) {
				
				
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
					Log.e("weightsandbiases", "Exception in saving", e);
				}
				
				
				}else{System.out.println("FILE EXISTS");}
				
				
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
				double[] weights = new double[count];
				for (int i = 0; i < count; i++) {
					String line = newscan.nextLine();
					weights[i] = Double.parseDouble(line);
				}
				
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
//				Helpers.ShowVector(weights3);
//				System.out.println("===================----------------------------======================");
				
				//System.out.println("\nWeights and biases are:");
				//Helpers.ShowVector(weights);

				//System.out.println("Loading neural network weights and biases");
				nn.SetWeights(weights);

				//System.out.println("\nSetting inputs:");
				double[] xValues = new double[] { 1.0, 2.0, 3.0 };
				//Helpers.ShowVector(xValues);

				//System.out.println("Loading inputs and computing outputs");
				double[] initialOutputs = nn.ComputeOutputs(xValues);

				//System.out.println("\nNeural network outputs are:");
				//Helpers.ShowVector(initialOutputs);

				double[] tValues = new double[] { -0.8500, 0.7500 }; // target
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

				double eta = 0.25; // learning rate - controls the maginitude of
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
				
				System.out.println("OUTPUTS:");
				//yValues = nn.ComputeOutputs(xValues);
				Helpers.ShowVector(yValues);
				
				double error = Error(tValues, yValues);
				/*while (ctr < 1000 && error > 0.01) {
					System.out
							.println("===================================================");
					System.out.println("iteration = " + ctr);
					System.out
							.println("Updating weights and biases using back-propagation");
					nn.UpdateWeights(tValues, eta, alpha);
					System.out.println("Computing new outputs:");
					yValues = nn.ComputeOutputs(xValues);
					//Helpers.ShowVector(yValues);
					System.out.println("\nComputing new error");
					error = Error(tValues, yValues);
					System.out.println("Error = " + error);
					++ctr;
				}*/
				/*System.out
						.println("===================================================");
				System.out.println("Error = " + error);
				System.out.println("\nBest weights and biases found:");
				double[] bestWeights = nn.GetWeights();
				
				
				try {
					FileWriter out = new FileWriter(file);
					for (int i = 0; i < bestWeights.length; i++) {
						out.write(Double.toString(bestWeights[i]) + "\n");
					}
				out.flush();
					out.close();
					
				} catch (IOException e) {
					Log.e("weightsandbiases", "Exception in saving", e);
				}
				
				
				
				Helpers.ShowVector(bestWeights);*/

				System.out.println("End Neural Network demo\n");
				// Console.ReadLine();
			} catch (Exception ex) {
				System.out.println("Fatal: " + ex);
				// Console.ReadLine();
			}
		}
	} // Main

	static double Error(double[] target, double[] output) // sum absolute error.
															// could put into
															// NeuralNetwork
															// class.
	{
		double sum = 0.0;
		for (int i = 0; i < target.length; ++i)
			sum += Math.abs(target[i] - output[i]);
		return sum;
	}
} // class NeuralNetworksProgram
// } // ns

// }