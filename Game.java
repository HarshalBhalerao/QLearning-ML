import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Game class
 *
 * @author Harshal
 */
public class Game extends JPanel implements Runnable {

    // Rows and cols variable
    public static int rows;
    public static int cols;

    // Size of the cell
    public static int cellSize = 100;

    // Width and height of the game screen
    public static int width;
    public static int height;

    // Node
    public static Node[][] node;

    // Q-Learning
    private final double alpha = 0.2; // Learning rate
    private final double gamma = 0.7; // Discount rate

    // Keep track of current position
    private Vector currentPos;

    // Epsilon
    private double maxEpsilon = 1.0;
    private double minEpsilon = 0.05;
    private double decayRate = 0.0005;

    // Random
    private Random random;

    // Thread
    private Thread thread;
    private boolean executing;

    /**
     * Game constructor
     *
     * @param gridSize
     */
    public Game(int gridSize) {
        // Calling the initialize method below
        initialize(gridSize);

        // Set the size of the screen
        this.setPreferredSize(new Dimension(width, height));

        // Make the background of the screen black
        this.setBackground(Color.BLACK);

        // Set the layout of the screen to grid with specific number of rows and cols.
        this.setLayout(new GridLayout(rows, cols));

        // Take the control of the game as soon as the screen spawns
        this.setFocusable(true);
    }

