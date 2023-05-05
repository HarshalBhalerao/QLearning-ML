import javax.swing.*;

/**
 * Main class
 *
 * @author Harshal
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        // GUI for the user to choose gridSize
        String[] grids = new String[2];
        grids[0] = "4";
        grids[1] = "6";
        int gridSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Choose a grid size:", "Grid Size", JOptionPane.QUESTION_MESSAGE, null, grids, "4").toString());

        // Set the title of the game
        frame.setTitle("Machine Learning: Q-Learning Algorithm Simulation");

        // Exit from the game by clicking the close button on top right
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Make the game to not resize
        frame.setResizable(false);

        // Add the game to the game frame and ensure that the window is sized properly
        // to fit the preferred size and its subcomponents.
        Game game = new Game(gridSize);
        frame.add(game);
        frame.pack();

        // Spawn the game frame in the middle of the computer screen
        frame.setLocationRelativeTo(null);

        // Make the game frame visible
        frame.setVisible(true);

        // Start the game thread
        game.start();
    }
}