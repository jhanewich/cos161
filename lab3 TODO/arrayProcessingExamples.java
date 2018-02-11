/*

Lab III   Array Processing


Team: John Hanewich


For this lab you are asked to do a few methods to calculate values from a one
dimensional array of real(double) values.

The array A is declared static, and loaded from a file whose name you supply
on the command line.  Of course, either the file must be present in
the working directory of the program, or you must supply the full path name
of the file.  Here are the files, which are in the archive file.

apTD1.txt
apTD2.txt
apTD3.txt
apTD4.txt
apTD5.txt
apTD6.txt
apTD7.txt
apTD8.txt

At the end of each is what the correct output should be for that file.

The story to go with the data file is that it represents a sequence of net changes 
of the cash on hand for some number of days at a company that does a lot of 
buying and selling.  For example, if the actual balances at the end of the day
for an eight day period were


1000 900 1100 800 1200 700 1300 600

then the file would have the seven values

-100 200 -300 400 -500 600 -700


The methods

private static void loadA(Scanner f)  -- loads the static array A from
                                         the file, up to MAX_LIST_SIZE values

private static double overallChange() -- calculates the effect of the entire
                                      sequence on the initial value by summing
                                      the array components

private static double[] bestNetChange() -- finds the maximum value, negative
                                           infinity if A is empty, and its location
                                           in A, -1 if A is empty
                                                                              
private static int lengthOfLongestNegSequence() -- returns length of longest 
                                   sequence of values that are all < 0, or 0
                                   there are no values in A < 0

are coded as examples.  You can adapt their code to the methods you are asked
to code if it appears useful.

You are asked to code as many of the methods

private static  int countPositives()
private static double averageChange()
private static int lengthOfLongestDeterioratingSequence()
private static int numberOfLocalMinima()

as you can.

The first two and the last are not too difficult.  The third is a little 
harder.  You should try to get three of them, and four if you are ambitious.


The notes file on list processing techniques is a companion
to this exercise and discusses the approach we recommend: as you scan the
list of values, maintain residual values summarizing what information you
need from the part of the list that is already scanned.

 ************************************************************/

import java.io.*;
import java.util.*;

/**
 * @author John Hanewich
 *
 */
public class arrayProcessingExamples {
   
   // most data that can be accomodated.
   private static final int MAX_LIST_SIZE = 1000;
   
   private static Scanner f;
   
   private static double[] A = new double[MAX_LIST_SIZE];
   
   /**********************************************************/
   
   // loads the static array A from the scanner f
   // up to MAX_LIST_SIZE doubles
   
 /**
 * @param f, f is a scanner object.
 */
private static void loadA(Scanner f) {
      
      int countSoFar = 0;
      
      // load A until there are no more doubles in f or
      // there are no more empty cells in A
      while (countSoFar < MAX_LIST_SIZE && f.hasNextDouble())
         A[countSoFar++] = f.nextDouble();
   
 
      if (f.hasNextDouble()){
         //
         // Here's a little application of boolean logic. 
         // !(p && q) == !p || !q
         // So, when the loop test fails, we have
         //  countSoFar >= MAX_LIST_SIZE ||  !f.hasNextDouble()
         // When we have  p || !q, and we also have q, we can deduce
         // p, giving us p && q, so after the test of the if, we
         // have 
         //  countSoFar >= MAX_LIST_SIZE && f.hasNextDouble()
         // which justifies the error message.
         //
         // You study logic in MAT 145, and it's absolutely
         // essential for programming.

         System.out.println("Cannot read more than " + MAX_LIST_SIZE
               + " doubles.  Extras were ignored.");
         return;
      }

      // cut A'a allocated memory back to its size if necessary;
      // it would be cheaper to use countSoFar globally to
      // indicate how much of A is loaded, but we want to show
      // the use of A.length below.  This also shows how reference 
      // variables work.

      if (countSoFar < MAX_LIST_SIZE) {
         
         // create B exactly right size
         double[] B = new double[countSoFar];
         
         // copy A to B
         for (int i = 0; i < countSoFar; i++)
            B[i] = A[i];
         
         // make A reference B
         A = B;
      }
      
   }
   

   /****************************************************************

   Calculates the net change over the entire sequence by summing 
   the array components.

   */   

