package project2;
    
/*************************************************************************
 *  Compilation:  javac Turtle.java
 *  Execution:    java Turtle
 *
 *  The Turtle class is to create a window to display 
 *  turtle graphics that is as easy to use as System.out.println.
 *
 *  The Turtle class contains a double buffered screen so we 
 *     can draw offscreen and then display the results.
 *  
 *  To use the Turtle 
 * 
 *       Turtle.create();
 *       .... various movement and draw commands
 *       Turtle.render();
 * 
 * Methods available
 * 
 * accessor methods

        double x()            return x position
        double y()            return y position
        double orientation()  return orientation
    
 * movement and drawing methods 
   
       create(int width, int height)    create a canvas with drawing area width-by-height
       destroy()                        prevent the turtle from doing any other actions
       clear()                          clear the background
       clear(Color background)          clear the background with a new color
       setColor(Color color)            set the current color
       setScale(double x)               sets the graph magnitude associated with a pixel
                                        x is the value in pixels of the unit 1
       fly(double x, double y)          Move to position x,y without drawing
       go(double x, double y)           Move to position x,y with drawing
       spot(double w, double h)         draw w-by-h rectangle, centered at current location
       spot(double d)                   draw circle of diameter d, centered at current location
       spot(String s)                   draw spot using gif - fix to be centered at (x, y)
       spot(String s, double w, double h) draw spot using gif, centered on (x, y), scaled of size w-by-h
       pixel(int x, int y)              set pixel at the current position to the current color
       rotate(double angle)             rotate turtle counterclockwise in degrees
       goForward(double d)              walk forward with pen down
       flyForward(double d)             walk forward with pen up
       write(String s)                  write the given string in the current font
       write(String s, Font f)          write the given string in the given font
       pause(int delay)                 wait for a short while *and* repaint
	   loopingSound(String s)           continuously play a wav or midi sound file
       Sound(String s)                  play a wav or midi sound file
       render()                         transfer the offscreen graphics to the screen
*
**************************************************************************/

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

// for images and audio
import java.net.URL;

// for audio files only
import java.applet.Applet;
import java.applet.AudioClip;


public class Turtle extends JFrame {
    private static Turtle turtle;

    private static Image offscreenImage;              // double buffered image
    private static Graphics2D offscreen;
	private static AudioClip clip;

    private static double x = 0.0, y = 0.0;           // turtle is at coordinate (x, y)
    private static double orientation = 0.0;          // facing this many degrees counterclockwise
    private static Insets insets;                     // border around JFrame that we shouldn't use
    private static int width, height;                 // size of drawing area in pixels
    private static Color background = Color.white;    // background color
    private static Color foreground = Color.black;    // foreground color
    private static double scale;
    
    // user can't instantiate new objects
    private Turtle() { }

	// accessor methods
	public static double x()           { return x;           }
	public static double y()           { return y;           }
	public static double scale()           { return scale;           }
	public static double orientation() { return orientation; }

    
	// create a canvas with drawing area width-by-height
	public static void create(int width, int height) {

		// If we don't already have a JFrame, open up a new one
		if (turtle == null) {
			turtle = new Turtle();
			Turtle.width = width;
			Turtle.height = height;
			Turtle.scale = 1.0;
			turtle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			turtle.setTitle("Turtle Graphics");
			turtle.setResizable(false);
			turtle.setVisible(true);

			// re-adjust the size of the frame so that we don't lose space for insets
			insets = turtle.getInsets();
			turtle.setSize(new Dimension(width  + insets.left + insets.right,
										 height + insets.top  + insets.bottom));

			// create double buffered image and graphics handle
			offscreenImage = turtle.createImage(width, height);
			offscreen = (Graphics2D) offscreenImage.getGraphics();

			// clear the screen
			clear(Color.white);
	   }
	   else {
		   System.out.println("Error: you should only call Turtle.create once");
		   System.out.println("       per program.");
	   }
	}

	// prevent the turtle from doing any other actions
	public static void destroy() {
		Turtle.render();

		turtle.dispose();      // close the window
		turtle = null;
	}



	// clear the background
	public static void clear() { clear(background); }

	// clear the background with a new color
	public static void clear(Color background) {
		Turtle.background = background;
		offscreen.setColor(background);
		offscreen.fillRect(0, 0, width, height);
		offscreen.setColor(foreground);
	}
   

	public static void setColor(Color color) {
		foreground = color;
		offscreen.setColor(foreground);
	}

