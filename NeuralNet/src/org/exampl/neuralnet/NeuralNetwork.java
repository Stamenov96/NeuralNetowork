package org.exampl.neuralnet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.nevec.rjm.*;

class NeuralNetwork {
	private int numInput;
	private int numHidden;
	private int numOutput;

	private BigDecimal[] inputs;
	private BigDecimal[][] ihWeights; // input-to-hidden
	private BigDecimal[] ihSums;
	private BigDecimal[] ihBiases;
	private BigDecimal[] ihOutputs;

	private BigDecimal[][] hoWeights; // hidden-to-output
	private BigDecimal[] hoSums;
	private BigDecimal[] hoBiases;
	private BigDecimal[] outputs;

	private BigDecimal[] oGrads; // output gradients for back-propagation
	private BigDecimal[] hGrads; // hidden gradients for back-propagation

	private BigDecimal[][] ihPrevWeightsDelta; // for momentum with back-propagation
	private BigDecimal[] ihPrevBiasesDelta;

	private BigDecimal[][] hoPrevWeightsDelta;
	private BigDecimal[] hoPrevBiasesDelta;

	public NeuralNetwork(int numInput, int numHidden, int numOutput)
    {
      this.numInput = numInput;
      this.numHidden = numHidden;
      this.numOutput = numOutput;

      inputs = new BigDecimal[numInput];
      ihWeights = Helpers.MakeMatrix(numInput, numHidden);
      ihSums = new BigDecimal[numHidden];
      ihBiases = new BigDecimal[numHidden];
      ihOutputs = new BigDecimal[numHidden];
      hoWeights = Helpers.MakeMatrix(numHidden, numOutput);
      hoSums = new BigDecimal[numOutput];
      hoBiases = new BigDecimal[numOutput];
      outputs = new BigDecimal[numOutput];

      oGrads = new BigDecimal[numOutput];
      hGrads = new BigDecimal[numHidden];

      ihPrevWeightsDelta = Helpers.MakeMatrix(numInput, numHidden);
      ihPrevBiasesDelta = new BigDecimal[numHidden];
      for (int i = 0; i < ihPrevBiasesDelta.length; i++) {
		ihPrevBiasesDelta[i]=new BigDecimal(0.0);
	}
      hoPrevWeightsDelta = Helpers.MakeMatrix(numHidden, numOutput);
      hoPrevBiasesDelta = new BigDecimal[numOutput];
      for (int i = 0; i < hoPrevBiasesDelta.length; i++) {
		hoPrevBiasesDelta[i]=new BigDecimal(0.0);
	}
    }


	public void SetWeights(BigDecimal[] weights) throws Exception
    {
      // copy weights and biases in weights[] array to i-h weights, i-h biases, h-o weights, h-o biases
      int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
      if (weights.length != numWeights)
        throw new Exception("The weights array length: " + weights.length + " does not match the total number of weights and biases: " + numWeights);

      int k = 0; // points into weights param

      for (int i = 0; i < numInput; ++i)
        for (int j = 0; j < numHidden; ++j)
          ihWeights[i][j] = weights[k++];

      for (int i = 0; i < numHidden; ++i)
        ihBiases[i] = weights[k++];

      for (int i = 0; i < numHidden; ++i)
        for (int j = 0; j < numOutput; ++j)
          hoWeights[i][j] = weights[k++];

      for (int i = 0; i < numOutput; ++i)
        hoBiases[i] = weights[k++];
    }

	

