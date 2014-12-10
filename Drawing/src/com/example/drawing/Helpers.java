package com.example.drawing;

public class Helpers
{
  public static double[][] MakeMatrix(int rows, int cols)
  {
    double[][] result = new double[rows][];
    for (int i = 0; i < rows; ++i)
      result[i] = new double[cols];
    return result;
  }

  public static void ShowVector(double[] vector)
  {
    for (int i = 0; i < vector.length; ++i)
    {
      if (i > 0 && i % 12 == 0) // max of 12 values per row 
        System.out.println("");
          if (vector[i] >= 0.0) System.out.println(" ");
      System.out.println(vector[i]); // 2 decimals
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