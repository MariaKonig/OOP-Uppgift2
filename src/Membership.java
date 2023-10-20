import javax.swing.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
public class Membership {
    public Boolean test = false;

    public Boolean getIsLessThanAYear(LocalDate fromDate, LocalDate toDate) {

        Period p = Period.between(fromDate, toDate);
        Period exactlyAYear = Period.of(1, 0, 0);
        return p.getYears() == 0 || p.equals(exactlyAYear);
    }

    public Member findMemberInList(String id, ArrayList<Member> list) {
        id = id.trim();

        for (Member member : list) {
            if (id.equals(member.getIdNr()) || id.equalsIgnoreCase(member.getName())) {
                return member;
            }
        }
        return null;
    }

    public void setMembershipStatus(Member member, LocalDate toDate) {
        String stringFromDate = member.getMembershipDate();
        LocalDate fromDate = LocalDate.parse(stringFromDate);
        Boolean status = getIsLessThanAYear(fromDate, toDate);
        member.setHasActiveMembership(status);
    }

    public String readInput(String OptionalText) {
        String input;
        if (test) {
            input = OptionalText;
        } else {
            input = JOptionPane.showInputDialog("Besökare: Namn Efternamn / personNr ÅÅÅÅMMDD");
        }
        if (input == null) {
            return null;
        }
        if (!isValidInput(input)) {
            return "Ogiltigt format";
        }
        return input.trim();
    }

    public Boolean isValidInput(String input) {
        input = input.trim();
        if (input.length() == 10 && Character.isDigit(input.charAt(0))) {
            return true;
        } else return input.contains(" ") && Character.isLetter(input.charAt(0));
    }
    //samlings-metod
    public String processInput(String id, ArrayList<Member> list, LocalDate today) {
        Member member = findMemberInList(id, list);
        if (member == null) {
            return "obehörig";
        }
        setMembershipStatus(member, today);
        if (!member.getHasActiveMembership()) {
            return "f.d. kund";
        } else {
            member.setWorkoutDate(today.toString());
            return "kund";
        }
    }
}