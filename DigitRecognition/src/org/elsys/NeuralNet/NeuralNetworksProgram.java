package org.elsys.NeuralNet;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

public class NeuralNetworksProgram {

	static int label;

	public static int main(View view, Bitmap resized, String path)
			throws Exception {

		int numofinput = 28 * 28;
		int numofhidden = 1000;
		int numofoutput = 10;
		NeuralNetwork nn = new NeuralNetwork(numofinput, numofhidden,
				numofoutput);

		File file = new File(path, "weightsandbiases.txt");

		int size = (numofinput * numofhidden) + numofhidden
				+ (numofhidden * numofoutput) + numofoutput;
		System.out.println("NUM LINES" + size);

		Scanner newscan = new Scanner(file);
		double[] weights = new double[size];
		String line;
		for (int i = 0; i < size; i++) {
			line = newscan.nextLine();
			weights[i] = Double.parseDouble(line);
		}

		try {
			nn.SetWeights(weights);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
		double[] xValues = new double[resized.getWidth() * resized.getHeight()];
		int itter = 0;
		double[][] image = new double[resized.getWidth()][resized.getHeight()];
		for (int colIdx = 0; colIdx < resized.getWidth(); colIdx++) {
			for (int rowIdx = 0; rowIdx < resized.getHeight(); rowIdx++) {
				int pixel = resized.getPixel(rowIdx,colIdx);
		        double B = Color.blue(pixel);
				 image[colIdx][rowIdx] =B;
		         image[colIdx][rowIdx]= (image[colIdx][rowIdx]/255);
				 xValues[itter]=image[colIdx][rowIdx];
				itter++;
			}
		}

			System.out.println("BEFORE COMP.OUTP");
			final double[] yValues = nn.ComputeOutputs(xValues); // prime the

			System.out.println("AFTER COMP.OUTP");												// back-propagation
															// loop
				int it = 0;
				double max = 0;
				for (int i = 0; i < yValues.length; i++) {
					if (max < yValues[i]) {
						max = yValues[i];
						it = i;
						System.out.println("Max");
						System.out.println(max);
					}
				}
				System.out.println("recognized as" + it);
				System.out.println("IN NNP");
				System.out.println(Thread.currentThread().getName());
				return it;

	} 
}