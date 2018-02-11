
//package project1part2;

import java.util.*;
import java.util.logging.Logger;
import java.io.*;

/***
 * 
 * Your name: John Hanewich
 * 
 * Extra Credits? Check which applies and set the static boolean variables below
 * consistently
 * 
 * none
 * 
 * first, not second
 * 
 * second, not first
 * 
 * both
 * 
 * 
 * 
 * 
 ***/

public class StockSolver2 {
    /*
     * 
     * This class is the template you can use for your solution to the first
     * programming project. You should
     * 
     * 
     * 1. copy your loadTableFromFile method, replacing the stub definition
     * given here
     * 
     * 2. define any additional static data structures you need for your
     * calculations or to hold the results. All data members should be private
     * and static.
     * 
     * 
     * 3. define any additional methods you want to calculate the statistics. It
     * is more efficient to scan the row of prices just once, but it is more
     * modular and probably easier to define a method for each statistic. You
     * may compromise and write a method that calculates several, but not all of
     * the statistical values and loads the components of a result array with
     * their values.
     * 
     * 
     * 4. it is possible to calculate and display each row of output on the fly,
     * but it may be more convenient to calculate the rows to display for each
     * company and save them in a separate table. Either way, you must use our
     * formatting methods to convert the results to strings. We have provided a
     * template displayTable method that you can adapt. It should display the
     * result table column headers, then iterate over the companies and display
     * a row of statistical output for each. However you want to deliver it, the
     * data row formatting methods want the statistical values delivered in a
     * one dimensional array of double.
     * 
     * 
     ************************************************************************/
    // Setup logging
    private final static Logger log = Logger.getLogger(StockSolver2.class.getName());

    private static final boolean DOING_XC_1 = false, DOING_XC_2 = false;

    private static final int

    // most companies that can be read
    MAX_COMPANIES = 20,

            // most prices that can be read for a company
            MAX_PRICES = 30,

            // length of prefix of company name that will be
            // displayed in the table
            MAX_COMPANY_NAME_DISPLAY_LENGTH = 7,

            // length of the field for the company names; must be
            // at least 1 more than the name display length
            COMPANY_NAME_FIELDWIDTH = 7,

            // number of columns for prices; must be at least 6
            PRICE_FIELDWIDTH = 6;

    private static int companyCount, periodCount;

    private static double multiplier;

    // array for names
    private static String[] companyNames  = new String[MAX_COMPANIES];

    // array for prices
    private static double[][] priceTable = new double[MAX_COMPANIES][MAX_PRICES];
    
    //
    private static double[][] newResults;


    // Lowest Price Statistic
    private static double lowestPrice() {
        double minValue = Double.POSITIVE_INFINITY;
        double temp = 0;
        for (int i = 0; i < companyCount; i++) {
            for (int j = 0; j < periodCount; j++) {
                temp = priceTable[i][j];
                if (temp < minValue) {
                    minValue = temp;
                }
            }
        }
        return minValue;
    }

    // Highest Price Statistic
    private static double highestPrice() {
        double maxValue = 0;
        double temp = 0;
        for (int i = 0; i < companyCount; i++) {
            for (int j = 0; j < periodCount; j++) {
                temp = priceTable[i][j];
                if (temp > maxValue) {
                    maxValue = temp;
                }
            }
        }
        return maxValue;
    }

    // Average Price Statistic
    private static double averagePrice() {
        double sum = 0;
        double average = 0;
        for (int idx = 0; idx < companyCount; idx++) {
            for (int j = 0; j < periodCount; j++) {
                sum += priceTable[idx][j];
            }
            average = sum / periodCount;
            sum = 0;
        }
        return  Math.floor(average * 100) / 100;
    }

    // Net Change Statistic
    private static double netChange() {
        double firstValue, lastValue, totalChange = 0;
        for (int i = 0; i < companyCount; i++) {
            firstValue = priceTable[i][0];
            lastValue = priceTable[i][periodCount - 1];
            totalChange = (lastValue - firstValue);
        }
        return Math.floor(totalChange*100)/100;
    }

    // Standard Div Statistic
    private static double standardDiv() {
        double variance = 0;
        double standardDev = 0;
        double average;

        // get sum
        for (int idx = 0; idx < companyCount; idx++) {
            for (int j = 0; j < periodCount; j++) {
                average = newResults[idx][3];
                variance += (priceTable[idx][j] - average) * (priceTable[idx][j] - average);
            }
            variance = variance / periodCount;
            standardDev = Math.sqrt(variance);
        }
        return Math.floor(standardDev*100)/100;
    }

