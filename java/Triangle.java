import java.awt.*;

/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling & David J. Barnes (modified by: C. Singer)
 * @version 1.0  (15 July 2000)
 */

public class Triangle
{
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    private Canvas canvas;

    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle(Canvas canvas)
    {
        height = 30;
        width = 40;
        xPosition = 50;
        yPosition = 15;
        color = "green";
        isVisible = false;
        this.canvas = canvas;
    }

    /**
     * Make this triangle visible. If it was already visible, do nothing.
     */
    public void makeVisible()
    {
        isVisible = true;
        draw();
    }
    
    /**
     * Make this triangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible()
    {
        erase();
        isVisible = false;
    }
    
    /**
     * Move in the direction defined by xDistance and yDistance
     * @param  distanceX  how far to move in the horizontal direction
     * @param  distanceY  how far to move in the verticalal direction
     */
    public void moveDirection(int xDistance, int yDistance)
    {
       erase();
       yPosition += yDistance;
       xPosition += xDistance;
       draw();
   }

    /**
     * Move to the location defined by xDistance and yDistance
     * @param  distanceX  
     * @param  distanceY  
     */
    public void moveTo(int newXPosition, int newYPosition)
    {
       erase();
       yPosition = newYPosition;
       xPosition = newXPosition;
       draw();
   }

    /**
     * Change the size to the new size (in pixels). Size must be >= 0.
     */
    public void changeSize(int newHeight, int newWidth)
    {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    /**
     * Change the color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(String newColor)
    {
        color = newColor;
        draw();
    }

    /*
     * Draw the triangle with current specifications on screen.
     */
    private void draw()
    {
        if(isVisible) {
            Polygon myPoly = new Polygon();                     //Points in order drawn:
            myPoly.addPoint(xPosition,yPosition);                       //First
            myPoly.addPoint(xPosition -height,yPosition + (width/2));  //Second
            myPoly.addPoint(xPosition - height,yPosition - (width/2));  //Third
            canvas.draw(this, color, myPoly);
        }
    }

    /*
     * Erase the triangle on screen.
     */
    public void erase()
    {
        if(isVisible) {
            canvas.erase(this);
        }
    }
    public int getXPosition()
    {
        return xPosition;
    }
    
    public int getYPosition()
    {
        return yPosition;
    }
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
