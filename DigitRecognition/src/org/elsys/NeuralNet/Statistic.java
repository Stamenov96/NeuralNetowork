package org.elsys.NeuralNet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


	public class Statistic {
		
		static int label;
		
		@SuppressWarnings("resource")
		public static void main(String[] args) throws IOException {
			
			String imagess = "src/org/elsys/NeuralNet/t10k-images.idx3-ubyte";
			String labelss = "src/org/elsys/NeuralNet/t10k-labels.idx1-ubyte";
			
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
			int ctr=0;
			int stat = 0;

			String path = "src/org/elsys/NeuralNet/";
			File file = new File(path, "weightsandbiases.txt"); // the File to save to
			
			if (!file.exists()&& !file.isFile()) {
				System.out.println("Error : file 'weightsandbias.txt' is missing!");
			}			
			
			int size = (numofinput * numofhidden) + numofhidden
					+ (numofhidden * numofoutput) + numofoutput;

			Scanner newscan = new Scanner(file);
			double[] weights = new double[size];
			for (int i = 0; i < size; i++) {
				String line = newscan.nextLine();
				double a = Double.parseDouble(line);
				weights[i]=a;
			}
			
			try {
				nn.SetWeights(weights);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			while (labels.available() > 0 && numLabelsRead < numLabels) {
				byte label = labels.readByte();
				numLabelsRead++;
				double[][] image = new double[numCols][numRows];
				double[] xValues = new double[numCols * numRows];
				int itter = 0;
				for (int colIdx = 0; colIdx < numCols; colIdx++) {
					for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
						image[colIdx][rowIdx] = images.readUnsignedByte();
						image[colIdx][rowIdx] = (image[colIdx][rowIdx] / 255);
						xValues[itter] = image[colIdx][rowIdx];
						itter++;
					}
				}

				numImagesRead++;
				System.out.println("The " + numImagesRead
						+ " number from data set is " + label);
		    
			
				try {
					double[] tValues =new double[10];
					for (int i = 0; i < tValues.length; i++) {
						tValues[i]=0.0;
					}
					tValues[label]= 1;// target
					
					double[] yValues = nn.ComputeOutputs(xValues); // prime the
																	// back-propagation
																	// loop
					System.out.println("===========-------------------===========================");
			
					int it=0;
					double max=0;
					for (int i = 0; i < yValues.length; i++) {
						if(max<yValues[i]){
							max=yValues[i];
							it=i;
						}
						
					}
					System.out.println("The " + label + " is recognized as "+ it);
					if(it==label){
						stat++;
					}
				
					
					System.out.println("===================================================");
	} catch (Exception ex) {
					System.out.println("Fatal: " + ex);
				}
				
				
				ctr++;
			
			}
		  double proc = (double)stat/(double)ctr;
		 System.out.printf("Percent of recognized digits is : "+proc);
		} 

	}