    // Length Of Trend Statistic
    private static double lengthOfTrend() {
        int count = 0, max = 0;
        for (int i = 0; i < companyCount; i++) {
            for (int j = 1; j < periodCount; j++) {
                if (priceTable[i][j] >= priceTable[i][j - 1]) {
                    count++;
                } else {
                    count = 0;
                }
                if (count > max) {
                    max = count;
                }
            }
        }
        return Math.floor(max*100)/100;
    }
    
    private static double bestGrowthRate(){//TODO
        double bestGrowthRate=0;
        double startOfTrend = 0, continuedTrend = 0;
        for (int i = 0; i < companyCount; i++) {
            for (int j = 1; j < periodCount; j++) {
                //       if K>=K-1
                if (priceTable[i][j] >= priceTable[i][j-1]) {
                    startOfTrend=priceTable[i][j-1];
                }
            }
        }
//        (value at end - value at start)/length <--growth rate calc
      return bestGrowthRate;
    }

    // Read txt data and fill table
    private static void loadTableFromFile(Scanner f) {
        double price;
        log.info("File loading ...");
        while (f.hasNext()) {
            if (f.hasNext()) {
                companyNames[companyCount] = f.next();
                f.nextLine();
            }
            periodCount = 0;
            while (f.hasNextDouble()) {
                price = f.nextDouble();
                priceTable[companyCount][periodCount] = price;
                periodCount++;
            }
            companyCount++;
        }
    }

    // display methods

    // Returns a string for the statistics table headers for
    // the basic assignment with no extra credits
    private static String calculateHeaders() {
        return String.format("\n%-" + COMPANY_NAME_FIELDWIDTH + "s    Low     Hi     Net    Ave    Dev Lng  BestTrRt",
                "Company");
    }

    // statistics table headers for the first extra credit
    private static String calculateHeadersXC() {
        return String.format("\n%-" + COMPANY_NAME_FIELDWIDTH
                + "s    Low     Hi     Net    Ave    Dev Lng  BestTrRt BestPerRt BestPer", "Company");
    }

    private static String calculateOneRow(String compName, double[] results) {
        /***
         * 
         * Returns the string for one data row of the statistics table for the
         * basic assignment.
         * 
         * The method assumes the parameters are not null and that the double
         * array has length seven and the calculated values are in it in the
         * order
         * 
         * lowest price highest price net change in price for the entire period
         * average price for the entire period standard deviation for the prices
         * of the period length of longest upward trend(could be 0) best growth
         * rate of any upward trend period
         * 
         * compName is the name of the company for the row. Just the first
         * MAX_COMPANY_NAME_DISPLAY_LENGTH characters of the name will be used.
         * 
         * 
         * Note, if the length of the longest upward trend is 0, then the last
         * is undefined, and will be printed as "n/a".
         * 
         ***/
        int sz = compName.length();
        String cnm = String.format("%-" + COMPANY_NAME_FIELDWIDTH + "s",
                compName.substring(0, (sz < MAX_COMPANY_NAME_DISPLAY_LENGTH ? sz : MAX_COMPANY_NAME_DISPLAY_LENGTH))),
                lo = String.format("%7.2f", results[0]), hi = String.format("%7.2f", results[1]),
                net = String.format("%8.2f", results[2]), ave = String.format("%7.2f", results[3]),
                dev = String.format("%7.2f", results[4]), lng = String.format("%4.0f", results[5]), bgrthrt;

        if (((int) (results[5])) == 0)
            bgrthrt = "       n/a";
        else
            bgrthrt = String.format("%10.2f", results[6]);

        return cnm + lo + hi + net + ave + dev + lng + bgrthrt;
    }

    private static String calculateOneRowXC(String compName, int numberOfPricesPerRow, double[] results) {
        /***
         * 
         * Returns the string for one data row of the statistics table with the
         * first the first extra credit option.
         * 
         * Just the first MAX_COMPANY_NAME_DISPLAY_LENGTH characters of the
         * compName will be used.
         * 
         * The method assumes the parameters are not null and that the double
         * array has length nine and the calculated values are in it in the
         * order
         * 
         * lowest price highest price net change in price for the entire period
         * average price for the entire period standard deviation for the prices
         * of the period length of longest upward trend(could be 0) best growth
         * rate of any upward trend period best period rate best period
         * 
         * Note, if the length of the longest upward trend is 0, then the best
         * upward trend rate is undefined, and will be printed as "n/a".
         * 
         * If the number of prices per row is 1, then the last two are also
         * undefined and will be given as "n/a".
         * 
         ***/
        int sz = compName.length();
        String cnm = String.format("%-" + (MAX_COMPANY_NAME_DISPLAY_LENGTH + 1) + "s",
                compName.substring(0, (sz < MAX_COMPANY_NAME_DISPLAY_LENGTH ? sz : MAX_COMPANY_NAME_DISPLAY_LENGTH))),
                lo = String.format("%7.2f", results[0]), hi = String.format("%7.2f", results[1]),
                net = String.format("%8.2f", results[2]), ave = String.format("%7.2f", results[3]),
                dev = String.format("%7.2f", results[4]), lng = String.format("%4.0f", results[5]), bgrthrt, bperrt,
                bper;

        if (numberOfPricesPerRow < 2)
            bper = "     n/a";
        else
            bper = String.format("%8.0f", results[8]);

        if (((int) (results[5])) == 0)
            bgrthrt = "       n/a";
        else
            bgrthrt = String.format("%10.2f", results[6]);

        bperrt = (numberOfPricesPerRow < 2 ? "       n/a" : String.format("%10.2f", results[7]));

        return cnm + lo + hi + net + ave + dev + lng + bgrthrt + bperrt + bper;
    }

