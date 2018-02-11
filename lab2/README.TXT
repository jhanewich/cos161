Lab II

Due at 8:00 A.M. on the Saturday after the lab.

The file ConwaysLife.java has a partial implementation of Conway's
game of life that you are asked to complete.

The game simulates the evolution of a population by calculating
a sequence of generations according to some simple rules.  The
calculation requires two arrays that are each two dimensional,
because the next generation must be calculated into a second 
array, which then becomes the current generation.

You must complete the code needed to perform that update.

WHAT YOU HAVE TO DO

1. create a project in Eclipse for this lab

2. within that project create a Java class, ConwaysLife.java, and
   copy that file from the archive to the Eclipse class(you will
   need to delete the text Eclipse puts in the class).

3. In the class

   a. code the neighborCount method as specified

   b. code the cellIsOccupiedInNextGen as specified

   c. complete the coding of the calculateNextGeneration
      method as specified

   All methods you must code are marked

   YOU MUST CODE THIS

4. Try running your program on the .txt input files below
   or by keying in the contents of one of the .txt input files 
   in the archive.  To read from the file directly, you would need
   to download it from the archive into Eclipse.

   blinker.txt
   toad.txt
   glider.txt
   pulsar.txt

   The gridsize should be at least 17 for pulsar.txt.


5. If they appear to run correctly, submit the ConwaysLife.java 
   source code file to Blackboard.

Be sure to identify the team members in the class in the top comment
of the source code file.

There is more information about the game at the Wikipedia article

https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life








