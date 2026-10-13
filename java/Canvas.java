import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.awt.event.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example. 
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 *
 * @version: 1.6 (shapes)
 * 
 * New version with non-static canvas to allow mutiple canvases
 * @author: Dave Maharry
 * @version: 2.0
 *
 * New version using graphics only. The previous JLabel is replaced with
 * a text graphic oblect - see the LabelDescription inner-class, DrwaString
 * and Redraw classes 
 * @author: Carl Singer
 * @version: 3.0 - August 2005
 */
public class Canvas
{
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    //  ----- instance part -----

    private JFrame frame;
    // Keep original game coordinates when the window fills the screen.
    private Dimension gameSize;
    private DrawingSurface canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List objects;
    private HashMap shapes;
    
    /**
     * Create a Canvas.
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * @param bgColour  the desired background colour of the canvas
     */
    public Canvas(String title, int width, int height, Color bgColour )
    {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new DrawingSurface();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        gameSize = new Dimension(width, height);
        canvas.setPreferredSize(gameSize);
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Create a Canvas.
     * creates canvas with a background of white
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * 

     */
    public Canvas(String title, int width, int height)
    {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new DrawingSurface();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        gameSize = new Dimension(width, height);
        canvas.setPreferredSize(gameSize);
        backgroundColour = new Color(18, 25, 39);    // background is white
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Create a Canvas.
     * creates canvas with a background of white and size of 300 by 300
     * @param title  title to appear in Canvas Frame
     * 

     */
    public Canvas(String title)
    {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new DrawingSurface();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        gameSize = new Dimension(300, 300);
        canvas.setPreferredSize(gameSize);
        backgroundColour = new Color(255, 255, 255);    // background is white
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Create a Canvas. 
     * creates canvas with a background of white and size of 300 by 300
     */
    public Canvas()
    {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new DrawingSurface();
        frame.setContentPane(canvas);
        frame.setTitle("CSC121 Canvas");
        gameSize = new Dimension(300, 300);
        canvas.setPreferredSize(gameSize);
        backgroundColour = new Color(255, 255, 255);    // background is white
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Set the canvas frame to be the key event object
     */
    public void setListener(KeyListener keylistener)
    { frame.addKeyListener(keylistener);
    }    

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible)
    {
        if(graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = gameSize;
            canvasImage = new java.awt.image.BufferedImage(size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_RGB);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.white);
        }
        if (visible) {
            GraphicsDevice screen = frame.getGraphicsConfiguration().getDevice();
            frame.setBounds(screen.getDefaultConfiguration().getBounds());
        }
        frame.setVisible(visible);
        if (visible) frame.requestFocus();
        
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public boolean getVisible()
    {
        return frame.isVisible();
    }

    /**
     * Draw a label (text) onto the canvas.
     * @param  referenceObject  an object to define identity for this label
     * @param  color            the color of the shape
     * @param  label            the label to be drawn on the canvas
     * @param  xCoord           the x coordinate of the label anchor point
     * @param  yCoord           the y coordinate of the label anchor point
     */
    public void drawString(Object referenceObject, String color, String label, int xCoord, int yCoord)
    {
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new LabelDescription(label, xCoord, yCoord, color));
        redraw();
    }
        
    /**
     * Draw a given shape onto the canvas.
     * @param  referenceObject  an object to define identity for this shape
     * @param  color            the color of the shape
     * @param  shape            the shape object to be drawn on the canvas
     */
     // Note: this is a slightly backwards way of maintaining the shape
     // objects. It is carefully designed to keep the visible shape interfaces
     // in this project clean and simple for educational purposes.
    public void draw(Object referenceObject, String color, Shape shape)
    {
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color, referenceObject.getClass().getSimpleName()));
        redraw();
    }
 
