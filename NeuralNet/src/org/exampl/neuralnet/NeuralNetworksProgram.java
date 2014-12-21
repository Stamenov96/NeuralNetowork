package org.exampl.neuralnet;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class NeuralNetworksProgram {
	public static void main(String[] args) {
		{
			try {
				System.out.println("\nBegin Neural Network demo\n");

				System.out
						.println("Creating a 3-input, 4-hidden, 2-output neural network");
				System.out
						.println("Using sigmoid function for input-to-hidden activation");
				System.out
						.println("Using tanh function for hidden-to-output activation");
				NeuralNetwork nn = new NeuralNetwork(3, 4, 2);

				double[] weightsbase = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5,
						0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, -2.0, -6.0, -1.0,
						-7.0, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, -2.5,
						-5.0 };

				// try {
				File statText = new File("/home/stefo/Desktop/statsTest.txt");
				FileWriter out = new FileWriter(statText);
				for (int i = 0; i < weightsbase.length; i++) {
					out.write(Double.toString(weightsbase[i]) + "\n");
				}
				out.close();

				Scanner scan = new Scanner(statText);
				ArrayList<String> temps = new ArrayList<String>();

				int count = 0;
				while (scan.hasNext()) {
					String line = scan.nextLine();
					count++;
				}

				Scanner newscan = new Scanner(statText);
				System.out.println(">>>>>>" + count);
				double[] weights = new double[count];
				for (int i = 0; i < count; i++) {
					String line = newscan.nextLine();
					weights[i] = Double.parseDouble(line);
				}
				// System.out.println("---------------------");
				// Helpers.ShowVector(sc);
				// System.out.println("---------------------");
				// while(scan.hasNext()){
				//
				// String line = scan.nextLine();
				// String[] numStrings = line.split(", ");
				// // parse number string into doubles
				// double[] nums = new double[numStrings.length];
				//
				// for (int i = 0; i < nums.length; i++){
				// nums[i] = Double.parseDouble(numStrings[i]);
				// }
				// }

				// FileInputStream input = new FileInputStream(statText);
				// DataInputStream datainput = new DataInputStream(input);
				// double d =datainput.readDouble();
				// System.out.println("DOUBLE : "+d+"<<<<<<<<");
				// datainput.close();

				// Scanner in = new Scanner(statText);
				// int i=0;
				// double [] ary = null;
				// while (in.hasNextLine()) {
				// ary[i++]=in.nextDouble();
				//
				// }
				// Scanner s = new Scanner(statText);
				// String line = s.next();
				// double gotDouble = Double.parseDouble(line);
				// System.out.println("DOUBLE::"+gotDouble);

				// Scanner s = new Scanner(statText);
				// double[] arr = new double[s.nextDouble()];
				// int[] array = new int[s.nextInt()];
				// for (int i = 0; i < array.length; i++)
				// array[i] = s.nextInt();

				// FileOutputStream is = new FileOutputStream(statText);
				// OutputStreamWriter osw = new OutputStreamWriter(is);
				// Writer w = new BufferedWriter(osw);
				// for (int i = 0; i < weights.length; i++) {
				// w.write(weights[i].toString);
				// }
				//
				// w.write("POTATO!!!");
				// w.close();
				// } catch (IOException e) {
				// System.err.println("Problem writing to the file statsTest.txt");
				// }

				// writer write = new writer();
				// write.writing();

				System.out.println("\nWeights and biases are:");
				Helpers.ShowVector(weights);

				System.out.println("Loading neural network weights and biases");
				nn.SetWeights(weights);

				System.out.println("\nSetting inputs:");
				double[] xValues = new double[] { 1.0, 2.0, 3.0 };
				Helpers.ShowVector(xValues);

				System.out.println("Loading inputs and computing outputs");
				double[] initialOutputs = nn.ComputeOutputs(xValues);

				System.out.println("\nNeural network outputs are:");
				Helpers.ShowVector(initialOutputs);

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
				System.out.println("Target outputs to learn are:");
				Helpers.ShowVector(tValues);

				double eta = 0.25; // learning rate - controls the maginitude of
									// the increase in the change in weights.
									// found by trial and error.
				double alpha = 0.04; // momentum - to discourage oscillation.
										// found by trial and error.
				System.out.println("Setting learning rate (eta) = " + eta
						+ " and momentum (alpha) = " + alpha);

				System.out
						.println("\nEntering main back-propagation compute-update cycle");
				System.out
						.println("Stopping when sum absolute error <= 0.01 or 1,000 iterations\n");
				int ctr = 0;
				double[] yValues = nn.ComputeOutputs(xValues); // prime the
																// back-propagation
																// loop
				double error = Error(tValues, yValues);
				while (ctr < 1000 && error > 0.01) {
					System.out
							.println("===================================================");
					System.out.println("iteration = " + ctr);
					System.out
							.println("Updating weights and biases using back-propagation");
					nn.UpdateWeights(tValues, eta, alpha);
					System.out.println("Computing new outputs:");
					yValues = nn.ComputeOutputs(xValues);
					Helpers.ShowVector(yValues);
					System.out.println("\nComputing new error");
					error = Error(tValues, yValues);
					System.out.println("Error = " + error);
					++ctr;
				}
				System.out
						.println("===================================================");
				System.out.println("\nBest weights and biases found:");
				double[] bestWeights = nn.GetWeights();
				Helpers.ShowVector(bestWeights);
				
				File newweights = new File("/home/stefo/Desktop/statsTest.txt");
				FileWriter newout = new FileWriter(newweights);
				for (int i = 0; i < bestWeights.length; i++) {
					newout.write(Double.toString(bestWeights[i]) + "\n");
				}
				newout.close();
				
				
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