	public BigDecimal[] ComputeOutputs(BigDecimal[] xValues) throws Exception {
		if (xValues.length != numInput)
			throw new Exception("Inputs array length " + inputs.length
					+ " does not match NN numInput value " + numInput);

		for (int i = 0; i < numHidden; ++i)
			ihSums[i] = new BigDecimal(0.0);
		
		
		for (int i = 0; i < numOutput; ++i)
			hoSums[i] = new BigDecimal(0.0);
		
		for (int i = 0; i < xValues.length; ++i)
			// copy x-values to inputs
			this.inputs[i] = xValues[i];
		
		// System.out.println("Inputs:");
		// Helpers.ShowVector(this.inputs);

//		 System.out.println("input-to-hidden weights:");
//		 Helpers.ShowMatrix(this.ihWeights, -1);

		for (int j = 0; j < numHidden; ++j){
			// compute input-to-hidden weighted sums
			for (int i = 0; i < numInput; ++i){
				ihSums[j] =ihSums[j].add(this.inputs[i].multiply(ihWeights[i][j]));
				//System.out.println(ihSums[j]+" += "+ inputs[i] + "*" +ihWeights[i][j]);
			}
	}
		
		
		//System.out.println("input-to-hidden sums:");
		//Helpers.ShowVector(this.ihSums);

		// System.out.println("input-to-hidden biases:");
		// Helpers.ShowVector(ihBiases);
		
		for (int i = 0; i < numHidden; ++i)
			// add biases to input-to-hidden sums
			ihSums[i] =ihSums[i].add(ihBiases[i]);
		
		//System.out.println("\ninput-to-hidden sums after adding i-h biases:");
		//Helpers.ShowVector(this.ihSums);

		for (int i = 0; i < numHidden; ++i)
			// determine input-to-hidden output
			// ihOutputs[i] = StepFunction(ihSums[i]); // step function
			ihOutputs[i] = SigmoidFunction(ihSums[i]);

		// System.out.println("\ninput-to-hidden outputs after sigmoid:");
		// Helpers.ShowVector(this.ihOutputs);

		// System.out.println("hidden-to-output weights:");
		// Helpers.ShowMatrix(hoWeights, -1);

		for (int j = 0; j < numOutput; ++j)
			// compute hidden-to-output weighted sums
			for (int i = 0; i < numHidden; ++i)
				hoSums[j] =hoSums[j].add( ihOutputs[i].multiply(hoWeights[i][j]));

		// System.out.println("hidden-to-output sums:");
		// Helpers.ShowVector(hoSums);

		// System.out.println("hidden-to-output biases:");
		// Helpers.ShowVector(this.hoBiases);

		for (int i = 0; i < numOutput; ++i)
			// add biases to input-to-hidden sums
			hoSums[i] =hoSums[i].add( hoBiases[i]);

		//System.out.println("hidden-to-output sums after adding h-o biases:");
		//Helpers.ShowVector(hoSums);

		
//		for (int i = 0; i < numOutput; ++i)   // determine hidden-to-output result
//			+      this.outputs[i] = HyperTanFunction(hoSums[i]);
//			+
//			+    double[] result = new double[numOutput];
//			+    result = this.outputs;
//			+    
//			+    return result;
//		
		
		for (int i = 0; i < numOutput; ++i)
			// determine hidden-to-output result
			outputs[i] = SoftmaxFunction(hoSums[i],hoSums);
		BigDecimal[] result = new BigDecimal[numOutput];
		result = outputs;	
		//System.out.printf("NEW RESULTS: ",result);
		//System.out.println("Debug");
		return result;
	} // ComputeOutputs

	public void step1(BigDecimal[] tValues){
	      double[] oGrads2 = new double[oGrads.length];
	      // 1. compute output gradients
	      for (int i = 0; i < oGrads.length; ++i)
	      {
	        BigDecimal derivative =(new BigDecimal(1).subtract(ihOutputs[i])).multiply(ihOutputs[i]); //(1 - outputs[i]) * (1 + outputs[i]); // derivative of tanh
	        oGrads[i] = derivative.multiply((tValues[i].subtract(outputs[i])));
	        
	        //double derivative2 = ((1-ihOutputs[i].doubleValue())*ihOutputs[i].doubleValue());
	        oGrads2[i]=derivative.doubleValue()*(tValues[i].doubleValue()-(outputs[i].doubleValue()));
	        
	      //  System.out.println("BIG DEC");
	       // System.out.println(oGrads[i]);
	        //System.out.println("Double");
	        //System.out.println(oGrads2[i]);
	        
	      }
	}
	
