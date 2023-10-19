import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    Membership m = new Membership();
    FileHandler fH = new FileHandler();

    public void run() {

        ArrayList<Member> members = fH.getMembersFromFile();

        while (true) {
            String input = m.readInput("");
            if (input == null) {
                break;
            } else if (input.equals("Ogiltigt format")) {
                JOptionPane.showMessageDialog(null, input);
                continue;
            }
            String output = m.processInput(input, members, LocalDate.now());
            JOptionPane.showMessageDialog(null, output);
        }
        fH.writeToPTFile(members);
    }

    public static void main(String[] args) {
        Main program = new Main();
        program.run();
    }
}
