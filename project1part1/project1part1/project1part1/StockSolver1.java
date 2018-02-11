package project1part1;

import java.util.*;
import java.util.logging.Logger;
import java.io.*;

/**
 * @author John Hanewich
 *
 */
public class StockSolver1 {

    // Setup logging
    private final static Logger log = Logger.getLogger(StockSolver1.class.getName());

    // constants to dimension the array and control the formatting
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
            COMPANY_NAME_FIELDWIDTH = 8,

            // number of columns for prices; must be at least 6
            PRICE_FIELDWIDTH = 6;

    private static int
    // number of companies read from the file
    companyCount,
            // number of prices with each company
            periodCount;

    // array for names
    private static String[] companyNames = new String[MAX_COMPANIES];

    // array for prices
    private static double[][] priceTable = new double[MAX_COMPANIES][MAX_PRICES];

    private static String headerString(int periodCount) {

        StringBuffer sb = new StringBuffer();
        int i = 0, iDigits = 1, spaceCount, j;

        // header for names column; should be same length as
        // MAX_COMPANY_NAME_DISPLAY_LENGTH
        sb.append("Company");

        // pad out with blanks, if necessary
        for (i = MAX_COMPANY_NAME_DISPLAY_LENGTH + 1; i <= COMPANY_NAME_FIELDWIDTH; i++)
            sb.append(' ');

        // headers for price columns; each price field is PRICE_FIELDWIDTH
        // columns
        for (i = 1; i <= periodCount; i++) {
            // calculate needed number of blank spaces
            spaceCount = PRICE_FIELDWIDTH - 3 - iDigits;
            for (j = 0; j < spaceCount; j++)
                sb.append(' ');
            sb.append("Per" + i);
            if (i == 9)
                iDigits = 2;
        }
        return sb.toString();
    }

    
    /**
     * @param cName company name
     * @param prices array of prices
     * @param periodCount the amount of prices the company holds
     * @return A single row of formated data
     */
    private static String rowString(String cName, double[] prices, int periodCount) {

        StringBuffer sb = new StringBuffer();

        int sz = cName.length();

        String modifiedcName = String.format("%-" + COMPANY_NAME_FIELDWIDTH + "s",
                cName.substring(0, (sz < MAX_COMPANY_NAME_DISPLAY_LENGTH ? sz : MAX_COMPANY_NAME_DISPLAY_LENGTH)));

        int i = 0;

        // add the name of the company in the first column
        sb.append(modifiedcName);

        // entries for prices
        for (i = 0; i < periodCount; i++)
            sb.append(String.format("%" + PRICE_FIELDWIDTH + ".1f", prices[i]));

        return sb.toString();
    }

    // Read txt data and fill table
    /**
     * @param Scanner object
     * 
     */
    private static void loadTableFromFile(Scanner f) {
        double price;
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
        log.info("File loading ...");
    }

    // Writes table using existing helper methods
    /**
     * @param companyCount
     * @param periodCount
     * @param companyNames
     * @param priceTable
     */
    private static void writeTable(int companyCount, int periodCount, String[] companyNames, double[][] priceTable) {
        log.info("Writting Table ...");
        int rowCount = 0;

        System.out.println(headerString(periodCount));

        for (String cName : companyNames) {
            if (cName != null) {
                System.out.println(rowString(cName, priceTable[rowCount], periodCount));
                rowCount++;
            }
        }
    }

    /**
     * @param Reads in file name from the command line.
     */
    public static void main(String[] args) {
        /*
         * 
         * Reads the data from the file named in args[0] and if it is not empty,
         * prints out the company names s and price data in a table.
         * 
         *************************************************************************/

        Scanner input = null;
        String fn = null;

        if (args.length == 0) {
            log.info("The name of the input data file" + " should be on the command line.\nTerminating.");
            return;
        } else {
            try {
                fn = args[0];
                input = new Scanner(new File(fn));
            } catch (Exception e) {
                log.info("Could not open the file " + fn + '.');
                return;
            }
        }
        // if control gets here, the Scanner object input is bound to a file

        loadTableFromFile(input);

        if (companyCount == 0)
            System.out.println("The file " + fn + " is empty.");
        else {
            writeTable(companyCount, periodCount, companyNames, priceTable);

            System.out.println("\nThere were " + companyCount + " companies in the file" + ", each with " + periodCount
                    + " prices.");
        }
    }
}