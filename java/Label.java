/**
 * This class provides a label object (text) for a canvas
 * 
 * @author Carl Singer
 * @version September 2005
 */
public class Label
{
    // instance variables - replace the example below with your own
    private String label;
    private int xCoord;
    private int yCoord;
    private String color;
    private boolean isVisible;
    private Canvas canvas;

    /**
     * Constructor for objects of class Label
     */
    public Label(Canvas canvas)
    {
        // initialise instance variables
        label = "";
        xCoord = 0;
        yCoord = 0;
        color = "black";
        isVisible = false;
        this.canvas = canvas;
    }

    /**
     * Constructor for objects of class Label
     */
    public Label(String label, int xCoord, int yCoord, Canvas canvas)
    {
        // initialise instance variables
        this.label = label;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        color = "black";
        isVisible = false;
        this.canvas = canvas;
    }
    /**
     * Make this circle visible. If it was already visible, do nothing.
     */
    public void makeVisible()
    {
        isVisible = true;
        draw();
    }
    
    /**
     * Make this circle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible()
    {
        erase();
        isVisible = false;
    }
    
    /**
     * Move to the location defined by xDistance and yDistance
     * @param  distanceX  how far to move in the horizontal direction
     * @param  distanceY  how far to move in the verticalal direction
     */
    public void moveTo(int newXCoord, int newYCoord)
    {
       erase();
       yCoord = newYCoord;
       xCoord = newXCoord;
       draw();
   }

    /**
     * Change the color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     * 
     * @param newColor 
     */
    public void changeColor(String newColor)
    {
        color = newColor;
        draw();
    }

    /**
     * Draw the label with current specifications on screen.
     */
    private void draw()
    {
        if(isVisible) {
            canvas.drawString(this, color, label, xCoord, yCoord);
        }
    }

    /**
     * Erase the label.
     */
    public void erase()
    {
        if(isVisible) {
            canvas.erase(this);
        }
    }

    /**
     * sets the text of the label.
     */
    public void setText(String label)
    {
        this.label = label;
        draw();
    }
    
    /**
     * @return the x coordinate of the center of the label.
     */
    public int getXCoord()
    {
        return xCoord;
    }
    
    /**
     * @returns the y coordinate of the center of the label.
     */
    public int getYCoord()
    {
        return yCoord;
    }
    
}
