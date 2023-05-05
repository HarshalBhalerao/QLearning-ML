import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Node class
 *
 * @author Harshal
 */
public class Node extends JButton implements ActionListener {

    // row and col
    private int row;
    private int col;

    // Different kinds of nodes
    private boolean start;
    private boolean finish;
    private boolean hole;
    private boolean idle;

    // The previous state of the node
    // start -> 0, finish -> 1, hole -> 2 and idle-> 3
    private int previous;

    // Reward of each node
    private int reward;

    // QValue of each node
    private double qValue;

    /**
     * Node constructor
     *
     * @param row
     * @param col
     */
    public Node(int row, int col) {
        this.row = row;
        this.col = col;

        setBackground(Color.WHITE);
        setOpaque(true);
        setFocusable(false);
        addActionListener(this);

        // Make this game compatible in other platforms
        try {
            UIManager.setLookAndFeel((UIManager.getCrossPlatformLookAndFeelClassName()));
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    // Getters and Setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        if (start) {
            // Visual attributes for start
            setBackground(Color.ORANGE);
            setOpaque(true);
            setText("Start");
            // Accessing start node back again costs -10 points
            setReward(-10);
        }
        this.start = start;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        if (finish) {
            // Visual attributes for finish node
            setBackground(Color.GREEN);
            setOpaque(true);
            setText("Goal");
            // Reaching Finish node gives 1000 points
            setReward(1000);
            // Setting its previous state to Finish Node(1)
            setPrevious(1);
        }
        this.finish = finish;
    }

    public boolean isHole() {
        return hole;
    }

    public void setHole(boolean hole) {
        if (hole) {
            // Visual attributes for hole node
            setBackground(Color.RED);
            setOpaque(true);
            setText("Hole");
            // Accessing hole node costs -1000 points
            setReward(-1000);
            // Setting its previous to hole node(2)
            setPrevious(2);
        }
        this.hole = hole;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        if (idle) {
            // Visual attributes for idle node
            setBackground(Color.WHITE);
            setOpaque(true);
            setText("Idle");
            // Accessing idle node costs 0 points
            setReward(0);
            // Setting its previous to hole node(2)
            setPrevious(3);
        }
        this.idle = idle;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public double getQValue() {
        return qValue;
    }

    public void setQValue(double qValue) {
        this.qValue = qValue;
    }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Do nothing here.
    }
}