 /**
 * @return The net change among the entire array A .
 */
private static double overallChange(){
      
      int 
      N = A.length;

      double
      sum = 0;

      /*

         Note,  if we have a variable var, and a binary operator, op, then
         often in Java, instead of

         var = var op (exp);

         where I put in parens to force that op is the last operator to 
         be evaluated

         you can write

         var op= exp;

         which is a little less typing.  

         here the residual value is the sum of all the
         components that have been examined, and it is
         updated by adding in the current component.

         the loop invariant is 

         0 <= i <= N and sum has A[0] + ... + A[i-1]

      */
      for (int i = 0; i < N; i++)
         sum += A[i];
      
      return sum;
      
   }
   
   /**********************************************************/

   
 /**
 * @return The best net change among the array A .
 */
private static double[] bestNetChange() {
   /*
    * 
    * returns a double array of size 2, res. res[0] is the maximum value in
    * A, or Double.NEGATIVE_INFINITY if A is empty. res[1] is the index where
    * the maximum value first occurs in A, or -1 if A is empty.
    * 
    * I use an array so I can pass back both results.
    * In Java a method can only return a single value, but there are
    * a number of ways to get around that restriction.
    *
    * *********************************************/
      
      double[] 
         res = new double[2];
      
      int
         N = A.length,
         i,
         maxIndex = -1;
      
      double 
         curr,
         maxSoFar = Double.NEGATIVE_INFINITY;
      
      /*
            there are two residuals here, the maximum of
            the scanned values, in maxSoFar, and the location
            in the array where it first appeared, in maxIndex.

            To update them we see if the current value being
            scanned is larger than the maxSoFar value.  If
            not, there is nothing to do.

      */

      for (i = 0; i < N; i++) {
         curr = A[i];
         if (curr > maxSoFar) {
            maxIndex = i;
            maxSoFar = curr;
         }
      }
      
      res[0] = maxSoFar;
      res[1] = maxIndex;
      return res;
      
   }
   
   
   /**********************************************************/

   
 /**
 * @return Length of the longest negative sequence.
 */
private static int lengthOfLongestNegSequence() {
      /*
       * 
       * returns the length of longest sequence of consecutive negative values
       * in A, if there are some negative values in A, or 0 if there are no
       * negative values in A.
       * 
       * ***********************************************
       */
      
      int 
         N = A.length,
         maxNegSeqLen = 0, // any nonempty sequence will have
                           // length > 0
         currNegSeqLen = 0;

      /*

         The residual values are a little more complicated here.

         maxNegSeqLen will be 0 if we have not completely scanned a
         sequence of negative values, otherwise it will be the length
         of the longest sequence of negative values that has been 
         completely scanned.

         currNegSeqLen will be 0 if we are not at this time scanning
         a sequence of negative values, that is, either we are looking
         at the first value, or the last value we looked at was not
         negative.

         We update maxNegSeqLen when we encounter a value >= 0, using
         currNeqSeqLen.  If we encounter a value < 0, then we 
         increment currNeqSeqLen.

      */      
      for (int i = 0; i < N; i++) {
         if (A[i] >= 0) { // may have reached the end of a negative sequence

            // update the max length
            if (currNegSeqLen > maxNegSeqLen)
               maxNegSeqLen = currNegSeqLen;

            // reinitiatlize the length of the current negative sequence;
            // generally, you want to consider how EACH residual variable
            // needs to be modified after scanning the next value of the
            // sequence;
            currNegSeqLen = 0;
            
         }
         else  // A[i] < 0, so we increment the count of negatives in 
               // the current sequence
            currNegSeqLen++;
      }

      // after the loop, in case the last value was < 0, we need to 
      // see if the length of the last sequence of negative
      // values was the longest
      if (currNegSeqLen > maxNegSeqLen)
         maxNegSeqLen = currNegSeqLen;
      
      return maxNegSeqLen;
      
   }
   
   /**********************************************************/

   
 /**
 * @return The number of components A[i], such that A[i] > 0 .
 */
private static int countPositives() {
      /*
       * 
       * Count how many of the components are > 0.
       * 
       * YOU MUST CODE THIS.
       * 
       * *******************************************************************
       */
      
      int 
      N = A.length,
      countOfPositives = 0;
      
      
      return countOfPositives;
      
   }
   
   /**********************************************************/

   
 /**
 * @return Calculate the average change of values in the array. Returns Nan (not a number) if array.size=0 .
 */
private static double averageChange() {
      /*
       * 
       * Calculate the average of the values in the array, or Double.NaN
       * if the array's length is 0.
       * 
       * For a nonempty array, the average is the sum of the values
       * divided by the length of the list.
       *  
       * 
       * 
       * YOU MUST CODE THIS.
       * 
       * 
       * ************************************************************************
       * **************
       */
      
      double average = Double.NaN;
      
      return average;
      
   }
   
