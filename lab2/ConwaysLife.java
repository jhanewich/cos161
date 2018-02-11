import java.util.*;
import java.io.*;

/******************************************************************

Lab Exercise II

Code the methods below that you are asked to code, and submit the 
modified class file to Blackboard.  Include the names of the people
on the team right below.

Team members:
John Hanewich





This program plays Conway's famous Game of Life, which is discussed at

http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

The game requires a 2D array of int(or boolean), n by n, for some n >= 3.
Each cell that is not on the border of the array will have EIGHT 
neighboring cells.  Cells in the corner will have only three neighbors,
and cells on the border but not in the corner will have  five neighbors.

Briefly, the initial configuration is a grid of cells such that each 
cell is either occupied with a single organism, or not occupied.  The next
generation, which also will have each cell occupied by a single 
organism or not occupied, is determined from the current by the following rules
(suppose the cell is indexed by [i][j])

If the cell is currently occupied then
   if the number of occupied neighboring cells is < 2
      the organism in cell[i][j] dies from loneliness, so
      cell[i][j] becomes unoccupied
   else if the number of occupied neighboring cells is > 3
      the organism in cell[i][j] dies from overcrowding, so
      cell[i][j] becomes unoccupied
   else // there are  2 or 3 occupied neigbboring cells
      the organism in cell[i][j] survives, so cell[i][j]
      remains occupied
else // the cell is currently unoccupied
   if the number of occupied neighboring cells is exactly 3,
      a new organism is born into cell[i][j], so it becomes
      occupied
   else
      cell [i][j] remains unoccupied


Note, you cannot update a single array in place, since if a
cell changes, it will affect the calculation for its neighboring
cells.  To calculate the change of a single generation, YOU MUST
USE TWO ARRAYS, one for the current generation and a second for the
next generation that is calculated from the current generation.

For convenience, the array has extra rows and columns so that a
cell that might actually hold an organism always has eight neighboring
cells within the array, but the outermost border of the array(topmost and
bottommost rows and leftmost and rightmost columns) should never be
loaded with organisms.


This implementation allows the user to select the size of the board
and key in cell indices that are occupied or read the indices from a text file
and other run parameters.


 ************************************************************************/

public class ConwaysLife{
   
   private static Scanner
   // for reading from the keyboard
   stdIn = new Scanner(System.in),
   // for reading from the file(optional)
   fileScanner;
   
   private static final int
   // values to control the size of the grid
   MIN_GRID_SIZE = 6,
   MAX_GRID_SIZE = 100;
   
   private static int 
   // the size of the non-border array; user selects
   gridSize;
   
   private static int[][]
         // NOTE: if GRID_SIZE is n, that arrays will be (n+2) by (n+2)
         // to hold the extra rows and columns on the border
         // array for the current generation
         currentGeneration,
         // and an array for calculating the next generation
         auxArray;
   
   private static boolean
   // true when the user wants to load the grid randomly
   loadRandomly;
   
   private static boolean
   // true when the user wants to pause between generations
   pauseBetweenGenerations;
   
   private static double
   // when the grid is loaded randomly, the probability that
   // a cell will be occupied
   probOfOccupancyInInitialGrid;
   
