/**
 * Action Enum
 *
 * @author Harshal
 */
public enum Action {
    UP(0), DOWN(1), LEFT(2), RIGHT(3);

    // We have this variable to get the respective direction's integer equivalent.
    private final int intEquivalent;

    // Constructor
    Action(int intEquivalent) {
        this.intEquivalent = intEquivalent;
    }

    // Getter method for getting the integer equivalent
    public int getIntEquivalent() {
        return intEquivalent;
    }

}
