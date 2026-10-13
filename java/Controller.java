// implement all of the driver class
// add a main method for use after the project is completed.

import java.awt.event.*;

/**
 * This class plays the role of controller of the simulation.
 * It creates the shuttle and space station objects as well as
 * the supporting canvas and label objects.
 * 
 * @author Carl Singer Brian Howard, Dave Maharry
 * @version September 2004
 * @version September 2005, revised by Carl Singer
 * @version August 2006, revised by Carl Singer
 */
public class Controller
    implements KeyListener
{
    // Terminal entry point. The original simulation remains in use.
    public static void main(String[] args)
    {
        new Controller().simulate();
        System.exit(0);
    }

    private SpaceStation spaceStation;
    private FuelCell fuelCell;
    private Shuttle shuttle;
    private Canvas canvas;
    private boolean sim;             // Used to determine whether (true) or 
                                     //      not (false)to continue the simulation 
    private boolean speedSet;        // Used to sequence the label at the top of the simulation window
    private boolean moveShuttle;     // true to move the shuttle up or down once
    private boolean launchFuelCell;  // true to launch a fuel cell
    private boolean fuelCellInMotion;
    private boolean inOrbit;         // true if the space station is orbiting
    // Events are handled by the simulation loop so drawing stays on one thread.
    private final java.util.concurrent.ConcurrentLinkedQueue<Character> inputs =
        new java.util.concurrent.ConcurrentLinkedQueue<Character>();
    private Label stats;
    private Label controls;
    private int round = 1;
    private int score = 0;
    private char difficulty = 'B';
    private boolean paused = false;
    private boolean finished = false;
    private Label label;             // The label at the top of the simulation window
    
    /**
     * Class constructor. Doesn't do much.
     */
    public Controller()
    {
        canvas = new Canvas("Space Station Fueling Simulator",800,600);
        canvas.setListener(this);
        spaceStation = new SpaceStation(canvas);
        shuttle = new Shuttle(canvas,spaceStation);
    }

    /**
     * startSimulation creates the simulation environment and runs the simulation
     * using an indefinite while loop that looks at the state of the simulation
     * and acts accordingly each time through the loop.
     */
    public void simulate()
    {
        speedSet = false;
        label = new Label("B EASY / I NORMAL / A HARD. S TO START.",20,28,canvas);
        stats = new Label("Round 1 / 3     Fuel 5     Score 0",20,54,canvas);
        controls = new Label("K/J MOVE   SPACE FIRE   P PAUSE   R RESTART   ESC EXIT",20,canvas.getHeight()-16,canvas);
        label.makeVisible();
        stats.makeVisible();
        controls.makeVisible();
        canvas.setVisible(true);
        sim = true;
        while (sim)
        {
            Character input;
            while ((input = inputs.poll()) != null) handleInput(input);
            if (!sim) break;
            if (inOrbit && !paused && !finished)
            {
                spaceStation.moveSmallDistance();
                if (moveShuttle)
                {
                    shuttle.moveSmallDistance();
                    moveShuttle = false;
                }
                if (launchFuelCell)
                {
                    launchFuelCell = false;
                    if (!fuelCellInMotion && shuttle.getNumberFuelCells() > 0)
                    {
                        fuelCell = shuttle.launchCell();
                        fuelCellInMotion = true;
                        label.setText("Fuel cell on its way.");
                        updateStats();
                    }
                }
                if (fuelCellInMotion)
                {
                    fuelCell.moveSmallDistance();
                    String status = fuelCell.status();
                    if (status.equals("refueled"))
                    {
                        fuelCellInMotion = false;
                        score += 100 * round + 20 * shuttle.getNumberFuelCells();
                        shuttle.dock();
                        if (round == 3)
                        {
                            finished = true;
                            label.setText("All three deliveries complete! R to play again.");
                        }
                        else
                        {
                            round++;
                            shuttle.addFuelCells(2);
                            spaceStation.startRound(difficulty, round);
                            label.setText("NICE SHOT! +2 FUEL. NEXT ROUND IS FASTER.");
                        }
                        updateStats();
                    }
                    else if (status.equals("missed"))
                    {
                        fuelCellInMotion = false;
                        label.setText("MISSED. AIM A LITTLE AHEAD.");
                    }
                }
                if (!finished && (spaceStation.getYPosition() >= canvas.getHeight()-35 ||
                    (shuttle.getNumberFuelCells() == 0 && !fuelCellInMotion)))
                {
                    finished = true;
                    label.setText(shuttle.getNumberFuelCells() == 0 ?
                        "Out of fuel cells. R to try again." : "The station drifted away. R to try again.");
                }
            }
            canvas.wait(20);
        }
        canvas.setVisible(false);
    }

    private void updateStats()
    {
        stats.setText("Round " + round + " / 3     Fuel " + shuttle.getNumberFuelCells() + "     Score " + score);
    }

    private void handleInput(char key)
    {
        if (key == 'X' || key == 27) { sim = false; return; }
        if (key == 'R')
        {
            if (fuelCell != null) fuelCell.cancel();
            round = 1;
            score = 0;
            paused = finished = fuelCellInMotion = moveShuttle = launchFuelCell = false;
            shuttle.reset();
            spaceStation.startRound(difficulty, round);
            inOrbit = true;
            speedSet = true;
            label.setText("Fresh start. Refuel the station three times.");
            updateStats();
            return;
        }
        if (!inOrbit)
        {
            if (key == 'B' || key == 'I' || key == 'A')
            {
                difficulty = key;
                spaceStation.startRound(difficulty, round);
                speedSet = true;
                label.setText("Difficulty: " + (key == 'B' ? "Easy" : key == 'I' ? "Normal" : "Hard") + ". Press S to start.");
            }
            else if (key == 'S' && speedSet)
            {
                inOrbit = true;
                label.setText("REFUEL THE STATION THREE TIMES. AIM AHEAD.");
            }
            return;
        }
        if (finished) return;
        if (key == 'P')
        {
            paused = !paused;
            moveShuttle = launchFuelCell = false;
            label.setText(paused ? "Paused. P to continue." : "Keep going. Aim ahead of the station.");
            return;
        }
        if (paused) return;
        if (key == 'J' || key == 'K')
        {
            moveShuttle = true;
            shuttle.setDirection(key == 'J' ? "down" : "up");
        }
        else if (key == ' ' && !fuelCellInMotion) launchFuelCell = true;
    }

    public void keyTyped(KeyEvent ke)
    {
        inputs.add(Character.toUpperCase(ke.getKeyChar()));
    }
    /**
     * Ignore methods below this point
     */
    public void keyReleased(KeyEvent ke)
    {
    }

    public void keyPressed(KeyEvent ke)
    {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) inputs.add((char)27);
    }
}