    /**
     * initialize method initializes the game objects
     *
     * @param gridSize
     */
    public void initialize(int gridSize) {
        //Based on the user input gridSize, we will change out number of rows and columns.
        rows = gridSize;
        cols = gridSize;

        // We will also change the width and height of the game screen.
        width = cellSize * cols;
        height = cellSize * rows;

        // We would also create our main Node array
        node = new Node[rows][cols];

        // Initialize random
        random = new Random();

        // Add cell nodes to the game
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                node[i][j] = new Node(i, j);
                node[i][j].setIdle(true);
                this.add(node[i][j]);
            }
        }

        // CurrentPosition of the agent is set to 0, 0
        currentPos = new Vector(0, 0);

        // Hard-code start position and finish position as these are supposed to be fixed.
        node[0][0].setPrevious(3); // Set its previous state to idle
        node[0][0].setStart(true);
        node[rows - 1][cols - 1].setFinish(true);


        // We would put holes in our map in random positions. We make sure that the holes don't spawn
        // right next to the start node or finish node or completely surround them.
        int min = 1;
        int max;
        if(gridSize == 4){
            max = gridSize - 1;
        } else{
            max = gridSize - 2;
        }
        int count = 0;

        // Here we keep the count of the holes as the half the number of row or column plus 1.
        while (count < (gridSize / 2) + 1) {
            // Randomly place the holes
            int randomRow = random.nextInt(max - min) + min;
            int randomCol = random.nextInt(max - min) + min;
            if (!node[randomRow][randomCol].isStart() && !node[randomRow][randomCol].isFinish() && !node[randomRow][randomCol].isHole()) {
                node[randomRow][randomCol].setHole(true);
                count++; // Increment the count when the holes are placed in valid positions.
            }
        }

    }

    /**
     * start method: This synchronized method starts a new thread. This game runs on a single thread. We set
     * our boolean variable executing to true for our game loop.
     */
    public synchronized void start() {
        try {
            thread = new Thread(this);
            thread.start();
            executing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * stop method: This synchronized method stops the executing thread. This method is
     * called at the end of the game loop to safely stop the execution of the thread.
     */
    public synchronized void stop() {
        try {
            thread.join();
            executing = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * processInput method: This method makes the thread sleep to ensure smooth running of the game.
     * It introduces lag which makes the simulation viewable for human eyes.
     */
    private void processInput() {
        try {
            int lag = 20;
            Thread.sleep(lag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * changeAgentPos method: This method would change the position of the agent AI. In this game, we want
     * the AI to move towards the finish node.
     *
     * @param nextPos
     */
    public void changeAgentPos(Vector nextPos) {
        // First we iterate through the entire node array
        for (int i = 0; i < node.length; i++) {
            for (int j = 0; j < node[i].length; j++) {
                // And whenever we find a start node we set it to false
                if (node[i][j].isStart()) {
                    node[i][j].setStart(false);
                    // Then we set the previous state of that node to idle, finish or hole (whatever it was)
                    if (node[i][j].getPrevious() == 0) {
                        // Start
                        node[i][j].setIdle(true);
                        node[i][j].setPrevious(3); // Set previous to idle
                    }
                    if (node[i][j].getPrevious() == 1) {
                        // Finish
                        node[i][j].setFinish(true);
                        node[i][j].setPrevious(1); // Set previous to finish
                    }
                    if (node[i][j].getPrevious() == 2) {
                        // Hole
                        node[i][j].setHole(true);
                        node[i][j].setPrevious(2); // Set previous to hole
                    }
                    if (node[i][j].getPrevious() == 3) {
                        // Idle
                        node[i][j].setIdle(true);
                        node[i][j].setPrevious(3); // Set previous to idle
                    }
                }
            }
        }
        // Then we set the nextPosition of the start to true
        node[nextPos.getRow()][nextPos.getCol()].setStart(true);
    }

    /**
     * getAction method gets all the possible actions that the AI agent could make. We will be returning
     * Action ArrayList which would store all the directions that this agent can take from its current
     * position.
     *
     * @param currentPos
     * @return Action ArrayList
     */
    public ArrayList<Action> getAction(Vector currentPos) {
        // Get the row and col
        int row = currentPos.getRow();
        int col = currentPos.getCol();

        ArrayList<Action> possibleActions = new ArrayList<>();
        // Is it possible to visit the Node above
        if (row > 0) {
            possibleActions.add(Action.UP);
        }
        if (col > 0) {
            // Is it possible to visit the Node to the left
            possibleActions.add(Action.LEFT);
        }
        if (row < rows - 1) {
            // Is it possible to visit the Node below
            possibleActions.add(Action.DOWN);
        }
        if (col < cols - 1) {
            // Is it possible to visit the Node to the right
            possibleActions.add(Action.RIGHT);
        }
        return possibleActions;
    }

    /**
     * isFinish method would return true if the agent has reached the finish node.
     *
     * @param row
     * @param col
     * @return boolean
     */
    private boolean isFinish(int row, int col) {
        if (node[row][col].isFinish()) {
            return true;
        }
        return false;
    }

    /**
     * getVectorPos method: Depending on the direction the agent can move to, we change the position of the agent by
     * changing its vector position. We would return the vector position to mark the nextPosition that the agent would
     * take.
     *
     * @param currentPos
     * @param action
     * @return Vector - the nextPosition the agent can take.
     */
    private Vector getVectorPos(Vector currentPos, int action) {
        Vector nextPos = Vector.clone(currentPos);

        // If the agent can move UP and if it is in bounds, then that would be its nextPos
        if (action == Action.UP.getIntEquivalent() && nextPos.getRow() > 0) {
            nextPos.setRow(nextPos.getRow() - 1);
        }
        // If the agent can move DOWN and if it is in bounds, then that would be its nextPos
        if (action == Action.DOWN.getIntEquivalent() && nextPos.getRow() < rows - 1) {
            nextPos.setRow(nextPos.getRow() + 1);
        }
        // If the agent can move LEFT and if it is in bounds, then that would be its nextPos
        if (action == Action.LEFT.getIntEquivalent() && nextPos.getCol() > 0) {
            nextPos.setCol(nextPos.getCol() - 1);
        }
        // If the agent can move RIGHT and if it is in bounds, then that would be its nextPos
        if (action == Action.RIGHT.getIntEquivalent() && nextPos.getCol() < cols - 1) {
            nextPos.setCol(nextPos.getCol() + 1);
        }
        return nextPos; // Return the vector position.
    }

    /**
     * getMaxQValue method would return the neighbouring node's Q-Value which has the highest Q-Value
     *
     * @param currentPos
     * @return
     */
    private double getMaxQValue(Vector currentPos) {
        double maxQValue = -1000;
        ArrayList<Action> actions = getAction(currentPos);

        // Get all the valid directions the agent can go to in its current position
        for (int i = 0; i < actions.size(); i++) {
            // Get their integer equivalent
            int getAction = actions.get(i).getIntEquivalent();
            // Get that neighboring node's vector position.
            Vector neighbouringNode = getVectorPos(currentPos, getAction);
            // Get its Q-value
            double value = node[neighbouringNode.getRow()][neighbouringNode.getCol()].getQValue();

            // Get the node with the highest Q-value
            if (value > maxQValue) {
                maxQValue = value;
            }
        }
        return maxQValue; // Return that Q-value
    }

    /**
     * getMaxQIndex method is the same as above, except here we are returning the neighbouring node's vector
     * positon which has the highest Q-Value.
     *
     * @param currentPos
     * @return Vector - neighbouring node with the highest Q-Value.
     */
    private Vector getMaxQIndex(Vector currentPos) {
        double maxQValue = -1000;
        ArrayList<Action> actions = getAction(currentPos);
        Vector nextPos = new Vector(0, 0);

        for (int i = 0; i < actions.size(); i++) {
            int getAction = actions.get(i).getIntEquivalent();
            Vector neighbouringNode = getVectorPos(currentPos, getAction);
            double value = node[neighbouringNode.getRow()][neighbouringNode.getCol()].getQValue();

            if (value > maxQValue) {
                maxQValue = value;
                nextPos = neighbouringNode;
            }
        }
        return nextPos;
    }


    /**
     * calculateEpsilon method helps us decide if the agent should keep exploring or whether the agent should exploit
     * what it has learnt i.e., follow the path with the highest Q-value. This epsilon value slowly keeps decreasing
     * as our number of episodes increase, making the AI chose the path with highest Q-value as the game progresses.
     * <p>
     * Formula taken from: https://www.datacamp.com/tutorial/introduction-q-learning-beginner-tutorial
     *
     * @param episode
     * @return
     */
    private double calculateEpsilon(int episode) {
        return this.minEpsilon + (this.maxEpsilon - this.minEpsilon) * Math.exp(-this.decayRate * episode);
    }

    /**
     * decideAction method would use the epsilon value calculated above to either make the agent explore
     * the environment by giving it a random index, or it could make the agent follow a fixed path. This
     * completely depends on the epsilon value.
     *
     * @param currentPos
     * @param actionDirection
     * @param episode
     * @return
     */
    private Vector decideAction(Vector currentPos, int actionDirection, int episode) {
        // Calculate epsilon value
        double epsilon = calculateEpsilon(episode);
        // Get random double between [0,1]
        double randomInt = random.nextDouble(1);
        Vector action;
        if (randomInt > epsilon) {
            // Get the max QValue Index from the current node's neighbours.
            action = getMaxQIndex(currentPos);
        } else {
            // Get a random vector position
            action = getVectorPos(currentPos, actionDirection);
        }
        return action;
    }

    /**
     * printQTable method would print Q Table
     */
    private void printQTable() {
        for (int i = 0; i < node.length; i++) {
            for (int j = 0; j < node[i].length; j++) {
                System.out.println(i + ", " + j + " QValue: " + node[i][j].getQValue());
            }
        }
    }

    /**
     * qLearningAlgorithm method: We are training our agent in the qLearningAlgorithm 10000 times (episodes)
     * and during this the agent is both getting trained and is searching for the finish node. Based on the
     * epsilon value we either get the neighbouring node randomly (explore) or we get the node by getting its maxQValue.
     * The aim of this method is to access all the neighbouring nodes of the current node and set their Q-value.
     * The agent will explore until it finds the finish node and when it does it would have trained more than
     * 100000 times, this will get us a clear picture of the best Q values.
     * We make our agent move to the neighbouring nodes with the highest Q value eventually leading to the final node
     * by avoiding obstacles and getting the shortest (and highest reward) path possible.
     *
     * @param currentPos
     * @return Vector - next position to head to
     */
    private Vector qLearningAlgorithm(Vector currentPos) {
        // Everytime this method is called, train 10000 times
        for (int episodes = 0; episodes < 10000; episodes++) {
            // Clone currentPos vector
            Vector currentPosClone = Vector.clone(currentPos);
            // Get the list of possible actions
            ArrayList<Action> possibleActions = getAction(currentPosClone);
            // We go through each of the possible actions
            for (int i = 0; i < possibleActions.size(); i++) {
                // Get the possible action's integer equivalent
                int actionDirection = possibleActions.get(i).getIntEquivalent();

                // We get the nextPos by checking the epsilon value
                // The number of episodes play a huge part, the epsilon value will keep
                // decreasing if the number of episodes increase. This also guarantees that
                // as the agent gets trained it would choose paths wisely by checking Q-Value
                // and not just simply choosing a random path.
                Vector nextPos = decideAction(currentPosClone, actionDirection, episodes);

                // We get the QValue of the next node
                double nextQValue = node[nextPos.getRow()][nextPos.getCol()].getQValue();
                // Get the max Q value of neighbouring nodes of next node.
                double maxQ = getMaxQValue(nextPos);
                // Get the reward value of next node
                int nextReward = node[nextPos.getRow()][nextPos.getCol()].getReward();

                // Bellman's equation to calculate Q-value
                double value = nextQValue + alpha * (nextReward + gamma * maxQ - nextQValue);

                // Set the nextPosition's Q-value to the one calculated above
                node[nextPos.getRow()][nextPos.getCol()].setQValue(value);

                //currentPosition is set to the nextPosition
                currentPosClone.setRow(nextPos.getRow());
                currentPosClone.setCol(nextPos.getCol());
            }
        }

        // We want our agent to make wise decisions, and we also want to move node by node.
        // Using the current position of the agent we get the neighboring node with the
        //highest Q-value and set our current position vector to that node position.
        Vector nextPos = getMaxQIndex(currentPos);
        currentPos.setRow(nextPos.getRow());
        currentPos.setCol(nextPos.getCol());

        return currentPos; // We return that position.
    }

    /**
     * update method would update the state of the game.
     */
    public void update() {
        // Introduce some time interval in order to see the agent change position
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the next position from the qLearningAlgorithm
        Vector nextPos = qLearningAlgorithm(currentPos);
        changeAgentPos(nextPos); // Change the agent's position

        // For debugging purposes
        //printQTable();

        // Introduce some time interval in order to see the agent change position
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If we have reached the finish node
        if (isFinish(nextPos.getRow(), nextPos.getCol())) {
            // Set the row and col back to start position (0,0)
            currentPos.setRow(0);
            currentPos.setCol(0);
            // Change position
            changeAgentPos(currentPos);
        }
    }

    /**
     * run method would keep the game running in an infinite loop.
     */
    @Override
    public void run() {
        while (executing) {
            processInput();
            update();
        }
        stop();
    }
}