	public void step2(){
	      double[] hGrads2 = new double[hGrads.length];
	      for (int i = 0; i < hGrads.length; ++i)
	      {
	        BigDecimal derivative = (new BigDecimal(1).subtract(ihOutputs[i])).multiply(ihOutputs[i]); // (1 / 1 + exp(-x))'  -- using output value of neuron
	        BigDecimal sum = new BigDecimal(0.0);
	        double sum2=0.0;
	        for (int j = 0; j < numOutput; ++j){ // each hidden delta is the sum of numOutput terms
	          sum =sum.add( oGrads[j].multiply(hoWeights[i][j])); // each downstream gradient * outgoing weight
	          sum2=sum2+(oGrads[j].doubleValue()*(hoWeights[i][j].doubleValue()));
	          
//	        System.out.println("BIG DEC");
//	        System.out.println(sum);
//	        System.out.println("Double");
//	        System.out.println(sum2);
//	         
	          
	        }
	        hGrads[i] = derivative.multiply(sum);
	        hGrads2[i] = derivative.doubleValue()*sum2;
//	      System.out.println("BIG DEC");
//	      System.out.println(hGrads2[i]);
//	      System.out.println("Double");
//	      System.out.println(hGrads2[i]);

	      }
	}
	
	void step3(BigDecimal eta, BigDecimal alpha){
		 //double delta2=0;
		 for (int i = 0; i < ihWeights.length; ++i) // 0..2 (3)
	      {
	        for (int j = 0; j < ihWeights[0].length; ++j) // 0..3 (4)
	        {
	          BigDecimal delta = eta.multiply(hGrads[j]).multiply(inputs[i]); // compute the new delta        
	          //delta2 = eta.doubleValue()*(hGrads[j].doubleValue()*(inputs[]))
	          ihWeights[i][j] =ihWeights[i][j].add( delta); // update
	          ihWeights[i][j] =ihWeights[i][j].add( alpha.multiply(ihPrevWeightsDelta[i][j])); // add momentum using previous delta. on first pass old value will be 0.0 but that's OK. 
	        }
	      }

	      // 3b. update input to hidden biases
	      for (int i = 0; i < ihBiases.length; ++i)
	      {
	        BigDecimal delta = eta.multiply(hGrads[i]).multiply(new BigDecimal(1.0)); // the 1.0 is the constant input for any bias; could leave out
	        ihBiases[i] = ihBiases[i].subtract(delta);
	        ihBiases[i] = ihBiases[i].add( alpha.multiply(ihPrevBiasesDelta[i]));
	      }
	}
	