   private static int
   // maximum number of generations to calculate
   maxGenerations;
   
   
   private static void getConfigurationParameters(){
      /*

      Prompts the user and reads in

      gridSize

      whether to load the grid randomly or from user keyed cell indices
      (y or Y means yes, load the grid randomly)

      number of generations to print(will quit if the populations dies out)

      whether to pause between printing each generation

       ***************************************************************/
      
      System.out.println("Enter a size n for the grid(" +
            MIN_GRID_SIZE + " <=  n <= " + MAX_GRID_SIZE + ')');
      
      // this illustrates using a try-catch block to avoid throwing
      // an exception
      try{
         gridSize = stdIn.nextInt();
         while (gridSize < MIN_GRID_SIZE || gridSize > MAX_GRID_SIZE){
            System.out.println("Value must be >= " + MIN_GRID_SIZE +
                  " and <= " + MAX_GRID_SIZE + ". Please try again.");
            gridSize = stdIn.nextInt();
         }
      }
      catch (Exception e){
         System.out.println("Error on the input for grid size.\n" + e.toString());
         System.out.println("\nProgram terminating.");
         e.printStackTrace();
         System.exit(0);
      }
      
      // add rows and columns for the border area
      currentGeneration = new int[gridSize + 2][gridSize + 2];
      auxArray = new int[gridSize + 2] [gridSize + 2];
      
      System.out.println("Do you want to load the initial grid randomly?"
            + "\nEnter y or n.");
      // this illustrates using the Scanner's hasNext method to test for the
      // availability of input, an alternative to the try-catch
      if (stdIn.hasNext()){
         char reply = stdIn.next().charAt(0);
         loadRandomly = reply == 'y' || reply == 'Y';
      }
      else{
         System.out.println("Reached end of file without a response.");
         System.out.println("\nProgram terminating.");
         System.exit(0);
      }
      
      // clear the line
      stdIn.nextLine();
      // give user the opportunity to read from a file
      if (!loadRandomly){
         System.out.println("If you would like to read the initial configuration\n"
               + "from a file, enter the name of the file.  If you want to enter\n"
               + "the data from the keyboard, just hit enter.");
         
         try{
            String fileName = stdIn.nextLine();
            if (!fileName.equals("")) // read from a file
               fileScanner = new Scanner(new File(fileName));
         }
         catch(Exception e){
            System.out.println("Some error on the input threw an exception.\n"
                  + "program terminating.");
            e.printStackTrace();
            System.exit(0);
         }
      }
      
      System.out.println("Enter the number of generations to print.");
      
      // this uses the hasNextInt method to skip over input that is
      // not an integer value
      maxGenerations = -1;
      while (maxGenerations < 0){
         while (!stdIn.hasNextInt()){
            System.out.println("Next token is not an integer. Skipping this line.");
            stdIn.nextLine();
         }
         maxGenerations = stdIn.nextInt();
         if (maxGenerations < 0){
            System.out.println("Value must be >= 0.  Please try again.");
         }
      }
      
      
      System.out.println("Do you want to pause after printing each"
            + " generation?\nEnter y or n.");
      try{
         char reply = stdIn.next().charAt(0);
         pauseBetweenGenerations = reply == 'y' || reply == 'Y';
      }
      catch(Exception e){
         System.out.println("Error on the input.\n" + e.toString());
         System.out.println("\nProgram terminating.");
         e.printStackTrace();
         System.exit(0);
      }
      // to clear the current line;
      stdIn.nextLine();
      
      
   }
   
   // provides the initial contents for currentGeneration and
   // returns true when at LEAST ONE of the cells has an
   // occupant
   private static boolean loadInitialConfiguration(){
      int i, j, organismCount = 0, goalOrganismCount;
      boolean stillEmpty = true;
      
      if (loadRandomly){
         // get the probability of a cell being occupied from the user
         System.out.println("What do you want for the probability"
               + " of occupancy?.\nMust be between 0 and 1.");
         try{
            probOfOccupancyInInitialGrid = stdIn.nextDouble();
            while (probOfOccupancyInInitialGrid <= 0 || probOfOccupancyInInitialGrid >= 1){
               System.out.println("Value must be strictly between 0 and 1"
                     + ".  Please try again.");
               probOfOccupancyInInitialGrid = stdIn.nextDouble();
            }
            // push to the next line to empty the buffer
            stdIn.nextLine();
         }
         catch (Exception e){
            System.out.println("Error on the input.\n" + e.toString());
            System.out.println("\nProgram terminating.");
            e.printStackTrace();
            System.exit(0);
         }
         // determine how many organism should be entered
         goalOrganismCount = (int)(probOfOccupancyInInitialGrid * gridSize * gridSize) + 1;
         System.out.println("We will randomly choose cells to load " + goalOrganismCount +
         " organism" + (goalOrganismCount > 1 ? "s" : "") + '.');
         if (goalOrganismCount >= gridSize * gridSize) // fill the array
            for (i = 1; i <= gridSize; i++)
               for (j = 1; j <= gridSize; j++)
                  currentGeneration[i][j] = 1;
         else{
            // it will have to load at least one
            stillEmpty = false;
            
            
            // keep trying until goalOrganismCount have been loaded into the array
            // scanning row by row from left to right
            i = j = 1;
            while (organismCount < goalOrganismCount){
               if (currentGeneration[i][j] == 0) // skip if already loaded
                  // roll the dice and load if they come up right
                  if (Math.random() <= probOfOccupancyInInitialGrid){
                     currentGeneration[i][j] = 1;
                     organismCount++;
                  }
               // advance
               j++;
               if (j > gridSize){ 
                  // end of the row
                  if (i > gridSize)
                     // go back to first row
                     i = 1;
                  else
                     i++;
                  j = 1;
               }
               else
                  j++;
            }
         }
         return true;
      }
      else{ // not loading randomly
         if (fileScanner == null){// loading from the keyboard
            System.out.println("Enter row and column indices "
                  + "for occupied cells, which\nmust be between 1 and " +
                  gridSize + ". Entry will stop if it encounters eof or \nthe first time"
                  + " an invalid index value is entered.");
            fileScanner = stdIn;
         }
         
         // quits when it reaches eof or the first time it reads
         // a value that is not in the grid range or the next token
         // is not an integer
         while (fileScanner.hasNextInt()){
            i = fileScanner.nextInt();
            if (i >= 1 && i <= gridSize){
               if (fileScanner.hasNextInt()){
                  j = fileScanner.nextInt();           
                  if (j >= 1 && j <= gridSize){
                     stillEmpty = false;
                     currentGeneration[i][j] = 1;
                  }
                  else
                     return !stillEmpty;
               }
               else
                  return !stillEmpty;
            }
            else
               return !stillEmpty;
         }
         return !stillEmpty;
      }
   }
   
