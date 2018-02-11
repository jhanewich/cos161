Lab III  Practice Debugging with Eclipse

Due Saturday after the lab meeting at 8 A.M.

The BuggyBlackjack.java file has some code to play the game
of blackjack with the user, but it has some issues and your
job is to find as many of the bugs in it as you can.

The version of Blackjack played here is a variation of the
casino version.  The most significant difference is that the ace
will always count 1 in this version. Face cards all count
10, and other cards count their values, as in regular blackjack.

One hand or game is played as follows.  First, the user chooses an amount
to bet, which must be >= 0, before any cards are dealt. Then the dealer 
deals a face down card to the player and itself, then a face up card to
the player and itself.  The program will display all these values to the
user EXCEPT the dealer's face down card.

The user is then allowed to ask for more cards until the sum of
the user's cards's values is > 21(which means the user "busted"),
or the user declines another card(which means the user "stands").

If the user didn't bust, then the dealer deals itself cards face
up until the dealer's score is >= 17, showing each card to the user.

The user wins the hand if she/he did not bust and either the
dealer busted or the user's card values summed to a value greater
than the dealer's sum.

The dealer wins the hand if the user busted or if neither busted
and the dealer's cards sum to a value that is higher  than the user's
sum.

If neither busted and the sums are the same, we will call the
hand a draw or tie.

The program will keep track of the net winnings/losings by
updating a variable after each hand.  It will also keep track
of the number of wins, losses, and ties and report these counts to 
the user.

The program currently has a number of bugs in it and does not
do these things correctly.


WHAT YOU HAVE TO DO

When you run the program, you should see a number of errors in
what it produces as output.  Your task is to use the Eclipse
IDE features(setting breakpoints, examining variables, etc.) to discover
as many of the bugs in the code as you can.  Whenever you modify
the code, identify where you made a change with a comment

//////////////  modified the next line(s)

right above the section of code you modified. 

When you have fixed it so it runs correctly, indentify the team
members in the source code file and submit it to Blackboard.

Depending on how you count them, there are around eight bugs in the
code.

Note, to help you with debugging, the program prints out the first 
24 card values so you can choose cards to force different outcomes.
