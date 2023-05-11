/*
Main class to create only one object of the GUI's class.
The object contains various implementation details to run the program.
 */
public class Main {
    public static void main(String[] args) {
        // Calling the static method from eBookGUI to create the program's frame:
        eBookGUI.getInstance();
    }

}