	public void step4(BigDecimal eta, BigDecimal alpha){
		
		
		   for (int i = 0; i < hoWeights.length; ++i)  // 0..3 (4)
		      {
		        for (int j = 0; j < hoWeights[0].length; ++j) // 0..1 (2)
		        {
		          BigDecimal delta = eta.multiply(oGrads[j]).multiply(ihOutputs[i]);  // see above: ihOutputs are inputs to next layer
		          hoWeights[i][j] = hoWeights[i][j].add(delta);
		          hoWeights[i][j] = hoWeights[i][j].add( alpha.multiply(hoPrevWeightsDelta[i][j]));
		          hoPrevWeightsDelta[i][j] = delta;
		        }
		      }

		      // 4b. update hidden to output biases
		      for (int i = 0; i < hoBiases.length; ++i)
		      {
		        BigDecimal delta = eta.multiply(oGrads[i]).multiply(new BigDecimal(1.0));
		        hoBiases[i] = hoBiases[i].add(delta);
		        hoBiases[i] = hoBiases[i].add(alpha.multiply(hoPrevBiasesDelta[i]));
		        hoPrevBiasesDelta[i] = delta;
		      }
	}
	
    
    public void UpdateWeights(BigDecimal[] tValues, BigDecimal eta, BigDecimal alpha) throws Exception // update the weights and biases using back-propagation, with target values, eta (learning rate), alpha (momentum)
    {
      // assumes that SetWeights and ComputeOutputs have been called and so all the internal arrays and matrices have values (other than 0.0)
      if (tValues.length != numOutput)
        throw new Exception("target values not same length as output in UpdateWeights");

/*      double[] oGrads2 = new double[oGrads.length];
      // 1. compute output gradients
      for (int i = 0; i < oGrads.length; ++i)
      {
        BigDecimal derivative =(new BigDecimal(1).subtract(ihOutputs[i])).multiply(ihOutputs[i]); //(1 - outputs[i]) * (1 + outputs[i]); // derivative of tanh
        oGrads[i] = derivative.multiply((tValues[i].subtract(outputs[i])));
        
        //double derivative2 = ((1-ihOutputs[i].doubleValue())*ihOutputs[i].doubleValue());
        oGrads2[i]=derivative.doubleValue()*(tValues[i].doubleValue()-(outputs[i].doubleValue()));
        
      //  System.out.println("BIG DEC");
       // System.out.println(oGrads[i]);
        //System.out.println("Double");
        //System.out.println(oGrads2[i]);
        
      }*/
      
      step1(tValues);

      // 2. compute hidden gradients
      
/*      double[] hGrads2 = new double[hGrads.length];
      for (int i = 0; i < hGrads.length; ++i)
      {
        BigDecimal derivative = (new BigDecimal(1).subtract(ihOutputs[i])).multiply(ihOutputs[i]); // (1 / 1 + exp(-x))'  -- using output value of neuron
        BigDecimal sum = new BigDecimal(0.0);
        double sum2=0.0;
        for (int j = 0; j < numOutput; ++j){ // each hidden delta is the sum of numOutput terms
          sum =sum.add( oGrads[j].multiply(hoWeights[i][j])); // each downstream gradient * outgoing weight
          sum2=sum2+(oGrads[j].doubleValue()*(hoWeights[i][j].doubleValue()));
          
//        System.out.println("BIG DEC");
//        System.out.println(sum);
//        System.out.println("Double");
//        System.out.println(sum2);
//         
          
        }
        hGrads[i] = derivative.multiply(sum);
        hGrads2[i] = derivative.doubleValue()*sum2;
//      System.out.println("BIG DEC");
//      System.out.println(hGrads2[i]);
//      System.out.println("Double");
//      System.out.println(hGrads2[i]);

      }*/
      step2();

      // 3. update input to hidden weights (gradients must be computed right-to-left but weights can be updated in any order
    /*  for (int i = 0; i < ihWeights.length; ++i) // 0..2 (3)
      {
        for (int j = 0; j < ihWeights[0].length; ++j) // 0..3 (4)
        {
          BigDecimal delta = eta.multiply(hGrads[j]).multiply(inputs[i]); // compute the new delta        
          ihWeights[i][j] =ihWeights[i][j].add( delta); // update
          ihWeights[i][j] =ihWeights[i][j].add( alpha.multiply(ihPrevWeightsDelta[i][j])); // add momentum using previous delta. on first pass old value will be 0.0 but that's OK. 
        }
      }

      // 3b. update input to hidden biases
      for (int i = 0; i < ihBiases.length; ++i)
      {
        BigDecimal delta = eta.multiply(hGrads[i]).multiply(new BigDecimal(1.0)); // the 1.0 is the constant input for any bias; could leave out
        ihBiases[i] = ihBiases[i].subtract(delta);
        ihBiases[i] = ihBiases[i].add( alpha.multiply(ihPrevBiasesDelta[i]));
      }*/
      
      step3(eta, alpha);

      // 4. update hidden to output weights
   /*   for (int i = 0; i < hoWeights.length; ++i)  // 0..3 (4)
      {
        for (int j = 0; j < hoWeights[0].length; ++j) // 0..1 (2)
        {
          BigDecimal delta = eta.multiply(oGrads[j]).multiply(ihOutputs[i]);  // see above: ihOutputs are inputs to next layer
          hoWeights[i][j] = hoWeights[i][j].add(delta);
          hoWeights[i][j] = hoWeights[i][j].add( alpha.multiply(hoPrevWeightsDelta[i][j]));
          hoPrevWeightsDelta[i][j] = delta;
        }
      }

      // 4b. update hidden to output biases
      for (int i = 0; i < hoBiases.length; ++i)
      {
        BigDecimal delta = eta.multiply(oGrads[i]).multiply(new BigDecimal(1.0));
        hoBiases[i] = hoBiases[i].add(delta);
        hoBiases[i] = hoBiases[i].add(alpha.multiply(hoPrevBiasesDelta[i]));
        hoPrevBiasesDelta[i] = delta;
      }*/
      
      step4(eta, alpha);
    }