    private static int neighborCount(int i, int j) {
        int count= 0;
        count+=currentGeneration[i-1][j];
        count+=currentGeneration[i+1][j];
        count+=currentGeneration[i][j-1];
        count+=currentGeneration[i][j+1];
        count+=currentGeneration[i-1][j-1];
        count+=currentGeneration[i+1][j+1];
        count+=currentGeneration[i-1][j+1];
        count+=currentGeneration[i+1][j-1];
        return count;
    }

    
    private static boolean cellIsOccupiedInNextGen(int i, int j) {
        int neighbors = neighborCount(i, j);
        boolean alive = false;
        if (currentGeneration[i][j] == 1) {
            if (neighbors == 2 || neighbors == 3) {
                alive = true;
            }
        }
        if (currentGeneration[i][j] == 0) {
            if (neighbors == 3) {
                alive = true;
            }
        }
        return alive;
    }
   
   /*
      Constructs the array for the next generation based on the
      currentGeneration, and makes currentGeneration that constructed
      array.  

      The code calculates the next generation values into auxArray, 
      which already exists as an int array of the correct size.  Once
      it has been completely loaded, the method then swaps the values
      of currentGeneration and auxArray, which makes the newly calculated
      array the currentGeneration.  This is easy to do since they
      are reference types.

      It returns true if at least ONE of the cells in the next
      generation is occupied, else false.  Once the grid goes
      empty, it won't change, so we want to detect that on the
      calculation.

      YOU MUST CODE THIS.  

      Part of it has been done, but you must complete it.
      Use the cellIsOccupiedInNextGen method you just defined.

    */
   private static boolean calculateNextGeneration(){
      
      int i,j;
      boolean hasAtLeastOne = false;
      int[][] temp;
      
      // iterate over the entire array and set each cell of
      // auxArray to what the next generation should have;
      // set hasAtLeastOne to true if any cell is loaded with
      // an organism
      for (i = 1; i <= gridSize; i++)
         for (j = 1; j <= gridSize; j++){
             if(cellIsOccupiedInNextGen(i,j)){
               auxArray[i][j] = 1;
               hasAtLeastOne =true;
             }
             else{
                 auxArray[i][j] = 0;
             }
         }
      for(i=1;i<=gridSize;i++){
          for(j=1;j<=gridSize;j++){
              currentGeneration[i][j]=auxArray[i][j];
          }
      }
      return hasAtLeastOne;
   }
   
   
   // write the interior of the currentGeneration array to standard out
   // using an asterisk(*) to stand for an organism
   private static void displayCurrentGeneration(){
      int i,j;
      
      System.out.println();
      
      for (i = 1; i <= gridSize; i++){
         for (j = 1; j <= gridSize; j++)
            System.out.print((currentGeneration[i][j] == 1? '*' : '-'));
         System.out.println();
      }
      
   }
   
   // plays one game
   public static void main(String[] args){
      int currentIteration = 1;
      boolean currentHasOccupants;
      
      
      System.out.println("Welcome to Conway's Game of Life!\n"
            + "First, we need to establish the game parameters.");
      
      getConfigurationParameters();
      
      currentHasOccupants = loadInitialConfiguration();
      
      System.out.println("Initial Contents are");
      displayCurrentGeneration();
      
      while (currentHasOccupants && currentIteration < maxGenerations){
         currentIteration++;
         currentHasOccupants = calculateNextGeneration();
         if (pauseBetweenGenerations){
            // the buffer is currently empty, so the nextLine call will
            // cause the program to pause for the user to enter a line
            System.out.println("Hit return to see the next generation.");
            stdIn.nextLine();
         }
         System.out.println("Generation " + currentIteration + " is");
         neighborCount(5,5);
         displayCurrentGeneration();
      }
      
      if (!currentHasOccupants)
         System.out.println("Sadly, the population has died out.");
   }
}