    // calculates the string for the multiplier value mult at the start of a
    // line
    // separated from prior text by a blank line
    private static String calculateMultiplierString(double mult) {

        return String.format("\n\nPerfect investing during this period would achieve" + " a multiplier of %1.2f", mult);

    }
    
    //Building results array with data
    private static void buildData() {
        newResults = new double[companyCount][COMPANY_NAME_FIELDWIDTH];
        log.info("Computing Statistics");
        for (int i = 0; i < companyCount; i++) {
            newResults[i][0] = lowestPrice();
            newResults[i][1] = highestPrice();
            newResults[i][2] = netChange();
            newResults[i][3] = averagePrice();
            newResults[i][4] = standardDiv();
            newResults[i][5] = lengthOfTrend();
            newResults[i][6] = bestGrowthRate();
//            newResults[i][7] = bperrt;
//            newResults[i][9] = bper;
        }
    }

    private static void displayTable() {
        /*
         * YOU MUST CODE THIS 
        */
        
        log.info("displayTable stub was called.");
        System.out.println(calculateHeaders());
//        for(String company: companyNames){
//       }
        
        if (DOING_XC_1)
            System.out.println("will display the two extra columns for best rate and best" + " period.");
        else
            System.out.println("Extra credit 1 was not implemented.");

    }

    public static void main(String[] args) {
        /*
         * 
         * Reads the data from the file named in args[0] and if it is not empty,
         * calculates and displays statistics for each company in the file, as
         * specified by the assignment.
         * 
         *************************************************************************/

        Scanner input = null;
        String fn = null;

        if (args.length == 0) {
            System.out.println(
                    String.format("The name of the input data file" + " should be on the command line.\nTerminating."));
            return;
        } else {
            try {
                fn = args[0];
                input = new Scanner(new File(fn));
            } catch (Exception e) {
                System.out.println("Could not open the file " + fn + '.');
                return;
            }
        }
        // if control gets here, the Scanner object input is bound to a file
        log.info("Processing test fitle : " + fn);
        loadTableFromFile(input);

        if (companyCount == 0)
            System.out.println("The file " + fn + " is empty.");
        else {
            // YOUR CODE TO CALCULATE THE STATISTICS AND DISPLAY THEM
            // IN THE TABLE SHOULD GO HERE
            // You should code and call the display table method
            buildData();
            displayTable();
            if (DOING_XC_2)
                System.out.println(calculateMultiplierString(multiplier));

        }

        // IMPORTANT: DELETE THE FOLLOW LINES BEFORE YOU SUBMIT YOUR PROGRAM
        //
        // the next dozen lines of code test the display methods, and you should
        // delete them or comment them out before submitting your program
//        double[] A = { 100.56, 213.78, 300.23, 257.45, 11.77, 0, 1.1 },
//                B = { 100.56, 213.78, 300.23, 257.45, 11.77, 3, 4.1 },
//                C = { 100.56, 213.78, 300.23, 257.45, 11.77, 0, 1.1, 7.6, 3.5 },
//                D = { 100.56, 213.78, 300.23, 257.45, 11.77, 29, 1.1, 8.64, 4.4 };

//        System.out.println(calculateHeaders());
//        for(double result[] : newResults){
//            System.out.println(calculateOneRow("AVeryLongNameShouldBeShortened", result));
//        }
//        
//        System.out.println(calculateHeaders());
//        System.out.println(calculateOneRow("AVeryLongNameShouldBeShortened", A));
//        System.out.println(calculateOneRow("x", B));
//        System.out.println(calculateHeadersXC());
//        System.out.println(calculateOneRowXC("AVeryLongNameShouldBeShortened", 10, C));
//        System.out.println(calculateOneRowXC("x", 1, D));
//        System.out.println(calculateMultiplierString(12345.67));

    }
}