    /**
     * Erase a given shape's from the screen.
     * @param  referenceObject  the shape object to be erased 
     */
    public void erase(Object referenceObject)
    {
        objects.remove(referenceObject);   // just in case it was already there
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * @param  newColor   the new colour for the foreground of the Canvas 
     */
    public void setForegroundColor(String colorString)
    {
        if(colorString.equals("red"))
            graphic.setColor(Color.red);
        else if(colorString.equals("black"))
            graphic.setColor(new Color(225, 230, 240));
        else if(colorString.equals("blue"))
            graphic.setColor(Color.blue);
        else if(colorString.equals("yellow"))
            graphic.setColor(Color.yellow);
        else if(colorString.equals("green"))
            graphic.setColor(Color.green);
        else if(colorString.equals("magenta"))
            graphic.setColor(Color.magenta);
        else if(colorString.equals("white"))
            graphic.setColor(Color.white);
        else
            graphic.setColor(new Color(225, 230, 240));
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        } 
        catch (Exception e)
        {
            // ignoring exception at the moment
        }
    }

    /**
     * @return - the width of the canvas in pixels.
     */
    public int getWidth()
    {
        return gameSize.width;
    }
    
    /**
     * @return - the height of the canvas in pixels
     */
    public int getHeight()
    {
        return gameSize.height;
    }
    
    
    /**
     * Redraw all shapes currently on the Canvas.
     */
    private void redraw()
    {
        String objectType;
        Object object;
        
        erase();
        for(Iterator i=objects.iterator(); i.hasNext(); ) {
            object = shapes.get(i.next());
            if ( ((object.getClass()).toString()).indexOf("Label")==-1 ){
                ((ShapeDescription)object).draw(graphic);
            }
            else{
                ((LabelDescription)object).draw(graphic);
            }   
        }
        canvas.repaint();
    }
       
    /**
     * Remove all shapes currently on the Canvas.
     */
    public void eraseAll()
    {
        objects.clear();
        shapes.clear();
    }
       
    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase()
    {
        if (!getVisible()){
            setVisible(false);
        }
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = gameSize;
        PixelTheme.background(graphic, size.width, size.height);
        graphic.setColor(original);
    }


    /************************************************************************
     * Inner class DrawingSurface - the actual canvas component contained in the
     * Canvas frame. This is essentially a DrawingSurface with added capability to
     * refresh the image drawn on it.
     */
    private class DrawingSurface extends JComponent
    {
        public DrawingSurface()
        {
            this.setDoubleBuffered(true);
        }
        public void paint(Graphics g)
        {
            // Fullscreen display; preserve the original 800 x 600 playfield.
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
            double scale = Math.min(getWidth() / (double)gameSize.width,
                                    getHeight() / (double)gameSize.height);
            int width = (int)(gameSize.width * scale);
            int height = (int)(gameSize.height * scale);
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                           RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.drawImage(canvasImage, (getWidth()-width)/2, (getHeight()-height)/2,
                        width, height, null);
        }
    }
    
    /************************************************************************
     * Inner class ShapeDescription - objects supporting the rendering of shape objects
     */
    private class ShapeDescription
    {
        private Shape shape;
        private String kind;
        private String colorString;

        public ShapeDescription(Shape shape, String color, String kind)
        {
            this.shape = shape;
            this.kind = kind;
            colorString = color;
        }

        public void draw(Graphics2D graphic)
        {
            setForegroundColor(colorString);
            PixelTheme.sprite(graphic, kind, shape.getBounds(), colorString);
        }
    }

    /************************************************************************
     * Inner class LabelDescription - objects supporting the rendering of text
     */
    private class LabelDescription
    {
        private String label;
        private int xCoord;
        private int yCoord;
        private String colorString;

        public LabelDescription(String label, int xCoord, int yCoord, String color)
        {
            this.label = label;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.colorString = color;
        }
        
        public void draw(Graphics2D graphic)
        {
            setForegroundColor(colorString);
            PixelTheme.text(graphic, label, xCoord, yCoord, yCoord == 54 ? 0x8cc9a7 : 0xe2e6ed);
        }
    }

}