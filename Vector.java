/**
 * Vector class
 *
 * @author Harshal
 */
public class Vector {

    // Variables
    private int row;
    private int col;

    // Vector constructor
    public Vector(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * clone method creates a clone of the vector supplied as a parameter
     *
     * @param vector
     * @return
     */
    public static Vector clone(Vector vector) {
        Vector newVector = new Vector(0, 0);
        newVector.setRow(vector.getRow());
        newVector.setCol(vector.getCol());
        return newVector;
    }

    // Getters and setters
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
}