   /**********************************************************/

   
 /**
 * @return length of longest deteriorating subsequence. Ex : 10,-15,-20, 0, 10, 20 returns length 3.
 */
private static int lengthOfLongestDeterioratingSequence() {
      /*
       * 
       * return the length of the longest subsequence in A where all the values
       * in the subsequence, except the first, are strictly less than the prior value.
       * Here we are defining the length of the sequence to be the number of values
       * in it, so
       *
       * 10 -10 -15 0 10 20 18 17 16 15 15
       * 
       * has two deteriorating sequences of length > 1
       *
       *  10 -10 -15     of length 3
       *
       * and
       *
       *  20 18 17 16 15 of length 5
       *
       * Any sequence of length 1 can be regarded as a deteriorating sequence.
       * If A is empty, 0 should be returned, but if A is not empty, a value >=
       * 1 should be returned.
       * 
       * 
       * YOU MUST CODE THIS.
       * 
       * ************************************************************************
       */
      
      int N = A.length, maxDetSeqLen = 0; // any nonempty A will have max length
                                          // >= 1
      
      /*

          You need to think of what you would like to have for residual information
          from the part of the array that you have already scanned and consider how
          that residual information should be updated after examination of the 
          current array component.

          Then for each residual value, you declare a variable and and above the
          loop initialize the variable to a value that is appropriate.

          Since the end of a deteriorating sequence is marked by a value that is >=
          the predecessor value, you will need to consider the length of the LAST
          sequence BELOW the loop.

      */
      return maxDetSeqLen;
      
   }
   
   /**********************************************************/

   
 /**
 * @return The number of components A[i], where 1 <= A.length -2 and A[i-1] > A[i] < A[i+1] .
 */
private static int numberOfLocalMinima() {
      /*
       * 
       * returns the number of components A[i], where
       * 
       * 1 <= i <= A.length - 2
       * 
       * and
       * 
       * A[i-1] > A[i] < A[i+1]
       * 
       * this number could be 0 if A.length < 3 or no components satisfy the
       * inequality.
       * 
       * YOU MUST CODE THIS.
       * 
       * ************************************************************************
       */
      
      int 
      i,
      minimaCount = 0,
      N = A.length;
      
      return minimaCount;
   }
   
   /**********************************************************/

     /**
     * @param args File name to process via the command line.
     */
    public static void main(String[] args) {
      
      String fname;

      if (args.length < 1){
         System.out.println("File name must be given on the command line."
         + "\nProgram terminating.");
         System.exit(0);
      }

      try{
         fname = args[0];
         f = new Scanner(new File(fname));
      }
      catch (Exception e){
         System.out.println("Creating the scanner threw an exception.\n");
         e.printStackTrace(System.out);
         System.out.println("Program terminating.");
         System.exit(0);
      }         
      
      loadA(f);
      
      System.out.println("\nA was loaded with " + A.length + " values.");
      
      System.out.println("\nThe overall change for the period was "
            + overallChange() + ".\n");
      
      double[] best = bestNetChange();
      
      if (Double.isInfinite(best[0]))
         System.out.println("\nThere are no values in A.\n");
      else
         System.out.println("\nThe best net change in A is " + best[0]
         + "\nand it first occurs at index "
         + String.format("%1.0f", best[1]));
      
      
      int lngstNegSeqLen = lengthOfLongestNegSequence();
      
      if (lngstNegSeqLen == 0)
         System.out.println("\nThere are no negative sequences in A.\n");
      else
         System.out.println("\nThe length of the longest sequence of negative "
               + "values in A is " + lngstNegSeqLen);
      
      System.out.println("\nThere are " + countPositives()
            + " positive values in A.\n");
      
      double average = averageChange();
      
      if (Double.isNaN(average))
         System.out.println("\nThere are no values in A.\n");
      else
         System.out.println("\nThe average change during the period is "
            + average + ".\n");
      
      int lngstDetSeqLen = lengthOfLongestDeterioratingSequence();
      
      if (lngstDetSeqLen == 0)
         System.out.println("\nThere are no deteriorating sequences in A.\n");
      else
         System.out.println("\nThe length of the longest deteriorating" +
         " sequence of values in A is " + lngstDetSeqLen);
      
      System.out.println("\nThere are " + numberOfLocalMinima()
            + " local minima components in A.\n");
      
   }
}


