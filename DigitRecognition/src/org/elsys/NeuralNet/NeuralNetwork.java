package org.elsys.NeuralNet;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class NeuralNetwork {
	private int numInput;
	private int numHidden;
	private int numOutput;

	private double[] inputs;
	private double[][] ihWeights; // input-to-hidden
	private double[] ihSums;
	private double[] ihBiases;
	private BigDecimal[] ihOutputs;

	private double[][] hoWeights; // hidden-to-output
	private double[] hoSums;
	private double[] hoBiases;
	private double[] outputs;

	private double[] oGrads; // output gradients for back-propagation
	private double[] hGrads; // hidden gradients for back-propagation

	private double[][] ihPrevWeightsDelta; // for momentum with back-propagation
	private double[] ihPrevBiasesDelta;

	private double[][] hoPrevWeightsDelta;
	private double[] hoPrevBiasesDelta;

	public NeuralNetwork(int numInput, int numHidden, int numOutput)
    {
      this.numInput = numInput;
      this.numHidden = numHidden;
      this.numOutput = numOutput;

      inputs = new double[numInput];
      ihWeights = Helpers.MakeMatrix(numInput, numHidden);
      ihSums = new double[numHidden];
      ihBiases = new double[numHidden];
      ihOutputs = new BigDecimal[numHidden];
      hoWeights = Helpers.MakeMatrix(numHidden, numOutput);
      hoSums = new double[numOutput];
      hoBiases = new double[numOutput];
      outputs = new double[numOutput];

      oGrads = new double[numOutput];
      hGrads = new double[numHidden];

      ihPrevWeightsDelta = Helpers.MakeMatrix(numInput, numHidden);
      ihPrevBiasesDelta = new double[numHidden];

      hoPrevWeightsDelta = Helpers.MakeMatrix(numHidden, numOutput);
      hoPrevBiasesDelta = new double[numOutput];
    }



	public void SetWeights(final double[] weights) throws Exception
    {
      // copy weights and biases in weights[] array to i-h weights, i-h biases, h-o weights, h-o biases
      int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
      if (weights.length != numWeights)
        throw new Exception("The weights array length: " + weights.length + " does not match the total number of weights and biases: " + numWeights);

    //  k points into weights param

    Runnable first = new Runnable() {
		
		@Override
		public void run() {
			int k=0;
			 for (int i = 0; i < numInput; ++i)
			        for (int j = 0; j < numHidden; ++j)
			          ihWeights[i][j] = weights[k++];
		}
	};  
     
	Runnable second = new Runnable() {
		
		@Override
		public void run() {
			int k = numInput*numHidden;
		      for (int i = 0; i < numHidden; ++i)
		          ihBiases[i] = weights[k++];
		}
	};
     
	Runnable third = new Runnable() {
		
		@Override
		public void run() {
			int k = (numInput*numHidden)+numHidden;
			for (int i = 0; i < numHidden; ++i)
		        for (int j = 0; j < numOutput; ++j)
		          hoWeights[i][j] = weights[k++];
			
		}
	};

	Runnable fourth = new Runnable() {
		
		@Override
		public void run() {
			int k = (numInput*numHidden)+numHidden+(numHidden*numOutput);
		    for (int i = 0; i < numOutput; ++i)
		        hoBiases[i] = weights[k++];
			
		}
	};
      
	Thread firstThread = new Thread(first);
	Thread secondThread = new Thread(second);
	Thread thirdThread = new Thread(third);
	Thread fourthThread = new Thread(fourth);
	
	firstThread.start();
	secondThread.start();
	thirdThread.start();
	fourthThread.start();
	
	firstThread.join();
	secondThread.join();
	thirdThread.join();
	fourthThread.join();
	
	
    }
	

	public double[] ComputeOutputs(double[] xValues) throws Exception {
		 		if (xValues.length != numInput)
					throw new Exception("Inputs array length " + inputs.length
							+ " does not match NN numInput value " + numInput);
		
				for (int i = 0; i < numHidden; ++i)
					ihSums[i] = 0.0;
				
				
				for (int i = 0; i < numOutput; ++i)
					hoSums[i] = 0.0;
				
				for (int i = 0; i < xValues.length; ++i)
					inputs[i] = xValues[i];
				
				for (int j = 0; j < numHidden; ++j){
					// compute input-to-hidden weighted sums
					for (int i = 0; i < numInput; ++i){
						ihSums[j] += inputs[i] * ihWeights[i][j];
						
					}
				}
						
				for (int i = 0; i < numHidden; ++i){
					// add ihBiases to input-to-hidden weighted sums
					ihSums[i] += ihBiases[i];
				}
				for (int i = 0; i < numHidden; ++i){
					// activation function 
					ihOutputs[i] = SigmoidFunction(new BigDecimal(ihSums[i]));
				}
				
				for (int j = 0; j < numOutput; ++j){
					// compute hidden-to-output weighted sums
					for (int i = 0; i < numHidden; ++i){
						hoSums[j] += (ihOutputs[i].multiply(new BigDecimal(hoWeights[i][j]))).doubleValue();;
					}
				}
				
				
				for (int i = 0; i < numOutput; ++i){
					// add ihBiases to input-to-hidden weighted sums
					hoSums[i] += hoBiases[i];
				}
				
				for (int i = 0; i < numOutput; ++i){
					//output function
					outputs[i] = SoftmaxFunction(new BigDecimal(hoSums[i]),hoSums);
				}
				
				return outputs;
			} // ComputeOutputs
	
	public void step1(double[] tValues){
	   
	            // 1. compute output gradients
	            for (int i = 0; i < oGrads.length; ++i)
	            {
	            	double derivative = 1.0;//((new BigDecimal(1).subtract(ihOutputs[i])).multiply(ihOutputs[i])).doubleValue();//derivative of softmax
	            	oGrads[i] = derivative * (tValues[i] - outputs[i]);
	            }
	}
	
	public void step2(){
	   // 2. compute hidden gradients
	            for (int i = 0; i < hGrads.length; ++i)
	            {            	
	            double derivative = ((new BigDecimal(1).subtract(ihOutputs[i])).multiply(ihOutputs[i])).doubleValue();

	            double sum = 0.0;
	            for (int j = 0; j < numOutput; j++) {
	            	sum+=oGrads[j]*hoWeights[i][j];
	            }
	            	hGrads[i]=derivative*(sum);
				
	            }

	}
	

	
	
	void step3(double eta, double alpha){
		 // 3. update input to hidden weights (gradients must be computed right-to-left but weights can be updated in any order
		      for (int i = 0; i < ihWeights.length; ++i) 
		      {
		        for (int j = 0; j < ihWeights[0].length; ++j) 
		        {
		        	double delta2=eta*hGrads[j]*inputs[i];
		        	ihWeights[i][j] +=delta2;//update
		        	ihWeights[i][j]+=alpha*ihPrevWeightsDelta[i][j];
		        }
		      }

		      // 3b. update input to hidden biases
		            for (int i = 0; i < ihBiases.length; ++i)
		            {

		            	double delta = eta*hGrads[i]*1.0;
		            	ihBiases[i]+=delta;
		            	ihBiases[i]+=alpha*ihPrevBiasesDelta[i];
		            }
	}
	
	
    
	public void step4(double eta, double alpha){
		
		
		// 4. update hidden to output weights
		      for (int i = 0; i < hoWeights.length; ++i)  // 0..3 (4)
		      {
		        for (int j = 0; j < hoWeights[0].length; ++j) // 0..1 (2)
		        {
		    
		        	double delta = (new BigDecimal(eta).setScale(10, RoundingMode.HALF_UP).multiply(new BigDecimal(oGrads[j]).setScale(10, RoundingMode.HALF_UP)).multiply(ihOutputs[i]).setScale(10, RoundingMode.HALF_UP)).doubleValue();
		        	hoWeights[i][j] += delta;
		        	hoWeights[i][j] += alpha * hoPrevWeightsDelta[i][j];
		        	hoPrevWeightsDelta[i][j] = delta;
		        
		        }
		      }
		
		      // 4b. update hidden to output biases
		      for (int i = 0; i < hoBiases.length; ++i)
		      {
		    
		    	  double delta = eta * oGrads[i];
		    	  hoBiases[i] += delta;
		    	  hoBiases[i] += alpha * hoPrevBiasesDelta[i];
		    	  hoPrevBiasesDelta[i] = delta;
		      
		      }
	}
	
    
    public void UpdateWeights(double[] tValues, double eta, double alpha) throws Exception // update the weights and biases using back-propagation, with target values, eta (learning rate), alpha (momentum)
    {
      // assumes that SetWeights and ComputeOutputs have been called and so all the internal arrays and matrices have values (other than 0.0)
      if (tValues.length != numOutput)
        throw new Exception("target values not same length as output in UpdateWeights");

      step1(tValues);
      step2();
      step3(eta, alpha);
      step4(eta, alpha);

    }

    public double[] GetWeights()
    {
      int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
      double[] result = new double[numWeights];
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
		
		double bb = Math.pow(Math.E,((-1)*(ihSums2.doubleValue())));
		BigDecimal b = new BigDecimal(bb);
		b=b.add(new BigDecimal(1));
		b=new BigDecimal(1).divide(b,MathContext.DECIMAL128);
		 return b;
	}

	private double SoftmaxFunction(BigDecimal hoSums2, double[] hoSums3) {

			double sum2 =0.0;
			for(int i =0; i<hoSums3.length;i++){
				sum2 = sum2 + (Math.pow(Math.E, hoSums3[i]));
			}
			double bb=Math.pow(Math.E, hoSums2.doubleValue());
			double a=bb/sum2;

			return a;	
	}

} // class NeuralNetwork