package org.exampl.neuralnet;

import java.math.BigDecimal;

public class Helpers
{
  public static BigDecimal[][] MakeMatrix(int rows, int cols)
  {
    BigDecimal[][] result = new BigDecimal[rows][];
    for (int i = 0; i < rows; ++i)
      result[i] = new BigDecimal[cols];
    
    for (int i = 0; i < rows; i++) {
		for (int j = 0; j < cols; j++) {
			result[i][j] = new BigDecimal(0);
		}
	}
    return result;
  }

  public static void ShowVector(BigDecimal[] yValues)
  {
    for (int i = 0; i < yValues.length; ++i)
    {
      if (i > 0 && i % 12 == 0) // max of 12 values per row 
        System.out.println("");
          if (yValues[i].compareTo(new BigDecimal(0.0)) >= 0)System.out.println(" ");
      System.out.println(yValues[i]); // 2 decimals
    }
    System.out.println("\n");
  }

  public static void ShowMatrix(double[][] matrix, int numRows){
    int ct = 0;
    int MaxValue = 0;
    if (numRows == -1) {numRows = MaxValue;} // if numRows == -1, show all rows
    for (int i = 0; i < matrix.length && ct < numRows; ++i)
    {
      for (int j = 0; j < matrix[0].length; ++j)
      {
        if (matrix[i][j] >= 0.0) System.out.println(" "); // blank space instead of '+' sign
        System.out.println(matrix[i][j]);
      }
      System.out.println("");
      ++ct;
    }
    System.out.println("");
  }
} // class Helpers