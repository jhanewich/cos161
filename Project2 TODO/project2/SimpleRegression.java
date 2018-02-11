package project2;

import java.util.*;
import java.io.*;
import java.awt.*;


/************************************************************

A template class that you should use to solve the basic version of
the  second programming project.


YOU SHOULD WRITE CODE TO

1. declare a static variable for holding an array of Point objects
   and variables to hold the results

2. load the array from the data file given on the command line,
   setting numberOfPoints, atLeastTwoXs and atLeastTwoYs
   correctly

3. calculate the thirteen values requested in the basic assignment

4. call the displayOutput method we supply to print these values to 
   standard out.  

5. create the Turtle, plot the points in blue and afterwards, the line
   in black


***************************************************************/
public class SimpleRegression{

   private static boolean
      READ_LINE_BEFORE_TERMINATION;

   private static final int
      MAX_POINTS = 2000;

   private static int
      numberOfPoints;

   private static Scanner
      stdIn = new Scanner(System.in),
      infile;

   private static boolean
      atLeastTwoXs,    // true exactly when the variance of X is > 0
      atLeastTwoYs;    // true exactly when the variance of Y is > 0

/**

The method you must use to display your calculated results.

The code you write should not produce any text to standard out, so I can
do simple file compares to test your programs.

The parameter names and text labels should make it plain what values are intended.


***************************************************************************/

   private static void displayOutput(
      int numPoints,
      double xMax,
      double yMax,
      double xAve,
      double yAve,
      double varX,
      double varY,
      double covXY,
      double beta0,
      double beta1,
      double SSres,
      double SStot,
      double RSqrd
       ) // this closes the parameter list for the method


    {

      if (numPoints < 3 || varX == 0 || varY == 0 || SStot == 0) 
         // these are the conditions that would lead to division by 0;
         // your code should check for that, so this test should never
         // evaluate to true; if you are setting numberOfPoints, atLeastTwoXs,
         // and atLeastTwoYs correctly, and calculating the results correctly,
         // this code should not be executed

         System.out.println("Not enough distinct points in the data file.");
      else{
         System.out.println("Number of points = " + numPoints);
         System.out.println("Maximum X value = " + String.format("%1.4f",xMax));
         System.out.println("Maximum Y value = " + String.format("%1.4f",yMax));
         System.out.println("Average X value = " + String.format("%1.4f",xAve));
         System.out.println("Average Y value = " + String.format("%1.4f",yAve));

         System.out.println("Var(X) = " + String.format("%1.4f",varX));
         System.out.println("Var(Y) = " + String.format("%1.4f",varY));
         System.out.println("Covariance(X,Y) = " + String.format("%1.4f",covXY));
         System.out.println("beta0 = " + String.format("%1.4f",beta0));
         System.out.println("beta1 = " + String.format("%1.4f",beta1));
         System.out.println("SSres = " + String.format("%1.4f",SSres));
         System.out.println("SStot = " + String.format("%1.4f",SStot));
         System.out.println("R Squared = " + String.format("%1.4f",RSqrd));
      }
   }




public static void main(String[] args){


   if (args.length == 0){
      System.out.println("Data file name should be on the "
      + "command line.\nProgram terminating.");
      return;
   }
   else{
      try{
         infile = new Scanner(new File(args[0]));
         if (args.length > 1)
            READ_LINE_BEFORE_TERMINATION = true;

      }
      catch(Exception e){
         System.out.println("Unable to create the scanner from "
         + args[0]);
         e.printStackTrace();
         System.exit(0);
      }
   }

   // if control reaches here, the scanner is created

   // YOU MUST ADD CODE TO 
   // read points from the file and load them into the array,
   // counting how many as you read into numberOfPoints and
   // recording in the booleans atLeastTwoXs and atLeastTwoYs
   // whether there are >= 2 distinct X coordinate values and
   // >= 2 distinct Y coordinate values in the input file; 
   

   if (numberOfPoints < 3  ||
      !atLeastTwoXs       ||
      !atLeastTwoYs)
      System.out.println("Data inappropriate for the regression "
      + "calculation.\nProgram terminating.");
   else{

       // YOU MUST ADD CODE TO
       // calculate the thirteen requested data values ;
       // (you may do some or all of the calculations as you read the data in)



       // YOU MUST ADD CODE TO
       // call the displayOutput method here and pass in your results



       // YOU MUST ADD CODE TO
       // create the Turtle and set its scale Turtle as described, plot
       // the points in blue and after plotting the points,
       // draw the regression line in black as  given in the instructions



       // if there is a second argument on the command line, the
       // program will wait until the user enters a line from the
       // keyboard before destroing the Turtle and exiting.
       // In any event it will pause for a second;
       if (READ_LINE_BEFORE_TERMINATION)
          stdIn.nextLine();

       Turtle.render();
       Turtle.pause(1000);
       Turtle.destroy();
    }

   
}
}
         
   