    public BigDecimal[] GetWeights()
    {
      int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
      BigDecimal[] result = new BigDecimal[numWeights];
      int k = 0;
      for (int i = 0; i < ihWeights.length; ++i)
        for (int j = 0; j < ihWeights[0].length; ++j)
          result[k++] = ihWeights[i][j];
      for (int i = 0; i < ihBiases.length; ++i)
        result[k++] = ihBiases[i];
      for (int i = 0; i < hoWeights.length; ++i)
        for (int j = 0; j < hoWeights[0].length; ++j)
          result[k++] = hoWeights[i][j];
      for (int i = 0; i < hoBiases.length; ++i)
        result[k++] = hoBiases[i];
      return result;
    }

	private  BigDecimal SigmoidFunction(BigDecimal ihSums2) {	
//		 if (x < -45.0) return 0.0;
//		  else if (x > 45.0) return 1.0;
//		  else {
		//setScale(1000, BigDecimal.ROUND_FLOOR);
		//BigDecimal b = BigDecimalMath.pow(new BigDecimal(Math.E).setScale(100, BigDecimal.ROUND_FLOOR), (new BigDecimal(-1).multiply(ihSums2).setScale(100,BigDecimal.ROUND_FLOOR))) ;
		
		double bb = Math.pow(Math.E,((-1)*(ihSums2.doubleValue())));
		BigDecimal b = new BigDecimal(bb);
		b=b.add(new BigDecimal(1));
		
//		System.out.println("double");
//		System.out.println(bbb);
//		System.out.println("GOSHO:  ");
//		System.out.println(b);
//		
		b=new BigDecimal(1).divide(b,MathContext.DECIMAL128);

		
		//double constant = (double)1; 
//		BigDecimal test = new BigDecimal(b);
//		BigDecimal test2 = new BigDecimal(constant);
//		BigDecimal test3= test.add(test2);
//		//String w = test3.toString();
		//Double value=Double.valueOf(w);
		//double g = Double.parseDouble(w);
		//BigDecimal bd = new BigDecimal(Double.toString(b));
		//test3 = test3.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		//double g = test3..doubleValue();
		//BigDecimal bd = new BigDecimal(test3).setScale(2, RoundingMode.HALF_EVEN);
		//test3.setScale(w.length(), RoundingMode.HALF_EVEN);
		//double test4 = test3.doubleValue();

		//double a  =(1.0 / (1.0 + Math.exp(-1*ihSums2)));//(1.0 / (1.0 + Math.exp(-x)));
		 return b;
	}

	private BigDecimal SoftmaxFunction(BigDecimal hoSums2, BigDecimal[] hoSums3) {

			//BigDecimal sum= new BigDecimal(0);
			//for (int i = 0; i < hoSums3.length; i++) {
			//	sum=sum.add(BigDecimalMath.pow(new BigDecimal(Math.E).setScale(100, BigDecimal.ROUND_FLOOR),hoSums3[i].setScale(100, BigDecimal.ROUND_FLOOR)));
			//}
			double sum2 =0.0;
			for(int i =0; i<hoSums3.length;i++){
				sum2 = sum2 + (Math.pow(Math.E, hoSums3[i].doubleValue()));
			}
			/// sys sum stava
			

			
			//System.out.println("===========sum==================");
			//System.out.println(sum);
			//BigDecimal b =BigDecimalMath.pow(new BigDecimal(Math.E).setScale(100, BigDecimal.ROUND_FLOOR), (hoSums2.setScale(100, BigDecimal.ROUND_FLOOR)));
			//System.out.println("==========b==================");
			double bb=Math.pow(Math.E, hoSums2.doubleValue());
			
			BigDecimal sum = new BigDecimal(sum2);
			BigDecimal b = new BigDecimal(bb);
			
			// i za B vaji 
			
			//System.out.println(b);
			BigDecimal a  =b.divide(sum,MathContext.DECIMAL128);
			
//			double bbb=bb/(sum2);
//			System.out.println("double");
//			System.out.println(bbb);
//			//double gosho = sum.doubleValue();
//			System.out.println("GOSHO:  ");
//			System.out.println(a);
//			
			
			return a;	
		//}
	}

} // class NeuralNetwork