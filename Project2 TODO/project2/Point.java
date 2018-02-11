package project2;

/**

A class to represent points in the plane.

Methods include getters, setters, toString, distance
calculation, and a compareTo that uses lexicographic
ordering.

YOU MUST CODE A DRAW METHOD.

***/


public class Point implements Comparable<Point>{

  // for the Turtle.spot method calls;
  private static int SPOTSIZE = 5;

  private static int getSPOTSIZE(){
     return SPOTSIZE;
  }

  private static void setSPOTSIZE(int newsize){
     SPOTSIZE = newsize;
  }

  private double
     x,y;

  // constructs the origin, (0,0)
  public Point(){
  }

  // constructs (xx,yy)
  public Point(double xx, double yy){
     x = xx;
     y = yy;
  }

  // uses the lexicographic ordering
  public int compareTo(Point other){
     if (x < other.x)
        return -1;
     else
        if (other.x < x)
           return 1;
     else  // x coordinates are equal
        if (y < other.y)
           return -1;
        else 
           if (other.y < y)
              return 1;
           else // both coordinates are equal
              return 0;
   }

   // getters

   public double getX(){
      return x;
   }

   public double getY(){
      return y;
   }

   // setters

   public void setX(double newX){
      x = newX;
   }

   public void setY(double newY){
      y = newY;
   }

   // returns the Euclidean distance between this and
   // other
   public double dist(Point other){
      double
         deltaX = x - other.x,
         deltaY = y - other.y,
         dXSqrd = deltaX * deltaX,
         dYSqrd = deltaY * deltaY;

      return Math.sqrt(dXSqrd + dYSqrd);
   }

   // returns the conventional representation of (x, y)
   public String toString(){
      return "" + '(' + x + ", " + y + ')';
   }

   // returns the conventional representation of (x, y)
   // but with x and y rounded to n digits of precision
   public String toString(int n){
      return "" + '(' + String.format("%1." + n + "f, %1." + n + "f)", x, y);
   }

   // YOU MUST CODE THIS
   public void draw(double pointSize){
   /*
      Use's the Turtle's fly and spot methods to draw a circle 
      at the coordinates of the receiver of pointSize.

   */


   }

   // test the constructors and methods
   public static void main(String[] args){

      Point
         origin = new Point(),
         oneOne = new Point(1,1),
         twoTwo = new Point(2,2),
         twoOne = new Point(2,1);

      System.out.println("" + origin + '\n' +
         oneOne + '\n' +
         twoTwo + '\n' +
         twoOne + '\n' +
         origin.toString(2) + '\n' +
         oneOne.toString(2) + '\n' +
         twoTwo.toString(2) + '\n' +
         twoOne.toString(2) + '\n' +
         oneOne.dist(origin) + '\n' +
         oneOne.dist(oneOne) + '\n' +
         oneOne.dist(twoTwo) + '\n' +
         oneOne.dist(twoOne) + '\n' +
         twoTwo.dist(origin) + '\n' +
         twoTwo.dist(oneOne) + '\n' +
         twoTwo.dist(twoTwo) + '\n' +
         twoTwo.dist(twoOne) + '\n' +
         twoOne.dist(origin) + '\n' +
         twoOne.dist(oneOne) + '\n' +
         twoOne.dist(twoTwo) + '\n' +
         twoOne.dist(twoOne) + '\n' +
         twoOne.getX() + '\n' +
         twoOne.getY());
         origin.setX(10);
         origin.setY(20);
         System.out.println(origin.toString());
   }
}