	public static void setScale(double scale) {
		Turtle.scale = scale;
	}

	
	public static void fly(double x, double y) {

		Turtle.x = x * scale;
		Turtle.y = y * scale;
	}

	public static void go(double x, double y) {
		double xs = x * scale;
		double ys = y * scale;
		
		offscreen.draw(new Line2D.Double(Turtle.x, height - Turtle.y, xs, height - ys));
		Turtle.x = xs;
		Turtle.y = ys;
	}

	// draw w-by-h rectangle, centered at current location
	public static void spot(double w, double h) {
		offscreen.fill(new Rectangle2D.Double(x - w/2, height - y - h/2, w, h));
	}

	// draw circle of diameter d, centered at current location
	public static void spot(double d) {
	   offscreen.fill(new Ellipse2D.Double(x - d/2, height - y - d/2, d, d));
	}

	// draw spot using gif - fix to be centered at (x, y)
	public static void spot(String s) {
		URL url     = Turtle.class.getResource(s); 
		Image image = Toolkit.getDefaultToolkit().getImage(url); 

		// Wait for image to load
		MediaTracker tracker = new MediaTracker(turtle);
		tracker.addImage(image, 1);
		try { tracker.waitForAll(); }
		catch (InterruptedException e) { }

		int w = image.getWidth(null);
		int h = image.getHeight(null);
   
		// center of rotation is x, height - y
		offscreen.rotate(Math.toRadians(orientation), x, height - y);
		offscreen.drawImage(image, (int) (x - w/2.0), (int) (height - y - h/2.0), null);
		offscreen.rotate(Math.toRadians(-orientation), x, height - y);
	}

	// draw spot using gif, centered on (x, y), scaled of size w-by-h
	public static void spot(String s, double w, double h) {
		URL url     = Turtle.class.getResource(s); 
		Image image = Toolkit.getDefaultToolkit().getImage(url); 

		// Wait for image to load
		MediaTracker tracker = new MediaTracker(turtle);
		tracker.addImage(image, 1);
		try { tracker.waitForAll(); }
		catch (InterruptedException e) { }

		offscreen.rotate(Math.toRadians(orientation), x, height - y);
		offscreen.drawImage(image, (int) (x - w/2.0), (int) (height - y - h/2.0),
								   (int) w, (int) h, null);
		offscreen.rotate(Math.toRadians(-orientation), x, height - y);
	}

	public static void pixel(int x, int y) {
		int xs = new Double(x * scale).intValue();
		int ys = new Double(y * scale).intValue();
		
		offscreen.drawRect(xs, height - ys, 1, 1);
	}

	// rotate counterclockwise in degrees
	public static void rotate(double angle) { orientation += angle; }

	// walk forward with pen down
	public static void goForward(double d) {
		double oldx = x;
		double oldy = y;
		x += (d * scale) * Math.cos(Math.toRadians(orientation));
		y += (d * scale) * Math.sin(Math.toRadians(orientation));
		offscreen.draw(new Line2D.Double(x, height - y, oldx, height - oldy));
	}

	// walk forward with pen up
	public static void flyForward(double d) {
		x += (d * scale) * Math.cos(Math.toRadians(orientation));
		y += (d * scale) * Math.sin(Math.toRadians(orientation));
	}

	// write the given string in the current font
	public static void write(String s) {
		FontMetrics metrics = offscreen.getFontMetrics();
		int w = metrics.stringWidth(s);
		int h = metrics.getHeight();
		offscreen.drawString(s, (float) (x - w/2.0), (float) (height - y + h/2.0));
	}

	// write the given string in the given font
	public static void write(String s, Font f) {
		offscreen.setFont(f);
		write(s);
	}


	// wait for a short while *and* repaint
	public static void pause(int delay) {
		Turtle.render();
		try { 
			Thread.currentThread().sleep(delay); }
		catch (InterruptedException e) { }
	}


	// play a wav or midi sound
	public static void loopingSound(String s) {
		URL url = Turtle.class.getResource(s); 
		clip = Applet.newAudioClip(url);
		clip.loop();
		clip.play();
	}

	public static void Sound(String s) {
		URL url = Turtle.class.getResource(s); 
		clip = Applet.newAudioClip(url);
		clip.play();
	}


	// transfer the offscreen graphics to the screen
	public static void render() { turtle.repaint(); }

	// user does not directly call this method
	public void paint(Graphics g) {
		if (g != null && offscreenImage != null)
			g.drawImage(offscreenImage, insets.left, insets.top, null);
	}



}

