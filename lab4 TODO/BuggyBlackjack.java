import java.util.*;

/*

Team:



Lab Exercise:

This program is intended to play blackjack with the user, but 
sadly it has a number of bugs(depending on how you count them, 7
or 8 to be precise).  Your task is to find the bugs and eradicate them.

Locate and fix as many of them as you can.  whenever you modify
the code, identify where you made a change with a comment

//////////////  modified the next line(s)

right above the code you modified.  

When you are finished submit this file, with the team members listed in it,
to Blackboard.


This program plays games of blackjack with the user, recording the
counts of user wins, losses, ties, and how much the user has won, as
described in the README.txt file, which also discusses the game.

Betting occurs on each hand before the cards are dealt and the net user
winnings are updated on the result of each hand.
 
*/

public class BuggyBlackjack{

   private static boolean DEBUGGING = true; 

   private static Scanner stdIn = new Scanner(System.in);

   private static final long RANDOM_SEED = 0L;


   private static Random random = new Random(RANDOM_SEED);
   // in production, you would want to randomize program behavior
   // by seeding the random number generator with the clock value,
   // but while debugging, you want the generated numbers to repeat,
   // so we comment out the production statement and seed it with 0
   // in the line above
   // private static Random random = new Random(System.currentTimeMillis());

   private static int 
      netUserWinnings,
      amountOfCurrentBet,
      userWins,
      ties,
      userLosses;


   /*********************************************************************/

   // get amount of bet from user; reject negative values
   private static void getAmountOfBet(){
      int amountOfCurrentBet = 0;
      
      System.out.println("How much do you want to bet on this hand?");

      boolean haveValidBet = false;
      // reject non-int tokens and integers < 0
      while (!haveValidBet){
         while (!stdIn.hasNextInt()){
            // issue error message
            System.out.println("You must enter a nonnegative integer.");
            // skip over the token
            stdIn.next();
         }
         // get the integer
         amountOfCurrentBet = stdIn.nextInt();
         if (amountOfCurrentBet < 0)
            // issue the error message
            System.out.println("The integer value must be >= 0. Try again.");
         else
            // set the boolean
            haveValidBet = true;
      }
   }

   /*********************************************************************/

  // get the next card as a value between 1 and 13
  // for ace through king
  private static int dealNextCard(){
     // for a value from 1 to 13 inclusive;
     return random.nextInt(13) + 1;
  }

   /*********************************************************************/

  // return the display string for the card
  private static String stringForCard(int card){
     if (card == 1)
        return "A";
     else if (card < 11)
        return "" + card;
     else
        switch (card){
           case 11:
              return "J";
           case 12:
              return "Q";
           default:
              return "K";
        }
   }

   /*********************************************************************/

   // return the count value of the card
   private static int valueOf(int card){
      if (card <= 10)
         return 10;
      else
         return card;
   }

   /*********************************************************************/

   // tells the user what the net winnings value is
   private static void reportCurrentWinnings(){

     System.out.print("At this time ");
     if (netUserWinnings > 0)
        System.out.println("I owe you " + netUserWinnings + " dollars.");
     else if (netUserWinnings < 0)
        System.out.println("you owe me " + (-netUserWinnings) + " dollars.");
     else
        System.out.println("We are even on the winnings.");
   }

   /*********************************************************************/

   /*
      plays one hand of the game and updates the
      netWinnings and counts of losses, wins, and ties
      based on the result.  reports the current user winnings.
   */
   private static void playOneHand(){
      char reply;

      boolean 
         userBusted,
         userWantsACard = false;

      int
         dHoleCard,
         dNextCard,
         uHoleCard,
         uNextCard,
         sumOfUserCards,
         sumOfDealerCards;

      

     getAmountOfBet();

     uHoleCard = dealNextCard();
     dHoleCard = dealNextCard();
     uNextCard = dealNextCard();
     dNextCard = dealNextCard();

     System.out.println("My cards are : X " + stringForCard(dNextCard));
     System.out.println("Your cards are : " + stringForCard(uHoleCard) +
     ' ' + stringForCard(uNextCard));
     

     sumOfUserCards = valueOf(uHoleCard) + valueOf(uNextCard);
     sumOfDealerCards = valueOf(dHoleCard) + valueOf(uNextCard);

     // allow user to ask for more cards
     System.out.println("Another card?(y or Y)");
     reply = stdIn.next().charAt(0);
     userWantsACard = reply == 'y' && reply == 'Y';
     while (sumOfUserCards < 22 && userWantsACard){
        uNextCard = dealNextCard();
        System.out.println("The card is " + stringForCard(uNextCard));
        sumOfUserCards += valueOf(uNextCard);
        if (sumOfUserCards < 22){
           System.out.println("Another card?");
           reply = stdIn.next().charAt(0);
           userWantsACard = reply == 'y' && reply == 'Y';
        }
     }
     if (sumOfUserCards >= 22){
        userLosses++;
        netUserWinnings -= amountOfCurrentBet;
        System.out.println("You busted! I win!");
     }
     else{
        // Dealer tips over the hole card
        System.out.println("My cards are: " + stringForCard(dHoleCard) + ' ' 
        + stringForCard(dNextCard));
        // dealer takes cards until reaches 17
        while(sumOfDealerCards < 17){
           dNextCard = dealNextCard();
           System.out.println("Next card for me is " + stringForCard(dNextCard));
           sumOfDealerCards += valueOf(dNextCard);
        }
        if (sumOfDealerCards > 21){
           System.out.println("I busted!  You win!");
           netUserWinnings += amountOfCurrentBet;
        }
        else if (sumOfDealerCards > sumOfUserCards){
           userLosses++;
           System.out.println(sumOfDealerCards + " beats " + sumOfUserCards +
           ", so I win!");
           netUserWinnings -= amountOfCurrentBet;
        }
        else if (sumOfDealerCards <= sumOfUserCards){
           userWins++;
           System.out.println(sumOfUserCards + " beats " + sumOfDealerCards +
           ", so you win!");
           netUserWinnings += amountOfCurrentBet;
        }
        else{ // it's a tie
           System.out.println("Our hands have the same value, so" +
           " it\'s a tie!");
           ties++;
        }
     }
     reportCurrentWinnings();
  }

   public static void main(String[] args){
      char reply;
      boolean userWantsToPlay;

      // this displays the first 24 cards that will be dealt
      if (DEBUGGING){
         System.out.println("The first 24 cards dealt are as follows.");
         for (int i = 0; i < 24; i++){
            System.out.print(stringForCard(dealNextCard()) + ' ');
            if (i == 11)
               System.out.println();
         }
         System.out.println('\n');
         // reset the random number generator
         random = new Random(RANDOM_SEED);
      }
      System.out.println("Want to play a hand of blackjack?");
      reply = stdIn.next().charAt(0);
      userWantsToPlay = reply == 'y' || reply == 'Y';

      while (userWantsToPlay){
         playOneHand();
         System.out.println("Play another?");
         reply = stdIn.next().charAt(0);
      }
 
      System.out.println("You can settle up with the cashier.");
      System.out.println("You won " + userWins + " hands, lost " +
      userLosses + " hands, and " + ties + " hands were ties.");
   }
}



  
